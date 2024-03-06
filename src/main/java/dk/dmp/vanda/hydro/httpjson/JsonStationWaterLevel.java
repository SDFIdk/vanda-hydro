package dk.dmp.vanda.hydro.httpjson;

import java.util.List;

public class JsonStationWaterLevel {
    public String stationId;
    public String operatorStationId;
    public List<JsonWaterLevelMeasurement> results;
}
