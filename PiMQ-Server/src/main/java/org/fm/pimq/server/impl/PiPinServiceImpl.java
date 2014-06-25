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
package org.fm.pimq.server.impl;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.commons.lang.NotImplementedException;
import org.fm.pimq.IPinCommand;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.impl.PinCommandImpl;
import org.fm.pimq.server.IPiPinService;
import org.fm.pimq.server.dataobject.PiPin;
import org.fm.pimq.server.helpers.PiPinServiceHelper;
import org.fm.pimq.server.helpers.PiPinServiceResponseHelper;

import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Fabio Marini
 */
public class PiPinServiceImpl implements IPiPinService {

    @GET
    @Path("/getPin/{piPinId}")
    @Produces({"application/xml","application/json"})
    @Consumes({"application/xml","application/json","text/html", "application/xhtml+xml" ,"image/webp","application/xml"})
    @Override
    public PiPin getPinState(@PathParam("piPinId") int piPinId) {
        throw new NotImplementedException("Not implemented yet");
    }

    @POST
    @Path("/setPin")
    @Produces({"application/xml","application/json", "text/plain", "application/x-www-form-urlencoded"})
    @Consumes({"application/xml","application/json","text/html", "application/xhtml+xml" ,"image/webp","application/xml","text/plain", "application/x-www-form-urlencoded"})
    @Override
    public Response setPinState(@FormParam("piPinId") int piPinId, @FormParam("state") int state) {
        try {

            IPinCommand command = new PinCommandImpl(new PinMQ(piPinId), (state == 0) ? PinStateMQ.LOW : PinStateMQ.HIGH);
            PiPinServiceHelper.getDefaultInstance().sendCommand(command);

            return PiPinServiceResponseHelper.getDefaultInstance().buildResponse(Response.Status.OK);

        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();


            return PiPinServiceResponseHelper.getDefaultInstance().buildResponse(Response.Status.BAD_REQUEST);
        }
    }
}
