package ru.romanov.miaomd.processor;

import lombok.Getter;
import lombok.Setter;
import ru.romanov.miaomd.model.OperationEnum;
import ru.romanov.miaomd.processor.operations.ClosureMorphology;
import ru.romanov.miaomd.processor.operations.CutBrightnessRange;
import ru.romanov.miaomd.processor.operations.DilationMorphology;
import ru.romanov.miaomd.processor.operations.EqualizationHistogram;
import ru.romanov.miaomd.processor.operations.ErosionMorphology;
import ru.romanov.miaomd.processor.operations.GaussianBlur;
import ru.romanov.miaomd.processor.operations.GlobalThresholdFilter;
import ru.romanov.miaomd.processor.operations.ImageHistogram;
import ru.romanov.miaomd.processor.operations.Laplacian;
import ru.romanov.miaomd.processor.operations.LinearSmoothingFilter;
import ru.romanov.miaomd.processor.operations.MedianFilter;
import ru.romanov.miaomd.processor.operations.Negative;
import ru.romanov.miaomd.processor.operations.OpeningMorphology;
import ru.romanov.miaomd.processor.operations.OtsuMethodThresholdFilter;
import ru.romanov.miaomd.processor.operations.PowerLawTransformation;
import ru.romanov.miaomd.processor.operations.RobertsGradient;
import ru.romanov.miaomd.processor.operations.SelectionBoundariesMorphology;
import ru.romanov.miaomd.processor.operations.SkeletonMorphology;
import ru.romanov.miaomd.processor.operations.SobelGradient;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Arrays;

public class ImageProcessor {

    private final JPanel originalPanel;
    private final JPanel resultPanel;

    @Getter
    @Setter
    private BufferedImage originalImage;

    @Getter
    @Setter
    private BufferedImage resultImage;

    @Getter
    @Setter
    private boolean applyToResult = false;

    public ImageProcessor(JPanel original, JPanel result) {
        this.originalPanel = original;
        this.resultPanel = result;
    }

    public void setImage(BufferedImage bufferedImage) {
        this.originalImage = bufferedImage;
        this.resultImage = cloneImage(bufferedImage);
        updatePanel(originalPanel, originalImage);
        updatePanel(resultPanel, null);
    }

    private BufferedImage cloneImage(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return newImage;
    }

    private void updatePanel(JPanel panel, BufferedImage image) {
        panel.removeAll();
        if (image != null) {
            JLabel label = new JLabel(new ImageIcon(image)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics g2 = g.create();
                    g.dispose();

                    extracted(g2, image, getWidth(), getHeight(), this);

                    super.paintComponent(g);
                }
            };
            label.setHorizontalAlignment(JLabel.CENTER);
            panel.add(label, BorderLayout.CENTER);
        }
        panel.revalidate();
        panel.repaint();
    }


    private void extracted(
            Graphics g,
            BufferedImage image,
            int width,
            int height,
            ImageObserver observer
    ) {
        // Вычисление новых размеров с сохранением пропорций
        double aspectRatio = (double) image.getWidth() / image.getHeight();
        int newWidth, newHeight;
        if (width / aspectRatio <= height) {
            newWidth = width;
            newHeight = (int) (width / aspectRatio);
        } else {
            newHeight = height;
            newWidth = (int) (height * aspectRatio);
        }

        // Масштабирование и отрисовка изображения
        java.awt.Image scaledImage = image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
        g.drawImage(scaledImage, (width - newWidth) / 2, (height - newHeight) / 2, observer);
    }

    private BufferedImage getCurrentImage() {
        return applyToResult ? resultImage : originalImage;
    }

    public void createProcessMenu(JMenu processMenu) {
        Arrays.stream(OperationEnum.values()).forEach(o -> {
            JMenuItem menuItem = new JMenuItem(o.getString());
            menuItem.addActionListener(e -> applyOperation(o));
            processMenu.add(menuItem);
        });
    }

    private void applyOperation(OperationEnum operation) {
        BufferedImage sourceImage = getCurrentImage();
        if (sourceImage == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Сначала загрузите изображение",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        BufferedImage result = applyImageOperation(cloneImage(sourceImage), operation);
        if (result != null) {
            this.resultImage = result;
            updatePanel(resultPanel, resultImage);
        }
    }

    /**
     * Применяет методы:
     * <ol>
     *     <li>Негатив +
     *     <li>Степенное преобразование +
     *     <li>Вырезание диапазона яркостей +
     *     <li>Линейный сглаживающий (усредняющий) фильтр +
     *     <li>Медианный фильтр +
     *     <li>Градиент Робертса +
     *     <li>Градиент Собеля +
     *     <li>Лапласиан +
     *     <li>Гистограмма изображения +
     *     <li>Эквализация гистограммы +
     *     <li>Пороговый фильтр с глобальным порогом +
     *     <li>Пороговый фильтр методом Оцу +
     *     <li>Морфология: дилатация +
     *     <li>Морфология: эрозия +
     *     <li>Морфология: замыкание +
     *     <li>Морфология: размыкание +
     *     <li>Морфология: выделение границ +
     *     <li>Морфология: остов +
     * </ol>
     * Для курсовой работы:
     * <ol>
     *     <li>Гауссов фильтр
     * </ol>
     */
    private BufferedImage applyImageOperation(BufferedImage image, OperationEnum operation) {
        try {
            return switch (operation) {
                case NEGATIVE -> Negative.apply(image);
                case POWER_LAW_TRANSFORMATION -> PowerLawTransformation.apply(image);
                case CUT_BRIGHTNESS_RANGE -> CutBrightnessRange.apply(image);
                case LINEAR_SMOOTHING_FILTER -> LinearSmoothingFilter.apply(image);
                case MEDIAN_FILTER -> MedianFilter.apply(image);
                case ROBERTS_GRADIENT -> RobertsGradient.apply(image);
                case SOBEL_GRADIENT -> SobelGradient.apply(image);
                case LAPLACIAN -> Laplacian.apply(image);
                case IMAGE_HISTOGRAM -> ImageHistogram.apply(image);
                case EQUALIZATION_HISTOGRAM -> EqualizationHistogram.apply(image);
                case GLOBAL_THRESHOLD_FILTER -> GlobalThresholdFilter.apply(image);
                case OTSU_METHOD_THRESHOLD_FILTER -> OtsuMethodThresholdFilter.apply(image);
                case DILATION_MORPHOLOGY -> DilationMorphology.apply(image);
                case EROSION_MORPHOLOGY -> ErosionMorphology.apply(image);
                case CLOSURE_MORPHOLOGY -> ClosureMorphology.apply(image);
                case OPENING_MORPHOLOGY -> OpeningMorphology.apply(image);
                case SELECTION_BOUNDARIES_MORPHOLOGY -> SelectionBoundariesMorphology.apply(image, cloneImage(image));
                case SKELETON_MORPHOLOGY -> SkeletonMorphology.apply(image);
                case GAUSSIAN_BLUR -> GaussianBlur.apply(image);
                default -> throw new IllegalArgumentException("Неизвестная операция");
            };
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка обработки: " + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }
}