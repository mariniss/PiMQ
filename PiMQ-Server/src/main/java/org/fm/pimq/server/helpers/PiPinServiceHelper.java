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

import org.fm.pimq.IPinMessage;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.net.IConnectionProviderStrategy;

import javax.jms.*;

/**
 * Helper class to simplify {@link org.fm.pimq.server.IPiPinService} implementation
 * @author Fabio Marini
 */
public class PiPinServiceHelper {

    private IConnectionProviderStrategy connectionProviderStrategy;

    /**
     * Send the given {@link IPinMessage} to the PiMQ command queue
     * @param pinCommand the command to send
     * @param conf the configuration to connect to the queue
     * @throws JMSException if there are problems during the connection or the command communication
     */
    public void sendCommand(IPinMessage pinCommand, Configuration conf) throws JMSException {
        if(pinCommand!= null && conf != null) {

            Connection connection = connectionProviderStrategy.createConnection(conf);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(conf.getCommandsQueueName());

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            ObjectMessage message = session.createObjectMessage(pinCommand);
            producer.send(message);

            session.close();
            connection.close();
        }
    }

    public void setConnectionProviderStrategy(IConnectionProviderStrategy connectionProviderStrategy) {
        this.connectionProviderStrategy = connectionProviderStrategy;
    }
}
