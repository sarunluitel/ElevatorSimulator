import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;


public class GUIcontrol extends AnimationTimer
{
  @FXML
  ProgressBar elev0, elev1, elev2, elev3;

  @FXML
  RadioButton rbCab0, rbCab1, rbCab2, rbCab3;

  private Cabin[] cabins;
  private RadioButton[] selectedCabin = new RadioButton[4];
  private long lastUpdate = 0;

  @FXML
  void initialize()
  {
    // This method is run before the FXML file is loaded.

    while (this.cabins == null)
    {
      // waits until it gets all elevator's references.
      this.cabins = MapView.getInstance().getElevators();
    }
    selectedCabin[0] = rbCab0;
    selectedCabin[1] = rbCab1;
    selectedCabin[2] = rbCab2;
    selectedCabin[3] = rbCab3;

    MapView.getInstance().setSelectedCabin(selectedCabin);
    this.start();

  }

  @FXML
  private void floorRequests(Event e)
  {
    Button pressed = (Button) e.getSource();

    int floor = Integer.parseInt(pressed.getId().substring(3, 5));

    int direction;
    if (pressed.getId().substring(5).equalsIgnoreCase("down"))
    {
      direction = -1;
    } else
    {
      direction = 1;
    }
    int[] a = {floor, direction};

    MapView.getInstance().setFloorRequests(a);
  }

  private void updateCabin(Cabin cabin, ProgressBar elev)
  {
    int floor = cabin.getCurrentFloor();
    elev.setLayoutY(710 - (floor * 70));

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
