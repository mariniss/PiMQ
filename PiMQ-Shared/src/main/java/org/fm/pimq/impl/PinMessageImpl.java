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
package org.fm.pimq.impl;

import org.fm.pimq.AbstractPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.W1ThermMQ;

/**
 * Concrete implementation of AbstractPinMessage
 *
 * @author Fabio Marini
 */
public class PinMessageImpl extends AbstractPinMessage {

    /**
     * Serial number, necessary to serialize the objects
     */
    private static final long serialVersionUID = 774159115195548134L;

    /**
     * Build the object with the given values
     * @param pin the raspberry pin
     */
    public PinMessageImpl(PinMQ pin) {
        setPin(pin);
    }


    /**
     * Build the object with the given values
     * @param pin the raspberry pin
     * @param state the pin status
     */
    public PinMessageImpl(PinMQ pin, PinStateMQ state) {
        setPin(pin);
        setState(state);
    }


    /**
     * Build the object with the given values
     * @param pin the raspberry pin
     * @param therm the pin w1 therm data
     */
    public PinMessageImpl(PinMQ pin, W1ThermMQ therm) {
        setPin(pin);
        setW1Data(therm);
    }
}
