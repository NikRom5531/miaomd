package ru.romanov.miaomd.processor.operations;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Линейный сглаживающий (усредняющий) фильтр
 */
public class LinearSmoothingFilter {

    /**
     * Применяет линейный сглаживающий фильтр к изображению.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение после применения фильтра
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, bufferedImage.getType());

        // Проходим по всем пикселям изображения
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int redSum = 0, greenSum = 0, blueSum = 0;

                // Окно свертки 3x3
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = bufferedImage.getRGB(x + kx, y + ky);

                        // Извлекаем компоненты цвета
                        int red = (pixel >> 16) & 0xFF;
                        int green = (pixel >> 8) & 0xFF;
                        int blue = pixel & 0xFF;

                        // Суммируем значения
                        redSum += red;
                        greenSum += green;
                        blueSum += blue;
                    }
                }

                // Усредняем значения (окно 3x3 содержит 9 пикселей)
                int redAvg = redSum / 9;
                int greenAvg = greenSum / 9;
                int blueAvg = blueSum / 9;

                // Собираем новый пиксель
                int newPixel = (0xFF << 24) | (redAvg << 16) | (greenAvg << 8) | blueAvg;

                // Устанавливаем новый пиксель в выходное изображение
                outputImage.setRGB(x, y, newPixel);
            }
        }

        return outputImage;
    }
}
