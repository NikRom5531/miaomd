module ru.romanov.miaomd {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.swing;
    requires static lombok;

    opens ru.romanov.miaomd to javafx.fxml;
    exports ru.romanov.miaomd;
    exports ru.romanov.miaomd.processor;
    opens ru.romanov.miaomd.processor to javafx.fxml;


}