package cn.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.impl.SearchServiceImpl;

public class ItemImportListener implements MessageListener {
	
	@Autowired
	private SearchServiceImpl searchServiceImpl;

	@Override
	public void onMessage(Message message) {
		TextMessage tesxtMessage = (TextMessage) message;
		try {
			String text = tesxtMessage.getText();
			Long itemId = Long.parseLong(text);
			searchServiceImpl.importSearchItemListById(itemId);
		} catch (JMSException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

}
