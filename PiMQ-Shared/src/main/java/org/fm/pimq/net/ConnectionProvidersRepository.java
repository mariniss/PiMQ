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
package org.fm.pimq.net;

import org.apache.commons.lang.StringUtils;
import org.fm.pimq.activemq.ConnectionProvider;

/**
 *
 * @author Fabio Marini
 */
public class ConnectionProvidersRepository {

    /**
     * Enum that defines all connection providers
     */
    private enum ConnectionProviderEnum{

        ACTIVEMQ("activemq", new ConnectionProvider());

        private String name;
        private IConnectionProviderStrategy strategy;

        /**
         * Build the strategy definition
         * @param name JMS server name
         * @param strategy connection provider strategy
         */
        private ConnectionProviderEnum(String name, IConnectionProviderStrategy strategy){
            this.name = name;
            this.strategy = strategy;
        }

        public String getName() {
            return name;
        }

        public IConnectionProviderStrategy getStrategy() {
            return strategy;
        }
    }

    /**
     * Returns the connection strategy for the given JMS server
     * @param jmsServerName the JMS server name
     * @return the connection provider strategy
     */
    public static IConnectionProviderStrategy getConnectionStrategy(String jmsServerName){

        if(StringUtils.isNotBlank(jmsServerName)) {
            for(ConnectionProviderEnum connectionProvider : ConnectionProviderEnum.values()) {
                if(jmsServerName.equals(connectionProvider.getName())) {
                    return connectionProvider.getStrategy();
                }
            }

            throw new IllegalArgumentException("Unknown jmsServerName");
        }
        else {
            throw new IllegalArgumentException("jmsServerName cannot be empty");
        }
    }
}
