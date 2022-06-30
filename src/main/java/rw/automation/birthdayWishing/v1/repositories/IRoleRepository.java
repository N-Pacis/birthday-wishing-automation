package rw.automation.birthdayWishing.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.automation.birthdayWishing.v1.enums.ERole;
import rw.automation.birthdayWishing.v1.models.Role;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole role);
}
