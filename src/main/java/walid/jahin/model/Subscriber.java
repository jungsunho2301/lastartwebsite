package walid.jahin.model;

import jakarta.persistence.*;

@Entity
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)  // ✅ 이름 길이 제한
    private String name;

    @Column(unique = true, length = 100)  // ✅ 이메일 중복 방지 + 길이 제한
    private String email;

    public Subscriber() {}

    public Subscriber(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
