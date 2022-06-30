package rw.automation.birthdayWishing.v1.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.automation.birthdayWishing.v1.enums.EBirthdayConnectionStatus;
import rw.automation.birthdayWishing.v1.models.BirthdayConnection;
import rw.automation.birthdayWishing.v1.models.User;
import rw.automation.birthdayWishing.v1.repositories.IBirthdayConnectionRepository;
import rw.automation.birthdayWishing.v1.services.IBirthdayConnectionService;

import java.util.List;

public class BirthdayConnectionServiceImpl implements IBirthdayConnectionService {
    @Autowired
    private IBirthdayConnectionRepository birthdayConnectionRepository;

    @Override
    public List<BirthdayConnection> listUserBirthdayConnections(User user) {
        return birthdayConnectionRepository.findBirthdayConnectionByIntendedUser(user);
    }

    @Override
    public Page<BirthdayConnection> getUserBirthdayConnections(User user, Pageable pageable) {
        return birthdayConnectionRepository.findBirthdayConnectionByIntendedUser(user,pageable);
    }

    @Override
    public void saveBirthdayConnection(BirthdayConnection birthdayConnection) {
        birthdayConnectionRepository.save(birthdayConnection);
    }

    @Override
    public void approveBirthdayConnection(BirthdayConnection birthdayConnection) {
        birthdayConnection.setStatus(EBirthdayConnectionStatus.APPROVED);
        birthdayConnectionRepository.save(birthdayConnection);
    }

    @Override
    public void rejectBirthdayConnection(BirthdayConnection birthdayConnection) {
        birthdayConnection.setStatus(EBirthdayConnectionStatus.REJECTED);
        birthdayConnectionRepository.save(birthdayConnection);
    }

    @Override
    public void archiveBirthdayConnection(BirthdayConnection birthdayConnection) {
        birthdayConnection.setStatus(EBirthdayConnectionStatus.ARCHIVED);
        birthdayConnectionRepository.save(birthdayConnection);
    }

    @Override
    public List<BirthdayConnection> listUserBirthdayConnectionsByStatus(User user, EBirthdayConnectionStatus status) {
        return birthdayConnectionRepository.findBirthdayConnectionByIntendedUserAndStatus(user,status);
    }

    @Override
    public Page<BirthdayConnection> getUserBirthdayConnectionsByStatus(User user, EBirthdayConnectionStatus status,Pageable pageable) {
        return birthdayConnectionRepository.findBirthdayConnectionByIntendedUserAndStatus(user,status,pageable);
    }

    @Override
    public List<BirthdayConnection> listMyBirthdayConnectionsRequests(User user) {
        return birthdayConnectionRepository.findBirthdayConnectionByConnectionRequestor(user);
    }

    @Override
    public Page<BirthdayConnection> getMyBirthdayConnectionsRequests(User user,Pageable pageable) {
        return birthdayConnectionRepository.findBirthdayConnectionByConnectionRequestor(user,pageable);
    }

    @Override
    public List<BirthdayConnection> listMyBirthdayConnectionsRequestsByStatus(User user, EBirthdayConnectionStatus status) {
        return birthdayConnectionRepository.findBirthdayConnectionByConnectionRequestorAndStatus(user,status);
    }

    @Override
    public Page<BirthdayConnection> getMyBirthdayConnectionsRequestsByStatus(User user, EBirthdayConnectionStatus status,Pageable pageable) {
        return birthdayConnectionRepository.findBirthdayConnectionByConnectionRequestorAndStatus(user,status,pageable);
    }
}
