package expressMgmt.express.mq;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.IndentPrinter;

/**
 * A simple tool for publishing messages
 * 
 * 
 */
public class ProducerTool extends Thread implements MessageListener{

	private Destination destination;
	private int messageCount = 10;
	private long sleepTime;
	private int messageSize = 255;
	private static int parallelThreads = 1;
	private long timeToLive;
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String subject = "controlQ";
	private boolean topic = false;
	private boolean transacted;
	private boolean persistent;
	private static Object lockResults = new Object();

	public static void main(String[] args) {
		ArrayList<ProducerTool> threads = new ArrayList();
		ProducerTool producerTool = new ProducerTool();

		producerTool.showParameters();
		for (int threadCount = 1; threadCount <= parallelThreads; threadCount++) {
			producerTool = new ProducerTool();
			producerTool.start();
			threads.add(producerTool);
		}

		while (true) {
			Iterator<ProducerTool> itr = threads.iterator();
			int running = 0;
			while (itr.hasNext()) {
				ProducerTool thread = itr.next();
				if (thread.isAlive()) {
					running++;
				}
			}
			if (running <= 0) {
				System.out
						.println("All threads completed their work---- running "
								+ running);
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	public void showParameters() {
		System.out.println("Connecting to URL: " + url + " (" + user + ":"
				+ password + ")");
		System.out.println("Publishing a Message with size " + messageSize
				+ " to " + (topic ? "topic" : "queue") + ": " + subject);
		System.out.println("Using "
				+ (persistent ? "persistent" : "non-persistent") + " messages");
		System.out.println("Sleeping between publish " + sleepTime + " ms");
		System.out.println("Running " + parallelThreads + " parallel threads");

		if (timeToLive != 0) {
			System.out.println("Messages time to live " + timeToLive + " ms");
		}
	}

	public void run() {
		Connection connection = null;
		try {
			// Create the connection.
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			Session session = connection.createSession(transacted,
					Session.AUTO_ACKNOWLEDGE);
			if (topic) {
				destination = session.createTopic(subject);
			} else {
				destination = session.createQueue(subject);
			}

			// Create the producer.
			MessageProducer producer = session.createProducer(destination);
			if (persistent) {
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			} else {
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}
			if (timeToLive != 0) {
				producer.setTimeToLive(timeToLive);
			}

			// Start sending messages
			TextMessage message = session
					.createTextMessage("add-user");

			producer.send(message);
			
			
			
			MessageConsumer consumer = null;
			 
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);

			System.out.println("[" + this.getName() + "] Done.");

			synchronized (lockResults) {
				ActiveMQConnection c = (ActiveMQConnection) connection;
				System.out.println("[" + this.getName() + "] Results:\n");
				c.getConnectionStats().dump(new IndentPrinter());
			}

		} catch (Exception e) {
			System.out.println("[" + this.getName() + "] Caught: " + e);
			e.printStackTrace();
		} finally {
			try {
//				connection.close();
			} catch (Throwable ignore) {
			}
		}
	}

	public void onMessage(Message message) {
		
		System.out.println("&&&&&&&&");
		try {
			
			System.out.println(message.getJMSMessageID()+" "+message.getJMSReplyTo());
			
//			connection.close();
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	public void setPersistent(boolean durable) {
		this.persistent = durable;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public void setParallelThreads(int parallelThreads) {
		if (parallelThreads < 1) {
			parallelThreads = 1;
		}
		this.parallelThreads = parallelThreads;
	}

	public void setTopic(boolean topic) {
		this.topic = topic;
	}

	public void setQueue(boolean queue) {
		this.topic = !queue;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
