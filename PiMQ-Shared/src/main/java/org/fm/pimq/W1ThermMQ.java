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
package org.fm.pimq;

import java.io.Serializable;

/**
 * Define the W1 Therm Raspberry Pi data to be sent by JMS
 *
 * @author Fabio Marini
 */
public class W1ThermMQ implements Serializable {

    private Float therm;


    /**
     * Default constructor
     */
    public W1ThermMQ() {
    }


    /**
     * Build the object with the given therm data
     *
     * @param therm the therm data
     */
    public W1ThermMQ(Float therm) {
        this();

        this.therm = therm;
    }


    /**
     * Rertun the w1-therm data
     * @return w1-therm data
     */
    public Float getTherm() {
        return therm;
    }


    /**
     * Sets the w1-therm data
     * @param therm the therm data to set
     */
    public void setTherm(Float therm) {
        this.therm = therm;
    }
}
