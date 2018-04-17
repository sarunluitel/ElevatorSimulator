import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class GUIcontrol extends AnimationTimer {
    @FXML
    ProgressBar elev1, elev2, elev3, elev4;
    private double counter;
    private double countere = 920;

    @FXML
    void initialize(){
        this.start();
    }

    @Override
    public void handle(long now) {
        counter = counter + 0.01;
        countere = countere - 0.5;
        elev1.setProgress(counter);
        elev1.setLayoutY(countere);

    }
}
