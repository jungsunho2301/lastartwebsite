package walid.jahin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ipAddress;
    private boolean success;

    private LocalDateTime attemptTime;

    public LoginLog() {}

    public LoginLog(String username, String ipAddress, boolean success, LocalDateTime attemptTime) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.success = success;
        this.attemptTime = attemptTime;
    }

    // (getter/setter 필요시 추가)
}
