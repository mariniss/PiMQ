/**
 * Copyright 2014 Fabio Marini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fm.pimq.bus.w1.therm;

import org.fm.pimq.bus.w1.device.DevicesSnifer;
import org.fm.pimq.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Singleton class to read the temperature data from a W1-Therm device
 *
 * @author Fabio Marini
 */
public class TemperatureDeviceReader {

    /**
     * The W1-Therm device output file
     */
    public static final String INPUT_FILE_NAME = "w1_slave";

    /**
     * The prefix of temperature data wrote on device output file
     */
    public static final String TEMPERATURE_PREFIX = "t=";

    /**
     * The default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TemperatureDeviceReader.class);


    private String dirPath;

    /************************************************************
     *          Singleton implementation - start
     ************************************************************/

    /**
     * Default instance
     */
    private static TemperatureDeviceReader instance = null;

    /**
     * Default constructor
     */
    private TemperatureDeviceReader(){

    }

    public static TemperatureDeviceReader getInstance(Configuration configuration){
        if(instance == null){
            synchronized (DevicesSnifer.class){
                if(instance == null){
                    instance = new TemperatureDeviceReader();
                    instance.dirPath = configuration.getW1DirPath();
                }
            }
        }

        return instance;
    }

    /************************************************************
     *          Singleton implementation - end
     ************************************************************/

    /**
     * Load the W1-Therm device output file form the given file directory and returns the wrote temperature
     * @param file the W1-Therm work directory
     * @return the temperature detected from the W1-Therm device that work on the given file directory
     */
    public Temperature getTemperature(File file) {
        Temperature temperature = new Temperature();

        File f = new File(getDeviceDataPath(file.getName()));

        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            String output;
            while((output = br.readLine()) != null) {
                int idx = output.indexOf(TEMPERATURE_PREFIX);
                if(idx > -1) {
                    // Temp data (in Millis Celsius) in 5 chars after t= and two spaces
                    float tempC = Float.parseFloat(output.substring(output.indexOf(TEMPERATURE_PREFIX) + 2));

                    temperature.setMillisCelsius(tempC);
                }
            }
        }
        catch(Exception exc) {
            logger.error("Error reading temperature data for device " + file.getName(), exc);
        }

        return temperature;
    }


    /**
     * Build the complete path of W1-Therm device output file
     * @param deviceName the device name
     * @return the filesystem path of device output file
     */
    private String getDeviceDataPath(String deviceName) {
        StringBuilder path = new StringBuilder();

        path.append(dirPath);
        path.append(File.separator);
        path.append(deviceName);
        path.append(File.separator);
        path.append(INPUT_FILE_NAME);

        return path.toString();
    }
}
