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

import com.pi4j.io.gpio.GpioController;
import org.fm.pimq.conf.Configuration;

/**
 * Extension to use mocks for Pi4J libraries
 * @author Fabio Marini
 */
public class GPIOStatesProducerForTest extends GPIOStatesProducer {

    /**
     * Build the instance for the unit test
     * @param conf the JMS server configuraiton
     * @param controller GPIO controller (mock)
     */
    public GPIOStatesProducerForTest(Configuration conf, GpioController controller) {
        super(conf, true);

        this.gpioController = controller;
    }
}
