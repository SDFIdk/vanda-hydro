package dk.dmp.vanda.hydro.httpjson;

import java.util.List;
import java.util.StringJoiner;

public class JsonStationResults<T> extends JsonStationId {
    public List<T> results;

    @Override
    public String toString() {
        StringJoiner s = Labler.joiner(JsonStationResults.class);
        s.add(Labler.lable(stationId, "stationId"));
        s.add(Labler.lable(operatorStationId, "operatorStationId"));
        s.add(Labler.lable(results, "results"));
        return s.toString();
    }
}
