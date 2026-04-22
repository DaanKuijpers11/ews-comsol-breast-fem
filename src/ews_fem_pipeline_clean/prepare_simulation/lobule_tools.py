import matplotlib.pyplot as plt
import numpy as np


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
    seed=42,
):
    """
    Generate a deterministic fan-shaped set of lobules around the nipple.
    """
    rng = np.random.default_rng(seed)
    lobules = []
    nipple = np.array(nipple, dtype=float)
    angles = np.linspace(-spread_angle, spread_angle, n_lobes)

    for theta in angles:
        direction = np.array([0.0, np.cos(theta), np.sin(theta)])
        direction /= np.linalg.norm(direction)

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
                    "amp_c1": amp_c1,
                    "amp_c2": amp_c2,
                    "amp_rho": amp_rho,
                }
            )

    return lobules


def visualize_lobules_2d(lobules, settings, resolution=200):
    """
    Visualize the projected glandular field in the x-y plane.
    """
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
