package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Морфология: выделение границ
 */
public class SelectionBoundariesMorphology {

    /**
     * Применяет морфологическую операцию выделения границ.
     * Эта операция использует разницу между дилатацией и эрозией.
     *
     * @param bufferedImage Исходное бинарное изображение
     * @return Новое изображение, где выделены только границы объектов
     */
    public static BufferedImage apply(BufferedImage bufferedImage, BufferedImage buf2) {
        BufferedImage erodedImage = ErosionMorphology.apply(buf2);
        return subtractImages(bufferedImage, erodedImage);
    }

    /**
     * Метод для вычисления разности между двумя изображениями.
     * Разница получается путём вычитания одного пикселя из другого.
     *
     * @param img1 Первое изображение (например, дилатация)
     * @param img2 Второе изображение (например, эрозия)
     * @return Результат разности изображений
     */
    private static BufferedImage subtractImages(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Проходим по всем пикселям и вычитаем значения
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel1 = img1.getRGB(x, y);
                int pixel2 = img2.getRGB(x, y);

                // Логика вычитания (например, просто сравниваем пиксели)
                // Здесь можно добавить дополнительную обработку для бинарных изображений
                if (pixel1 != pixel2) result.setRGB(x, y, ColorUtil.createWhite()); // Белый цвет для границы
                else result.setRGB(x, y, ColorUtil.createBlack()); // Черный цвет для фона

            }
        }

        return result;
    }
}
