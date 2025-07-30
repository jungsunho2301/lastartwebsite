package walid.jahin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walid.jahin.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
