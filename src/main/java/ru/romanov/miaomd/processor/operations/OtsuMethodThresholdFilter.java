package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Пороговый фильтр методом Оцу
 */
public class OtsuMethodThresholdFilter {

    /**
     * Применяет пороговый фильтр методом Оцу к изображению.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение после применения порогового фильтра
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int[] histogram = calculateHistogram(bufferedImage); // Вычисляем гистограмму
        int threshold = calculateOtsuThreshold(histogram, bufferedImage); // Вычисляем оптимальный порог методом Оцу

        // Создаем новое бинаризованное изображение
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = ColorUtil.getGray(bufferedImage.getRGB(x, y));
                int binaryColor = (gray >= threshold) ? ColorUtil.createWhite() : ColorUtil.createBlack();
                resultImage.setRGB(x, y, binaryColor);
            }
        }

        return resultImage;
    }

    /**
     * Вычисляет гистограмму яркости изображения.
     *
     * @param bufferedImage Исходное изображение
     * @return Гистограмма (массив с 256 значениями)
     */
    private static int[] calculateHistogram(BufferedImage bufferedImage) {
        int[] histogram = new int[256];
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int gray = ColorUtil.getGray(bufferedImage.getRGB(x, y));
                histogram[gray]++;
            }
        }
        return histogram;
    }

    /**
     * Вычисляет оптимальный порог методом Оцу.
     *
     * @param histogram     Гистограмма яркости
     * @param bufferedImage Исходное изображение
     * @return Оптимальное значение порога
     */
    private static int calculateOtsuThreshold(int[] histogram, BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int brightness = ColorUtil.getGray(bufferedImage.getRGB(x, y));
                histogram[brightness]++;
            }
        }

        double total = width * (double) height;
        double sumB = 0;
        double wB = 0;
        double maximum = 0.0;
        double threshold = 0.0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB != 0) {
                double wF = total - wB;
                if (wF != 0) {
                    sumB += i * histogram[i];
                    double mB = sumB / wB;
                    double betweenClassVariance = wB * wF * Math.pow(mB, 2);
                    if (betweenClassVariance > maximum) {
                        maximum = betweenClassVariance;
                        threshold = i;
                    }
                } else {
                    break;
                }
            }
        }

        return maximum == 0 ? 100 : (int) threshold;
    }
}
