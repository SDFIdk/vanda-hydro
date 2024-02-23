package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Examination;
import dk.dmp.vanda.hydro.MeasurementPoint;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import org.locationtech.jts.geom.Point;

import java.util.*;

import static dk.dmp.vanda.hydro.httpjson.Labler.joiner;
import static dk.dmp.vanda.hydro.httpjson.Labler.lable;

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

    private Integer intakeNumber;
    public void setIntakeNumber(Integer n) {
        intakeNumber = n;
    }
    @Override
    public Integer intakeNumber() {
        return intakeNumber;
    }

    private List<? extends Examination> examinations;
    @JsonbTypeAdapter(JsonExamination.ListJsonAdapter.class)
    public void setExaminations(List<? extends Examination> a) {
        examinations = a;
    }
    @Override
    public List<? extends Examination> examinations() {
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
            && Objects.equals(intakeNumber, that.intakeNumber())
            && Objects.equals(examinations, that.examinations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, measurementPointTypeSc, location);
    }

    @Override
    public String toString() {
        StringJoiner sj = joiner(JsonMeasurementPoint.class);
        sj.add(lable(number, "number"));
        sj.add(lable(name, "name"));
        sj.add(lable(measurementPointType, "measurementPointType"));
        sj.add(lable(measurementPointTypeSc, "measurementPointTypeSc"));
        sj.add(lable(description, "description"));
        sj.add(lable(location, "location"));
        sj.add(lable(intakeNumber, "intakeNumber"));
        sj.add(lable(examinations, "examinations"));
        return sj.toString();
    }

}
