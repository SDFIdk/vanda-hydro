package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Examination;
import jakarta.json.bind.annotation.JsonbProperty;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class JsonExamination implements Examination {
    private String parameter;
    public void setParameter(String str) {
        parameter = str;
    }
    @Override
    public String parameter() {
        return parameter;
    }

    private Integer parameterSc;
    public void setParameterSc(Integer n) {
        parameterSc = n;
    }
    @Override
    public Integer parameterSc() {
        return parameterSc;
    }

    private String examinationType;
    public void setExaminationType(String str) {
        examinationType = str;
    }
    @Override
    public String examinationType() {
        return examinationType;
    }

    private Integer examinationTypeSc;
    public void setExaminationTypeSc(Integer n) {
        examinationTypeSc = n;
    }
    @Override
    public Integer examinationTypeSc() {
        return examinationTypeSc;
    }

    private String unit;
    public void setUnit(String str) {
        unit = str;
    }
    @Override
    public String unit() {
        return unit;
    }

    private Integer unitSc;
    public void setUnitSc(Integer n) {
        unitSc = n;
    }
    @Override
    public Integer unitSc() {
        return unitSc;
    }

    private OffsetDateTime earliestResult;
    @JsonbProperty("firstResult")
    public void setEarliestResult(OffsetDateTime t) {
        earliestResult = t;
    }
    @Override
    public OffsetDateTime earliestResult() {
        return earliestResult;
    }

    private OffsetDateTime latestResult;
    public void setLatestResult(OffsetDateTime t) {
        latestResult = t;
    }
    @Override
    public OffsetDateTime latestResult() {
        return latestResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Examination that)) return false;
        return Objects.equals(parameter, that.parameter())
                && Objects.equals(parameterSc, that.parameterSc())
                && Objects.equals(examinationType, that.examinationType())
                && Objects.equals(examinationTypeSc, that.examinationTypeSc())
                && Objects.equals(unit, that.unit())
                && Objects.equals(unitSc, that.unitSc())
                && Objects.equals(earliestResult, that.earliestResult())
                && Objects.equals(latestResult, that.latestResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameterSc, examinationTypeSc, unitSc);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "JsonExamination {", "}");
        sj.add(str(parameter, "parameter"));
        sj.add(str(parameterSc, "parameterSc"));
        sj.add(str(examinationType, "examinationType"));
        sj.add(str(examinationTypeSc, "examinationTypeSc"));
        sj.add(str(unit, "unit"));
        sj.add(str(unitSc, "unitSc"));
        sj.add(str(earliestResult, "earliestResult"));
        sj.add(str(latestResult, "latestResult"));
        return sj.toString();
    }

    private String str(Object obj, String name) {
        if (obj instanceof CharSequence)
            return name + " = \"" + obj + "\"";
        else
            return name + " = " + obj;
    }
}
