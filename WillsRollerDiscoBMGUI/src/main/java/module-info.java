module com.example.willsrollerdiscobmgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.knowm.xchart;
    requires mysql.connector.j;


    opens com.example.willsrollerdiscobmgui to javafx.fxml;
    exports com.example.willsrollerdiscobmgui;
}