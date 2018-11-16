package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class GenHtmlController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception
	{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("test.tfl");
		Map map = new HashMap<>();
		map.put("hello", "ni hao");
		FileWriter out = new FileWriter(new File("d:/hello.html"));
		template.process(map, out);
		out.close();
		return "ok";
	}
}
