package walid.jahin.service;

import org.springframework.stereotype.Service;
import walid.jahin.model.Subscriber;
import walid.jahin.repository.SubscriberRepository;

@Service
public class SubscribeService {

    private final SubscriberRepository subscriberRepository;

    public SubscribeService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    // 이메일 중복 확인 및 저장
    public boolean subscribe(String name, String email) {
        if (subscriberRepository.existsByEmail(email)) {
            return false;
        }
        subscriberRepository.save(new Subscriber(name, email));
        return true;
    }
}
