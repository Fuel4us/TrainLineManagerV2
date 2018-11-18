package TrainClasses;

import java.util.Objects;

/**
 * @author Gonçalo Fonseca (1150503@isep.ipp.pt)
 */
public class Station {

    String name;
    String code;
    double latitude;
    double longitude;

    public Station(String name, double latitude, double longitude) {
      this.name = name;
      this.latitude = latitude;
      this.longitude = longitude;
      this.code = "Central";
    }

    public Station(String name, String code) {
      this.name = name;
      this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name) &&
                Objects.equals(code, station.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.code);
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
