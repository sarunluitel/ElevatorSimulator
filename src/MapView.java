import javafx.scene.control.RadioButton;

public class MapView {
    private static MapView mapView = null;
    private RadioButton[] selectedCabin;
    private
    Cabin[] cabins;

    private MapView() {
    }
    static MapView getInstance() {
        if (mapView == null) mapView= new MapView();
        return mapView;
    }

    void setRequests(){
        // code to make requesats in order

    }
    void setElevators(Cabin[] a) {
        this.cabins= a;
    }

    public Cabin[] getElevators() {
        return this.cabins;
    }

    public void setSelectedCabin(RadioButton[] a){ this.selectedCabin =a;}


    public int getSelectedCabin(){
        for (int i = 0; i < 4; i++) {
            if(selectedCabin[i].selectedProperty().get()) return i;
        }
        return -1;
    }
}

