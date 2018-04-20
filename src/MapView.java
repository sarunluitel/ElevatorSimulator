import java.util.ArrayList;

public class MapView {
    private static MapView mapView = null;
    Cabin[] cabins;

    private MapView() {
    }
    static MapView getInstance() {
        if (mapView == null) mapView= new MapView();
        return mapView;
    }

    void setElevators(Cabin[] a) {
        this.cabins= a;
        System.out.println(this.cabins[2].getcabinId()*1000000);


    }

    public Cabin[] getElevators() {
        return this.cabins;
    }
}

