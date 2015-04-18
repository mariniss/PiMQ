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
package org.fm.pimq.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

/**
 * PiMQ configuration provider. <br />
 * Usage : <br />
 * <p>
 *     ConfigurationProvider.getInstance().[method to call]([params])
 * </p>
 * @author Fabio Marin
 */
public class ConfigurationProvider {

    /**
     * The default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationProvider.class);

    /**
     * Default configuration file name
     */
    public static final String DEFAULT_CONFIGURATION_FILE_NAME = "conf.properties";

    /**
     * Default configuration full path folder
     */
    public static final String DEFAULT_CONFIGURATION_FOLDER_FULL_PATH = "/home/pi/PiMQ/conf";

    /**
     * configuration separator
     */
    public static final String CONFIGURATION_FILE_SEPARATOR= "=";

    /**
     * Default JMS connection url
     */
    public static final String DEFAULT_SERVER_TYPE= "activemq";

    /**
     * Default JMS connection url
     */
    public static final Long DEFAULT_MESSAGES_TIME_TO_LIVE = 60000L;

    /**
     * Default JMS connection url
     */
    public static final String DEFAULT_CONNECTION_STRING = "tcp://localhost:61616";

    /**
     * Default queue name for GPIO commands
     */
    public static  final String DEFAULT_COMMAND_QUEUE_IDENTIFIER = "PiMQ.GPIO.Commands";

    /**
     * Default queue name for GPIO states
     */
    public static  final String DEFAULT_STATES_QUEUE_IDENTIFIER = "PiMQ.GPIO.States";

    /**
     * Default flag value to enable/disable GPIO commands consumer
     */
    public static final boolean DEFAULT_COMMANDS_ENABLE = true;

    /**
     * Default flag value to enable/disable GPIO states producer
     */
    public static final boolean DEFAULT_STATES_ENABLE = false;

    /**
     * Default flag value to enable/disable w1 bus consumer
     */
    public static final boolean DEFAULT_W1_ENABLE = true;

    /**
     * Default refresh time to read data from w1 bus
     */
    public static final long DEFAULT_W1_REFRESH_TIME = 60;

    /**
     * Default path for w1 bus
     */
    public static  final String DEFAULT_W1BUS_DIRPATH = "/sys/bus/w1/devices";

    /**
     * Default prefix for W1-Therm devices
     */
    public static  final String DEFAULT_W1BUS_DEVICES_PREFIX = "28-";

    /**
     * Default queue name for W1-Therm data
     */
    public static  final String DEFAULT_W1_QUEUE_IDENTIFIER = "PiMQ.GPIO.w1";


    /**
     * Default queue name for W1-Therm data requests
     */
    public static  final String DEFAULT_W1_REQUESTS_QUEUE_IDENTIFIER = "PiMQ.GPIO.w1.requests";

    /************************************************************
     *          Singleton implementation - start
     ************************************************************/

    /**
     * Default instance
     */
    private static ConfigurationProvider instance = null;

    /**
     * Default constructor
     */
    private ConfigurationProvider(){

    }

    public static ConfigurationProvider getInstance(){
        if(instance == null){
            synchronized (ConfigurationProvider.class){
                if(instance == null){
                    instance = new ConfigurationProvider();
                }
            }
        }

        return instance;
    }

    /************************************************************
    *          Singleton implementation - end
    ************************************************************/

    /**
     * Reads the configuration from file system and returns it as {@link Configuration}
     * with the defaults for the mandatory configuration values not specified
     * @param configurationFolderFullPath full path folder that contains the configuration file, if is null the default value is used : {@link ConfigurationProvider#DEFAULT_CONFIGURATION_FOLDER_FULL_PATH}
     * @param configurationFileName configuration file name, if is null the default value is used : {@link ConfigurationProvider#DEFAULT_CONFIGURATION_FILE_NAME}
     * @return the client configuration
     */
    public Configuration getConfiguration(String configurationFolderFullPath, String configurationFileName){
         Configuration conf = new Configuration();

        loadDefaults(conf);

        if(configurationFolderFullPath == null || configurationFolderFullPath.trim().length() == 0) {
            configurationFolderFullPath = DEFAULT_CONFIGURATION_FOLDER_FULL_PATH;
        }

        if(configurationFileName == null || configurationFileName.trim().length() == 0) {
            configurationFileName = DEFAULT_CONFIGURATION_FILE_NAME;
        }

        Path fullPath =  FileSystems.getDefault().getPath(configurationFolderFullPath, configurationFileName);
        logger.debug(MessageFormat.format("Reding configuration form file {0}", fullPath));

        try {
            List<String> lines = Files.readAllLines(fullPath, Charset.defaultCharset());

            for(String line : lines){
                String[] values = line.split(CONFIGURATION_FILE_SEPARATOR);
                if(values != null && values.length == 2){
                    setConfigurationProperty(conf, values[0], values[1]);
                }
            }

        } catch (IOException e) {
            logger.error(MessageFormat.format("Error loading configuration file : {0}", e.getLocalizedMessage()));
        }

        return conf;
    }

    /**
     * Sets the given property on configuration object
     * @param conf the configuration object
     * @param property property name
     * @param value property value
     */
    private void setConfigurationProperty(Configuration conf, String property, String value) {
        if(conf != null && property != null && value != null){
            switch (property){
                case "org.fm.pimq.client.servertype":
                    conf.setServerType(value);
                    break;
                case "org.fm.pimq.client.connection.url" :
                    conf.setConnectionUrl(value);
                    break;
                case "org.fm.pimq.client.msg.ttl":
                    conf.setMessagesTimeToLive(Long.valueOf(value));
                    break;
                case "org.fm.pimq.client.commands.enable" :
                    conf.setEnableCommandsMessages(Boolean.valueOf(value));
                    break;
                case "org.fm.pimq.client.commands.queue" :
                    conf.setCommandsQueueName(value);
                    break;
                case "org.fm.pimq.client.states.enable" :
                    conf.setEnableStatesMessages(Boolean.valueOf(value));
                    break;
                case "org.fm.pimq.client.states.queue" :
                    conf.setStatusQueueName(value);
                    break;
                case "org.fm.pimq.client.ssl.username" :
                    conf.setUsername(value);
                    break;
                case "org.fm.pimq.client.ssl.password" :
                    conf.setPassword(value);
                    break;
                case "org.fm.pimq.client.w1bus.enable" :
                    conf.setEnableW1BusMessages(Boolean.valueOf(value));
                    break;
                case "org.fm.pimq.client.w1bus.refresh.time" :
                    conf.setW1BusMessagesRefresh(Long.valueOf(value));
                    break;
                case "org.fm.pimq.client.w1bus.path" :
                    conf.setW1DirPath(value);
                    break;
                case "org.fm.pimq.client.w1bus.devics.prefix" :
                    conf.setW1DevicesPrefix(value);
                    break;
                case "org.fm.pimq.client.w1bus.queue" :
                    conf.setW1QueueName(value);
                    break;
                case "org.fm.pimq.client.w1bus.requests.queue" :
                    conf.setW1RequestsQueueName(value);
                    break;
            }
        }
    }

    /**
     * Fill the given configuration object with the defaults value
     * @param conf the configuration object to fill
     */
    private void loadDefaults(Configuration conf) {
        if(conf != null){
            conf.setServerType(DEFAULT_SERVER_TYPE);

            conf.setConnectionUrl(DEFAULT_CONNECTION_STRING);

            conf.setMessagesTimeToLive(DEFAULT_MESSAGES_TIME_TO_LIVE);

            conf.setEnableCommandsMessages(DEFAULT_COMMANDS_ENABLE);
            conf.setCommandsQueueName(DEFAULT_COMMAND_QUEUE_IDENTIFIER);

            conf.setEnableStatesMessages(DEFAULT_STATES_ENABLE);
            conf.setStatusQueueName(DEFAULT_STATES_QUEUE_IDENTIFIER);

            conf.setEnableW1BusMessages(DEFAULT_W1_ENABLE);
            conf.setW1BusMessagesRefresh(DEFAULT_W1_REFRESH_TIME);
            conf.setW1DirPath(DEFAULT_W1BUS_DIRPATH);
            conf.setW1DevicesPrefix(DEFAULT_W1BUS_DEVICES_PREFIX);
            conf.setW1QueueName(DEFAULT_W1_QUEUE_IDENTIFIER);
            conf.setW1RequestsQueueName(DEFAULT_W1_REQUESTS_QUEUE_IDENTIFIER);

            conf.setUsername(null);
            conf.setPassword(null);
        }
    }
}
