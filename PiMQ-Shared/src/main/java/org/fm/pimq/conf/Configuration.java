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

/**
 * This class encapsulates all information necessary to configure PiMQ client
 *
 * @author Fabio Marini
 */
public class Configuration {

    private String serverType;

    private String connectionUrl;

    private boolean enableCommandsMessages;

    private String commandsQueueName;

    private boolean isEnableStatesMessages;

    private String statusQueueName;

    private String username;

    private String password;

    /**
     * Default constructor
     */
    public Configuration(){

    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public boolean isEnableCommandsMessages() {
        return enableCommandsMessages;
    }

    public void setEnableCommandsMessages(boolean enableCommandsMessages) {
        this.enableCommandsMessages = enableCommandsMessages;
    }

    public String getCommandsQueueName() {
        return commandsQueueName;
    }

    public void setCommandsQueueName(String commandsQueueName) {
        this.commandsQueueName = commandsQueueName;
    }

    public boolean isEnableStatesMessages() {
        return isEnableStatesMessages;
    }

    public void setEnableStatesMessages(boolean isEnableStatesMessages) {
        this.isEnableStatesMessages = isEnableStatesMessages;
    }

    public String getStatusQueueName() {
        return statusQueueName;
    }

    public void setStatusQueueName(String statusQueueName) {
        this.statusQueueName = statusQueueName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Configuration{" +
                "serverType='" + serverType + '\'' +
                ", connectionUrl='" + connectionUrl + '\'' +
                ", enableCommandsMessages=" + enableCommandsMessages +
                ", commandsQueueName='" + commandsQueueName + '\'' +
                ", isEnableStatesMessages=" + isEnableStatesMessages +
                ", statusQueueName='" + statusQueueName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
