package TrainClasses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class Percurso {

    LinkedList<Station> stationLines;
    Map<Station, String> stationTime;
    String startHour;

    public Percurso(LinkedList<Station> stationLines, String startHour) {
        this.stationLines = stationLines;
        this.startHour = startHour;
        this.stationTime = new HashMap<>();
    }

    public void addStep(Station prevStation, int tempo) {
        String atual = addTime(tempo);
        stationTime.put(prevStation, atual);
    }

    /**
     * Could use date but was to complex for the purpose
     * @param time time before conversion
     * @return Final Time
     */
    private String addTime(int time) {
        int hour = Integer.parseInt(startHour.split(":")[0]);
        int min = Integer.parseInt(startHour.split(":")[1]);
        // int sec = Integer.parseInt(startHour.split(":")[2]);
        min += time;
        if (min >= 60) {
            hour = hour + 1;
            min = min - 60;
        }
        return String.valueOf(hour)+":"+String.valueOf(min);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Percurso percurso = (Percurso) o;
        return Objects.equals(stationLines, percurso.stationLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationLines);
    }
}
