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
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.impl.PinMessageImpl;
import org.fm.pimq.net.ConnectionProvidersRepository;
import org.fm.pimq.net.IConnectionProviderStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The JMS producer for the GPIO states. <br/>
 * Puts all states of GPIO to a JMS queue.
 *
 * @author Fabio Marini
 */
public class GPIOStatesProducer implements Runnable, ExceptionListener {
    /**
     * The default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(GPIOStatesProducer.class);

    /**
     * GPIO controller
     */
    protected GpioController gpioController;

    /**
     * Map of pin -> Digital output manager
     */
    protected Map<Pin, GpioPinDigitalInput> pinDigitalInputMap = new HashMap<>();

    /**
     *  Configuration instance
     */
    protected Configuration configuration;

    protected Session session;
    protected MessageProducer producer;

    /**
     * Build the producer with the given attributes
     * @param configuration configuration object with all information necessary to connect to JMS server
     * @param instanceForTest flags that indicate if the created instance is used only to unit testing
     */
    public GPIOStatesProducer(Configuration configuration, boolean instanceForTest) {
        this.configuration = configuration;

        if (!instanceForTest) {
            gpioController = GpioFactory.getInstance();
        }
    }

    /**
     * Build the producer with the given attributes
     *
     * @param configuration configuration object with all information necessary to connect to JMS server
     */
    public GPIOStatesProducer(Configuration configuration) {
        this(configuration, false);
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        try {
            if(configuration == null){
                throw new IllegalArgumentException("Configuration cannot be null");
            }

            IConnectionProviderStrategy connectionProvider = ConnectionProvidersRepository.getConnectionStrategy(configuration.getServerType());
            Connection connection = connectionProvider.createConnection(configuration);

            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(configuration.getStatusQueueName());

            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            fillPinDigitalInputMap();

            Set<Map.Entry<Pin, GpioPinDigitalInput>> entries = pinDigitalInputMap.entrySet();
            Iterator<Map.Entry<Pin, GpioPinDigitalInput>> iterator = entries.iterator();
            while(iterator.hasNext()){
                Map.Entry<Pin, GpioPinDigitalInput> gpioIn = iterator.next();
                // create and register gpio pin listener
                gpioIn.getValue().addListener(new GpioPinListenerDigital() {
                    @Override
                    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                        logger.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                        try {
                            sendStatusMessage(event);
                        } catch (JMSException e) {
                            logger.error("Caught: " + e);
                        }
                    }

                });
            }

            for ( ; ; ) {
                Thread.sleep(60000);
            }

            // Clean up - not necessary
            //session.close();
            //connection.close();

        } catch (Exception e) {
            logger.error("Caught: " + e);
        }
    }

    private void fillPinDigitalInputMap() {
        pinDigitalInputMap.put(RaspiPin.GPIO_00, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_01, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_02, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_03, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_04, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_05, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_06, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_07, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_08, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_08, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_09, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_09, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_10, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_10, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_11, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_11, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_12, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_12, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_13, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_13, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_14, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_14, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_15, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_15, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_16, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_16, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_17, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_17, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_18, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_18, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_19, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_19, PinPullResistance.PULL_DOWN));
        pinDigitalInputMap.put(RaspiPin.GPIO_20, gpioController.provisionDigitalInputPin(RaspiPin.GPIO_20, PinPullResistance.PULL_DOWN));
    }

    private void sendStatusMessage(GpioPinDigitalStateChangeEvent event) throws JMSException {
        IPinMessage status = createStatusMessage(event.getPin(), event.getState());
        if(status != null) {
            ObjectMessage message = session.createObjectMessage(status);
            producer.send(message);

            logger.debug("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        }
        else {
            logger.warn("Cannot send message for event : " + event.toString());
        }
    }

    private IPinMessage createStatusMessage(GpioPin pin, PinState state) {
        PinMQ pinMQ;
        PinStateMQ pinStateMQ;

        if(pin != null && state != null) {
            int piAddress = pin.getPin().getAddress();

            pinMQ = new PinMQ(piAddress);

            if(state.equals(PinState.HIGH)) {
                pinStateMQ = PinStateMQ.HIGH;
            }
            else{
                pinStateMQ = PinStateMQ.LOW;
            }

            return new PinMessageImpl(pinMQ, pinStateMQ);
        }

        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void onException(JMSException exception) {
        logger.error("JMS Exception occurred.  Shutting down client.");
    }
}