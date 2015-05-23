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
package org.fm.pimq.snippet;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.impl.PinMessageImpl;

import javax.jms.*;

/**
 * Snippet class to consume W1Therm data provided from PiMQ Client
 *
 * @author Fabio Marini
 */
public class W1ThermDataConsumerSnippet {

    public static void main(String[] args) throws Exception {
        thread(new GPIOW1RequestConsumer(), false);
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

    public static class GPIOW1RequestConsumer implements Runnable {
        public void run() {
            try {

                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("http://192.168.1.3:61616");

                RedeliveryPolicy policy = new RedeliveryPolicy();
                policy.setInitialRedeliveryDelay(1000L);
                policy.setMaximumRedeliveries(RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES);

                connectionFactory.setRedeliveryPolicy(policy);

                Connection connection = connectionFactory.createConnection("fabio", "fabio");

                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue("PiMQ.GPIO.w1");

                MessageConsumer consumer = session.createConsumer(destination);

                for ( ; ; ) {
                    Message message = consumer.receive();
                    System.out.println("Received message " + message.hashCode() + " : " + Thread.currentThread().getName());

                    IPinMessage commandMessage = (IPinMessage) ((ActiveMQObjectMessage) message).getObject();
                    System.out.println("Received therm: " + commandMessage.getW1Data().getTherm());
                }

                // Clean up
                //session.close();
                //connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }
}
