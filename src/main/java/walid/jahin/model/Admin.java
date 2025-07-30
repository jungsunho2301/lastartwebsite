package walid.jahin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Admin {

    @Id
    private String username;

    @Column(length = 100)  // 최대 100자까지 저장 가능하도록 설정
    private String password; // ✅ BCrypt 해시로 저장됨

    public Admin() {}

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
