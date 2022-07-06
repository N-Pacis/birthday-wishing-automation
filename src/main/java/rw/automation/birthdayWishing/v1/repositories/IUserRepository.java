package rw.automation.birthdayWishing.v1.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rw.automation.birthdayWishing.v1.enums.EUserStatus;
import rw.automation.birthdayWishing.v1.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findByStatusAndEmailNotLike(EUserStatus status,String email);

    Page<User> findByStatusAndEmailNot(EUserStatus status, String email,Pageable pageable);

    boolean existsByActivationCodeAndEmail(String activationCode, String email);


    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);


    @Query("SELECT u FROM User u WHERE  ( (u.email = :email ) OR (u.phoneNumber = :phoneNumber ))  AND (u.status <> :status) ")
    Optional<User> findByEmailOrPhoneNumberAndStatusNot(String email, String phoneNumber, EUserStatus status);

    @Query(value = "select u from User u where month(u.DOB) = ?1 and day(u.DOB) = ?2 and u.status = ?3")
    List<User> findByDOBAndStatus(int month, int dat,EUserStatus status);

    Page<User> findByDOBAndStatus(LocalDate DOB, EUserStatus status,Pageable pageable);

}
