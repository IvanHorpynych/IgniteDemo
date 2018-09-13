package app;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

/**
 * A new Vehicle Service Node will be started in a separate JVM process when this class gets executed.
 */
public class VehicleServiceNodeStartup {
    /**
     * Start up a Vehicle Service Node.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
        Ignition.start("config/vehicle-service-node-config.xml");
    }
}
