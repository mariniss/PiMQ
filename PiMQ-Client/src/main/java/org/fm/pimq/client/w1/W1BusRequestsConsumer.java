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
package org.fm.pimq.client.w1;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.W1ThermMQ;
import org.fm.pimq.bus.w1.device.DevicesSnifer;
import org.fm.pimq.bus.w1.therm.Temperature;
import org.fm.pimq.bus.w1.therm.TemperatureDeviceReader;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.impl.PinMessageImpl;
import org.fm.pimq.net.ConnectionProvidersRepository;
import org.fm.pimq.net.IConnectionProviderStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.File;

/**
 * The JMS consumer for the W1 Therm data request. <br/>
 * Sends a JMS message with the W1-Therm data every receive a request a from its JMS request queue
 *
 * @author Fabio Marini
 */
public class W1BusRequestsConsumer implements Runnable, ExceptionListener {

    /**
     * The GPIO pin linked to the W1-Therm
     */
    public static final int W1_GPIO_PIN_NUMBER = 4;

    /**
     * The default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(W1BusRequestsConsumer.class);

    /**
     *  Configuration instance
     */
    protected Configuration configuration;


    /**
     * ActiveMQ objects to send JMS messages
     */
    protected Session session;
    protected MessageProducer producer;
    protected Destination w1DataDestination;

    /**
     * Build the consumer with the given attributes
     * @param configuration configuration object with all information necessary to connect to JMS server
     */
    public W1BusRequestsConsumer(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        try {
            if(configuration == null){
                throw new IllegalArgumentException("Configuration cannot be null");
            }

            IConnectionProviderStrategy connectionProvider = ConnectionProvidersRepository.getConnectionStrategy(configuration.getServerType());
            Connection connection = connectionProvider.createConnection(configuration);

            connection.start();
            connection.setExceptionListener(this);

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination w1RequestsDestination = session.createQueue(configuration.getW1RequestsQueueName());
            MessageConsumer consumer = session.createConsumer(w1RequestsDestination);

            w1DataDestination = session.createQueue(configuration.getW1QueueName());
            producer = session.createProducer(w1DataDestination);

            for ( ; ; ) {
                Message message = consumer.receive();

                if (((ActiveMQObjectMessage) message).getObject() instanceof IPinMessage) {
                    IPinMessage requestMessage = (IPinMessage) ((ActiveMQObjectMessage) message).getObject();

                    StringBuilder errorMsg = new StringBuilder();
                    if (isValidCommand(requestMessage, errorMsg)) {
                        logger.debug("Received request to read w1 therm data");

                        executeRequest(requestMessage);
                    } else {
                        logger.error("Received invalid command : " + errorMsg.toString());
                    }

                } else {
                    logger.error("Received JMS messages that is not a IPinMessage. Main loop ended");
                    break;
                }

                message = null;
            }

            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            logger.error("Caught: " + e);
        }
    }

    /**
     * Checks if the received message can be handled or not
     *
     * @param requestMessage the request message to check
     * @param errorMsg       the error message, if is not valid
     * @return true if the message is valid, false otherwise
     */
    private boolean isValidCommand(IPinMessage requestMessage, StringBuilder errorMsg) {
        if (requestMessage == null) {
            errorMsg.append("Null command");
            return false;
        }

        if (requestMessage.getPin() == null) {
            errorMsg.append("Null pin");
            return false;
        }

        if (requestMessage.getPin().getPinNumber() == null) {
            errorMsg.append("Null pin number");
            return false;
        }

        if (requestMessage.getPin().getPinNumber() != 4) {
            errorMsg.append("Pin number for w1 bus must be 4");
            return false;
        }

        if (requestMessage.getState() != null || requestMessage.getW1Data() != null) {
            errorMsg.append("Received request with invalid data");
            return false;
        }

        return true;
    }


    /**
     * Executes the given read W1-Therm data request
     * @param requestMessage the W1-Therm data request
     * @throws JMSException when there are errors sending JMS message
     */
    private void executeRequest(IPinMessage requestMessage) throws JMSException {
        if(requestMessage != null) {
            DevicesSnifer snifer = DevicesSnifer.getInstance(configuration);
            TemperatureDeviceReader reader = TemperatureDeviceReader.getInstance(configuration);

            if (snifer.count() > 0) {
                for (File file : snifer.getDevicesDir()) {
                    Temperature temperature = reader.getTemperature(file);
                    logger.debug("Read temperature: C:" + temperature.getCelsius() + " - F:" + temperature.getFahrenheit());

                    sendW1ThermMessage(temperature);
                }
            }
        }
    }


    /**
     * Send a JMS message with the given temperature read from W1-Therm senson
     * @param temperature the temperature data to send
     * @throws JMSException when there are errors sending JMS message
     */
    private void sendW1ThermMessage(Temperature temperature) throws JMSException {
        IPinMessage therm = createW1ThermMessage(temperature);
        if (therm != null) {
            ObjectMessage message = session.createObjectMessage(therm);
            producer.send(message);

            logger.debug("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        }
        else {
            logger.warn("Cannot send w1 therm data message : " + temperature.toString());
        }
    }


    /**
     * Create the PinMessage to send by JMS
     * @param temperature the temperature data to send
     * @return an instance of IPinMessage with the given temperature data
     */
    private IPinMessage createW1ThermMessage(Temperature temperature) {
        if(temperature != null) {
            PinMQ pinMQ = new PinMQ(W1_GPIO_PIN_NUMBER);
            W1ThermMQ w1ThermMQ = new W1ThermMQ(temperature.getMillisCelsius());

            return new PinMessageImpl(pinMQ, w1ThermMQ);
        }

        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void onException(JMSException exc) {
        logger.warn("caught JMSException, most likely a timeout continuing main loop", exc);

        this.run();
    }
}