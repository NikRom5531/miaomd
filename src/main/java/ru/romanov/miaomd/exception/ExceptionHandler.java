package ru.romanov.miaomd.exception;

import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

public class ExceptionHandler {

    /**
     * Устанавливает глобальный перехватчик исключений.
     */
    public static void setGlobalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((_, throwable) -> showExceptionDialog(throwable));
    }

    /**
     * Показывает диалоговое окно с информацией об исключении.
     *
     * @param throwable Объект исключения
     */
    public static void showExceptionDialog(Throwable throwable) {
        // Форматирование текста исключения
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Уведомление об ошибке");
        alert.setHeaderText(throwable.getMessage());
        VBox dialogPaneContent = new VBox();
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }
}
