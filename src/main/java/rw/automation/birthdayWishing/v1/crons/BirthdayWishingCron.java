package rw.automation.birthdayWishing.v1.crons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rw.automation.birthdayWishing.v1.enums.EBirthdayConnectionStatus;
import rw.automation.birthdayWishing.v1.enums.EUserStatus;
import rw.automation.birthdayWishing.v1.models.BirthdayConnection;
import rw.automation.birthdayWishing.v1.models.User;
import rw.automation.birthdayWishing.v1.repositories.IBirthdayConnectionRepository;
import rw.automation.birthdayWishing.v1.repositories.IUserRepository;
import rw.automation.birthdayWishing.v1.services.MailService;

import java.time.LocalDate;
import java.util.List;

@Component
public class BirthdayWishingCron {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBirthdayConnectionRepository birthdayConnectionRepository;

    @Autowired
    private MailService mailService;

    @Scheduled(cron = "0 10 11 * * ?")
    public void runEveryTwoMinutes(){
        System.out.println("--- RUNNING CRON JOB ----");
        LocalDate localDate = LocalDate.now();
        List<User> users = userRepository.findByDOBAndStatus(localDate.getMonthValue(),localDate.getDayOfMonth(),EUserStatus.ACTIVE);
        for(User user : users){
            List<BirthdayConnection> birthdayConnections = birthdayConnectionRepository.findBirthdayConnectionByIntendedUserAndStatus(user, EBirthdayConnectionStatus.APPROVED);
            mailService.sendBirthdayWishingToBirthdayHolder(user);
            for(BirthdayConnection birthdayConnection:birthdayConnections){
                User userToSendEmailTo = birthdayConnection.getConnectionRequestor();
                System.out.println("Names of the user to send email to: "+userToSendEmailTo.getFullName());
            }
        }
    }
}
