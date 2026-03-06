package com.spring.auth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.spring.auth.configurations.SecurityConfiguration;
import com.spring.auth.entities.Signupresponse;
import com.spring.auth.entities.User;
import com.spring.auth.repositories.SpringAuthRepository;
import com.spring.auth.repositories.UserRepository;
import com.spring.auth.utils.CustomException;

import jakarta.mail.internet.MimeMessage;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Value("${sendgrid.api.key}")
    private String sendGridApiKey;

	@Autowired
	SpringAuthRepository repository;

	@Autowired
	JpaUserDetailService detailService;

	@Autowired
	SecurityConfiguration securityconfig;

	@Autowired
	UserRepository userRepository;
	
//	@Autowired
//	JavaMailSender mailSender;

	@Override
	public ResponseEntity<Signupresponse> signup(Signupresponse signupresponse) throws CustomException {
		try {
			Boolean savesignuprespexists = repository.getByUsername(signupresponse.getUsername());
			if (savesignuprespexists) {
				CustomException ce = new CustomException();
				ce.setMessage("Error-User Already Exists");
				ce.setCode(10);
				ce.printStackTrace();
				throw ce;
			}
			Signupresponse newsignupresp = new Signupresponse();
			if (detailService.isEmail(signupresponse.getUsername())) {
				newsignupresp.setUsername(signupresponse.getUsername());
			} else {
				CustomException ce = new CustomException();
				ce.setMessage("Error-Wrong Email Format");
				ce.setCode(10);
				ce.printStackTrace();
				throw ce;
			}
			if (signupresponse.getPassword().equals(signupresponse.getConfirmpassword())
					&& (signupresponse.getPassword().length() >= 6)) {
				newsignupresp.setPassword(securityconfig.passwordEncoder().encode(signupresponse.getPassword()));
			} else {
				CustomException ce = new CustomException();
				ce.setMessage("Error-Invalid Password");
				ce.setCode(10);
				ce.printStackTrace();
				throw ce;
			}
			User user = new User();
			user.setUsername(newsignupresp.getUsername());
			user.setPassword(newsignupresp.getPassword());
			
			if(sendVerificationEmail(signupresponse.getUsername())) {
			repository.save(newsignupresp);
			userRepository.save(user);
			return ResponseEntity.ok(newsignupresp);
			}else {
				return (ResponseEntity<Signupresponse>) ResponseEntity.internalServerError();
			}

		} catch (Exception e) {
			CustomException ce = new CustomException();
			if (e.getMessage().contains("Error")) {
				ce.setMessage(e.getMessage());
			} else {
				ce.setMessage(e.getMessage());
			}
			throw ce;
		}
	}
	
	public Boolean sendVerificationEmail(String to) throws IOException {
		try {
        Email from = new Email("jenowac691@alibto.com");
        String subject = "Verify Your Account";
        Email recipient = new Email(to);

        String verificationLink = "http://localhost:1010/auth/verify" ;

        Content content = new Content("text/html",
                "<h2>Welcome!</h2>" +
                "<p>Please click below:</p>" +
                "<a href='" + verificationLink + "'>Verify Account</a>");

        Mail mail = new Mail(from, subject, recipient, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        System.out.println(response.getStatusCode());
        return true;
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
			
		}
    }
}


