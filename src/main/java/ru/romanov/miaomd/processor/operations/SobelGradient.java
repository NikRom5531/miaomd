package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Градиент Собеля
 */
public class SobelGradient {

    /**
     * Применяет градиент Собеля к изображению.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение после применения фильтра
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, bufferedImage.getType());

        // Собелевские ядра
        int[][] gx = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] gy = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gradientX = 0;
                int gradientY = 0;

                // Применяем ядра
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = ColorUtil.getGray(bufferedImage.getRGB(x + kx, y + ky));
                        gradientX += pixel * gx[ky + 1][kx + 1];
                        gradientY += pixel * gy[ky + 1][kx + 1];
                    }
                }

                // Рассчитываем результирующий градиент
                int magnitude = ColorUtil.clamp((int) Math.sqrt(gradientX * gradientX + gradientY * gradientY));

                // Устанавливаем новый пиксель в оттенке серого
                int newPixel = (0xFF << 24) | (magnitude << 16) | (magnitude << 8) | magnitude;
                outputImage.setRGB(x, y, newPixel);
            }
        }

        return outputImage;
    }
}
