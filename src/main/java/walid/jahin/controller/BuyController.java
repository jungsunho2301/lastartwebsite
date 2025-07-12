package walid.jahin.controller;

import walid.jahin.dto.BuyRequest;
import walid.jahin.dto.InquiryRequest;
import walid.jahin.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BuyController {

    @Autowired
    private MailService mailService;

    @PostMapping("/buy")
    public ResponseEntity<String> sendBuyInquiry(@RequestBody BuyRequest request) {
        try {
            mailService.sendBuyInquiryMail(request);
            return ResponseEntity.ok("Buy Inquiry Email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/inquiry")
    public ResponseEntity<String> sendInquiry(@RequestBody InquiryRequest request) {
        try {
            mailService.sendInquiryMail(request);
            return ResponseEntity.ok("Inquiry email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send inquiry: " + e.getMessage());
        }
    }
}
