# VanDa Hydrometry API

[VanDa test OpenAPI](https://vandah.test.miljoeportal.dk/api/swagger/index.html)

[VanDa demo OpenAPI](https://vandah.demo.miljoeportal.dk/api/swagger/index.html)
currently no loggers are connected, thus measurements are missing.

[VanDa prod OpenAPI](https://vandah.miljoeportal.dk/api/swagger/index.html)
([local copy of spec](doc/vandah.miljoeportal.dk_api_swagger_v1_swagger.json))

[Further documentation](https://github.com/danmarksmiljoeportal/VanDa/wiki/Hydro-API)

## Data model

```mermaid
---
title: VanDa Hydrometry API data model
---
classDiagram
    class Station {
        digit[8]? stationId
        string? operatorStationId
        string? oldStationNumber
        string? locationType
        int locationTypeSc
        string? stationOwnerCvr
        string? stationOwnerName
        string? operatorCvr
        string? operatorName
        string? name
        string? description
        string? loggerId
        stations(stationId?, ... parameterSc?, examinationTypeSc?, ...) Station[]$
    }
    Station *-- Point : location
    Station *-- "1..2" MeasurementPoint : measurementPoints
    class Point {
        double x
        double y
        string? srid
    }
    class MeasurementPoint {
        int number
        string? name
        string? measurementPointType
        int measurementPointTypeSc
        string? description
    }
    MeasurementPoint *-- Point : location
    MeasurementPoint *-- "*" Examination : examinations
    class Examination {
        string? parameter
        int? parameterSc
        string? examinationType
        int? examinationTypeSc
        string? unit
        int? unitSc
        DateTime? firstResult
        DateTime? latestResult
    }
    class Measurement {
        water-levels(stationId, from, to) Measurement[]$
        water-flows(stationId, from, to) Measurement[]$
        measurements_current(stationId, from?, to?, examinationTypeSc?, parameterSc?) Measurement[]$
        water-levels_for_last_24_hours(stationId) Measurement[]$
        water-flows_for_last_24_hours(stationId) Measurement[]$
    }
    note for Measurement "'current' returns the currently registered
    measurements across all measurement dates.
    Do not use the 'all' or 'valid-from' operations,
    they will return registration history, i.e. superseded registrations."
    Measurement --> Station
    Measurement *-- "*" Result : results
    class Result {
        DateTime measurementDateTime
        double result
        double? resultElevationCorrected
        string? loggerId
        string? formulaId
        DateTime createdTimestamp
        int? reasonSc
        int? reason
    }
    Result "*" --> MeasurementPoint
    Result "*" --> Examination
    note for Result "Date and time in UTC, RFC3339.
    resultElevationCorrected only on water level."
```

### Mapping Between Examination, Parameter, and Unit

StanCodes

| ExaminationType (SC1101)  | Parameter (SC1008) | Unit (SC1009) | Min    | Max    | Decimals |
|---------------------------|--------------------|---------------|--------|--------|----------|
| Elevation Level (21)      | (439)              | m (63)        | -15    | 100    | 3        |
| Scale Pole Level (22)     | Water Level (1233) | cm (19)       | -1.500 | 10.000 | 1        |
| Water Level (25)          | Water Level (1233) | cm (19)       | -1.500 | 10.000 | 1        |
| Water Flow (27)           | Water Flow (1155)  | l/sek (55)    | -      | -      | 1        |
| Measured Water Flow (24)  | Water Flow (1155)  | l/sek (55)    | -      | -      | 1        |
