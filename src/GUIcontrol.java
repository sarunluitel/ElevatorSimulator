import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

import javax.swing.plaf.TableHeaderUI;
import java.util.ArrayList;

public class GUIcontrol extends AnimationTimer {
    @FXML
    ProgressBar elev1, elev2, elev3, elev4;

    private Cabin[] cabins;
    private double counter;
    private long lastUpdate = 0;
    long lastSec = System.currentTimeMillis() / 1000;

    @FXML
    void initialize() {
        System.out.println(Thread.currentThread());
        while (this.cabins == null) {
            this.cabins = MapView.getInstance().cabins;
        }
        this.start();

    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= 8_333_333) // force 60fps in all machines.
        {
            for (Cabin c : cabins) {
                if (c.getcabinId() == 0) {
                    int floor = c.getCurrentFloor();
                    elev1.setLayoutY(1000 - (floor * 100));

                    if (c.door.getisDoorOpen()) {
                        elev1.setProgress(0);
                    } else {
                        elev1.setProgress(1);
                    }

                }

                if (c.getcabinId() == 1) {
                    int floor = c.getCurrentFloor();
                    elev2.setLayoutY(1000 - (floor * 100));
                }

                if (c.getcabinId() == 2) {
                    int floor = c.getCurrentFloor();
                    elev3.setLayoutY(1000 - (floor * 100));
                }

                if (c.getcabinId() == 3) {
                    int floor = c.getCurrentFloor();
                    elev4.setLayoutY(1000 - (floor * 100));
                }

            }

            lastUpdate = now;

        }

    }
}
