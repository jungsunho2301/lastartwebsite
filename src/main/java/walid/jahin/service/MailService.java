package walid.jahin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import walid.jahin.dto.BuyRequest;
import walid.jahin.dto.InquiryRequest;
import walid.jahin.model.ArtistInfo;
import walid.jahin.repository.ArtistInfoRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ArtistInfoRepository artistInfoRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendBuyInquiryMail(BuyRequest request) {
        String artistEmail = getArtistEmail();
        if (artistEmail == null) return; // 이메일 없으면 전송 X

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(artistEmail);
        message.setSubject("New Purchase Inquiry from " + request.getName());
        message.setText(buildBuyContent(request));
        mailSender.send(message);
    }

    public void sendInquiryMail(InquiryRequest request) {
        String artistEmail = getArtistEmail();
        if (artistEmail == null) return; // 이메일 없으면 전송 X

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(artistEmail);
        message.setSubject("New Inquiry from " + request.getName());
        message.setText(buildInquiryContent(request));
        mailSender.send(message);
    }

    // 📌 이메일 DB에서 가져오는 메서드
    private String getArtistEmail() {
        Optional<ArtistInfo> artist = artistInfoRepository.findAll().stream().findFirst();
        return artist.map(ArtistInfo::getEmail).orElse(null);
    }

    private String buildBuyContent(BuyRequest r) {
        return String.format(
            "Name: %s\nEmail: %s\nAddress: %s\nPhone: %s\nMessage:\n%s",
            r.getName(), r.getEmail(), r.getAddress(), r.getPhone(), r.getMessage()
        );
    }

    private String buildInquiryContent(InquiryRequest r) {
        return String.format(
            "Name: %s\nEmail: %s\nArt number: %s\nPhone: %s\nMessage:\n%s",
            r.getName(), r.getEmail(), r.getArtnum(), r.getPhone(), r.getMessage()
        );
    }
}
