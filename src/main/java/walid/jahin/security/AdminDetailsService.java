
package walid.jahin.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import walid.jahin.model.Admin;
import walid.jahin.repository.AdminRepository;

@Service
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("No administrator account found: " + username));

        return new AdminUserDetails(admin);
    }
}