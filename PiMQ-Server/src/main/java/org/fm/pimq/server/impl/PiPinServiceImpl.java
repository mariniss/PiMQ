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

import org.apache.commons.lang.NotImplementedException;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.conf.ConfigurationProvider;
import org.fm.pimq.impl.PinMessageImpl;
import org.fm.pimq.server.IPiPinService;
import org.fm.pimq.server.dataobject.PiPin;
import org.fm.pimq.server.helpers.PiPinServiceHelper;
import org.fm.pimq.server.helpers.PiPinServiceResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * RESTFull implementation for the service {@link IPiPinService}
 *
 * @author Fabio Marini
 */
public class PiPinServiceImpl implements IPiPinService {

    /**
     * The default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(IPiPinService.class);

    private PiPinServiceHelper piPinServiceHelper;

    private String configurationFolderFullPath;
    private String configurationFileName;

    /** {@inheritDoc} */
    @GET
    @Path("/getPin/{piPinId}")
    @Produces({"application/xml","application/json"})
    @Consumes({"application/xml","application/json","text/html", "application/xhtml+xml" ,"image/webp","application/xml"})
    @Override
    public PiPin getPinState(@PathParam("piPinId") int piPinId) {
        throw new NotImplementedException("Not implemented yet");
    }

    /** {@inheritDoc} */
    @POST
    @Path("/setPin")
    @Produces({"application/xml","application/json", "text/plain", "application/x-www-form-urlencoded"})
    @Consumes({"application/xml","application/json","text/html", "application/xhtml+xml" ,"image/webp","application/xml","text/plain", "application/x-www-form-urlencoded"})
    @Override
    public Response setPinState(@FormParam("piPinId") int piPinId, @FormParam("state") int state) {
        try {
            IPinMessage command = new PinMessageImpl(new PinMQ(piPinId), (state == 0) ? PinStateMQ.LOW : PinStateMQ.HIGH);

            Configuration configuration = ConfigurationProvider.getInstance()
                                                    .getConfiguration(configurationFolderFullPath,
                                                            configurationFileName);

            piPinServiceHelper.sendCommand(command, configuration);

            return PiPinServiceResponseHelper.getDefaultInstance().buildResponse(Response.Status.OK);

        } catch (Exception e) {
            logger.error("Caught: {}", e.getLocalizedMessage());

            return PiPinServiceResponseHelper.getDefaultInstance().buildResponse(Response.Status.BAD_REQUEST);
        }
    }

    public void setConfigurationFolderFullPath(String configurationFolderFullPath) {
        this.configurationFolderFullPath = configurationFolderFullPath;
    }

    public void setConfigurationFileName(String configurationFileName) {
        this.configurationFileName = configurationFileName;
    }

    public void setPiPinServiceHelper(PiPinServiceHelper piPinServiceHelper) {
        this.piPinServiceHelper = piPinServiceHelper;
    }
}
