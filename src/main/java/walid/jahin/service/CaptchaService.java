package walid.jahin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class CaptchaService {

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CaptchaService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public boolean verify(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", recaptchaSecret);
            params.add("response", token);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(VERIFY_URL, request, String.class);

            Map<String, Object> result = objectMapper.readValue(
                response.getBody(),
                new TypeReference<Map<String, Object>>() {}
            );

            return Boolean.TRUE.equals(result.get("success"));
        } catch (Exception e) {
            log.warn("reCAPTCHA verification failed: {}", e.getMessage(), e);
            return false;
        }
    }
}
