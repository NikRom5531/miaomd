package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Негатив
 */
public class Negative {

    public static BufferedImage apply(BufferedImage bufferedImage) {

        if (bufferedImage != null) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                for (int x = 0; x < bufferedImage.getWidth(); x++) {
                    int rgba = bufferedImage.getRGB(x, y);
                    int r = 255 - ColorUtil.getRed(rgba);
                    int g = 255 - ColorUtil.getGreen(rgba);
                    int b = 255 - ColorUtil.getBlue(rgba);
                    int newRgba = ColorUtil.createRgba(r, g, b, ColorUtil.getAlpha(rgba));
                    bufferedImage.setRGB(x, y, newRgba);
                }
            }
        }

        return bufferedImage;
    }
}
