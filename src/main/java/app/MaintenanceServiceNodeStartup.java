package app;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

/**
 * A new Maintenance Service Node will be started in a separate JVM process when this class gets executed.
 */
public class MaintenanceServiceNodeStartup {
    /**
     * Start up a Maintenance Node.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
        Ignition.start("config/maintenance-service-node-config.xml");
    }
}
