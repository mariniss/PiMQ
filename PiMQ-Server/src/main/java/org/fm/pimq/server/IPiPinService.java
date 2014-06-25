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
package org.fm.pimq.server;

import org.fm.pimq.server.dataobject.PiPin;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Basic REST service to send commands and request state of Raspberry Pi pins
 *
 * @author Fabio Marini
 */
public interface IPiPinService {

    /**
     * Returns the state of requested Raspberry Pi pin
     * @param piPinId the pin id
     * @return the pin state
     */
    PiPin getPinState(int piPinId);


    /**
     * Make a JMS request to set a specific state to the given Raspberry Pi pin
     * @param piPinId the Raspberry Pi pin
     * @param state the state to set
     * @return a positive response if if the request has been sent successfully, negative otherwise
     */
    Response setPinState(int piPinId, int state);
}
