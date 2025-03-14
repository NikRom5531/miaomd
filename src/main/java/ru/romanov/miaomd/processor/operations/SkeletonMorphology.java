package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Морфология: остов
 */
public class SkeletonMorphology {

    /**
     * Применяет морфологическую операцию остова.
     * Этот метод использует итеративное удаление пикселей, чтобы получить минимальную структуру.
     *
     * @param bufferedImage Исходное бинарное изображение
     * @return Новое изображение с остовом объектов
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        boolean[][] image = new boolean[height][width];
        boolean[][] marker = new boolean[height][width];

        // Инициализация массива пикселей
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = bufferedImage.getRGB(x, y);
                image[y][x] = (ColorUtil.getGray(rgb) > 128); // Белые пиксели как true
            }
        }

        boolean hasChanged;
        do {
            hasChanged = false;

            // Первый проход
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    if (image[y][x] && canBeRemoved(image, x, y, true)) {
                        marker[y][x] = true;
                        hasChanged = true;
                    }
                }
            }
            applyMarker(image, marker);

            // Второй проход
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    if (image[y][x] && canBeRemoved(image, x, y, false)) {
                        marker[y][x] = true;
                        hasChanged = true;
                    }
                }
            }
            applyMarker(image, marker);

        } while (hasChanged);

        // Создаем результирующее изображение
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = image[y][x] ? 0xFFFFFF : 0x000000;
                result.setRGB(x, y, color);
            }
        }

        return result;
    }

    private static void applyMarker(boolean[][] image, boolean[][] marker) {
        for (int y = 1; y < image.length - 1; y++) {
            for (int x = 1; x < image[0].length - 1; x++) {
                if (marker[y][x]) {
                    image[y][x] = false;
                    marker[y][x] = false;
                }
            }
        }
    }

    private static boolean canBeRemoved(boolean[][] image, int x, int y, boolean firstStep) {
        // Получаем значения соседних пикселей
        int p2 = getPixel(image, x, y-1);   // Вверх
        int p3 = getPixel(image, x+1, y-1); // Вверх-вправо
        int p4 = getPixel(image, x+1, y);   // Вправо
        int p5 = getPixel(image, x+1, y+1); // Вниз-вправо
        int p6 = getPixel(image, x, y+1);   // Вниз
        int p7 = getPixel(image, x-1, y+1); // Вниз-влево
        int p8 = getPixel(image, x-1, y);   // Влево
        int p9 = getPixel(image, x-1, y-1); // Влево-вверх

        // Вычисляем количество соседей
        int B = p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
        if (B < 2 || B > 6) return false;

        // Вычисляем количество переходов 0-1
        int A = 0;
        if (p2 == 0 && p3 == 1) A++;
        if (p3 == 0 && p4 == 1) A++;
        if (p4 == 0 && p5 == 1) A++;
        if (p5 == 0 && p6 == 1) A++;
        if (p6 == 0 && p7 == 1) A++;
        if (p7 == 0 && p8 == 1) A++;
        if (p8 == 0 && p9 == 1) A++;
        if (p9 == 0 && p2 == 1) A++;

        if (A != 1) return false;

        // Дополнительные условия для разных шагов
        if (firstStep) {
            return (p2 * p4 * p6) == 0 && (p4 * p6 * p8) == 0;
        } else {
            return (p2 * p4 * p8) == 0 && (p2 * p6 * p8) == 0;
        }
    }

    private static int getPixel(boolean[][] image, int x, int y) {
        if (x < 0 || y < 0 || x >= image[0].length || y >= image.length) {
            return 0;
        }
        return image[y][x] ? 1 : 0;
    }
}
