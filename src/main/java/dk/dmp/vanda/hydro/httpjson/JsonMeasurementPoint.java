package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Examination;
import dk.dmp.vanda.hydro.MeasurementPoint;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import org.locationtech.jts.geom.Point;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class JsonMeasurementPoint implements MeasurementPoint {
    private int number;
    public void setNumber(int n) {
        number = n;
    }
    @Override
    public int number() {
        return number;
    }

    private String name;
    public void setName(String str) {
        name = str;
    }
    @Override
    public String name() {
        return name;
    }

    private String measurementPointType;
    public void setMeasurementPointType(String str) {
        measurementPointType = str;
    }
    @Override
    public String measurementPointType() {
        return measurementPointType;
    }

    private Integer measurementPointTypeSc;
    public void setMeasurementPointTypeSc(Integer n) {
        measurementPointTypeSc = n;
    }
    @Override
    public Integer measurementPointTypeSc() {
        return measurementPointTypeSc;
    }

    private String description;
    public void setDescription(String str) {
        description = str;
    }
    @Override
    public String description() {
        return description;
    }

    private Point location;
    @JsonbTypeAdapter(PointJsonAdapter.class)
    public void setLocation(Point p) {
        location = p;
    }
    @Override
    public Point location() {
        return location;
    }

    private Examination[] examinations;
    public void setExaminations(JsonExamination[] a) {
        examinations = a;
    }
    @Override
    public Examination[] examinations() {
        return examinations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasurementPoint that)) return false;
        return number == that.number()
                && Objects.equals(name, that.name())
                && Objects.equals(measurementPointType, that.measurementPointType())
                && Objects.equals(measurementPointTypeSc, that.measurementPointTypeSc())
                && Objects.equals(description, that.description())
                && Objects.equals(location, that.location())
                && Arrays.equals(examinations, that.examinations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, measurementPointTypeSc, location);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "JsonMeasurementPoint {", "}");
        sj.add(str(number, "number"));
        sj.add(str(name, "name"));
        sj.add(str(measurementPointType, "measurementPointType"));
        sj.add(str(measurementPointTypeSc, "measurementPointTypeSc"));
        sj.add(str(description, "description"));
        sj.add(str(location, "location"));
        sj.add("examinations = " + Arrays.toString(examinations));
        return sj.toString();
    }

    private String str(Object obj, String name) {
        if (obj instanceof CharSequence)
            return name + " = \"" + obj + "\"";
        else
            return name + " = " + obj;
    }
}
