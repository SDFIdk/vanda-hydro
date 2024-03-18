package dk.dmp.vanda.hydro.httpjson;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * According to the OpenAPI specification of the VanDa Hydro service,
 * all input timestamp arguments must be given as a UTC timestamps in
 * the RFC 3339 date+time format without seconds.
 */
public class RFC3339NoSecondsFormatter {
    private static final DateTimeFormatter RFC_3339_NO_SECONDS =
        new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(java.time.temporal.ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(java.time.temporal.ChronoField.MINUTE_OF_HOUR, 2)
            .appendOffsetId()
            .toFormatter();

    /**
     * Express the given timestamp in UTC and format in the
     * RFC 3339 date+time format without seconds.
     * @param t The timestamp to express.
     * @return The formatted timestamp.
     */
    public static String formatUTC(OffsetDateTime t) {
        return t.withOffsetSameInstant(ZoneOffset.UTC).format(RFC_3339_NO_SECONDS);
    }
}
