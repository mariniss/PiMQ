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

import org.fm.pimq.conf.Configuration;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Interface to define the connection provider strategy
 * @author Fabio Marini
 */
public interface IConnectionProviderStrategy {

    /**
     * Create the connection with the JMS server
     * @param conf the configuration object
     * @return the connection instance
     * @throws JMSException if there are problems during the connection or the command communication
     */
    Connection createConnection(Configuration conf) throws JMSException;
}
