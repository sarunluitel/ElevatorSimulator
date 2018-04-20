import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;


public class GUIcontrol extends AnimationTimer
{
  @FXML
  ProgressBar elev0, elev1, elev2, elev3;

  private Cabin[] cabins;
  private long lastUpdate = 0;

  @FXML
  void initialize()
  {
    // This method is run before the FXML file is loaded.

    while (this.cabins == null)
    {
      this.cabins = MapView.getInstance().cabins;
    }
    this.start();

  }

  private void updateCabin(Cabin cabin, ProgressBar elev)
  {
    int floor = cabin.getCurrentFloor();
    elev.setLayoutY(1000 - (floor * 100));

    if (cabin.door.getisDoorOpen())
    {
      elev.setProgress(0);
    } else
    {
      elev.setProgress(1);
    }

  }

  @Override
  public void handle(long now)
  {
    if (now - lastUpdate >= 8_333_333) // force 60fps in all machines.
    {
      for (Cabin c : cabins)
      {

        if (c.getcabinId() == 0) updateCabin(c, elev0);
        if (c.getcabinId() == 1) updateCabin(c, elev1);
        if (c.getcabinId() == 2) updateCabin(c, elev2);
        if (c.getcabinId() == 3) updateCabin(c, elev3);
      }

      lastUpdate = now;

    }

  }
}
