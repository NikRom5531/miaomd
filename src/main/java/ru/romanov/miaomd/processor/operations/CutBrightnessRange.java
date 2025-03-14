package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.model.RedGreenBlueAlpha;
import ru.romanov.miaomd.utils.ColorUtil;
import ru.romanov.miaomd.utils.MenuUtil;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс реализации метода Вырезание диапазона яркостей
 */
public class CutBrightnessRange {

    private static final double kr = 0.2126;
    private static final double kg = 0.7152;
    private static final double kb = 0.0722;

    public static BufferedImage apply(BufferedImage bufferedImage) {
        if (bufferedImage != null) {
            int[] brightnessRange = showMinMaxBrightnessDialog();
            double minBrightness = (double) brightnessRange[0] / 255;
            double maxBrightness = (double) brightnessRange[1] / 255;

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            BufferedImage outputImage = new BufferedImage(width, height, bufferedImage.getType());

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Извлекаем цветовые компоненты
                    RedGreenBlueAlpha rgba = ColorUtil.px2rgba(bufferedImage.getRGB(x, y));

                    // Преобразуем цвет в яркость (0.0 - чёрный, 1.0 - белый)
                    double brightness = (kr * rgba.getRed() + kg * rgba.getGreen() + kb * rgba.getBlue()) / 255;

                    // Проверяем диапазон яркости
                    if (minBrightness < brightness && brightness < maxBrightness) {
                        outputImage.setRGB(x, y, ColorUtil.rgba2px(rgba)); // Если оставляем пиксель без изменений
                    } else {
                        outputImage.setRGB(x, y, ColorUtil.createBlack()); // Иначе яркость вне диапазона, делаем пиксель чёрным
                    }
                }
            }

            return outputImage;
        }

        return null;
    }

    public static int[] showMinMaxBrightnessDialog() {
        Optional<String[]> result = MenuUtil.showInputDialog(
                List.of(
                        Map.entry("Минимальная яркость:", "0"),
                        Map.entry("Максимальная яркость:", "255")
                ),
                "Brightness Range",
                "Введите диапазон яркости"
        );

        return result.map(values -> {
            try {
                int min = Integer.parseInt(values[0]);
                int max = Integer.parseInt(values[1]);
                return new int[]{min, max};
            } catch (NumberFormatException e) {
                return new int[]{0, 255}; // Если ввод некорректный, возвращаем значения по умолчанию
            }
        }).orElse(new int[]{0, 255});
    }
}
