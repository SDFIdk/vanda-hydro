package dk.dmp.vanda.hydro.httpjson;

import java.util.List;
import java.util.StringJoiner;

public class JsonStationWaterLevel {
    public String stationId;
    public String operatorStationId;
    public List<JsonWaterLevelMeasurement> results;

    @Override
    public String toString() {
        StringJoiner s = Labler.joiner(JsonStationWaterLevel.class);
        s.add(Labler.lable(stationId, "stationId"));
        s.add(Labler.lable(operatorStationId, "operatorStationId"));
        return s.toString();
    }
}
