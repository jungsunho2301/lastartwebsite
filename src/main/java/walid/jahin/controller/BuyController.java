package walid.jahin.controller;

import walid.jahin.dto.BuyRequest;
import walid.jahin.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buy")
public class BuyController {

    @Autowired
    private MailService mailService;

    @PostMapping
    public ResponseEntity<String> sendInquiry(@RequestBody BuyRequest request) {
        try {
            mailService.sendInquiryMail(request);
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send email: " + e.getMessage());
        }
    }
}
