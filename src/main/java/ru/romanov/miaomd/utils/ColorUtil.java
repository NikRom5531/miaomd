package ru.romanov.miaomd.utils;

import ru.romanov.miaomd.model.RedGreenBlueAlpha;

public class ColorUtil {

    /**
     * Получает красный компонент из 32-битного значения RGBA.
     *
     * @param rgba 32-битное цветовое значение
     * @return Значение красного компонента (0-255)
     */
    public static int getRed(int rgba) {
        return (rgba >> 16) & 0xFF;
    }

    /**
     * Получает зелёный компонент из 32-битного значения RGBA.
     *
     * @param rgba 32-битное цветовое значение
     * @return Значение зелёного компонента (0-255)
     */
    public static int getGreen(int rgba) {
        return (rgba >> 8) & 0xFF;
    }

    /**
     * Получает синий компонент из 32-битного значения RGBA.
     *
     * @param rgba 32-битное цветовое значение
     * @return Значение синего компонента (0-255)
     */
    public static int getBlue(int rgba) {
        return rgba & 0xFF;
    }

    /**
     * Получает альфа-компонент из 32-битного значения RGBA.
     *
     * @param rgba 32-битное цветовое значение
     * @return Значение альфа-компонента (0-255)
     */
    public static int getAlpha(int rgba) {
        return (rgba >> 24) & 0xFF;
    }

    /**
     * Создаёт 32-битное значение RGBA из отдельных значений компонентов.
     *
     * @param red   Значение красного компонента (0-255)
     * @param green Значение зелёного компонента (0-255)
     * @param blue  Значение синего компонента (0-255)
     * @param alpha Значение альфа-компонента (0-255)
     * @return 32-битное цветовое значение RGBA
     */
    public static int createRgba(int red, int green, int blue, int alpha) {
        return (clamp(alpha) << 24) | (clamp(red) << 16) | (clamp(green) << 8) | clamp(blue);
    }

    /**
     * Создаёт 32-битное значение RGB (с альфа по умолчанию = 255).
     *
     * @param red   Значение красного компонента (0-255)
     * @param green Значение зелёного компонента (0-255)
     * @param blue  Значение синего компонента (0-255)
     * @return 32-битное цветовое значение RGB с альфа = 255
     */
    public static int createRgb(int red, int green, int blue) {
        return createRgba(red, green, blue, 255); // Альфа по умолчанию
    }

    /**
     * Ограничивает значение в диапазоне [0, 255].
     *
     * @param value Значение цвета или альфа-компонента
     * @return Ограниченное значение (0-255)
     */
    public static int clamp(int value) {
        return clamp(value, 0, 255);
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Преобразует 32-битное значение пикселя в объект {@link RedGreenBlueAlpha}.
     *
     * @param px 32-битное значение пикселя
     * @return Объект {@link RedGreenBlueAlpha}
     */
    public static RedGreenBlueAlpha px2rgba(int px) {
        return new RedGreenBlueAlpha(getRed(px), getGreen(px), getBlue(px), getAlpha(px));
    }

    /**
     * Преобразует объект {@link RedGreenBlueAlpha} в 32-битное значение пикселя.
     *
     * @param rgba Объект {@link RedGreenBlueAlpha}
     * @return 32-битное значение пикселя
     */
    public static int rgba2px(RedGreenBlueAlpha rgba) {
        return createRgba(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
    }

    /**
     * Извлекает яркость (оттенок серого) из пикселя.
     *
     * @param pixel Пиксель изображения
     * @return Яркость (0-255)
     */
    public static int getGray(int pixel) {
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
        return (red + green + blue) / 3; // Усредненная яркость
    }

    /**
     * Создаёт Белое 32-битное значение RGB (с альфа по умолчанию = 255).
     *
     * @return Белое 32-битное цветовое значение RGB с альфа = 255
     */
    public static int createWhite() {
        return createRgba(255, 255, 255, 255); // Альфа по умолчанию
    }

    /**
     * Создаёт Чёрное 32-битное значение RGB (с альфа по умолчанию = 255).
     *
     * @return Чёрное 32-битное цветовое значение RGB с альфа = 255
     */
    public static int createBlack() {
        return createRgba(0, 0, 0, 0); // Альфа по умолчанию
    }
}
