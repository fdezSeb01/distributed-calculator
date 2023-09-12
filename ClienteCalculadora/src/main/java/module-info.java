module com.upcompdistr.calculadora {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.upcompdistr.calculadora to javafx.fxml;
    exports com.upcompdistr.calculadora;
}
