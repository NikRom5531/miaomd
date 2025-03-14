package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;
import ru.romanov.miaomd.utils.MenuUtil;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс реализации метода Пороговый фильтр с глобальным порогом
 */
public class GlobalThresholdFilter {

    /**
     * Применяет пороговый фильтр с глобальным порогом к изображению.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение после применения порогового фильтра
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int threshold = 255 - showThresholdDialog();
        if (threshold == 255) threshold = calculateThreshold(bufferedImage);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // Создаем новое изображение
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Извлекаем яркость пикселя
                int gray = ColorUtil.getGray(bufferedImage.getRGB(x, y));
                // Применяем порог
                int binaryColor = (gray >= threshold) ? 0xFFFFFF : 0x000000;
                resultImage.setRGB(x, y, binaryColor);
            }
        }

        return resultImage;
    }

    public static int showThresholdDialog() {
        Optional<String[]> result = MenuUtil.showInputDialog(
                List.of(Map.entry("Значение порога (0–255):", "255")),
                "Threshold",
                "Введите значение порога"
        );

        return result.map(values -> {
            try {
                return Integer.parseInt(values[0]);
            } catch (NumberFormatException e) {
                return 255; // Если ввод некорректный, возвращаем значения по умолчанию
            }
        }).orElse(255);
    }

    /**
     * Автоматический расчет порога на основе средней яркости.
     *
     * @param bufferedImage Исходное изображение
     * @return Среднее значение яркости (глобальный порог)
     */
    public static int calculateThreshold(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        long sum = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sum += ColorUtil.getGray(bufferedImage.getRGB(x, y));
            }
        }

        return (int) (sum / (width * height)); // Средняя яркость
    }
}
