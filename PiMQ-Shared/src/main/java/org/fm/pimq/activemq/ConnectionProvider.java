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
package org.fm.pimq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.commons.lang.StringUtils;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.net.IConnectionProviderStrategy;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Connection provider for ActiveMQ JMS server
 * @author Fabio Marini
 */
public class ConnectionProvider implements IConnectionProviderStrategy{

    /** {@inheritDoc} */
    @Override
    public Connection createConnection(Configuration conf) throws JMSException {

        if(conf == null)
        {
            throw new IllegalArgumentException("Configuration object cannot be null");
        }

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(conf.getConnectionUrl());
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setInitialRedeliveryDelay(1000L);
        policy.setMaximumRedeliveries(RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES);
        connectionFactory.setRedeliveryPolicy(policy);

        Connection connection;
        if(StringUtils.isNotBlank(conf.getUsername())) {
            connection = connectionFactory.createConnection(conf.getUsername(), conf.getPassword());
        }
        else{
            connection = connectionFactory.createConnection();
        }

        return connection;
    }
}
