package app;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

/**
 * A new Data Node will be started in a separate JVM process when this class gets executed.
 */
public class DataNodeStartup {
    /**
     * Start up a Data Node.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
        Ignite ignite = Ignition.start("config/data-node-config.xml");
    }
}
