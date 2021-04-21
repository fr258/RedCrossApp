package com.bloodygoodgpa.samples;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import org.bson.BsonArray;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Filters.*;
import java.util.Random;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MongoCross {
	private static String currentCode = null;
	private static MongoClientURI uri = new MongoClientURI("mongodb+srv://admin:admin@cluster0.23udo.mongodb.net/Courses?retryWrites=true&w=majority");
	private static MongoClient client = new MongoClient(uri);
	private static MongoDatabase database = client.getDatabase("Courses");	
	private static MongoCollection<Document> userCollection = database.getCollection("StudentCollection"); 
	private static MongoCollection<Document> courseCollection = database.getCollection("CourseCollection"); 
	private static MongoCollection<Document> redeemedCollection = database.getCollection("RedeemedCollection"); 
	private static MongoCollection<Document> driveCollection = database.getCollection("DriveCollection"); 


	
	public MongoCross() {}
	
	public void newEvent(){
		Document event = new Document("_id", new ObjectId());
		
		
		driveCollection.insertOne(event);
	}
	
	public void newUser(String netid, String password){
	   Document student = new Document("_id", new ObjectId());
		
	   student.append("netid", netid)
			  .append("courses", new ArrayList<Document>())
			  .append("numTokens", 0)
			  .append("numTimesDonated", 0)
			  .append("numTokens", 0)
			  .append("pass", password);

	   userCollection.insertOne(student);
	}
	
	
	
	Document getStudent(String netid) {
		return userCollection.find(eq("netid", netid)).first();
	}
	
	public Boolean login(String netid, String password) {
		Document result = userCollection.find(eq("netid", netid)).first();
		if(result != null) {
			if(result.getString("pass").equals(password)) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean deleteUser(String netid) {
		//collection.updateOne(eq("netid", netid), Updates.set("netid", "newid"));
		
		userCollection.deleteOne(eq("netid", netid));
		
		return true;
		
	}
	
	public ArrayList<String> getCourseArray(String netid) {
		Document result = userCollection.find(eq("netid", netid)).first(); //get student
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
		Document result = userCollection.find(eq("netid", netid)).first(); //get student
		String code = inputCode.toUpperCase().trim();
		if(result != null) {

			Bson toRemove = Updates.pull("courses", new Document("course", code));
			if(toRemove != null) {
				userCollection.updateOne(eq("netid", netid), toRemove);
				return true;
			}
		}
		return false; //student not found
	}
	
	//for student view
	public boolean addCourse(String inputCode, String inputProfessor, String netid) {
		Document result = userCollection.find(eq("netid", netid)).first(); //get student
		String code = inputCode.toUpperCase().trim();
		String professor = inputProfessor.trim();
		if(result != null) {
			List<Document> list = result.get("courses", List.class);
			//duplicate check
			for(Document doc: list) {
				if(doc.getString("course").equals(code) && doc.getString("professor").equals(professor)) return false;
			}
			
			Bson toAdd = Updates.push("courses", new Document("course", code).append("professor", professor));
			if(toAdd != null) {
				userCollection.updateOne(eq("netid", netid), toAdd);
				return true;
			}


		}
		return false;
	}
	
	public boolean adminAddCourse(String inputCode, String inputProfessor) {
		String code = inputCode.trim().toUpperCase();
		String professor = inputProfessor.trim();
		Document result = courseCollection.find(eq("code", inputCode)).first(); //see if course already exists
		if(result != null) {
			ArrayList<Document> profList = (ArrayList<Document>)result.get("professors", List.class);
			for(Document doc: profList) {
				if(doc.getString("professor").equals(professor)) return false; //already exists in collection
			}
			
			Bson toAdd = Updates.push("professors", new Document("professor", professor));
			if(toAdd != null) {
				courseCollection.updateOne(eq("code", code), toAdd);
			}
		}
		else {
		   Document course = new Document("_id", new ObjectId());
			ArrayList<Document> professors = new ArrayList<>();
			professors.add(new Document("professor", professor));
		   course.append("code", code)
				  .append("professors", professors);
				  
		   courseCollection.insertOne(course);

		}
		
		Document redeemed = new Document("_id", new ObjectId());
		redeemed.append("redeemed", new ArrayList<String>())
					.append("professor|code", professor+"|"+code);
					
		redeemedCollection.insertOne(redeemed);
		
		return true;
	}
	
	public ArrayList<String> getRedeemableCourses(String netid) {
		Document result = userCollection.find(eq("netid", netid)).first();
		List<Document> courseList = new ArrayList<>();
		ArrayList<String> eligibleCourseList = new ArrayList<>();
		String courseCode, professor;
		if(result != null) {
			courseList = result.get("courses", List.class);
			//for every course the student has added
			for(Document a: courseList) {
				courseCode = a.getString("course"); //get course code
				professor = a.getString("professor"); //get course professor
				Document masterCourse = courseCollection.find(eq("code", courseCode)).first(); //get course from course collection
				if(masterCourse != null) { //make sure it exists
					List<Document> profList = masterCourse.get("professors", List.class); //get professors array
					for(Document b: profList) { //iterate through array to look for current professor
						if(b.getString("professor").equals(professor)) { //if professor found, add course code to list to return
							eligibleCourseList.add(courseCode);
							break;
						}
					}
				}
			}
		}
		return eligibleCourseList;
	}
	
	public int getNumTokens(String netid) {
		Document result = userCollection.find(eq("netid", netid)).first();
		if(result != null) {
			int numTokens = result.getInteger("numTokens");
			return numTokens;
		}
		
		return 0;
	}
	
	private void addToken(String netid) {
		Document result = userCollection.find(eq("netid", netid)).first();
		if(result != null) {
			int numTokens = result.getInteger("numTokens");
			numTokens++;
			userCollection.updateOne(eq("netid", netid), Updates.set("numTokens", numTokens));
		}
	}
	
	private void removeToken(String netid) {
		Document result = userCollection.find(eq("netid", netid)).first();
		if(result != null) {
			int tokenNum = result.getInteger("numTokens");
			tokenNum--;
			
			userCollection.updateOne(eq("netid", netid), Updates.set("numTokens", tokenNum));
		}
	}
	
	public void donateBlood(String netid) {
		addToken(netid);
		
		Document result = userCollection.find(eq("netid", netid)).first();
		if(result != null) {
			int count = result.getInteger("numTimesDonated");
			count++;
			userCollection.updateOne(eq("netid", netid), Updates.set("numTimesDonated", count));
		}
		
		
	}
	
	public int getLifetimeDonationNum(String netid) {
		Document result = userCollection.find(eq("netid", netid)).first();
		if(result != null) {
			int numTokens = result.getInteger("numTimesDonated");
			return numTokens;
		}
		
		return -1;
	}
	

	
	public boolean redeem(String netid, String courseCode) {
		Document result = userCollection.find(eq("netid", netid)).first();
		if(result != null) {
			
			int numTokens = result.getInteger("numTokens");
			
			String professor = null;
			List<Document> courseList = result.get("courses", List.class);
			for(Document course: courseList) {
				if(course.getString("course").equals(courseCode)) {
					professor = course.getString("professor");
				}
			}
			
			if(numTokens > 0) { //can redeem
				Document masterCourse = courseCollection.find(eq("code", courseCode)).first(); //get course from course collection
				if(masterCourse != null) { //make sure it exists
					List<Document> profList = masterCourse.get("professors", List.class); //get professors array
					List<String> redeemedList = null;
					for(Document a: profList) { //iterate through array to look for current professor
						if(a.getString("professor").equals(professor)) { //professor found
							Bson toAdd = Updates.push("redeemed", new Document("student", netid));
							if(toAdd != null) {
								redeemedCollection.updateOne(eq("professor|code", professor+"|"+courseCode), toAdd);
							}
							removeToken(netid);
							
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isAdmin(String netid) {
		try{
			BasicDBObject query = new BasicDBObject();
			query.put("netid", netid);
			query.put("admin", "true");
			Document result = userCollection.find(query).first();
			if(result != null) return true;
			return false;
		}
		catch(NullPointerException e) {
			return false;
		}
	}
	
			//ex. if netid is fr258, will send to fr258@scarletmail.rutgers.edu
	public boolean sendCode(String netid) {

        final String username = "bloodygoodgpa@gmail.com";
        final String password = "Welcome2014!";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(netid+"@scarletmail.rutgers.edu")
            );
			Random rand = new Random();
			currentCode = String.format("%06d", Math.abs(rand.nextInt())).substring(0,6);
			
            message.setSubject("Bloody Good GPA Authentication");
			
            message.setText("Here is your 6 digit code: "+currentCode+"\n\n");

            Transport.send(message);

        } catch (MessagingException e) {
            return false;
        }
		return true;
    }
	
	public boolean isCorrectCode(String code) {
		if(code.trim().equals(currentCode)) return true;
		return false;
	}


	
	

	
	
}


