import java.util.ArrayList;

public class MapView
{
  private static MapView mapView = null;
  private ArrayList<Object> elevators = new ArrayList();

  private MapView(){}


  public static MapView getInstance(){
    if (mapView ==null) return new MapView();
    return  mapView;
  }

  public void setElevators(Object a){
    elevators.add(a);
  }
}
