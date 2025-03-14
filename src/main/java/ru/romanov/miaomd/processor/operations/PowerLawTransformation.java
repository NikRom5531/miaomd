package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;
import ru.romanov.miaomd.utils.MenuUtil;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс реализации метода Степенное преобразование
 */
public class PowerLawTransformation {

    public static BufferedImage apply(BufferedImage bufferedImage) {
        double gamma = showGammaInputDialog();

        if (bufferedImage != null) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            double c = 1.0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    var rgba = ColorUtil.px2rgba(bufferedImage.getRGB(x, y));

                    // Применяем степенное преобразование к каждому каналу
                    int newR = powerLaw(c, rgba.getRed(), gamma);
                    int newG = powerLaw(c, rgba.getGreen(), gamma);
                    int newB = powerLaw(c, rgba.getBlue(), gamma);

                    // Создаем новый пиксель и устанавливаем его
                    bufferedImage.setRGB(x, y, ColorUtil.createRgba(newR, newG, newB, rgba.getAlpha()));
                }
            }
        }

        return bufferedImage;
    }

    public static double showGammaInputDialog() {
        Optional<String[]> result = MenuUtil.showInputDialog(
                List.of(Map.entry("Гамма:", "1.0")),
                "Gamma Correction",
                "Введите значение"
        );

        return result.map(values -> {
            try {
                return Double.parseDouble(values[0]); // Используем первое поле как значение гамма
            } catch (NumberFormatException e) {
                return 1.0; // Если ввод некорректный, возвращаем значение по умолчанию
            }
        }).orElse(1.0);
    }

    private static int powerLaw(double c, int color, double gamma) {
        return ColorUtil.clamp((int) (c * Math.pow(color / 255.0, gamma) * 255));
    }
}
