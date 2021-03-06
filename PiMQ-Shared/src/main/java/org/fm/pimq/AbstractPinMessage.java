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
 * Base implementation of {@link IPinMessage}
 *
 * @author Fabio Marini
 */
public abstract class AbstractPinMessage implements IPinMessage {

    /**
     * The raspberry pint to send/receive command
     */
    protected PinMQ pin;

    /**
     * The pin state to set/receive command
     */
    protected PinStateMQ state;


    /**
     * The w1-therm data
     */
    protected W1ThermMQ therm;


    /** {@inheritDoc} */
    @Override
    public PinMQ getPin() {
        return this.pin;
    }

    /** {@inheritDoc} */
    @Override
    public PinStateMQ getState() {
        return this.state;
    }

    /** {@inheritDoc} */
    @Override
    public void setPin(PinMQ pin) {
        this.pin = pin;
    }

    /** {@inheritDoc} */
    @Override
    public void setState(PinStateMQ state) {
        this.state = state;
    }

    /** {@inheritDoc} */
    @Override
    public void setW1Data(W1ThermMQ data) {
        this.therm = data;
    }

    /** {@inheritDoc} */
    @Override
    public W1ThermMQ getW1Data() {
        return this.therm;
    }
}
