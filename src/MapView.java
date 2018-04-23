import javafx.scene.control.RadioButton;

public class MapView
{
  private volatile static MapView mapView = null;
  private volatile RadioButton[] selectedCabin;
  private volatile int[] floorRequest = null;
  volatile Cabin[] cabins;
  private boolean DEBUG = true;
  private int cabinfloorRequest =-10; // null equivalent for  no cabins selected yet.

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
    //if(DEBUG) System.out.println(req[0]+"   "+req[1]);
    //req is an int array [10,-1] from floor 10, wants to go down. +1 for up.
    this.floorRequest = req;
  }

  int[] getFloorRequests()
  {
    // returns array [floor,direction] -1 for down, +1 for up. floor in int.
    int[] a = this.floorRequest;
    this.floorRequest = null;
    return a;
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

  // returns 0,1,2,3.. The selected cabin from Radio Button.
  public int getSelectedCabin()
  {
    for (int i = 0; i < 4; i++)
    {
      // gives position of the selected button.
      if (selectedCabin[i].selectedProperty().get()) return i;
    }

    return -1; // code should never get to here.
  }

  void setCabinFloorRequest(int b)
  {
    this.cabinfloorRequest = b;
    int[] a = {this.cabinfloorRequest, this.getSelectedCabin()};
    System.out.println("floor " + a[0] + " fom cabin "+ a[1]);
  }

  int[] getCabinFloorRequest()
  {
    if (this.cabinfloorRequest == -10) return null;
    int[] a = {this.cabinfloorRequest, this.getSelectedCabin()};
    this.cabinfloorRequest = -1;
    this.cabinfloorRequest = -10;

    return a;
  }
}

