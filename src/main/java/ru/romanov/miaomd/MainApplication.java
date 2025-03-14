package ru.romanov.miaomd;

import ru.romanov.miaomd.exception.ExceptionHandler;
import ru.romanov.miaomd.processor.ImageMenu;
import ru.romanov.miaomd.processor.ImageProcessor;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

public class MainApplication {
    private JPanel originalImagePanel;
    private JPanel resultImagePanel;
    private ImageProcessor imageProcessor;
    private JCheckBox applyToResultCheckBox;
    private ImageMenu imageMenu;

    public static void main(String[] args) {
        ExceptionHandler.setGlobalExceptionHandler();
        SwingUtilities.invokeLater(() -> {
            MainApplication app = new MainApplication();
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Image Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Панели для изображений
        originalImagePanel = createImagePanel();
        resultImagePanel = createImagePanel();

        // Инициализация компонентов
        imageProcessor = new ImageProcessor(originalImagePanel, resultImagePanel);
        imageMenu = new ImageMenu(imageProcessor, frame);

        setupUI(frame, imageMenu);

        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupUI(JFrame frame, ImageMenu imageMenu) {
        // Основная панель с BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Создание скролл-панелей
        JScrollPane originalScrollPane = new JScrollPane(originalImagePanel);
        JScrollPane resultScrollPane = new JScrollPane(resultImagePanel);

        // Панель для двух изображений
        JPanel imageBox = new JPanel(new GridLayout(1, 2, 10, 10));
        imageBox.add(originalScrollPane);
        imageBox.add(resultScrollPane);

        // Чекбокс
        applyToResultCheckBox = new JCheckBox("Применить к результату");
        applyToResultCheckBox.addActionListener(e ->
                imageProcessor.setApplyToResult(applyToResultCheckBox.isSelected())
        );

        // Добавление компонентов на основную панель
        mainPanel.add(imageMenu.getMenuBar(), BorderLayout.NORTH);
        mainPanel.add(imageBox, BorderLayout.CENTER);
        mainPanel.add(applyToResultCheckBox, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(400, 300);
            }
        };
        panel.setBackground(Color.GRAY);
        return panel;
    }
}
