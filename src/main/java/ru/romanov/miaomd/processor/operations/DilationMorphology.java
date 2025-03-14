package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Морфология: дилатация
 */
public class DilationMorphology {

    // Структурный элемент 3x3
    private static final int[][] structElem = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };

    /**
     * Применяет морфологическую операцию дилатации (расширения) к изображению.
     *
     * @param bufferedImage Исходное бинарное изображение
     * @return Новое изображение после применения дилатации
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // Копируем входное изображение
        BufferedImage resultImage = new BufferedImage(width, height, bufferedImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (applyDilation(bufferedImage, x, y, structElem)) resultImage.setRGB(x, y, ColorUtil.createWhite()); // Белый цвет
                else resultImage.setRGB(x, y, ColorUtil.createBlack()); // Черный цвет
            }
        }

        return resultImage;
    }

    /**
     * Применяет структурный элемент для дилатации к пикселю.
     *
     * @param image       Исходное изображение
     * @param x           Координата X пикселя
     * @param y           Координата Y пикселя
     * @param structElem  Структурный элемент
     * @return true, если пиксель должен быть белым, иначе false
     */
    private static boolean applyDilation(BufferedImage image, int x, int y, int[][] structElem) {
        int structHeight = structElem.length;
        int structWidth = structElem[0].length;
        int offsetX = structWidth / 2;
        int offsetY = structHeight / 2;

        for (int j = 0; j < structHeight; j++) {
            for (int i = 0; i < structWidth; i++) {
                int pixelX = x + i - offsetX;
                int pixelY = y + j - offsetY;

                if (pixelX >= 0 && pixelX < image.getWidth() && pixelY >= 0 && pixelY < image.getHeight()) {
                    int pixelColor = image.getRGB(pixelX, pixelY) & 0xFFFFFF; // Извлечение цвета
                    if (structElem[j][i] == 1 && pixelColor == 0xFFFFFF) {
                        return true; // Если есть совпадение с белым пикселем
                    }
                }
            }
        }

        return false;
    }
}