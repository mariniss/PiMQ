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

    private Long messagesTimeToLive;

    private boolean enableCommandsMessages;

    private String commandsQueueName;

    private boolean enableStatesMessages;

    private String statusQueueName;

    private String username;

    private String password;

    private boolean enableW1BusMessages;

    private Long w1BusMessagesRefresh;

    private String w1DirPath;

    private String w1DevicesPrefix;

    private String w1QueueName;

    private String w1RequestsQueueName;

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

    public Long getMessagesTimeToLive() {
        return messagesTimeToLive;
    }

    public void setMessagesTimeToLive(Long messagesTimeToLive) {
        this.messagesTimeToLive = messagesTimeToLive;
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
        return enableStatesMessages;
    }

    public void setEnableStatesMessages(boolean isEnableStatesMessages) {
        this.enableStatesMessages = isEnableStatesMessages;
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

    public boolean isEnableW1BusMessages() {
        return enableW1BusMessages;
    }

    public void setEnableW1BusMessages(boolean enableW1BusMessages) {
        this.enableW1BusMessages = enableW1BusMessages;
    }

    public String getW1DirPath() {
        return w1DirPath;
    }

    public void setW1DirPath(String w1DirPath) {
        this.w1DirPath = w1DirPath;
    }

    public Long getW1BusMessagesRefresh() {
        return w1BusMessagesRefresh;
    }

    public void setW1BusMessagesRefresh(Long w1BusMessagesRefresh) {
        this.w1BusMessagesRefresh = w1BusMessagesRefresh;
    }

    public String getW1DevicesPrefix() {
        return w1DevicesPrefix;
    }

    public void setW1DevicesPrefix(String w1DevicesPrefix) {
        this.w1DevicesPrefix = w1DevicesPrefix;
    }

    public String getW1QueueName() {
        return w1QueueName;
    }

    public String getW1RequestsQueueName() {
        return w1RequestsQueueName;
    }

    public void setW1RequestsQueueName(String w1RequestsQueueName) {
        this.w1RequestsQueueName = w1RequestsQueueName;
    }

    public void setW1QueueName(String w1QueueName) {
        this.w1QueueName = w1QueueName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Configuration{" +
                "serverType='" + serverType + '\'' +
                ", connectionUrl='" + connectionUrl + '\'' +
                ", messagesTimeToLive=" + messagesTimeToLive +
                ", enableCommandsMessages=" + enableCommandsMessages +
                ", commandsQueueName='" + commandsQueueName + '\'' +
                ", enableStatesMessages=" + enableStatesMessages +
                ", statusQueueName='" + statusQueueName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enableW1BusMessages=" + enableW1BusMessages +
                ", w1BusMessagesRefresh=" + w1BusMessagesRefresh +
                ", w1DirPath='" + w1DirPath + '\'' +
                ", w1DevicesPrefix='" + w1DevicesPrefix + '\'' +
                ", w1QueueName='" + w1QueueName + '\'' +
                ", w1RequestsQueueName='" + w1RequestsQueueName + '\'' +
                '}';
    }
}
