package dk.dmp.vanda.hydro.httpjson;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class JsonStationResults<T extends JsonMeasurement> extends JsonStationId {
    public List<T> results;

    public Stream<T> denormalize() {
        if (results == null) return Stream.empty();
        return results.stream().peek(r -> {
            r.setStationId(stationId);
            r.setOperatorStationId(operatorStationId);
        });
    }

    @Override
    protected void appendFieldsTo(StringJoiner s) {
        super.appendFieldsTo(s);
        s.add(Labler.lable(results, "results"));
    }
}
