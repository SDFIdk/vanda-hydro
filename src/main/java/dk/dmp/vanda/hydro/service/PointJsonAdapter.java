package dk.dmp.vanda.hydro.service;

import jakarta.json.bind.adapter.JsonbAdapter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class PointJsonAdapter implements JsonbAdapter<Point,JsonPoint> {
    @Override
    public JsonPoint adaptToJson(Point point) {
        JsonPoint r = new JsonPoint();
        r.srid = point.getSRID() == 0 ? null : String.valueOf(point.getSRID());
        r.x = point.getX();
        r.y = point.getY();
        return r;
    }

    @Override
    public Point adaptFromJson(JsonPoint jsonPoint) {
        int srid = Integer.parseInt(jsonPoint.srid);
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), srid);
        return gf.createPoint(new Coordinate(jsonPoint.x, jsonPoint.y));
    }
}
