package amq.demo;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

public class App {

	static ActiveMQConnectionFactory connectionFactory;
	static PooledConnectionFactory pooledConnectionFactory;
	static Connection connection;
	static Session session;

	final static String mqUser = "xxxxxxxxx";
	final static String mqPassword = "xxxxxxxx";
	final static String mqEndpoint = "ssl://b-f30b40f1-700b-45da-a895-xxxxxxxxxxxxx-1.mq.eu-west-2.amazonaws.com:61617";
	final static String demoQueue = "demo-queue";
	final static String demoTopic = "demo-topic";

	public static void main(String[] args) throws JMSException {
		createSession();
		sendMessageToQueue("Hello ActiveMQ Queue", demoQueue);
		sendMessageToTopic("Hello ActiveMQ Topic", demoTopic);
		endSession();
	}

	private static void createSession() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(mqEndpoint);
		connectionFactory.setUserName(mqUser);
		connectionFactory.setPassword(mqPassword);
		pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(connectionFactory);
		pooledConnectionFactory.setMaxConnections(10);
		connection = pooledConnectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private static void sendMessageToQueue(String message, String queue) throws JMSException {
		// Get a reference to the target queue.
		// Note this does not actually create a queue, the method is somewhat misleading.
		final Destination producerDestination = session.createQueue(queue);
		final MessageProducer producer = session.createProducer(producerDestination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		final TextMessage producerMessage = session.createTextMessage(message);
		producer.send(producerMessage);
		System.out.println("Message sent.");
		producer.close();
	}

	private static void sendMessageToTopic(String message, String topic) throws JMSException {
		// Get a reference to the target queue.
		// Note this does not actually create a queue, the method is somewhat misleading.
		final Destination producerDestination = session.createTopic(topic);
		final MessageProducer producer = session.createProducer(producerDestination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		final TextMessage producerMessage = session.createTextMessage(message);
		producer.send(producerMessage);
		System.out.println("Message sent.");
		producer.close();
	}

	private static void endSession() throws JMSException {
		session.close();
		connection.close();
	}

}
