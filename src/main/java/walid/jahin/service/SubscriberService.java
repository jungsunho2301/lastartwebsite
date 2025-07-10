package walid.jahin.service;

import walid.jahin.dto.SubscribeRequest;
import walid.jahin.model.Subscriber;
import walid.jahin.repository.SubscriberRepository;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final JavaMailSender mailSender;

    public SubscriberService(SubscriberRepository repo, JavaMailSender mailSender) {
        this.subscriberRepository = repo;
        this.mailSender = mailSender;
    }

    public void subscribe(SubscribeRequest request) {
        Subscriber s = new Subscriber();
        s.setName(request.getName());
        s.setEmail(request.getEmail());
        subscriberRepository.save(s);
    }

    public void notifyAllSubscribers(String subject, String content) {
        List<Subscriber> list = subscriberRepository.findAll();

        for (Subscriber sub : list) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(sub.getEmail());
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
        }
    }
}
