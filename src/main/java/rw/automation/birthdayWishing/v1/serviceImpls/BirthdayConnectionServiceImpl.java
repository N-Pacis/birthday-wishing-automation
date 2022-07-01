package rw.automation.birthdayWishing.v1.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rw.automation.birthdayWishing.v1.enums.EBirthdayConnectionStatus;
import rw.automation.birthdayWishing.v1.exceptions.BadRequestException;
import rw.automation.birthdayWishing.v1.exceptions.ResourceNotFoundException;
import rw.automation.birthdayWishing.v1.models.BirthdayConnection;
import rw.automation.birthdayWishing.v1.models.User;
import rw.automation.birthdayWishing.v1.repositories.IBirthdayConnectionRepository;
import rw.automation.birthdayWishing.v1.services.IBirthdayConnectionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BirthdayConnectionServiceImpl implements IBirthdayConnectionService {
    @Autowired
    private IBirthdayConnectionRepository birthdayConnectionRepository;

    @Override
    public BirthdayConnection findById(UUID id) {
        return this.birthdayConnectionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Birthday connection", "id", id.toString()));
    }

    @Override
    public Boolean checkIfUserIsIntendedBirthdayConnectionUser(User user, BirthdayConnection birthdayConnection){
        if(user.getEmail().equals(birthdayConnection.getIntendedUser().getEmail())){
            return true;
        }
        return false;
    }

    @Override
    public List<BirthdayConnection> listUserBirthdayConnections(User user) {
        return birthdayConnectionRepository.findBirthdayConnectionByIntendedUser(user);
    }

    @Override
    public Page<BirthdayConnection> getUserBirthdayConnections(User user, Pageable pageable) {
        return birthdayConnectionRepository.findBirthdayConnectionByIntendedUser(user,pageable);
    }

    @Override
    public BirthdayConnection saveBirthdayConnection(BirthdayConnection birthdayConnection) {
        Optional<BirthdayConnection> checkBirthdayConnection = birthdayConnectionRepository.findBirthdayConnectionByConnectionRequestorAndIntendedUser(birthdayConnection.getConnectionRequestor(),birthdayConnection.getIntendedUser());
        if(checkBirthdayConnection.isPresent()){
            throw new BadRequestException(String.format("You have already requested to connect with '%s'",birthdayConnection.getIntendedUser().getFullName()));
        }
        else{
            return birthdayConnectionRepository.save(birthdayConnection);
        }
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
