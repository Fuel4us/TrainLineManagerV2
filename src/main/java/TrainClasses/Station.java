package TrainClasses;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Gonçalo Fonseca (1150503@isep.ipp.pt)
 */
public class Station {

    String name;
    double latitude;
    double longitude;
    ArrayList<String> line;

    public Station(String name, double latitude, double longitude) {
      this.name = name;
      this.latitude = latitude;
      this.longitude = longitude;
      line = new ArrayList<>();
    }

    public void addLine(String code) {
        if (!line.contains(code)) {
            line.add(code);
        }
    }

//    public Station(String name, String code) {
//      this.name = name;
//      this.code = code;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
