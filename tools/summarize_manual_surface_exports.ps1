param(
    [Parameter(Mandatory = $true)]
    [string[]] $Case,

    [Parameter(Mandatory = $true)]
    [string] $OutputDir
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Split-CaseSpec {
    param([string] $Spec)
    $idx = $Spec.IndexOf("=")
    if ($idx -lt 1) {
        throw "Use NAME=CSV_PATH for each -Case argument."
    }
    [pscustomobject]@{
        Name = $Spec.Substring(0, $idx)
        Path = $Spec.Substring($idx + 1)
    }
}

function Find-SurfaceHeader {
    param([string] $Path)
    $reader = [System.IO.StreamReader]::new($Path)
    try {
        while (($line = $reader.ReadLine()) -ne $null) {
            $trimmed = $line.Trim()
            if (-not $trimmed.StartsWith("%")) {
                continue
            }
            $content = $trimmed.TrimStart("%").Trim()
            if ($content.StartsWith("X,") -or $content.StartsWith("x,")) {
                return $content.Split(",")
            }
        }
    }
    finally {
        $reader.Close()
    }
    throw "No COMSOL surface header found in $Path"
}

function Get-TimeGroups {
    param([string[]] $Header)
    $groups = @{}
    for ($i = 0; $i -lt $Header.Count; $i++) {
        $column = $Header[$i]
        if ($column -match "@\s*t=([0-9.Ee+-]+)") {
            $time = [double]$Matches[1]
            $base = ($column -split "@", 2)[0].Trim()
            if (-not $groups.ContainsKey($time)) {
                $groups[$time] = @{}
            }
            $groups[$time][$base] = $i
        }
    }
    return $groups
}

function Convert-Value {
    param([string] $Value)
    return [double]::Parse($Value, [System.Globalization.CultureInfo]::InvariantCulture)
}

function Read-SurfaceRows {
    param(
        [string] $Path,
        [int] $ExpectedColumns
    )
    $rows = New-Object System.Collections.Generic.List[double[]]
    $reader = [System.IO.StreamReader]::new($Path)
    try {
        $seenHeader = $false
        while (($line = $reader.ReadLine()) -ne $null) {
            $trimmed = $line.Trim()
            if ([string]::IsNullOrWhiteSpace($trimmed)) {
                continue
            }
            if ($trimmed.StartsWith("%")) {
                $content = $trimmed.TrimStart("%").Trim()
                if ($content.StartsWith("X,") -or $content.StartsWith("x,")) {
                    $seenHeader = $true
                }
                continue
            }
            if (-not $seenHeader) {
                continue
            }
            $parts = $trimmed.Split(",")
            if ($parts.Count -ne $ExpectedColumns) {
                continue
            }
            $values = New-Object double[] $ExpectedColumns
            $ok = $true
            for ($i = 0; $i -lt $ExpectedColumns; $i++) {
                try {
                    $values[$i] = Convert-Value $parts[$i]
                }
                catch {
                    $ok = $false
                    break
                }
            }
            if ($ok) {
                $rows.Add($values)
            }
        }
    }
    finally {
        $reader.Close()
    }
    return $rows
}

function Get-Percentile {
    param(
        [double[]] $Values,
        [double] $Q
    )
    if ($Values.Count -eq 0) {
        return [double]::NaN
    }
    [array]::Sort($Values)
    if ($Values.Count -eq 1) {
        return $Values[0]
    }
    $position = ($Values.Count - 1) * $Q
    $lower = [math]::Floor($position)
    $upper = [math]::Ceiling($position)
    if ($lower -eq $upper) {
        return $Values[$lower]
    }
    $frac = $position - $lower
    return $Values[$lower] * (1.0 - $frac) + $Values[$upper] * $frac
}

function Summarize-SurfaceCase {
    param(
        [string] $Name,
        [string] $Path
    )
    $header = Find-SurfaceHeader $Path
    $groups = Get-TimeGroups $header
    $rows = Read-SurfaceRows -Path $Path -ExpectedColumns $header.Count
    if ($rows.Count -eq 0) {
        throw "No numeric rows found in $Path"
    }

    $summary = New-Object System.Collections.Generic.List[object]
    foreach ($time in ($groups.Keys | Sort-Object)) {
        $indices = $groups[$time]
        if (-not ($indices.ContainsKey("w (m)") -and $indices.ContainsKey("solid.disp (m)") -and $indices.ContainsKey("solid.mises (N/m^2)"))) {
            continue
        }
        $wIdx = $indices["w (m)"]
        $dispIdx = $indices["solid.disp (m)"]
        $vmIdx = $indices["solid.mises (N/m^2)"]

        $dispVals = New-Object double[] $rows.Count
        $absWVals = New-Object double[] $rows.Count
        $sumDisp = 0.0
        $maxDisp = [double]::NegativeInfinity
        $sumW = 0.0
        $sumAbsW = 0.0
        $minW = [double]::PositiveInfinity
        $maxW = [double]::NegativeInfinity
        $sumVm = 0.0
        $maxVm = [double]::NegativeInfinity

        for ($i = 0; $i -lt $rows.Count; $i++) {
            $row = $rows[$i]
            $w = $row[$wIdx] * 1000.0
            $disp = $row[$dispIdx] * 1000.0
            $vm = $row[$vmIdx] / 1000.0
            $absW = [math]::Abs($w)

            $dispVals[$i] = $disp
            $absWVals[$i] = $absW
            $sumDisp += $disp
            if ($disp -gt $maxDisp) { $maxDisp = $disp }
            $sumW += $w
            $sumAbsW += $absW
            if ($w -lt $minW) { $minW = $w }
            if ($w -gt $maxW) { $maxW = $w }
            $sumVm += $vm
            if ($vm -gt $maxVm) { $maxVm = $vm }
        }

        $summary.Add([pscustomobject]@{
            Case = $Name
            TimeS = $time
            Nodes = $rows.Count
            MeanDispMm = $sumDisp / $rows.Count
            MaxDispMm = $maxDisp
            MeanWMm = $sumW / $rows.Count
            MeanAbsWMm = $sumAbsW / $rows.Count
            MinWMm = $minW
            MaxWMm = $maxW
            MeanVmKpa = $sumVm / $rows.Count
            MaxVmKpa = $maxVm
            P95DispMm = Get-Percentile -Values $dispVals -Q 0.95
            P95AbsWMm = Get-Percentile -Values $absWVals -Q 0.95
        })
    }
    return $summary
}

function Get-PeakSummary {
    param([object[]] $Rows)
    $peaks = New-Object System.Collections.Generic.List[object]
    foreach ($caseName in ($Rows | Select-Object -ExpandProperty Case -Unique)) {
        $caseRows = @($Rows | Where-Object { $_.Case -eq $caseName })
        $peakMean = $caseRows | Sort-Object MeanDispMm -Descending | Select-Object -First 1
        $peakMax = $caseRows | Sort-Object MaxDispMm -Descending | Select-Object -First 1
        $peakAbsW = $caseRows | Sort-Object MeanAbsWMm -Descending | Select-Object -First 1
        $peakVm = $caseRows | Sort-Object MaxVmKpa -Descending | Select-Object -First 1
        $review = $caseRows | Sort-Object { [math]::Abs($_.TimeS - 1.375) } | Select-Object -First 1
        $mostNeg = $caseRows | Sort-Object MinWMm | Select-Object -First 1
        $peaks.Add([pscustomobject]@{
            Case = $caseName
            Nodes = $peakMean.Nodes
            PeakMeanDispMm = $peakMean.MeanDispMm
            PeakMeanDispTimeS = $peakMean.TimeS
            PeakMaxDispMm = $peakMax.MaxDispMm
            PeakMaxDispTimeS = $peakMax.TimeS
            PeakMeanAbsWMm = $peakAbsW.MeanAbsWMm
            PeakMeanAbsWTimeS = $peakAbsW.TimeS
            MostNegativeWMm = $mostNeg.MinWMm
            MostNegativeWTimeS = $mostNeg.TimeS
            ReviewMeanDispMm = $review.MeanDispMm
            ReviewMaxDispMm = $review.MaxDispMm
            ReviewMeanWMm = $review.MeanWMm
            ReviewMinWMm = $review.MinWMm
            PeakMaxVmKpa = $peakVm.MaxVmKpa
            PeakMaxVmTimeS = $peakVm.TimeS
        })
    }
    return $peaks
}

function Write-SummaryMarkdown {
    param(
        [string] $Path,
        [object[]] $PeakRows
    )
    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Surface Response Summary")
    $lines.Add("")
    $lines.Add("| Case | Nodes | Peak mean disp (mm) | Peak max disp (mm) | Peak mean abs w (mm) | Most negative w (mm) | Peak max VM (kPa) |")
    $lines.Add("|---|---:|---:|---:|---:|---:|---:|")
    foreach ($row in $PeakRows) {
        $lines.Add(("| {0} | {1} | {2:N3} | {3:N3} | {4:N3} | {5:N3} | {6:N3} |" -f $row.Case, $row.Nodes, $row.PeakMeanDispMm, $row.PeakMaxDispMm, $row.PeakMeanAbsWMm, $row.MostNegativeWMm, $row.PeakMaxVmKpa))
    }
    $lines.Add("")
    $lines.Add("Source: manual COMSOL outer-surface CSV exports.")
    Set-Content -LiteralPath $Path -Value $lines -Encoding UTF8
}

$caseSpecs = @($Case | ForEach-Object { Split-CaseSpec $_ })
New-Item -ItemType Directory -Path $OutputDir -Force | Out-Null

$allRows = New-Object System.Collections.Generic.List[object]
foreach ($spec in $caseSpecs) {
    $rows = Summarize-SurfaceCase -Name $spec.Name -Path $spec.Path
    foreach ($row in $rows) {
        $allRows.Add($row)
    }
}

$peakRows = @(Get-PeakSummary -Rows $allRows.ToArray())
$allRows | Export-Csv -LiteralPath (Join-Path $OutputDir "surface_timeseries_all_cases.csv") -NoTypeInformation
$peakRows | Export-Csv -LiteralPath (Join-Path $OutputDir "surface_summary.csv") -NoTypeInformation
Write-SummaryMarkdown -Path (Join-Path $OutputDir "surface_summary.md") -PeakRows $peakRows

Write-Host ("[surface summary] wrote {0} cases to {1}" -f $peakRows.Count, $OutputDir)
