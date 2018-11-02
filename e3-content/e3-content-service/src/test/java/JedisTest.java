import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.jedis.JedisClientPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void testJedis() throws Exception
	{
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		String str = jedis.get("str");
		System.out.println(str);
	}
	
	@Test
	public void testJedisPool() throws Exception
	{
		JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
		Jedis jedis = jedisPool.getResource();
		jedis.set("test", "1111");
		String test = jedis.get("test");
		System.out.println(test);
		
		jedis.hset("key", "field1", "111");
		jedis.hset("key", "field2", "222");
		List<String> list = jedis.hvals("key");
		System.out.println(list);

	}
	
	@Test
	public void testJedisClient() throws Exception
	{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedis = ac.getBean(JedisClient.class);
		List<String> lists = jedis.hvals("key");
		System.out.println(lists);
		
		jedis.set("test2", "2222");
		String test2 = jedis.get("test2");
		System.out.println("test2");
	}
}

