package walid.jahin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import walid.jahin.model.News;
import walid.jahin.model.Subscriber;
import walid.jahin.repository.NewsRepository;
import walid.jahin.repository.SubscriberRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {

    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

    private final SubscriberRepository subscriberRepository;
    private final JavaMailSender mailSender;
    private final NewsRepository newsRepository;

    public NewsService(SubscriberRepository subscriberRepository,
                       JavaMailSender mailSender,
                       NewsRepository newsRepository) {
        this.subscriberRepository = subscriberRepository;
        this.mailSender = mailSender;
        this.newsRepository = newsRepository;
    }

    public void sendNewsToSubscribers(String title, String content) {
        // ✅ 뉴스 저장
        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setCreatedAt(LocalDateTime.now()); // 명시적 생성시간
        newsRepository.save(news);

        // ✅ 구독자 목록 가져오기
        List<Subscriber> subscribers = subscriberRepository.findAll();

        // ✅ 비동기 이메일 전송
        for (Subscriber subscriber : subscribers) {
            sendEmailAsync(subscriber.getEmail(), title, content);
        }

        log.info("총 {}명의 구독자에게 비동기 뉴스 발송 요청 완료", subscribers.size());
    }

    // ✅ 개별 이메일 비동기 전송
    @Async
    public void sendEmailAsync(String to, String title, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[Walid Jahin] " + title);
            message.setText(content);
            mailSender.send(message);
            log.info("✅ 이메일 발송 성공");
        } catch (MailException e) {
            log.error("❌ 이메일 발송 실패");
        }
    }
}
