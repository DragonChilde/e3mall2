package cn.e3mall.activemq;

import static org.junit.Assert.*;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class testSpringActiveMq {

	@Test
	public void testSpringActiveMqProducer() throws Exception
	{
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		JmsTemplate jmsTemplate = classPathXmlApplicationContext.getBean(JmsTemplate.class);
		Destination destination = (Destination) classPathXmlApplicationContext.getBean("queueDestination");
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("this is spring mq");
				
				return textMessage;
			}
		});
	}

}
