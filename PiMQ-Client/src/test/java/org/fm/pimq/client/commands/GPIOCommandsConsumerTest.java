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
package org.fm.pimq.client.commands;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.easymock.EasyMock;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.impl.PinMessageImpl;
import org.junit.Test;

import javax.jms.*;

/**
 * @author Fabio Marini
 */
public class GPIOCommandsConsumerTest {

    private String jmsQueue = "TESTQUEUE";
    private String url = "vm://localhost?broker.persistent=false";

    @Test
    public void testMeasurment() {

        //Configuring mocks

        GpioController controllerMock = EasyMock.createNiceMock(GpioController.class);
        GpioPinDigitalOutput pinOutMock = EasyMock.createNiceMock(GpioPinDigitalOutput.class);

        EasyMock.expect(controllerMock.provisionDigitalOutputPin(EasyMock.anyObject(Pin.class), EasyMock.anyString(), EasyMock.anyObject(PinState.class))).andReturn(pinOutMock);

        pinOutMock.low();
        EasyMock.expectLastCall();

        EasyMock.replay(controllerMock, pinOutMock);

        //Sending messages to test

        IPinMessage command = new PinMessageImpl(new PinMQ(1), PinStateMQ.LOW);
        sendMessage(command);

        sendMessageToStop();

        // Starting instance to test

        GPIOCommandsConsumerForTest consumer = new GPIOCommandsConsumerForTest(url, jmsQueue, controllerMock);
        Thread brokerThread = new Thread(consumer);
        brokerThread.start();

        try {
            brokerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Verifies tests results
        EasyMock.verify(controllerMock);
        EasyMock.verify(pinOutMock);
    }

    private void sendMessage(IPinMessage command) {
        try {

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

            QueueConnection queueConn = (QueueConnection) connectionFactory.createConnection();
            queueConn.start();

            QueueSession queueSession = queueConn.createQueueSession(false,
                    Session.DUPS_OK_ACKNOWLEDGE);

            Destination destination = queueSession.createQueue(jmsQueue);

            MessageProducer queueSender = queueSession.createProducer(destination);
            queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            ObjectMessage message = queueSession.createObjectMessage(command);
            queueSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToStop() {
        try {

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

            QueueConnection queueConn = (QueueConnection) connectionFactory.createConnection();
            queueConn.start();

            QueueSession queueSession = queueConn.createQueueSession(false,
                    Session.DUPS_OK_ACKNOWLEDGE);

            Destination destination = queueSession.createQueue(jmsQueue);

            MessageProducer queueSender = queueSession.createProducer(destination);
            queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            ObjectMessage message = queueSession.createObjectMessage(new String());
            queueSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}