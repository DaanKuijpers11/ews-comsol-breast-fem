import numpy as np
import matplotlib.pyplot as plt


def visualize_lobules_2D(lobules, settings, plane="xy", resolution=200):
    """
    Visualize Gaussian lobules + anatomy reference
    """

    radius = settings.model.geometry.radius
    nipple = settings.material.glandular.hetero.nipple

    # grid
    x = np.linspace(-radius, radius, resolution)
    y = np.linspace(0.0, radius, resolution)

    X, Y = np.meshgrid(x, y)
    field = np.zeros_like(X)

    for L in lobules:
        cx, cy, cz = L.center
        s = L.width
        amp = L.amp_rho

        dx = X - cx
        dy = Y - cy

        field += amp * np.exp(-(dx**2 + dy**2) / (s**2))

    plt.figure(figsize=(6, 6))

    # heatmap
    plt.imshow(
        field,
        extent=[x.min(), x.max(), y.min(), y.max()],
        origin="lower"
    )

    plt.colorbar(label="Glandular density")

    # 🔵 breast boundary (circle cross-section)
    theta = np.linspace(0, np.pi, 200)
    bx = radius * np.cos(theta)
    by = radius * np.sin(theta)
    plt.plot(bx, by, 'w--', label="breast boundary")

    # 🔴 nipple
    plt.scatter(nipple[0], nipple[1], c='red', s=80, label="nipple")

    # ⚫ lobule centers
    for L in lobules:
        plt.scatter(L.center[0], L.center[1], c='black', s=10)

    plt.xlabel("x")
    plt.ylabel("y")
    plt.title("Glandular field with anatomy reference")
    plt.legend()
    plt.tight_layout()
    plt.show()