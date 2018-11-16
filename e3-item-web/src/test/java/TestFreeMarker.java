import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TestFreeMarker {
	@Test
	public void test() throws TemplateException
	{
		Configuration configuration = new Configuration(Configuration.getVersion());
		try {
			configuration.setDirectoryForTemplateLoading(new File("D:/eclipse-workspace/e3-item-web/src/main/webapp/WEB-INF/jsp/tfl"));
			configuration.setDefaultEncoding("utf-8");
			Template template = configuration.getTemplate("test.tfl");
			Map dataModel = new HashMap<>();
			dataModel.put("hello", "ni hao");
			Writer writer = new FileWriter(new File("d:/hello.html"));
			template.process(dataModel, writer);
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStudent() throws TemplateException
	{
		Configuration configuration = new Configuration(Configuration.getVersion());
		try {
			configuration.setDirectoryForTemplateLoading(new File("D:/eclipse-workspace/e3-item-web/src/main/webapp/WEB-INF/jsp/tfl"));
			configuration.setDefaultEncoding("utf-8");
			Template template = configuration.getTemplate("student.tfl");
			Map dataModel = new HashMap<>();
			Student student = new Student(1, "小李", 12);
			dataModel.put("student", student);
			Writer writer = new FileWriter(new File("d:/student.html"));
			template.process(dataModel, writer);
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStudentList() throws TemplateException
	{
		Configuration configuration = new Configuration(Configuration.getVersion());
		try {
			configuration.setDirectoryForTemplateLoading(new File("D:/eclipse-workspace/e3-item-web/src/main/webapp/WEB-INF/jsp/tfl"));
			configuration.setDefaultEncoding("utf-8");
			Template template = configuration.getTemplate("student.tfl");
			Map dataModel = new HashMap<>();
			Student student = new Student(1, "小李", 11);
			dataModel.put("student", student);
			
			List<Student> studentList = new ArrayList<>();
			studentList.add(new Student(1, "小李", 11));
			studentList.add(new Student(2, "小张", 12));
			studentList.add(new Student(3, "小明", 13));
			dataModel.put("studentList", studentList);
			
			dataModel.put("date", new Date());
			
			dataModel.put("val", 111);
			
			dataModel.put("hello", "ni hao");
			Writer writer = new FileWriter(new File("d:/student.html"));
			template.process(dataModel, writer);
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
