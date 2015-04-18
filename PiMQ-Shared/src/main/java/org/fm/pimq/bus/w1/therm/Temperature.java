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

/**
 * Class that define the W1-Therm temperature data
 *
 * @author Fabio Marini
 */
public class Temperature {

    private Float millisCelsius;

    /**
     * Default constructor
     */
    public Temperature() {}

    /**
     * Build the object with the given temperature, in millis celsius
     * @param millisCelsius the temperature to set in millis celsius
     */
    public Temperature(Float millisCelsius) {
        this.millisCelsius = millisCelsius;
    }


    /**
     * Returns the temperature in millis celsius
     * @return the millis celsius
     */
    public Float getMillisCelsius() {
        return millisCelsius;
    }

    /**
     * Set the given millis celsius temperature
     *
     * @param millisCelsius the millis celsius to set
     */
    public void setMillisCelsius(Float millisCelsius) {
        this.millisCelsius = millisCelsius;
    }


    /**
     * Get the temperature in Celsius
     * @return the Celsius, or null if the temperature is not set
     */
    public Float getCelsius() {
        Float celsius = null;

        if(millisCelsius != null) {
            celsius = millisCelsius / 1000;
        }

        return celsius;
    }

    /**
     * Get the temperature in Fahrenheit
     * @return the Fahrenheit, or null if the temperature is not set
     */
    public Float getFahrenheit() {
        Float fahrenheit = null;

        if(millisCelsius != null) {
            fahrenheit = getCelsius() * 9 / 5 + 32;
        }

        return fahrenheit;
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Temperature{" +
                "millisCelsius=" + millisCelsius +
                '}';
    }
}
