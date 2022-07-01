package rw.automation.birthdayWishing.v1.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rw.automation.birthdayWishing.v1.enums.EBirthdayConnectionStatus;
import rw.automation.birthdayWishing.v1.models.BirthdayConnection;
import rw.automation.birthdayWishing.v1.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBirthdayConnectionRepository extends JpaRepository<BirthdayConnection, UUID> {
    List<BirthdayConnection> findBirthdayConnectionByIntendedUser(User user);

    Page<BirthdayConnection> findBirthdayConnectionByIntendedUser(User user, Pageable pageable);

    List<BirthdayConnection> findBirthdayConnectionByIntendedUserAndStatus(User user, EBirthdayConnectionStatus status);

    Page<BirthdayConnection> findBirthdayConnectionByIntendedUserAndStatus(User user, EBirthdayConnectionStatus status,Pageable pageable);

    List<BirthdayConnection> findBirthdayConnectionByConnectionRequestor(User user);

    Page<BirthdayConnection> findBirthdayConnectionByConnectionRequestor(User user,Pageable pageable);

    List<BirthdayConnection> findBirthdayConnectionByConnectionRequestorAndStatus(User user,EBirthdayConnectionStatus status);

    Page<BirthdayConnection> findBirthdayConnectionByConnectionRequestorAndStatus(User user,EBirthdayConnectionStatus status,Pageable pageable);

    Optional<BirthdayConnection> findBirthdayConnectionByConnectionRequestorAndIntendedUser(User connectionRequestor, User intendedUser);
}
