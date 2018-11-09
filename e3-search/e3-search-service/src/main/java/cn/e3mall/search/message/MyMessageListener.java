package cn.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage testMessage = (TextMessage) message;
		try {
			String text = testMessage.getText();
			System.out.println(text);
		} catch (JMSException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
