package ru.romanov.miaomd.processor.operations;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Морфология: размыкание
 */
public class OpeningMorphology {

    /**
     * Применяет морфологическую операцию размыкания к изображению.
     *
     * @param bufferedImage Исходное бинарное изображение
     * @return Новое изображение после применения размыкания
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage erodedImage = ErosionMorphology.apply(bufferedImage);
        return DilationMorphology.apply(erodedImage);
    }
}
