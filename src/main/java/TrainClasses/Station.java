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
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }

        if (otherObject == null || this.getClass() != otherObject.getClass()) {
            return false;
        }
        Station otherEstacao = (Station) otherObject;

        return this.name.compareTo(otherEstacao.name) == 0 && this.code.compareTo(otherEstacao.code) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.code);
        return hash;
    }
}
