package dk.dmp.vanda.hydro.httpjson;

import java.util.StringJoiner;

public class JsonStationId {
    public String stationId;
    public String operatorStationId;

    @Override
    public String toString() {
        StringJoiner s = Labler.joiner(getClass());
        appendFieldsTo(s);
        return s.toString();
    }

    protected void appendFieldsTo(StringJoiner s) {
        s.add(Labler.lable(stationId, "stationId"));
        s.add(Labler.lable(operatorStationId, "operatorStationId"));
    }
}
