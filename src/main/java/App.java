import java.util.ArrayList;
import  java.util.Properties;
import java.util.concurrent.TimeUnit;

public class App {
	
	public static void main(String[] args) throws InterruptedException{
		MongoCross db = new MongoCross();	
		
		//db.newStudent("fr258");
	
		db.addCourse("fr258", "MAT251");
	
		TimeUnit.SECONDS.sleep(1);
			

		
		//db.deleteUser("newid");
		
		ArrayList<String> courses = db.getCourseArray("gc123");
		if(courses != null)
		for(String a: courses)
			System.out.println(a); 
		//db.deleteUser("fr258");
		//db.removeCourse("gc123","MAT251");

	}



}

