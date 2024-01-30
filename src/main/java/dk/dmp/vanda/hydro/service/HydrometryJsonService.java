package dk.dmp.vanda.hydro.service;

public class HydrometryJsonService implements HydrometryService {
    private final HydrometryTransport transport;
    public HydrometryJsonService(HydrometryTransport transportLayer) {
        transport = transportLayer;
    }

    @Override
    public StationsRequest stations() {
        return null;
    }

    @Override
    public WaterLevelsRequest waterLevels() {
        return null;
    }
}
