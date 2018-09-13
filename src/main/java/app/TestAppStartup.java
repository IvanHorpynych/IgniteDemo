package app;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import org.apache.ignite.*;
import services.maintenance.common.MaintenanceService;
import services.vehicles.common.Vehicle;
import services.vehicles.common.VehicleService;


public class TestAppStartup {

    private static final String[] VEHICLES_NAMES = new String[]{
            "TOYOTA", "BMW", "MERCEDES", "HYUNDAI", "FORD"};


    public static int TOTAL_VEHICLES_NUMBER = 20;



    public static void main(String[] args) throws IgniteException {
        final Ignite ignite = Ignition.start("config/client-node-config.xml");

        System.out.println("Client node has connected to the cluster");


        IgniteCache<Integer, Vehicle> vehiclesCache = ignite.cache("vehicles");

        Random rand = new Random();

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < TOTAL_VEHICLES_NUMBER; i++) {

            calendar.set(Calendar.MONTH, rand.nextInt(12));
            calendar.set(Calendar.YEAR, 2000 + rand.nextInt(17));

            vehiclesCache.put(i, new Vehicle(
                    VEHICLES_NAMES[rand.nextInt(VEHICLES_NAMES.length)],
                    calendar.getTime(),
                    (double) (11000 + rand.nextInt(10000))
            ));
        }

        System.out.println("Filled in Vehicles cache. Entries number: " + vehiclesCache.size());
    }
}