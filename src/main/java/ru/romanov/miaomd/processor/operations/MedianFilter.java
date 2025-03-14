package ru.romanov.miaomd.processor.operations;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Медианный фильтр
 */
public class MedianFilter {

    public static BufferedImage apply(BufferedImage bufferedImage) {
        if (bufferedImage != null) {
            BufferedImage outputImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
            int[] window = new int[9];

            for (int y = 1; y < bufferedImage.getHeight() - 1; y++) {
                for (int x = 1; x < bufferedImage.getWidth() - 1; x++) {
                    int i = 0;

                    for (int ky = -1; ky <= 1; ky++) {
                        for (int kx = -1; kx <= 1; kx++) window[i++] = bufferedImage.getRGB(x + kx, y + ky);
                    }
                    java.util.Arrays.sort(window);
                    outputImage.setRGB(x, y, window[4]); // Средний элемент
                }
            }

            return outputImage;
        }
        return null;
    }
}
