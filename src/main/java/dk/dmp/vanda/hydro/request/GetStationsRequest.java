package dk.dmp.vanda.hydro.request;

import dk.dmp.vanda.hydro.Station;

import java.io.IOException;
import java.util.concurrent.Flow;

/**
 * Request builder for the {@link dk.dmp.vanda.hydro.HydrometryService#getStations()} operation.
 */
public interface GetStationsRequest
        extends dk.dmp.vanda.hydro.request.generic.GetStationsRequestBuilder<GetStationsRequest>
{
    /**
     * Perform the request.
     * @return The stations fulfilling all conditions of the request.
     */
    Flow.Publisher<Station> exec() throws IOException;
}
