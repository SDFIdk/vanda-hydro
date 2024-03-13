package dk.dmp.vanda.hydro.httpjson;

import java.util.List;
import java.util.StringJoiner;

public class JsonStationResults<T> extends JsonStationId {
    public List<T> results;

    @Override
    protected void appendFieldsTo(StringJoiner s) {
        super.appendFieldsTo(s);
        s.add(Labler.lable(results, "results"));
    }
}
