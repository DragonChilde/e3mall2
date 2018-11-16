
public class Student {
	private Integer id;
	private String name;
	private Integer age;
	
	Student(int id,String name,int age)
	{
		this.setId(id);
		this.setName(name);
		this.setAge(age);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
}
