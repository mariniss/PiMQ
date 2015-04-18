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
package org.fm.pimq.server.snippet;

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
public class PiMQServerW1Therm {

    public static void main(String[] args) throws Exception {
        thread(new GPIOW1RequestProducer(), false);
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

    public static class GPIOW1RequestProducer implements Runnable {
        public void run() {
            try {

                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("http://192.168.1.3:61616");
                //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("http://ec2-54-77-129-207.eu-west-1.compute.amazonaws.com:61516");

                RedeliveryPolicy policy = new RedeliveryPolicy();
                policy.setInitialRedeliveryDelay(1000L);
                policy.setMaximumRedeliveries(RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES);

                connectionFactory.setRedeliveryPolicy(policy);

                //Connection connection = connectionFactory.createConnection();
                Connection connection = connectionFactory.createConnection("fabio", "fabio");

                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue("PiMQ.GPIO.w1.requests");

                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                IPinMessage command = new PinMessageImpl(new PinMQ(4));

                ObjectMessage message = session.createObjectMessage(command);

                System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);

                // Clean up
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }
}