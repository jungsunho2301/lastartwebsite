package walid.jahin.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "1234";

    public boolean login(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
