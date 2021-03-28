
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import org.bson.BsonArray;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Filters.*;
import java.util.Random;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.ArrayList;

public class MongoCross {
	
	private MongoClientURI uri;
	private MongoClient client;
	private MongoCollection<Document> collection;
	private MongoDatabase database;
	private String topic;
	
	public MongoCross() {
		//mongodb init
		uri = new MongoClientURI("mongodb+srv://admin:admin@cluster0.23udo.mongodb.net/Courses?retryWrites=true&w=majority");
		MongoClient client = new MongoClient(uri);
		database = client.getDatabase("Courses");	
		collection = database.getCollection("RedCrossCollection");
		


	}
	
/*	public String searchCourse(String courseCode) {
		Document result = collection.find(eq("course", courseCode)).first();
		if(result != null)
			return result.getString("professor");
		else
			return null;
	}*/
	
	public void newStudent(String netid){
	   Document student = new Document("_id", new ObjectId());
		
		ArrayList<Document> list = new ArrayList<>();
		//list.add(new Document("course", "MAT251"));
		//list.add(new Document("course", "CS216"));
		
	   student.append("netid", netid)
			  .append("courses", list);


	   collection.insertOne(student);

	}
	
	public Document getStudent(String netid) {
		return collection.find(eq("netid", netid)).first();
	}
	
	public boolean deleteUser(String netid) {
		//collection.updateOne(eq("netid", netid), Updates.set("netid", "newid"));
		
		collection.deleteOne(eq("netid", netid));
		
		return true;
		
	}
	
	public ArrayList<String> getCourseArray(String netid) {
		Document result = collection.find(eq("netid", netid)).first(); //get student
		if(result != null) {
			List<Document> list = result.get("courses", List.class);
			ArrayList<String> courses = new ArrayList<>();
			for(Document a: list)
				courses.add(a.getString("course"));
			return courses;
		
		}	
		return null;
	}
	
	public boolean removeCourse(String netid, String inputCode) {
		Document result = collection.find(eq("netid", netid)).first(); //get student
		String code = inputCode.toUpperCase().trim();
		if(result != null) {
			/*List<Document> list = result.get("courses", List.class);
			Document temp = null;
			for(Document a: list) {
				if (a.getString("course").equals(code)) {
					temp = a;
					break;
				}
			}
			if(temp != null) {
				System.out.println("will attempt to remove "+temp);
				list.remove(temp);
				return true;
			}*/
			Bson toRemove = Updates.pull("courses", new Document("course", code));
			if(toRemove != null) {
				collection.updateOne(eq("netid", netid), toRemove);
				return true;
			}
		}
		return false; //student not found
	}
	
	public boolean addCourse(String netid, String inputCode) {
		Document result = collection.find(eq("netid", netid)).first(); //get student
		String code = inputCode.toUpperCase().trim();
		if(result != null) {
			List<Document> list = result.get("courses", List.class);
			ArrayList<String> courses = new ArrayList<>();
			for(Document a: list) {
				courses.add(a.getString("course"));
			}
			if(!list.contains("code")) { //not adding duplicate-- good
				Bson toAdd = Updates.push("courses", new Document("course", code));
				if(toAdd != null) {
					collection.updateOne(eq("netid", netid), toAdd);
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
}