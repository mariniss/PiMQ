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
package org.fm.pimq.webapp

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.commons.lang.NotImplementedException;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.PinMQ;
import org.fm.pimq.PinStateMQ;
import org.fm.pimq.impl.PinMessageImpl

import javax.jms.*;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * 
 * @author Fabio Marini
 *
 */
class PiPinController {

    static allowedMethods = [update: ["GET", "PUT"]]

	static responseFormats = ['json', 'xml']

//	def update(PiPin pipin) {
//		if(piPin == null) {
//			return super.update();
//		}
//		
//		try {
//			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
//			RedeliveryPolicy policy = new RedeliveryPolicy();
//			policy.setInitialRedeliveryDelay(1000L);
//			policy.setMaximumRedeliveries(RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES);
//
//			connectionFactory.setRedeliveryPolicy(policy);
//			//connectionFactory.setUseRetroactiveConsumer(true);
//			Connection connection = connectionFactory.createConnection();
//
//			connection.start();
//
//			// Create a Session
//			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//			// Create the destination (Topic or Queue)
//			Destination destination = session.createQueue("GPIO.Commands");
//
//			// Create a MessageProducer from the Session to the Topic or Queue
//			MessageProducer producer = session.createProducer(destination);
//			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//
//			//IPinMessage command = new PinMessageImpl(RaspiPin.GPIO_01, PinState.HIGH);
//			IPinMessage command = new PinMessageImpl(pipin.pin, pipin.state);
//
//			ObjectMessage message = session.createObjectMessage(command);
//
//			// Tell the producer to send the message
//			System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
//			producer.send(message);
//
//			// Clean up
//			session.close();
//			connection.close();
//
//			return Response.status(Response.Status.OK).build();
//
//		} catch (Exception e) {
//			System.out.println("Caught: " + e);
//			e.printStackTrace();
//
//			return Response.status(Response.Status.BAD_REQUEST).build();
//		}
//	}
	
	def update() {
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			RedeliveryPolicy policy = new RedeliveryPolicy();
			policy.setInitialRedeliveryDelay(1000L);
			policy.setMaximumRedeliveries(RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES);

			connectionFactory.setRedeliveryPolicy(policy);
			//connectionFactory.setUseRetroactiveConsumer(true);
			Connection connection = connectionFactory.createConnection();

			connection.start();

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue("GPIO.Commands");

			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);
			//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			//IPinMessage command = new PinMessageImpl(RaspiPin.GPIO_01, PinState.HIGH);
			IPinMessage command = new PinMessageImpl(new PinMQ(Integer.valueOf(params.pinNumber)), (Integer.valueOf(params.pinState) == 0) ? PinStateMQ.LOW : PinStateMQ.HIGH);

			ObjectMessage message = session.createObjectMessage(command);

			// Tell the producer to send the message
			System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
			producer.send(message);

			// Clean up
			session.close();
			connection.close();

//			return Response.status(Response.Status.OK).build();

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();

//			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
}
