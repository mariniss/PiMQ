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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

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

    private String connectionUrl;

    private String queueName;

    public GPIOStatesProducer(String connectionUrl, String queueName) {
        this.connectionUrl = connectionUrl;
        this.queueName = queueName;
    }

    public void run() {
        //TODO:
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.jms.ExceptionListener#onException()
     */
    @Override
    public void onException(JMSException exception) {
        logger.error("JMS Exception occurred.  Shutting down client.");
    }
}