package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.leveldb.replicated.SlaveLevelDBStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


class GenHtmlListener implements MessageListener{

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			Long itemId = Long.parseLong(text);
			
			TbItem tbItem = itemService.getItemById(itemId);
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			Thread.sleep(1000);
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.tfl");
			Map map = new HashMap<>();
			map.put("item", tbItem);
			map.put("itemDesc", tbItemDesc);
			FileWriter out = new FileWriter(new File(HTML_GEN_PATH+itemId+".html"));
			template.process(map, out);
			out.close();
		} catch (JMSException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
