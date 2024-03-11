package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.WaterLevelMeasurement;

import java.util.Objects;
import java.util.StringJoiner;

import static dk.dmp.vanda.hydro.httpjson.Labler.lable;

public class JsonWaterLevelMeasurement extends JsonWatercourseMeasurement implements WaterLevelMeasurement {
    private Double resultElevationCorrected;
    public void setResultElevationCorrected(Double v) {
        resultElevationCorrected = v;
    }
    @Override
    public Double resultElevationCorrected() {
        return resultElevationCorrected;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o)
            && o instanceof WaterLevelMeasurement that
            && Objects.equals(resultElevationCorrected, that.resultElevationCorrected());
    }

    @Override
    protected void appendFieldsAndLablesTo(StringJoiner sj) {
        super.appendFieldsAndLablesTo(sj);
        sj.add(lable(resultElevationCorrected, "resultElevationCorrected"));
    }
}
