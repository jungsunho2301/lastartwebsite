package walid.jahin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_log")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_log_seq")
    @SequenceGenerator(name = "login_log_seq", sequenceName = "login_log_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "attempt_time", nullable = false)
    private LocalDateTime attemptTime;

    public LoginLog() {}

    public LoginLog(String username, String ipAddress, boolean success, LocalDateTime attemptTime) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.success = success;
        this.attemptTime = attemptTime;
    }

    // Getter & Setter (필요 시 Lombok 적용도 가능)
}
