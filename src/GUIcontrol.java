import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class GUIcontrol extends AnimationTimer
{
  @FXML
  ProgressBar elev1, elev2, elev3, elev4;
  private double counter;
  private double countere = 920;
  private long lastUpdate = 0;
  long lastSec = System.currentTimeMillis()/1000;

  @FXML
  void initialize()
  {

    this.start();

  }

  @Override
  public void handle(long now)
  {
    if (now - lastUpdate >= 8_333_333) // force 60fps in all machines.
    {

      counter = counter + 1;
      countere = countere -1 ;
      elev1.setProgress(counter);
      elev1.setLayoutY(countere);
      System.out.println(counter + "  "+ (System.currentTimeMillis()/1000 - lastSec));

      lastUpdate = now;

    }

  }
}
