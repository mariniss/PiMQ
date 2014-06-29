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

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.impl.PinMessageImpl;

import javax.jms.*;

/**
 * @author Fabio Marini
 */
public class PiPinServiceHelper {

    /****************************************************
     * Singleton implementation - start
     ****************************************************/

    /**
     *
     */
    private static PiPinServiceHelper instance = null;

    /**
     *
     */
    private PiPinServiceHelper() {

    }

    public static synchronized PiPinServiceHelper getDefaultInstance() {
        if (instance == null) {
            synchronized (PiPinServiceHelper.class) {
                if (instance == null) {
                    instance = new PiPinServiceHelper();
                }
            }
        }

        return instance;
    }

    /**
     * *************************************************
     * Singleton implementation - end
     * **************************************************
     */

    public static final String DEFAULT_ENDPOINT = "tcp://localhost:61616";

    /**
     * @param pinCommand
     * @throws JMSException
     */
    public void sendCommand(IPinMessage pinCommand) throws JMSException {
        sendCommand(pinCommand, DEFAULT_ENDPOINT);
    }

    /**
     * @param pinCommand
     * @param endpoint
     * @throws JMSException
     */
    public void sendCommand(IPinMessage pinCommand, String endpoint) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(endpoint);
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setInitialRedeliveryDelay(1000L);
        policy.setMaximumRedeliveries(RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES);

        connectionFactory.setRedeliveryPolicy(policy);
        //connectionFactory.setUseRetroactiveConsumer(true);
        Connection connection = connectionFactory.createConnection();

        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue("GPIO.Commands");

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        ObjectMessage message = session.createObjectMessage(pinCommand);

        // Tell the producer to send the message
        System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);

        // Clean up
        session.close();
        connection.close();
    }
}
