package walid.jahin.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        String rawPassword = "snsdoswjadksdor0.5";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode(rawPassword);
        System.out.println("Hashed: " + hashed);
    }
}
