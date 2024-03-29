package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Measurement;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import static dk.dmp.vanda.hydro.httpjson.Labler.lable;

public class JsonMeasurement implements Measurement {
    private String stationId;
    public void setStationId(String s) {
        stationId = s;
    }
    @Override
    public String stationId() {
        return stationId;
    }

    private String operatorStationId;
    public void setOperatorStationId(String s) {
        operatorStationId = s;
    }
    @Override
    public String operatorStationId() {
        return operatorStationId;
    }

    private int measurementPointNumber;
    public void setMeasurementPointNumber(int n) {
        measurementPointNumber = n;
    }
    @Override
    public int measurementPointNumber() {
        return measurementPointNumber;
    }

    private int parameterSc;
    public void setParameterSc(int c) {
        parameterSc = c;
    }
    @Override
    public int parameterSc() {
        return parameterSc;
    }

    private String parameter;
    public void setParameter(String s) {
        parameter = s;
    }
    @Override
    public String parameter() {
        return parameter;
    }

    private int examinationTypeSc;
    public void setExaminationTypeSc(int c) {
        examinationTypeSc = c;
    }
    @Override
    public int examinationTypeSc() {
        return examinationTypeSc;
    }

    private String examinationType;
    public void setExaminationType(String s) {
        examinationType = s;
    }
    @Override
    public String examinationType() {
        return examinationType;
    }

    private OffsetDateTime measurementDateTime;
    public void setMeasurementDateTime(OffsetDateTime t) {
        measurementDateTime = t;
    }
    @Override
    public OffsetDateTime measurementDateTime() {
        return measurementDateTime;
    }

    private double result;
    public void setResult(double v) {
        result = v;
    }
    @Override
    public double result() {
        return result;
    }

    private int unitSc;
    public void setUnitSc(int c) {
        unitSc = c;
    }
    @Override
    public int unitSc() {
        return unitSc;
    }

    private String unit;
    public void setUnit(String s) {
        unit = s;
    }
    @Override
    public String unit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Measurement that)) return false;
        return Objects.equals(stationId, that.stationId())
            && Objects.equals(operatorStationId, that.operatorStationId())
            && measurementPointNumber == that.measurementPointNumber()
            && parameterSc == that.parameterSc()
            && Objects.equals(parameter, that.parameter())
            && examinationTypeSc == that.examinationTypeSc()
            && Objects.equals(examinationType, that.examinationType())
            && Objects.equals(measurementDateTime, that.measurementDateTime())
            && Double.compare(result, that.result()) == 0
            && unitSc == that.unitSc()
            && Objects.equals(unit, that.unit());
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurementDateTime, result);
    }

    @Override
    public String toString() {
        StringJoiner sj = Labler.joiner(getClass());
        appendFieldsAndLablesTo(sj);
        return sj.toString();
    }

    protected void appendFieldsAndLablesTo(StringJoiner sj) {
        sj.add(lable(stationId, "stationId"));
        sj.add(lable(operatorStationId, "operatorStationId"));
        sj.add(lable(measurementPointNumber, "measurementPointNumber"));
        sj.add(lable(parameterSc, "parameterSc"));
        sj.add(lable(parameter, "parameter"));
        sj.add(lable(examinationTypeSc, "examinationTypeSc"));
        sj.add(lable(examinationType, "examinationType"));
        sj.add(lable(measurementDateTime, "measurementDateTime"));
        sj.add(lable(result, "result"));
        sj.add(lable(unitSc, "unitSc"));
        sj.add(lable(unit, "unit"));
    }
}
