module com.tool.translator.translator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires opencc4j;

    opens com.tool.translator to javafx.fxml;
    exports com.tool.translator;
}