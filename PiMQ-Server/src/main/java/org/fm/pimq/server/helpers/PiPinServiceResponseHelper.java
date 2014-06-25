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
package org.fm.pimq.server.helpers;

import javax.ws.rs.core.Response;
/**
 * @author Fabio Marini
 */
public class PiPinServiceResponseHelper {
    /****************************************************
     * Singleton implementation - start
     ****************************************************/

    /**
     *
     */
    private static PiPinServiceResponseHelper instance = null;

    /**
     *
     */
    private PiPinServiceResponseHelper(){

    }

    /**
     *
     * @return
     */
    public static PiPinServiceResponseHelper getDefaultInstance() {
        if (instance == null) {
            synchronized (PiPinServiceResponseHelper.class) {
                if (instance == null) {
                    instance = new PiPinServiceResponseHelper();
                }
            }
        }

        return instance;
    }
    /****************************************************
     * Singleton implementation - start
     ****************************************************/

    /**
     *
     * @return
     */
    public String buildXMLPositiveResponse(){
        return "<PiPinService><Response>Ok</Response></PiPinService>";
    }

    /**
     *
     * @return
     */
    public String buildXMLNegativeResponse(){
        return "<PiPinService><Response>Ok</Response></PiPinService>";
    }

    /**
     *
     * @param result
     * @param type
     * @return
     */
    public Response buildOkResponse(Object result, String type) {
        return Response.ok(result, type).build();
    }

    /**
     *
     * @param stausType
     * @return
     */
    public Response buildResponse(Response.StatusType stausType) {
        return Response.ok(stausType).build();
    }
}
