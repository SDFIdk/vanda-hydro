package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Examination;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.annotation.JsonbProperty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static dk.dmp.vanda.hydro.httpjson.Labler.joiner;
import static dk.dmp.vanda.hydro.httpjson.Labler.lable;

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
        StringJoiner sj = joiner(JsonExamination.class);
        sj.add(lable(parameter, "parameter"));
        sj.add(lable(parameterSc, "parameterSc"));
        sj.add(lable(examinationType, "examinationType"));
        sj.add(lable(examinationTypeSc, "examinationTypeSc"));
        sj.add(lable(unit, "unit"));
        sj.add(lable(unitSc, "unitSc"));
        sj.add(lable(earliestResult, "earliestResult"));
        sj.add(lable(latestResult, "latestResult"));
        return sj.toString();
    }

    public static class ListJsonAdapter implements JsonbAdapter<List<Examination>,List<JsonExamination>> {
        public List<JsonExamination> adaptToJson(List<Examination> es) {
            throw new UnsupportedOperationException();
        }
        @SuppressWarnings("unchecked")
        public List<Examination> adaptFromJson(List<JsonExamination> es) {
            return (List<Examination>)(List<?>)es;
        }
    }
}
