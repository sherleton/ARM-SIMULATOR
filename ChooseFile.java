import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public final class ChooseFile extends Application {
 
    private Desktop desktop = Desktop.getDesktop();
    public static String path=null;
 
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Select Flie");
        FileChooser chooser = new FileChooser();
        Button openButton = new Button("Open a txt file");
 
        openButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) 
                {
                    File file = chooser.showOpenDialog(stage);
                    if (file != null) 
                    {
                        openFile(file);
                        path=file.getAbsolutePath();
                    }
                }
            }); 
        final GridPane inputGridP = new GridPane();
        GridPane.setConstraints(openButton, 0, 0);
        inputGridP.setHgap(6);
        inputGridP.setVgap(6);
        inputGridP.getChildren().add(openButton);
        Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridP);
        rootGroup.setPadding(new Insets(200, 200, 200, 200));
        stage.setScene(new Scene(rootGroup));
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                ChooseFile.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
}