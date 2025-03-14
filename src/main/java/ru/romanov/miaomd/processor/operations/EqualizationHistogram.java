package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Эквализация гистограммы
 */
public class EqualizationHistogram {

    /**
     * Применяет эквализацию гистограммы к изображению.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение после эквализации гистограммы
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // Массив частот яркости (0–255)
        int[] histogram = new int[256];

        // Подсчёт яркости для каждого пикселя
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = ColorUtil.getGray(bufferedImage.getRGB(x, y));
                histogram[gray]++;
            }
        }

        // Вычисление CDF (кумулятивной суммы)
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histogram[i];
        }

        // Нормализация CDF
        int totalPixels = width * height;
        int minCdf = 0;
        for (int value : cdf) {
            if (value != 0) {
                minCdf = value;
                break;
            }
        }

        int[] equalizedLut = new int[256];
        for (int i = 0; i < 256; i++) {
            equalizedLut[i] = (int) (((double) (cdf[i] - minCdf) / (totalPixels - minCdf)) * 255);
        }

        // Преобразование изображения
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = ColorUtil.getGray(bufferedImage.getRGB(x, y));
                int equalizedGray = equalizedLut[gray];
                int rgb = (equalizedGray << 16) | (equalizedGray << 8) | equalizedGray; // Создание нового серого пикселя
                resultImage.setRGB(x, y, rgb);
            }
        }

        return resultImage;
    }
}
