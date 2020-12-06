package UI;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class OrganizerCancelEventAlertBox {
    public static void display(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Cancel Event");
        window.setMinWidth(400);

        Label label = new Label();
        label.setText("Are you sure that you want to cancel this event?");
        Button confirmButton = new Button("Yes");
        Button denyButton = new Button("No");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, confirmButton, denyButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.showAndWait();
    }
}