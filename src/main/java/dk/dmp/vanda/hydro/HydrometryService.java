package dk.dmp.vanda.hydro;

import dk.dmp.vanda.hydro.request.*;

/**
 * A selection of operations to interact with the VanDa Hydro API.
 */
public interface HydrometryService {
    /**
     * Get a list of stations
     * @return A request builder.
     */
    GetStationsRequest getStations();

    /**
     * Get water level measurements.
     * Returns the current results, i.e. no overwritten history.
     * @return A request builder.
     */
    GetWaterLevelsRequest getWaterLevels();
}
