package walid.jahin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import walid.jahin.dto.BuyRequest;

@Service
public class MailService {

    @Value("${artist.email}")
    private String artistEmail;

    @Autowired
    private JavaMailSender mailSender;

    public void sendInquiryMail(BuyRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(artistEmail);
        message.setSubject("New Purchase Inquiry from " + request.getName());
        message.setText(buildEmailContent(request));
        mailSender.send(message);
    }

    private String buildEmailContent(BuyRequest r) {
        return String.format(
            "Name: %s\nEmail: %s\nAddress: %s\nPhone: %s\nMessage:\n%s",
            r.getName(), r.getEmail(), r.getAddress(), r.getPhone(), r.getMessage()
        );
    }
}
