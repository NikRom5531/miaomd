package ru.romanov.miaomd.processor.operations;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Морфология: замыкание
 */
public class ClosureMorphology {

    /**
     * Применяет морфологическую операцию замыкания к изображению.
     *
     * @param bufferedImage Исходное бинарное изображение
     * @return Новое изображение после применения замыкания
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        // Шаг 1: Дилатация
        BufferedImage dilatedImage = DilationMorphology.apply(bufferedImage);

        // Шаг 2: Эрозия
        return ErosionMorphology.apply(dilatedImage);
    }
}
