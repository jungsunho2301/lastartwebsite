package walid.jahin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import walid.jahin.repository.AdminRepository;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 관리자 로그인 검증
     * @param username 사용자 입력 아이디
     * @param rawPassword 사용자 입력 비밀번호 (평문)
     * @return 로그인 성공 여부
     */
    public boolean login(String username, String rawPassword) {
        return adminRepository.findById(username)
            .map(admin -> {
                boolean result = passwordEncoder.matches(rawPassword, admin.getPassword());

                if (!result) {
                    log.warn("❌ 로그인 실패 - 비밀번호 불일치 (username: {})", username);
                }

                return result;
            })
            .orElseGet(() -> {
                log.warn("❌ 로그인 실패 - 존재하지 않는 계정 (username: {})", username);
                return false;
            });
    }
}
