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
package org.fm.pimq.client.conf;

import junit.framework.Assert;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.conf.ConfigurationProvider;
import org.junit.Test;

/**
 * Test class for {@link org.fm.pimq.conf.ConfigurationProvider}
 * @author Fabio Marini
 */
public class ConfigurationProviderTest {

    @Test
     public void getConfigurationLoadDefaultsTest(){

        //Method to test
        Configuration conf = ConfigurationProvider.getInstance().getConfiguration(null, null);

        //Checking results
        Assert.assertNotNull(conf);

        Assert.assertEquals("tcp://localhost:61616", conf.getConnectionUrl());
        Assert.assertEquals("PiMQ.GPIO.Commands", conf.getCommandsQueueName());
        Assert.assertEquals("PiMQ.GPIO.States", conf.getStatusQueueName());
        Assert.assertEquals(true, conf.isEnableCommandsMessages());
        Assert.assertEquals(false, conf.isEnableStatesMessages());
    }

    @Test
    public void getConfigurationTest(){

        //Method to test
        Configuration conf = ConfigurationProvider.getInstance().getConfiguration(getClass().getResource("/").getPath(), "conf-test.properties");

        //Checking results
        Assert.assertNotNull(conf);

        Assert.assertEquals("url-local", conf.getConnectionUrl());
        Assert.assertEquals("commands-test", conf.getCommandsQueueName());
        Assert.assertEquals("states-test", conf.getStatusQueueName());
        Assert.assertEquals(false, conf.isEnableCommandsMessages());
        Assert.assertEquals(true, conf.isEnableStatesMessages());
    }
}
