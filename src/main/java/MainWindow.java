import javafx.application.Platform;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.Stage;

import kappa.elements.Ui;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Kappa kappa;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/doge.png"));
    private Image kappaImage = new Image(this.getClass().getResourceAsStream("/images/kermit.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().addAll(
                DialogBox.getKappaDialog(Ui.showWelcomeMessage(), kappaImage)
        );
    }

    void setDuke(Kappa d) {
        kappa = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Kappa's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = kappa.getResponse(input);
        try {
            if (input.substring(0,3).equals("bye")) {
                createExitPopup();
                new ExitProgram();
            }
        } catch (IndexOutOfBoundsException error) {
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getKappaDialog(response, kappaImage)
            );
            userInput.clear();
        }
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getKappaDialog(response, kappaImage)
        );
        userInput.clear();
    }

    /**
     * Creates exit popup box to say goodbye to the user.
     */
    private void createExitPopup() {
        Stage popup = new Stage();
        popup.setWidth(400);
        popup.setHeight(300);
        ImageView imageView = new ImageView(kappaImage);
        Text text = new Text("Dont worry. I'll keep your tasks safe heheh");
        VBox vbox = new VBox(text, imageView);
        Scene scene = new Scene(vbox, 600, 300);
        popup.setScene(scene);
        popup.setTitle("Goodbye!");
        popup.show();
    }

    /**
     * Private static class that delays the closing of the application until the exit message has been loaded.
     */
    static class ExitProgram {

        Timer timer;

        ExitProgram() {
            timer = new Timer();
            timer.schedule(new RemindTask(), 3000);
        }

        private class RemindTask extends TimerTask {
            public void run() {
                Platform.exit();
                timer.cancel(); //Terminate the timer thread
            }
        }

    }

}
