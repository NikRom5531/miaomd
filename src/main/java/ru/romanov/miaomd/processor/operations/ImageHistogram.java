package ru.romanov.miaomd.processor.operations;

import ru.romanov.miaomd.utils.ColorUtil;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Класс реализации метода Гистограмма изображения
 */
public class ImageHistogram {

    /**
     * Создаёт гистограмму изображения.
     *
     * @param bufferedImage Исходное изображение
     * @return Новое изображение, представляющее гистограмму
     */
    public static BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // Массив частот яркости (0–255)
        int[] histogram = new int[256];

        showHistogram(histogram);

        // Заполнение гистограммы
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = ColorUtil.getGray(bufferedImage.getRGB(x, y));
                histogram[gray]++;
            }
        }

        // Нормализация частот для отображения
        int maxFrequency = 0;
        for (int value : histogram) {
            maxFrequency = Math.max(maxFrequency, value);
        }

        // Размеры изображения гистограммы
        int histogramWidth = 256; // По количеству уровней яркости
        int histogramHeight = 150; // Высота гистограммы
        BufferedImage histogramImage = new BufferedImage(histogramWidth, histogramHeight, BufferedImage.TYPE_INT_RGB);

        // Заполнение изображения гистограммы
        for (int x = 0; x < histogramWidth; x++) {
            int normalizedHeight = (int) ((histogram[x] / (double) maxFrequency) * histogramHeight);

            for (int y = histogramHeight - 1; y >= histogramHeight - normalizedHeight; y--) {
                histogramImage.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        return bufferedImage;
    }

    /**
     * Показывает гистограмму в отдельном окне.
     *
     * @param histogram Массив частот яркости
     */
    public static void showHistogram(int[] histogram) {
        // Создаём окно для отображения гистограммы
        JFrame frame = new JFrame("Гистограмма изображения");
        frame.setSize(600, 300);  // Размер окна можно настраивать

        // Панель управления диапазоном
        JPanel controlPanel = new JPanel();
        JSlider minSlider = new JSlider(0, 255, 0);
        JSlider maxSlider = new JSlider(0, 255, 255);

        HistogramPanel histogramPanel = new HistogramPanel(histogram);

        // Настройка слайдеров
        minSlider.setPaintTicks(true);
        minSlider.setMajorTickSpacing(32);
        maxSlider.setPaintTicks(true);
        maxSlider.setMajorTickSpacing(32);

        // Обработка изменений
        ChangeListener sliderListener = _ -> {
            int min = Math.min(minSlider.getValue(), maxSlider.getValue());
            int max = Math.max(minSlider.getValue(), maxSlider.getValue());

            if (minSlider.getValue() > maxSlider.getValue()) {
                int tmp = minSlider.getValue();
                minSlider.setValue(maxSlider.getValue());
                maxSlider.setValue(tmp);
            }

            histogramPanel.setRange(min, max);
        };

        minSlider.addChangeListener(sliderListener);
        maxSlider.addChangeListener(sliderListener);

        // Добавление компонентов
        controlPanel.add(new JLabel("Min:"));
        controlPanel.add(minSlider);
        controlPanel.add(new JLabel("Max:"));
        controlPanel.add(maxSlider);

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(histogramPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);  // Центрируем окно
        frame.setVisible(true);
    }

    /**
     * Панель для рисования гистограммы с улучшенным отображением.
     */
    static class HistogramPanel extends JPanel {
        private static final int BAR_GAP = 0;
        private static final Color AXIS_COLOR = new Color(0, 0, 0, 255);
        private static final Color GRID_COLOR = new Color(200, 200, 200);
        private static final Color BAR_COLOR = new Color(147, 0, 0);
        private static final Insets INSETS = new Insets(40, 60, 40, 40); // Верх, Левый, Низ, Правый

        private final int[] histogram;

        private int minRange = 0;
        private int maxRange = 255;

        public void setRange(int min, int max) {
            this.minRange = min;
            this.maxRange = max;
            repaint();
        }

        public HistogramPanel(int[] histogram) {
            this.histogram = histogram;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(
                    INSETS.top,
                    INSETS.left,
                    INSETS.bottom,
                    INSETS.right
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Рассчитываем доступную область для рисования
            int availableWidth = getWidth() - INSETS.left - INSETS.right;
            int availableHeight = getHeight() - INSETS.top - INSETS.bottom;
            int startX = INSETS.left;
            int startY = INSETS.top;

            // Рисуем сетку
            drawGrid(g2d, startX, startY, availableWidth, availableHeight);

            // Рисуем оси
            drawAxes(g2d, startX, startY, availableWidth, availableHeight);

            // Рисуем гистограмму
            drawBars(g2d, startX, startY, availableWidth, availableHeight);
        }

        private void drawGrid(Graphics2D g2d, int startX, int startY, int width, int height) {
            g2d.setColor(GRID_COLOR);

            // Вертикальные линии
            for (int i = 0; i <= 255; i += 32) {
                int x = startX + (int) (i * (width / 256.0));
                g2d.drawLine(x, startY, x, startY + height);
            }

            // Горизонтальные линии
            for (int i = 0; i <= 10; i++) {
                int y = startY + height - (i * height / 10);
                g2d.drawLine(startX, y, startX + width, y);
            }
        }

        private void drawAxes(Graphics2D g2d, int startX, int startY, int width, int height) {
            g2d.setColor(AXIS_COLOR);
            g2d.setStroke(new BasicStroke(0));

            // Ось Y
            g2d.drawLine(startX, startY, startX, startY + height);


            // Подписи оси X
            int maxFrequency = getMaxFrequency();
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            for (int i = minRange; i <= maxRange; i += 32) {
                String label = String.valueOf(i);
                int x = startX + (int) (i * (width / 256.0)) - 10;
                g2d.drawString(label, x, startY + height + 20);
            }

            // Подписи оси Y
            for (int i = 0; i <= 10; i++) {
                String label = String.valueOf(maxFrequency * i / 10);
                int y = startY + height - (i * height / 10) + 4;
                g2d.drawString(label, startX - 50, y);
            }
        }

        private int getMaxFrequency() {
            int maxFrequency = 0;

            for (int i = minRange; i <= maxRange; i++) {
                if (i >= 0 && i < histogram.length) maxFrequency = Math.max(maxFrequency, histogram[i]);
            }

            return maxFrequency == 0 ? 1 : maxFrequency;
        }

        private void drawBars(Graphics2D g2d, int startX, int startY, int width, int height) {
            // Рассчитываем максимальную частоту только в выбранном диапазоне
            int maxFrequency = getMaxFrequency();

            double barStep = width / 256.0;
            int visibleBars = maxRange - minRange + 1;
            int barWidth = (int) ((width / visibleBars) * (256.0 / visibleBars)) - BAR_GAP;
            barWidth = Math.min(barWidth, 2);

            g2d.setColor(BAR_COLOR);

            for (int i = 0; i < histogram.length; i++) {
                if (i < minRange || i > maxRange) continue;

                int barHeight = (int) ((histogram[i] / (double) maxFrequency) * height);
                int x = startX + (int) (i * (width / 256.0));
                int y = startY + height - barHeight;

                if (barHeight > 0) {
                    g2d.fillRect(x, y, barWidth > 1 ? barWidth : 2, barHeight);
                }
            }

            // Рисуем зону выделения
            g2d.setColor(new Color(0, 100, 200, 50));
            int selectionStart = startX + (int) (minRange * barStep);
            int selectionWidth = (int) ((maxRange - minRange + 1) * barStep);
            g2d.fillRect(selectionStart, startY, selectionWidth, height);
        }
    }
}
