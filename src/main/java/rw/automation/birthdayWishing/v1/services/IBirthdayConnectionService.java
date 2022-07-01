package rw.automation.birthdayWishing.v1.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.automation.birthdayWishing.v1.enums.EBirthdayConnectionStatus;
import rw.automation.birthdayWishing.v1.models.BirthdayConnection;
import rw.automation.birthdayWishing.v1.models.User;

import java.util.List;
import java.util.UUID;

public interface IBirthdayConnectionService {

    BirthdayConnection findById(UUID id);

    Boolean checkIfUserIsIntendedBirthdayConnectionUser(User user,BirthdayConnection birthdayConnection);

    List<BirthdayConnection> listUserBirthdayConnections(User user);

    Page<BirthdayConnection> getUserBirthdayConnections(User user, Pageable pageable);

    BirthdayConnection saveBirthdayConnection(BirthdayConnection birthdayConnection);

    void approveBirthdayConnection(BirthdayConnection birthdayConnection);

    void rejectBirthdayConnection(BirthdayConnection birthdayConnection);

    void archiveBirthdayConnection(BirthdayConnection birthdayConnection);

    List<BirthdayConnection> listUserBirthdayConnectionsByStatus(User user,EBirthdayConnectionStatus status);

    Page<BirthdayConnection> getUserBirthdayConnectionsByStatus(User user,EBirthdayConnectionStatus status,Pageable pageable);

    List<BirthdayConnection> listMyBirthdayConnectionsRequests(User user);

    Page<BirthdayConnection> getMyBirthdayConnectionsRequests(User user,Pageable pageable);

    List<BirthdayConnection> listMyBirthdayConnectionsRequestsByStatus(User user,EBirthdayConnectionStatus status);

    Page<BirthdayConnection> getMyBirthdayConnectionsRequestsByStatus(User user,EBirthdayConnectionStatus status,Pageable pageable);
}
