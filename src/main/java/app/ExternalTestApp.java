package app;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import services.maintenance.common.Maintenance;

/**
 * Sample app that interacts with Maintenance service via plan socket API. The app doesn't use Apache Ignite API
 * at all and it's not connected to the cluster somehow.
 *
 * Make sure to start {@link TestAppStartup} before that populates the cluster with some data.
 */
public class ExternalTestApp {
    /**
     * Entry point.
     *
     * @param args Optional.
     */
    public static void main(String[] args) {
        try {

            for (int vehicleId = 0; vehicleId < TestAppStartup.TOTAL_VEHICLES_NUMBER; vehicleId++) {
                System.out.println(" >>> Getting maintenance schedule for vehicle:" + vehicleId);

                Socket socket = new Socket("127.0.0.1", 50000);

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                dos.writeInt(vehicleId);

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                List<Maintenance> result = (List<Maintenance>)ois.readObject();

                for (Maintenance maintenance : result)
                    System.out.println("    >>>   " + maintenance);

                dos.close();
                ois.close();
                socket.close();
            }

            System.out.println(" >>> Shutting down the application.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
