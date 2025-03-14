package ru.romanov.miaomd.utils;

import ru.romanov.miaomd.model.RedGreenBlueAlpha;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtil {

    public static BufferedImage applyMaskToImage(BufferedImage originalImage, BufferedImage maskImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, originalImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Получаем исходный пиксель и соответствующую маску
                int originalPixel = originalImage.getRGB(x, y);
                int maskPixel = maskImage.getRGB(x, y);

                // Извлечение яркости из маски
                int maskIntensity = ColorUtil.px2rgba(maskPixel).getRed(); // Поскольку это градиент, R=G=B

                // Применение маски к исходному пикселю (например, модификация яркости)
                RedGreenBlueAlpha originalColor = ColorUtil.px2rgba(originalPixel);
                int newRed = ColorUtil.clamp(originalColor.getRed() - maskIntensity);
                int newGreen = ColorUtil.clamp(originalColor.getGreen() - maskIntensity);
                int newBlue = ColorUtil.clamp(originalColor.getBlue() - maskIntensity);

                // Создание нового пикселя
                int newPixel = ColorUtil.createRgb(newRed, newGreen, newBlue);
                outputImage.setRGB(x, y, newPixel);
            }
        }

        display(maskImage);

        return outputImage;
    }

    public static void display(BufferedImage image) {
        // Создание окна
        JFrame frame = new JFrame("Маска");

        // Создание панели для отображения изображения
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Получение размеров окна
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                // Вычисление новых размеров с сохранением пропорций
                double aspectRatio = (double) image.getWidth() / image.getHeight();
                int newWidth, newHeight;
                if (panelWidth / aspectRatio <= panelHeight) {
                    newWidth = panelWidth;
                    newHeight = (int) (panelWidth / aspectRatio);
                } else {
                    newHeight = panelHeight;
                    newWidth = (int) (panelHeight * aspectRatio);
                }

                // Масштабирование и отрисовка изображения
                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                g.drawImage(scaledImage, (panelWidth - newWidth) / 2, (panelHeight - newHeight) / 2, this);
            }
        };

        // Добавление панели в окно
        frame.getContentPane().add(panel);
        frame.setSize(800, 600); // Установка начального размера окна
        frame.setLocationRelativeTo(null); // Центрирование окна
        frame.setVisible(true);
    }
}
