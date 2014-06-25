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

package org.fm.pimq.server.dataobject;

import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Defines a generic Raspberry Pi pin whit its state (Hi or Low). < br/>
 * Its instances are used to send requests (and state response) through HTTP protocol.
 * @see {@link org.fm.pimq.PinMQ}
 * @see {@link org.fm.pimq.PinStateMQ}
 *
 * @author Fabio Marini
 */
@XmlRootElement(name="PiPin")
public class PiPin implements Serializable{

    private PinMQ pin;

    private PinStateMQ state;

    /**
     * Default constructor
     */
    public PiPin() {
    }

    /**
     * Build the object with the given values
     * @param pin the raspberry pin
     * @param state the pin state
     */
    public PiPin(PinMQ pin, PinStateMQ state) {
        this();

        this.pin = pin;
        this.state = state;
    }

    public PinMQ getPin() {
        return pin;
    }

    public void setPin(PinMQ pin) {
        this.pin = pin;
    }

    public PinStateMQ getState() {
        return state;
    }

    public void setState(PinStateMQ state) {
        this.state = state;
    }
}
