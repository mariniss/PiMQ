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
package org.fm.pimq.client.states;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.easymock.EasyMock;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.client.commands.GPIOCommandsConsumerForTest;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.impl.PinMessageImpl;
import org.junit.Test;

import javax.jms.*;

/**
 * @author Fabio Marini
 */
public class GPIOStatesProducerTest {

    private String jmsQueue = "TESTQUEUE";
    private String url = "vm://localhost?broker.persistent=false";

    @Test
    public void testSendCommandHigh() {
        //Configuring mocks

        GpioController controllerMock = EasyMock.createNiceMock(GpioController.class);
        GpioPinDigitalInput pinInMock = EasyMock.createNiceMock(GpioPinDigitalInput.class);

        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_08, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_09, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_10, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_11, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_12, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_13, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_14, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_15, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_16, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_17, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_18, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_19, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);
        EasyMock.expect(controllerMock.provisionDigitalInputPin(RaspiPin.GPIO_20, PinPullResistance.PULL_DOWN)).andReturn(pinInMock);

        pinInMock.addListener(EasyMock.anyObject(GpioPinListenerDigital.class));
        EasyMock.expectLastCall().times(20);

        EasyMock.replay(controllerMock, pinInMock);

        // Starting instance to test
        Configuration conf = new Configuration();
        conf.setConnectionUrl(url);
        conf.setStatusQueueName(jmsQueue);
        conf.setServerType("activemq");
        GPIOStatesProducerForTest producer = new GPIOStatesProducerForTest(conf, controllerMock);
        Thread brokerThread = new Thread(producer);
        brokerThread.start();

        try {
            Thread.sleep(5000);
            brokerThread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Verifies tests results

        EasyMock.verify(controllerMock);
        EasyMock.verify(pinInMock);
    }
}
