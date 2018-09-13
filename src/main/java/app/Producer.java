package app;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.apache.ignite.*;
import services.maintenance.common.MaintenanceService;
import services.vehicles.common.VehicleService;


public class Producer {

    public static void main(String[] args) throws IgniteException, InterruptedException {
        final Ignite ignite = Ignition.start("config/client-node-config.xml");

        System.out.println("Client node has connected to the cluster");


        IgniteLock cacheLock = ignite.reentrantLock("cacheProducerLock", true, true, true);

        Random rand = new Random();

        MaintenanceService maintenanceService = ignite.services().serviceProxy(MaintenanceService.SERVICE_NAME,
                MaintenanceService.class, false);

        VehicleService vehicleService = ignite.services().serviceProxy(VehicleService.SERVICE_NAME,
                VehicleService.class, false);

        while (true) {
            System.out.println("Getting info for a random vehicle using VehiclesService: " + vehicleService.getVehicle(
                    rand.nextInt(TestAppStartup.TOTAL_VEHICLES_NUMBER)));


            int vehicleId = rand.nextInt(TestAppStartup.TOTAL_VEHICLES_NUMBER);

            Date date;

            try {
                cacheLock.lock();
                date = maintenanceService.scheduleVehicleMaintenance(vehicleId);
            } finally {
                cacheLock.unlock();
            }

            if (!Objects.isNull(date))
                System.out.println("Scheduled maintenance service [vehicleID=" + vehicleId + ", " + "date=" + date + ']');

            Thread.sleep(2000);
        }

    }
}