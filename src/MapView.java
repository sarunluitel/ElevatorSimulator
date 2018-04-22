import javafx.scene.control.RadioButton;

public class MapView
{
  private static MapView mapView = null;
  private RadioButton[] selectedCabin;
  private int[] floorRequest = null;
  Cabin[] cabins;

  private MapView()
  {
    //Implements singleton design pattern to communicate to and From GUI
  }

  static MapView getInstance()
  {
    if (mapView == null) mapView = new MapView();
    return mapView;
  }

  void setFloorRequests(int[] req)
  {
    //req is an int array [10,-1] from floor 10, wants to go down. +1 for up.
    this.floorRequest = req;
  }

  int[] getFloorRequests (){
    // returns array [floor,direction] -1 for down, +1 for up. floor in int.
    return this.floorRequest;
    //this.floorRequest = null;
   // return a;
  }

  void setElevators(Cabin[] a)
  {
    // set references of elevators for GUI to reference them.
    this.cabins = a;
  }

  public Cabin[] getElevators()
  {
    //Called by GUI to keep references of all elevators.
    return this.cabins;
  }

  public void setSelectedCabin(RadioButton[] a)
  {
    // choice from radiobutton is set here.
    this.selectedCabin = a;
  }

  // returns 1,2,3,4.. The selected cabin from Radio Button.
  public int getSelectedCabin()
  {
    for (int i = 0; i < 4; i++)
    {
      // gives position of the selected button.
      if (selectedCabin[i].selectedProperty().get()) return i;
    }
    return -1; // code should never get to here.
  }
}

