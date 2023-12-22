package dk.dmp.vanda.hydro.raw;

import dk.dmp.vanda.hydro.raw.request.*;

/**
 * A selection of operations to interact with the VanDa Hydro API.
 */
public interface RawHydrometryService {
    /**
     * Get a list of stations
     * @return A request builder.
     */
    RawGetStationsRequest getStations();

    /**
     * Get water level measurements.
     * Returns the current results, i.e. no overwritten history.
     * @return A request builder.
     */
    RawGetWaterLevelsRequest getWaterLevels();
}