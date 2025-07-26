package walid.jahin.service;

import org.springframework.stereotype.Service;
import walid.jahin.repository.SubscriberRepository;
import walid.jahin.model.Subscriber;

@Service
public class SubscribeService {
    private final SubscriberRepository subscriberRepository;

    public SubscribeService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public void subscribe(String name, String email) {
        if (!subscriberRepository.existsByEmail(email)) {
            subscriberRepository.save(new Subscriber(name, email));
        }
    }
}
