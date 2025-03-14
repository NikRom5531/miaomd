package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Градиент Робертса
 */
public class RobertsGradient {

    /**
     * Применяет градиент Робертса к изображению.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение после применения фильтра
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, bufferedImage.getType());

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                // Получаем четыре соседних пикселя
                int pixel1 = bufferedImage.getRGB(x, y);       // P(x, y)
                int pixel2 = bufferedImage.getRGB(x + 1, y);   // P(x+1, y)
                int pixel3 = bufferedImage.getRGB(x, y + 1);   // P(x, y+1)
                int pixel4 = bufferedImage.getRGB(x + 1, y + 1); // P(x+1, y+1)

                // Извлечение яркости пикселей
                int intensity1 = ColorUtil.getGray(pixel1);
                int intensity2 = ColorUtil.getGray(pixel2);
                int intensity3 = ColorUtil.getGray(pixel3);
                int intensity4 = ColorUtil.getGray(pixel4);

                // Градиенты Робертса (с разницей соседних пикселей)
                int gradientX = Math.abs(intensity1 - intensity4);
                int gradientY = Math.abs(intensity2 - intensity3);

                // Результирующий градиент (по модулю)
                int magnitude = ColorUtil.clamp((int) Math.sqrt(gradientX * gradientX + gradientY * gradientY));

                // Установка нового пикселя в оттенке серого
                int newPixel = (0xFF << 24) | (magnitude << 16) | (magnitude << 8) | magnitude;
                outputImage.setRGB(x, y, newPixel);
            }
        }

        return outputImage;
    }
}
