import numpy as np


def _unit_vector(vec):
    norm = np.linalg.norm(vec)
    if norm < 1e-12:
        return np.array([0.0, -1.0, 0.0], dtype=float)
    return vec / norm


def _append_droplet_chain(
    lobules,
    base_center,
    nipple,
    base_width,
    amp_c1,
    amp_c2,
    amp_rho,
    droplet_length,
    droplet_components,
):
    """
    Approximate a teardrop lobule by chaining Gaussian components from bulb to duct.
    """
    base_center = np.array(base_center, dtype=float)
    nipple = np.array(nipple, dtype=float)
    toward_nipple = _unit_vector(nipple - base_center)

    n_comp = max(1, int(droplet_components))
    for idx in range(n_comp):
        t = 0.0 if n_comp == 1 else idx / (n_comp - 1)
        center = base_center + toward_nipple * (t * droplet_length)
        width_scale = 1.0 - 0.45 * t
        amp_scale = 1.0 - 0.35 * t

        lobules.append(
            {
                "center": center.tolist(),
                "width": base_width * width_scale,
                "width_x": base_width * 0.75 * width_scale,
                "width_y": base_width * 1.35 * width_scale,
                "width_z": base_width * (0.95 + 0.25 * abs(toward_nipple[2])) * width_scale,
                "amp_c1": amp_c1 * amp_scale,
                "amp_c2": amp_c2 * amp_scale,
                "amp_rho": amp_rho * amp_scale,
            }
        )


def _generate_fan_lobules(
    n_lobes,
    n_per_lobe,
    nipple,
    lobe_length,
    spread_angle,
    width,
    amp_c1,
    amp_c2,
    amp_rho,
    seed,
):
    """
    Deterministic fan-shaped lobule layout in yz-plane.
    """
    rng = np.random.default_rng(seed)
    lobules = []
    nipple = np.array(nipple, dtype=float)
    angles = np.linspace(-spread_angle, spread_angle, n_lobes)

    for theta in angles:
        direction = np.array([0.0, np.cos(theta), np.sin(theta)])
        direction = _unit_vector(direction)

        for index in range(n_per_lobe):
            t = (index + 1) / (n_per_lobe + 1)
            radial_decay = 1.0 - 0.35 * t
            base_pos = nipple + direction * lobe_length * t

            jitter = rng.normal(0, width * 0.25, size=3)
            jitter -= direction * np.dot(jitter, direction)
            pos = base_pos + jitter * radial_decay

            lobules.append(
                {
                    "center": pos.tolist(),
                    "width": width * radial_decay,
                    "width_x": width * 0.80 * radial_decay,
                    "width_y": width * 1.10 * radial_decay,
                    "width_z": width * (1.0 + 0.4 * abs(direction[2])) * radial_decay,
                    "amp_c1": amp_c1,
                    "amp_c2": amp_c2,
                    "amp_rho": amp_rho,
                }
            )

    return lobules


def _generate_chen_double_ring_lobules(
    nipple,
    width,
    amp_c1,
    amp_c2,
    amp_rho,
    seed,
    inner_ring_count,
    outer_ring_count,
    inner_ring_radius,
    outer_ring_radius,
    inner_depth,
    outer_depth,
    droplet_length,
    droplet_components,
):
    """
    Chen-inspired lobe layout:
    - 18 lobe submodels in a double ring around the nipple
    - each lobe approximated as a teardrop chain toward the nipple
    """
    rng = np.random.default_rng(seed)
    nipple = np.array(nipple, dtype=float)
    lobules = []

    def add_ring(count, ring_radius, depth, width_scale, phase):
        if count <= 0:
            return
        angles = np.linspace(0.0, 2.0 * np.pi, count, endpoint=False) + phase
        for phi in angles:
            base_center = np.array(
                [
                    ring_radius * np.cos(phi),
                    nipple[1] - depth,
                    ring_radius * np.sin(phi),
                ],
                dtype=float,
            )
            base_center += rng.normal(0.0, width * 0.08, size=3)
            _append_droplet_chain(
                lobules=lobules,
                base_center=base_center,
                nipple=nipple,
                base_width=width * width_scale,
                amp_c1=amp_c1,
                amp_c2=amp_c2,
                amp_rho=amp_rho,
                droplet_length=droplet_length,
                droplet_components=droplet_components,
            )

    add_ring(
        count=inner_ring_count,
        ring_radius=inner_ring_radius,
        depth=inner_depth,
        width_scale=1.05,
        phase=0.0,
    )
    add_ring(
        count=outer_ring_count,
        ring_radius=outer_ring_radius,
        depth=outer_depth,
        width_scale=0.95,
        phase=np.pi / max(1, outer_ring_count),
    )

    return lobules


def generate_lobules(
    n_lobes,
    n_per_lobe,
    nipple,
    lobe_length,
    spread_angle,
    width,
    amp_c1,
    amp_c2,
    amp_rho,
    generator_mode="fan",
    inner_ring_count=8,
    outer_ring_count=10,
    inner_ring_radius=0.005,
    outer_ring_radius=0.009,
    inner_depth=0.012,
    outer_depth=0.020,
    droplet_length=0.007,
    droplet_components=2,
    seed=42,
):
    """
    Generate lobules using either the legacy fan model or a Chen-inspired double-ring model.
    """
    if generator_mode == "chen_2024_double_ring":
        return _generate_chen_double_ring_lobules(
            nipple=nipple,
            width=width,
            amp_c1=amp_c1,
            amp_c2=amp_c2,
            amp_rho=amp_rho,
            seed=seed,
            inner_ring_count=inner_ring_count,
            outer_ring_count=outer_ring_count,
            inner_ring_radius=inner_ring_radius,
            outer_ring_radius=outer_ring_radius,
            inner_depth=inner_depth,
            outer_depth=outer_depth,
            droplet_length=droplet_length,
            droplet_components=droplet_components,
        )

    return _generate_fan_lobules(
        n_lobes=n_lobes,
        n_per_lobe=n_per_lobe,
        nipple=nipple,
        lobe_length=lobe_length,
        spread_angle=spread_angle,
        width=width,
        amp_c1=amp_c1,
        amp_c2=amp_c2,
        amp_rho=amp_rho,
        seed=seed,
    )


def visualize_lobules_2d_old(lobules, settings, resolution=200):
    """
    Visualize the projected glandular field in the x-y plane.
    """
    import matplotlib.pyplot as plt

    radius = settings.model.geometry.radius
    nipple = settings.material.glandular.hetero.nipple

    x = np.linspace(-radius, radius, resolution)
    y = np.linspace(0.0, radius, resolution)
    grid_x, grid_y = np.meshgrid(x, y)
    field = np.zeros_like(grid_x)

    for lobule in lobules:
        center_x, center_y, _ = lobule.center
        sigma = lobule.width
        amplitude = lobule.amp_rho
        field += amplitude * np.exp(-((grid_x - center_x) ** 2 + (grid_y - center_y) ** 2) / (sigma ** 2))

    plt.figure(figsize=(6, 6))
    plt.imshow(field, extent=[x.min(), x.max(), y.min(), y.max()], origin="lower")
    plt.colorbar(label="Glandular density")

    theta = np.linspace(0, np.pi, 200)
    boundary_x = radius * np.cos(theta)
    boundary_y = radius * np.sin(theta)
    plt.plot(boundary_x, boundary_y, "w--", label="breast boundary")
    plt.scatter(nipple[0], nipple[1], c="red", s=80, label="nipple")

    for lobule in lobules:
        plt.scatter(lobule.center[0], lobule.center[1], c="black", s=10)

    plt.xlabel("x")
    plt.ylabel("y")
    plt.title("Glandular field")
    plt.legend()
    plt.tight_layout()
    plt.show()

def visualize_lobules_2d(lobules, settings, plane="xy", resolution=200):
    """
    Visualize projected glandular field in xy or yz plane.
    """
    import matplotlib.pyplot as plt

    radius = settings.model.geometry.radius
    nipple = settings.material.glandular.hetero.nipple

    if plane == "xy":
        a = np.linspace(-radius, radius, resolution)   # x
        b = np.linspace(0.0, radius, resolution)       # y
        label_a, label_b = "x", "y"

    elif plane == "yz":
        a = np.linspace(0.0, radius, resolution)       # y
        b = np.linspace(-radius, radius, resolution)   # z
        label_a, label_b = "y", "z"

    else:
        raise ValueError("plane must be 'xy' or 'yz'")

    grid_a, grid_b = np.meshgrid(a, b)
    field = np.zeros_like(grid_a)

    for lobule in lobules:
        cx, cy, cz = lobule.center
        sigma_x = lobule.width_x if hasattr(lobule, "width_x") and lobule.width_x is not None else lobule.width
        sigma_y = lobule.width_y if hasattr(lobule, "width_y") and lobule.width_y is not None else lobule.width
        sigma_z = lobule.width_z if hasattr(lobule, "width_z") and lobule.width_z is not None else lobule.width
        amplitude = lobule.amp_rho

        if plane == "xy":
            da = grid_a - cx
            db = grid_b - cy
            field += amplitude * np.exp(-(da**2 / (sigma_x**2) + db**2 / (sigma_y**2)))
        else:  # yz
            da = grid_a - cy
            db = grid_b - cz
            field += amplitude * np.exp(-(da**2 / (sigma_y**2) + db**2 / (sigma_z**2)))

    plt.figure(figsize=(6, 6))
    plt.imshow(field, extent=[a.min(), a.max(), b.min(), b.max()], origin="lower")
    plt.colorbar(label="Glandular density")

    # 🔵 Breast boundary (halve cirkel)
    theta = np.linspace(0, np.pi, 200)
    if plane == "xy":
        boundary_a = radius * np.cos(theta)
        boundary_b = radius * np.sin(theta)
    else:  # yz
        boundary_a = radius * np.sin(theta)   # y
        boundary_b = radius * np.cos(theta)   # z

    plt.plot(boundary_a, boundary_b, "w--", label="breast boundary")

    # 🔴 Nipple
    if plane == "xy":
        plt.scatter(nipple[0], nipple[1], c="red", s=80, label="nipple")
    else:
        plt.scatter(nipple[1], nipple[2], c="red", s=80, label="nipple")

    # ⚫ Lobules
    for lobule in lobules:
        if plane == "xy":
            plt.scatter(lobule.center[0], lobule.center[1], c="black", s=10)
        else:
            plt.scatter(lobule.center[1], lobule.center[2], c="black", s=10)

    # 🟫 Chest wall (alleen zinvol in yz)
    if plane == "yz":
        # aannemen: chest wall op y = 0
        plt.axvline(x=0.0, color="cyan", linestyle="--", label="chest wall")

    plt.xlabel(label_a)
    plt.ylabel(label_b)
    plt.title(f"Glandular field ({plane}-plane)")
    plt.legend()
    plt.tight_layout()
    plt.show()
