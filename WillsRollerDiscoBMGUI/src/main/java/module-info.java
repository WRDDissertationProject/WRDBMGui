module com.example.willsrollerdiscobmgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.willsrollerdiscobmgui to javafx.fxml;
    exports com.example.willsrollerdiscobmgui;
}