/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package app;

import org.apache.ignite.*;
import services.maintenance.common.MaintenanceService;
import services.vehicles.common.Vehicle;
import services.vehicles.common.VehicleService;


import java.util.Random;


public class Consumer {

    public static void main(String[] args) throws IgniteException, InterruptedException {
        final Ignite ignite = Ignition.start("config/client-node-config.xml");

        System.out.println("Client node has connected to the cluster");

        Random rand = new Random();

        IgniteLock cacheLock = ignite.reentrantLock("cacheConsumerLock", true, true, true);


        VehicleService vehicleService = ignite.services().serviceProxy(VehicleService.SERVICE_NAME,
                VehicleService.class, false);

        MaintenanceService maintenanceService = ignite.services().serviceProxy(MaintenanceService.SERVICE_NAME,
                MaintenanceService.class, false);

        IgniteCache<Integer, Vehicle> maintenanceCache = ignite.cache("maintenance");
        while (true) {

            int vehicleId = rand.nextInt(TestAppStartup.TOTAL_VEHICLES_NUMBER);

            System.out.println("Getting info for a random vehicle using VehiclesService: " + vehicleService.getVehicle(
                    vehicleId));

            try {
                cacheLock.lock();
                maintenanceService.deleteMaintenanceRecords(vehicleId);
            } finally {
                cacheLock.unlock();
            }

                System.out.println("Schedule maintenance service for [vehicleID=" + vehicleId + "] successful done.");

            Thread.sleep(5000);
        }

    }
}