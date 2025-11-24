package walid.jahin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriber")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriber_seq")
    @SequenceGenerator(name = "subscriber_seq", sequenceName = "subscriber_seq", allocationSize = 1)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(unique = true, length = 100, nullable = false)
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
