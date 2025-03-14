package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;
import ru.romanov.miaomd.utils.MenuUtil;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

/**
 * Класс реализации метода Лапласиан
 */
public class Laplacian {

    private static final String METHOD_NAME = "[Вырезание диапазона яркостей]";
    private static final int[][] KERNEL_90 = {
            {0, -1, 0},
            {-1, 4, -1},
            {0, -1, 0}
    };
    private static final int[][] KERNEL_45 = {
            {-1, -1, -1},
            {-1, 8, -1},
            {-1, -1, -1}
    };
    private static final String k90 = "Кратные 90 градусов";
    private static final String k45 = "Кратные 45 градусов";

    /**
     * Применяет Лапласиан к изображению.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение после применения фильтра
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        // Ядро Лапласиана
        int[][] kernel = showKernelDialog();

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, bufferedImage.getType());

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int laplacian = 0;

                // Применение ядра
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = ColorUtil.getGray(bufferedImage.getRGB(x + kx, y + ky));
                        laplacian += pixel * kernel[ky + 1][kx + 1];
                    }
                }

                // Ограничиваем результат и устанавливаем пиксель
                int newPixel = ColorUtil.clamp(laplacian);
                int outputPixel = (0xFF << 24) | (newPixel << 16) | (newPixel << 8) | newPixel;
                outputImage.setRGB(x, y, outputPixel);
            }
        }

        return outputImage;
    }

    public static int[][] showKernelDialog() {
        Optional<String> result = MenuUtil.showSelectionDialog(
                List.of(k45, k90),
                "Маска фильтра"
        );

        return switch (result.orElse("")) {
            case k90 -> KERNEL_90;
            case k45 -> KERNEL_45;
            default -> showKernelDialog();
        };
    }
}
