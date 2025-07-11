package walid.jahin.service;

import org.springframework.stereotype.Service;
import java.util.List;
import walid.jahin.repository.SubscriberRepository;
import walid.jahin.model.Subscriber;
import jakarta.mail.MessagingException;  // import 추가 필요

@Service
public class NotificationService {

    private final SubscriberRepository subscriberRepository;
    private final MailService mailService;

    public NotificationService(SubscriberRepository subscriberRepository, MailService mailService) {
        this.subscriberRepository = subscriberRepository;
        this.mailService = mailService;
    }

    // 새 소식이 있을 때 구독자들에게 메일 보내는 메서드
    public void sendNewNotification(String subject, String content) {
        List<Subscriber> subscribers = subscriberRepository.findAll();

        for (Subscriber subscriber : subscribers) {
            String email = subscriber.getEmail();
            try {
                mailService.sendEmail(email, subject, content);
            } catch (MessagingException e) {
                // 예외 로그 출력, 필요하면 다른 처리 추가 가능
                System.err.println("Failed to send email to " + email);
                e.printStackTrace();
            }
        }
    }
}
