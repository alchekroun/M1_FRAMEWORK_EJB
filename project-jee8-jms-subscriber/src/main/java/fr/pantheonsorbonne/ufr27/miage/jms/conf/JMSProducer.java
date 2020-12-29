package fr.pantheonsorbonne.ufr27.miage.jms.conf;

import java.util.Hashtable;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.TopicConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory;

/**
 * THis class produces bean to be injected in JMS Classes
 * 
 * @author nherbaut
 *
 */
public class JMSProducer {

	// fake JNDI context to create object
	private static final Context JNDI_CONTEXT;

	static {
		Hashtable<String, String> jndiBindings = new Hashtable<>();
		jndiBindings.put(Context.INITIAL_CONTEXT_FACTORY, ActiveMQInitialContextFactory.class.getName());
		jndiBindings.put("topicConnectionFactory.TopicConnectionFactory", "tcp://localhost:61616");
		jndiBindings.put("topic.BulletinTopic", "BulletinTopic");
		// jndiBindings.put("queue.PaymentQueue", "PaymentQueue");
		// jndiBindings.put("queue.PaymentAckQueue", "PaymentAckQueue");

		Context c = null;
		try {
			c = new InitialContext(jndiBindings);
		} catch (NamingException e) {
			e.printStackTrace();
			c = null;
			System.exit(-1);

		} finally {
			JNDI_CONTEXT = c;
		}
	}

	/*
	 * Fichier original
	 * 
	 * @Produces
	 * 
	 * @Named("PaymentQueue") public Queue getPaymentQueue() throws NamingException
	 * { return (Queue) JNDI_CONTEXT.lookup("PaymentQueue"); }
	 * 
	 * @Produces
	 * 
	 * @Named("PaymentAckQueue") public Queue getPaymentAckQueue() throws
	 * NamingException { return (Queue) JNDI_CONTEXT.lookup("PaymentAckQueue"); }
	 */

	@Produces
	@Named("bulletin")
	public Topic getJMSQueue() throws NamingException {
		return (Topic) JNDI_CONTEXT.lookup("BulletinTopic");
	}

	@Produces
	public TopicConnectionFactory getJMSTopicConnectionFactory() throws NamingException {
		return (TopicConnectionFactory) JNDI_CONTEXT.lookup("TopicConnectionFactory");
	}

}
