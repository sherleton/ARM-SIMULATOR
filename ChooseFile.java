import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
/**
 * Class used to create a GUI interface to select the .MEM file from windows, this eliminate the need to determine 
 * the path of file every timea different file is to be loaded in ARM-Simulator.This class uses JavaFX to create stages and filechooser.
 * @author Kshitiz, Nikhil, Apoorv
 * 
 */
public final class ChooseFile extends Application {
 
	
    public static String path=null;
    public static boolean flag=false;
    public static Stage stage;

    @Override
    public void start(final Stage fstage) {
        stage=new Stage();
        stage.setTitle("Select Flie");
        FileChooser chooser = new FileChooser();
        Button openButton = new Button("Open a txt file");
        Label text=new Label("     Select File");
        Button submit=new Button("Submit File");
        
        openButton.setOnMouseClicked(e->{
            File file = chooser.showOpenDialog(stage);
            if (file != null) 
            {
                path=file.getAbsolutePath();
                text.setText("   "+file.getName());
                flag=true;
            }
        });
        submit.setOnMouseClicked(e ->{
            if(flag==true){
                try {
                    Read.readingfile();
                    System.exit(0);

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                flag=false;
            }
        });
        GridPane inputGridP = new GridPane();
        GridPane.setConstraints(openButton, 0, 0);
        GridPane.setConstraints(text, 0, 5);
        GridPane.setConstraints(submit,0, 9);
        inputGridP.setHgap(6);
        inputGridP.setVgap(6);
        inputGridP.getChildren().addAll(openButton,text,submit);
        Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridP);
        rootGroup.setPadding(new Insets(50, 100, 100, 100));
        stage.setScene(new Scene(rootGroup));
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
}