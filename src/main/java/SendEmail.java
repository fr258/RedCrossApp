import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.Properties;

public class SendEmail {


	private String currentCode;


	public SendEmail() {
	
	}
	
		//ex. if netid is fr258, will send to fr258@scarletmail.rutgers.edu
	boolean sendCode(String netid) {

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
			currentCode = Integer.toString(Math.abs(rand.nextInt())).substring(0,6);
			
            message.setSubject("Bloody Good GPA Authentication");
			
            message.setText("Here is your 6 digit code: "+currentCode+"\n\n");

            Transport.send(message);

        } catch (MessagingException e) {
            return false;
        }
		return true;
    }
	
	boolean isCorrectCode(String code) {
		if(code.trim().equals(currentCode)) return true;
		return false;
	}
	
	
	
	
	
	







}