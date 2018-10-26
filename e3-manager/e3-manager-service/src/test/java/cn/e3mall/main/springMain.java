package cn.e3mall.main;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class springMain {

	@Test
	public void testMain() throws Exception
	{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		while(true)
		{
			Thread.sleep(1000);
			
		}
	}
}
