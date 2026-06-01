"""
Generate systematically placed glandular lobules around the nipple.

This script creates realistic lobe-fan patterns anatomically motivated,
rather than random lobule placement. Output can be directly used in TOML configs.

Usage:
    python scripts/generate_systematic_lobules.py
    
    Then copy the printed TOML section into your config file.
"""

import numpy as np
import matplotlib.pyplot as plt
from pathlib import Path


def generate_systematic_lobules(
    n_lobes=6,
    n_per_lobe=3,
    nipple=(0.0, 0.068, 0.0),
    lobe_length=0.035,
    spread_angle=45.0,  # degrees, fan angle
    toward_chest_wall=True,
):
    """
    Generate anatomically realistic lobules in a radial fan around nipple.
    
    Args:
        n_lobes: Number of major lobe directions (6-8 realistic)
        n_per_lobe: Number of lobules along each lobe (3-4 realistic)
        nipple: (x, y, z) nipple position
        lobe_length: How far lobules extend from nipple (m)
        spread_angle: Total spread angle in y-z plane (degrees)
        toward_chest_wall: If True, generates lobules inward toward the chest wall.
    
    Returns:
        List of dicts with 'center', 'width', 'amp_*'
    """
    lobules = []
    nipple = np.array(nipple, dtype=float)
    
    # Convert angle to radians
    spread_rad = np.deg2rad(spread_angle)
    
    # Generate angles: fan shape in y-z plane, centered on +y (chest direction)
    angles = np.linspace(-spread_rad / 2, spread_rad / 2, n_lobes)
    
    for lobe_idx, theta in enumerate(angles):
        # Direction vector for this lobe (x stays 0, fan in y-z)
        # y is toward the outer breast surface; invert for chest-wall direction.
        y_dir = -np.cos(theta) if toward_chest_wall else np.cos(theta)
        direction = np.array([
            0.0,                    # x: stays centered
            y_dir,                  # y: main direction (chest wall if inward)
            np.sin(theta)           # z: spreads up/down
        ])
        direction /= np.linalg.norm(direction)
        
        # Generate lobules along this lobe direction
        for lobule_idx in range(n_per_lobe):
            # Parametric position: 0 = at nipple, 1 = far end
            t = (lobule_idx + 1) / (n_per_lobe + 1)
            
            # Position along lobe
            pos = nipple + direction * lobe_length * t
            
            # Width decreases as we move away from nipple (realistic)
            width = 0.004 * (1.0 - 0.3 * t)  # 0.004 base, shrinks to 70% at end
            
            # Material property amplitudes (same as baseline)
            lobule = {
                "center": pos.tolist(),
                "width": width,
                "amp_c1": 70.0,
                "amp_c2": 55.0,
                "amp_rho": 35.0,
            }
            lobules.append(lobule)
    
    return lobules


def print_toml_format(lobules, nipple=(0.0, 0.068, 0.0)):
    """
    Print lobules in TOML array-of-tables format for direct copy-paste.
    """
    print("\n" + "="*70)
    print("TOML FORMAT - Copy-paste this into [material.glandular.hetero] section:")
    print("="*70 + "\n")
    
    print("[material.glandular.hetero]")
    print("enabled = true")
    print("auto_generate = false")
    print(f"nipple = [ {nipple[0]:.5f}, {nipple[1]:.5f}, {nipple[2]:.5f},]")
    print("# Systematically placed lobules in anatomical fan configuration\n")
    
    for i, lob in enumerate(lobules):
        print(f"[[material.glandular.hetero.lobules]]")
        print(f"center = [ {lob['center'][0]:.5f}, {lob['center'][1]:.5f}, {lob['center'][2]:.5f},]")
        print(f"width = {lob['width']:.6f}")
        print(f"amp_c1 = {lob['amp_c1']:.1f}")
        print(f"amp_c2 = {lob['amp_c2']:.1f}")
        print(f"amp_rho = {lob['amp_rho']:.1f}")
        print()


def visualize_lobules(lobules, nipple=(0.0, 0.035, 0.0), radius=0.07):
    """
    Visualize lobule placement in 2D (x-y and y-z projections).
    """
    nipple = np.array(nipple)
    
    fig, axes = plt.subplots(1, 2, figsize=(12, 5))
    
    # === X-Y projection (frontal view) ===
    ax = axes[0]
    
    # Breast boundary (semicircle)
    theta_bound = np.linspace(0, np.pi, 100)
    x_bound = radius * np.cos(theta_bound)
    y_bound = radius * np.sin(theta_bound)
    ax.plot(x_bound, y_bound, "k--", linewidth=2, label="Breast boundary")
    
    # Lobules
    for lob in lobules:
        center = np.array(lob["center"])
        width = lob["width"]
        circle = plt.Circle((center[0], center[1]), width, color="red", alpha=0.6)
        ax.add_patch(circle)
    
    # Nipple
    ax.scatter(*nipple[:2], color="blue", s=100, marker="*", label="Nipple", zorder=5)
    
    ax.set_xlim(-radius * 1.1, radius * 1.1)
    ax.set_ylim(-0.01, radius * 1.1)
    ax.set_aspect("equal")
    ax.set_xlabel("X (left-right)")
    ax.set_ylabel("Y (anterior-posterior)")
    ax.set_title("X-Y Projection (Frontal View)")
    ax.legend()
    ax.grid(True, alpha=0.3)
    
    # === Y-Z projection (lateral view) ===
    ax = axes[1]
    
    # Breast boundary (semicircle in y-z)
    y_bound = radius * np.cos(theta_bound)
    z_bound = radius * np.sin(theta_bound)
    ax.plot(y_bound, z_bound, "k--", linewidth=2, label="Breast boundary")
    
    # Lobules
    for lob in lobules:
        center = np.array(lob["center"])
        width = lob["width"]
        circle = plt.Circle((center[1], center[2]), width, color="red", alpha=0.6)
        ax.add_patch(circle)
    
    # Nipple
    ax.scatter(nipple[1], nipple[2], color="blue", s=100, marker="*", label="Nipple", zorder=5)
    
    ax.set_xlim(-0.01, radius * 1.1)
    ax.set_ylim(-radius * 1.1, radius * 1.1)
    ax.set_aspect("equal")
    ax.set_xlabel("Y (anterior-posterior)")
    ax.set_ylabel("Z (superior-inferior)")
    ax.set_title("Y-Z Projection (Lateral View)")
    ax.legend()
    ax.grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.show()


def save_to_toml_file(lobules, output_path="runs/febio_runs/overnight_suite/manual_lobules_systematic.toml"):
    """
    Save the generated lobules to a complete TOML config file.
    """
    # Read the baseline config as template
    base_toml = Path("runs/febio_runs/overnight_suite/manual_lobules_balanced.toml")
    
    if not base_toml.exists():
        print(f"\nWARNING: Base config not found at {base_toml}")
        print("Cannot save full TOML file, but TOML section printed above.\n")
        return
    
    with open(base_toml, "r") as f:
        content = f.read()
    
    # Find and replace the lobules section
    import re
    
    # Build new lobules section
    new_lobules = "[material.glandular.hetero]\nenabled = true\nauto_generate = false\n\n"
    for lob in lobules:
        new_lobules += f"[[material.glandular.hetero.lobules]]\n"
        new_lobules += f"center = [ {lob['center'][0]:.5f}, {lob['center'][1]:.5f}, {lob['center'][2]:.5f},]\n"
        new_lobules += f"width = {lob['width']:.6f}\n"
        new_lobules += f"amp_c1 = {lob['amp_c1']:.1f}\n"
        new_lobules += f"amp_c2 = {lob['amp_c2']:.1f}\n"
        new_lobules += f"amp_rho = {lob['amp_rho']:.1f}\n\n"
    
    # Replace old lobules section with new one
    pattern = r"\[material\.glandular\.hetero\].*?(?=\[material\.adipose|$)"
    new_content, count = re.subn(pattern, new_lobules + "\n", content, flags=re.DOTALL)
    if count == 0:
        print("WARNING: Could not find [material.glandular.hetero] section to replace.")
        return
    
    # Write new config
    output_file = Path(output_path)
    output_file.parent.mkdir(parents=True, exist_ok=True)
    
    with open(output_file, "w") as f:
        f.write(new_content)
    
    print(f"\nSaved systematic lobules config to: {output_path}\n")


if __name__ == "__main__":
    print("\n" + "="*70)
    print("SYSTEMATIC GLANDULAR LOBULE GENERATOR")
    print("="*70)
    
    # Generate with realistic parameters
    print("\nGenerating 6 lobes × 3 lobules per lobe (18 total)...")
    print("Configuration:")
    print("  - Nipple placed near the outer breast surface")
    print("  - Lobules generated inward toward the chest wall")
    print("  - Lobe directions: 6 (radial fan in yz plane)")
    print("  - Lobules per lobe: 3 (along direction)")
    print("  - Spread angle: 45° (symmetric fan)")
    print("  - Lobe length: 0.035 m")
    print("  - Width decay: 0.004 m base, shrinking with distance")
    
    lobules = generate_systematic_lobules(
        n_lobes=6,
        n_per_lobe=3,
        nipple=(0.0, 0.068, 0.0),
        lobe_length=0.035,
        spread_angle=45.0,
        toward_chest_wall=True,
    )
    
    print(f"\nGenerated {len(lobules)} lobules\n")
    
    # Print in TOML format
    print_toml_format(lobules, nipple=(0.0, 0.068, 0.0))
    
    # Visualize
    print("\n" + "="*70)
    print("Showing 2D visualization (frontal + lateral views)...")
    print("="*70)
    visualize_lobules(lobules)
    
    # Save to file
    save_to_toml_file(
        lobules,
        output_path="runs/febio_runs/overnight_suite/manual_lobules_systematic.toml"
    )
    
    print("\n" + "="*70)
    print("NEXT STEPS:")
    print("="*70)
    print("1. Review the TOML output above")
    print("2. Review the 2D visualizations (frontal + lateral)")
    print("3. Test the new config:")
    print("   python -m ews_fem_pipeline_clean run runs/febio_runs/overnight_suite/manual_lobules_systematic.toml")
    print("4. Analyze results:")
    print("   $env:RUN_NAME='manual_lobules_systematic'")
    print("   python scripts/data_analysis_main.py")
    print("="*70 + "\n")
