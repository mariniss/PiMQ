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
 * This interface defines a generic status of a Raspberry Pi
 *
 * @author Fabio Marini
 */
public interface IPinStatus extends Serializable {

    /**
     * Returns the pin identifier
     * @return the raspberry pin
     */
    PinMQ getPin();

    /**
     * Return the pin state
     * @return the pin state
     */
    PinStateMQ getState();
}