package ru.romanov.miaomd.processor;

import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageMenu {
    @Getter
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("Файл");
    private final JMenu processMenu = new JMenu("Процесс");

    private final ImageProcessor imageProcessor;
    private final JFrame parentFrame;

    public ImageMenu(ImageProcessor imageProcessor, JFrame parentFrame) {
        this.imageProcessor = imageProcessor;
        this.parentFrame = parentFrame;
        createMenu();
    }

    private void createMenu() {
        createFileMenu();
        imageProcessor.createProcessMenu(processMenu);
        menuBar.add(fileMenu);
        menuBar.add(processMenu);
    }

    private void createFileMenu() {
        JMenuItem openItem = new JMenuItem("Открыть изображение");
        openItem.addActionListener(this::openImage);
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Сохранить изображение");
        saveItem.addActionListener(this::saveImage);
        fileMenu.add(saveItem);
    }

    private void openImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Изображения", "png", "jpg", "jpeg", "gif", "bmp"));

        int result = fileChooser.showOpenDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                imageProcessor.setImage(bufferedImage);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Ошибка загрузки изображения",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void saveImage(ActionEvent e) {
        BufferedImage image = imageProcessor.getResultImage();
        if (image == null) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Нет изображения для сохранения",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "PNG Images", "png"));

        int result = fileChooser.showSaveDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getParentFile(), file.getName() + ".png");
                }
                ImageIO.write(image, "png", file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Ошибка сохранения изображения",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}