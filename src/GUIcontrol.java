import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class GUIcontrol extends AnimationTimer
{

  @FXML
  private ProgressBar elev0, elev1, elev2, elev3;

  @FXML
  private RadioButton rbCab0, rbCab1, rbCab2, rbCab3, rbCP0, rbCP1, rbCP2, rbCP3;

  @FXML
  private Pane cpPane, cabinPane;

  private Cabin[] cabins;
  private RadioButton[] selectedCabin = new RadioButton[4];
  private RadioButton[] selectedCP = new RadioButton[4];

  private long lastUpdate = 0;

  private int selectedCPnum = 0;
  private int selectedCabnum = 0;

  private ArrayList<HashSet> activeButtons = new ArrayList<>();

  private int cabinSelection;
  private boolean DEBUG = true;
  private boolean buttonsReady = false;

  @FXML
  void initialize()
  {
    // This method is run before the FXML file is loaded.

    while (this.cabins == null)
    {
      // waits until it gets all elevator's references.
      this.cabins = MapView.getInstance().getElevators();
    }

    // Give map view pointer to  array of radio Buttons
    selectedCabin[0] = rbCab0;
    selectedCabin[1] = rbCab1;
    selectedCabin[2] = rbCab2;
    selectedCabin[3] = rbCab3;

    selectedCP[0] = rbCP0;
    selectedCP[1] = rbCP1;
    selectedCP[2] = rbCP2;
    selectedCP[3] = rbCP3;

    MapView.getInstance().setSelectedCabin(selectedCabin);
    this.start();

    for (int i = 0; i < 4; i++)
    {
      activeButtons.add(i, new HashSet());
    }


  }

  @FXML
  private void floorRequests(Event e)
  {
    Button pressed = (Button) e.getSource();
    //System.out.println(pressed.getId());

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

  @FXML
  private void cabinFlorRequest(Event e)
  {
    Button pressed = (Button) e.getSource();

    int floor = Integer.parseInt(pressed.getId().substring(6));
    MapView.getInstance().setCabinFloorRequest(floor);

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

  @FXML
  private void updateDisabled()
  {
    activeButtons.get(selectedCPnum).clear();
    for (Object b : cpPane.getChildren())
    {
      if (b instanceof ToggleButton)
      {
        ToggleButton selBtn = (ToggleButton) b;
        if (selBtn.isSelected())
        {
          int tempnum = Integer.parseInt(selBtn.getId().substring(5));

          activeButtons.get(selectedCPnum).add(tempnum);
          System.out.println();
        }
      }


    }
    buttonsReady = true;

  }

  private void disableButtons()
  {
    for (Object o : cabinPane.getChildren())
    {
      if (o instanceof Button)
      {
        Button b = (Button) o;
        if (!b.getId().equalsIgnoreCase("btnCabKey"))
        {
          int temp = Integer.parseInt(b.getId().substring(6));
          if (activeButtons.get(selectedCabnum).contains(temp))
          {
            b.setDisable(true);

          } else
          {
            b.setDisable(false);
          }
        }
      }

    }

  }

  private void updateToggle()
  {
    for (Object o : cpPane.getChildren())
    {
      if (o instanceof ToggleButton)
      {
        ToggleButton b = (ToggleButton) o;
        int temp = Integer.parseInt(b.getId().substring(6));
        if (activeButtons.get(selectedCPnum).contains(temp))
        {
          b.setSelected(true);

        } else
        {
          b.setSelected(false);
        }

      }

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
      for (int i = 0; i < 4; i++)
      {
        // gives position of the selected button.
        if (selectedCabin[i].selectedProperty().get()) selectedCabnum = i;
        if (selectedCP[i].selectedProperty().get()) selectedCPnum = i;
      }

      if (buttonsReady) disableButtons();
      if (buttonsReady) updateToggle();


    }// no code below this line on this method


  }
}
