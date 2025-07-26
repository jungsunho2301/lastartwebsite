package walid.jahin.service;

import org.springframework.stereotype.Service;
import walid.jahin.model.News;
import walid.jahin.model.Subscriber;
import walid.jahin.repository.SubscriberRepository;
import walid.jahin.repository.NewsRepository;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

@Service
public class NewsService {

    private final SubscriberRepository subscriberRepository;
    private final JavaMailSender mailSender;
    private final NewsRepository newsRepository;

    public NewsService(SubscriberRepository repo, JavaMailSender sender, NewsRepository newsRepo) {
        this.subscriberRepository = repo;
        this.mailSender = sender;
        this.newsRepository = newsRepo;
    }

    public void sendNewsToSubscribers(String title, String content) {
        // 소식 저장
        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        newsRepository.save(news);

        // 이메일 발송
        List<Subscriber> subscribers = subscriberRepository.findAll();
        for (Subscriber s : subscribers) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(s.getEmail());
            message.setSubject("[Walid Jahin] " + title);
            message.setText(content);
            mailSender.send(message);
        }
    }
}
