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


/**
 * This interface defines a generic command that you can send/receive to/from Raspberry Pi
 *
 * @author Fabio Marini
 */
public interface IPinCommand   {

    /**
     * Sets the pin of command
     * @param pin the raspberry pin
     */
    public void setPin(PinMQ pin);

    /**
     * Sets the state of command
     * @param state the pin state
     */
    public void setState(PinStateMQ state);

    /**
     * Sets the W1-Therm data
     * @param data the data to set
     */
    public void setW1Data(W1ThermMQ data);
}