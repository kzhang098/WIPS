package helper;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class AutoEmail {
	
	static Properties properties;
	static Session getMailSession;
	static MimeMessage wipsMessage;
 
	
	public static void generateAndSendEmail(String username, String password, String email) throws AddressException, MessagingException {
		 
		// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		properties = System.getProperties();
		properties.put("mail.smtp.port", "587");
		properties.put("mail.imap.auth.mechanisms", "XOAUTH2");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.trust","smtp.gmail.com");
		System.out.println("Mail Server Properties have been setup successfully..");
 
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(properties, null);
		wipsMessage = new MimeMessage(getMailSession);
		wipsMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		wipsMessage.setSubject("Your WIPS Account Information!");
		String emailBody = "Your WIPS Username and Password are:" + "<br><br>Username : " + username + "<br> Password : " + password + "<br>The WIPS Team. ";

		wipsMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");
 
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");
 

		transport.connect("smtp.gmail.com", "wipssystem", "WipsEmail");
		transport.sendMessage(wipsMessage, wipsMessage.getAllRecipients());
		transport.close();
	}
}
