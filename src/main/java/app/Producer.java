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
package main.java.app;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import org.apache.ignite.*;
import services.maintenance.common.MaintenanceService;
import services.vehicles.common.Vehicle;
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