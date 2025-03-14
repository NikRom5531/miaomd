package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

public class GaussianBlur {

    private static final int DEFAULT_RADIUS = 3;
    private static final double DEFAULT_SIGMA = 1.0;

    public static BufferedImage apply(BufferedImage bufferedImage) {
        return apply(bufferedImage, DEFAULT_RADIUS, DEFAULT_SIGMA);
    }

    public static BufferedImage apply(BufferedImage bufferedImage, int radius, double sigma) {
        // Создаем временные буферы
        BufferedImage tempImage = new BufferedImage(
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        BufferedImage result = new BufferedImage(
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        // Генерируем ядро Гаусса
        double[] kernel = createGaussianKernel(radius, sigma);

        // Применяем фильтр сначала по горизонтали, затем по вертикали
        applyHorizontalPass(bufferedImage, tempImage, kernel, radius);
        applyVerticalPass(tempImage, result, kernel, radius);

        return result;
    }

    private static double[] createGaussianKernel(int radius, double sigma) {
        int size = radius * 2 + 1;
        double[] kernel = new double[size];
        double twoSigmaSq = 2 * sigma * sigma;
        double sigmaRoot = Math.sqrt(Math.PI * twoSigmaSq);
        double total = 0.0;

        for (int i = -radius; i <= radius; i++) {
            double value = Math.exp(-(i * i) / twoSigmaSq) / sigmaRoot;
            kernel[i + radius] = value;
            total += value;
        }

        // Нормализация ядра
        for (int i = 0; i < size; i++) {
            kernel[i] /= total;
        }

        return kernel;
    }

    private static void applyHorizontalPass(BufferedImage src, BufferedImage dst,
                                            double[] kernel, int radius) {
        int width = src.getWidth();
        int height = src.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = 0, g = 0, b = 0;

                for (int i = -radius; i <= radius; i++) {
                    int xi = ColorUtil.clamp(x + i, 0, width - 1);
                    int pixel = src.getRGB(xi, y);

                    double weight = kernel[i + radius];
                    r += ((pixel >> 16) & 0xFF) * weight;
                    g += ((pixel >> 8) & 0xFF) * weight;
                    b += (pixel & 0xFF) * weight;
                }

                int rgb = ((int) r << 16) | ((int) g << 8) | (int) b;
                dst.setRGB(x, y, rgb);
            }
        }
    }

    private static void applyVerticalPass(BufferedImage src, BufferedImage dst,
                                          double[] kernel, int radius) {
        int width = src.getWidth();
        int height = src.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double r = 0, g = 0, b = 0;

                for (int i = -radius; i <= radius; i++) {
                    int yi = ColorUtil.clamp(y + i, 0, height - 1);
                    int pixel = src.getRGB(x, yi);

                    double weight = kernel[i + radius];
                    r += ((pixel >> 16) & 0xFF) * weight;
                    g += ((pixel >> 8) & 0xFF) * weight;
                    b += (pixel & 0xFF) * weight;
                }

                int rgb = ((int) r << 16) | ((int) g << 8) | (int) b;
                dst.setRGB(x, y, rgb);
            }
        }
    }

//    public static int[][] showKernelDialog() {
//        Optional<String> result = MenuUtil.showSelectionDialog(
//                List.of(k45, k90),
//                "Маска фильтра"
//        );
//
//        return switch (result.orElse("")) {
//            case k90 -> KERNEL_90;
//            case k45 -> KERNEL_45;
//            default -> showKernelDialog();
//        };
//    }
}