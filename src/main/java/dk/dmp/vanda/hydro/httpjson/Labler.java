package dk.dmp.vanda.hydro.httpjson;

import java.util.StringJoiner;

public class Labler {
public static StringJoiner joiner(Class<?> forClass) {
    return new StringJoiner(", ", forClass.getSimpleName() + "(", ")");
}
public static String lable(Object field, String name) {
    if (field instanceof CharSequence)
        return name + ": »" + field + "«";
    else
        return name + ": " + field;
}
}
