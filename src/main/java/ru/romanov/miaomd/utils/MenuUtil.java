package ru.romanov.miaomd.utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MenuUtil {

    public static Optional<String[]> showInputDialog(
            List<Map.Entry<String, String>> entries,
            String title,
            String headerText
    ) {
        // Создаем диалоговое окно
        JDialog dialog = new JDialog((Frame) null, title, true);
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Заголовок
        if (headerText != null) {
            JLabel headerLabel = new JLabel(headerText);
            headerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
            contentPanel.add(headerLabel, BorderLayout.NORTH);
        }

        // Панель с полями ввода
        JPanel inputPanel = new JPanel(new GridLayout(entries.size(), 2, 5, 5));
        List<JTextField> textFields = new ArrayList<>();

        entries.forEach(entry -> {
            JTextField textField = new JTextField(entry.getValue());

            inputPanel.add(new JLabel(entry.getKey()));
            inputPanel.add(textField);
            textFields.add(textField);
        });

        contentPanel.add(inputPanel, BorderLayout.CENTER);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        final String[] result = new String[entries.size()];
        final AtomicBoolean isOk = new AtomicBoolean(false);

        okButton.addActionListener(_ -> {
            for (int i = 0; i < textFields.size(); i++) {
                result[i] = textFields.get(i).getText();
            }
            isOk.set(true);
            dialog.dispose();
        });

        techInit(cancelButton, dialog, buttonPanel, okButton, contentPanel);

        return isOk.get() ? Optional.of(result) : Optional.empty();
    }

    public static Optional<String> showSelectionDialog(
            List<String> options,
            String title
    ) {
        List<String> elements = new ArrayList<>();
        elements.add("Выберите ...");
        elements.addAll(options);

        // Создаем диалоговое окно
        JDialog dialog = new JDialog((Frame) null, title, true);
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Основная панель с элементами
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Выпадающий список
        JComboBox<String> comboBox = new JComboBox<>(elements.toArray(String[]::new));
        mainPanel.add(comboBox);

        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        final AtomicReference<String> result = new AtomicReference<>();
        final AtomicBoolean isOk = new AtomicBoolean(false);

        okButton.addActionListener(_ -> {
            result.set((String) comboBox.getSelectedItem());
            isOk.set(true);
            dialog.dispose();
        });

        techInit(cancelButton, dialog, buttonPanel, okButton, contentPanel);

        return isOk.get() ? Optional.ofNullable(result.get()) : Optional.empty();
    }

    private static void techInit(JButton cancelButton, JDialog dialog, JPanel buttonPanel, JButton okButton, JPanel contentPanel) {
        cancelButton.addActionListener(_ -> dialog.dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Настройка диалога
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.setContentPane(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
