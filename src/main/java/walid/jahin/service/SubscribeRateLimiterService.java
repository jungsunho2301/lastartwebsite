package walid.jahin.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscribeRateLimiterService {

    private final Map<String, Instant> ipRequestTimes = new ConcurrentHashMap<>();
    private static final long MIN_INTERVAL_SECONDS = 5;

    public boolean isAllowed(String ip) {
        Instant now = Instant.now();
        Instant lastRequest = ipRequestTimes.get(ip);

        if (lastRequest != null && now.getEpochSecond() - lastRequest.getEpochSecond() < MIN_INTERVAL_SECONDS) {
            return false;
        }

        ipRequestTimes.put(ip, now);
        return true;
    }
}
