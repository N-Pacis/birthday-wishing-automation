package rw.automation.birthdayWishing.v1.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.automation.birthdayWishing.v1.audits.InitiatorAudit;
import rw.automation.birthdayWishing.v1.enums.EBirthdayConnectionStatus;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "birthday_connections")
public class BirthdayConnection extends InitiatorAudit {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "connection_requestor_id")
    private User connectionRequestor;

    @ManyToOne
    @JoinColumn(name ="intended_user_id")
    private User intendedUser;

    @Column(nullable = true)
    private String message;

    private EBirthdayConnectionStatus status;

    public BirthdayConnection(User connectionRequestor, User intendedUser,String message,EBirthdayConnectionStatus status){
        this.connectionRequestor = connectionRequestor;
        this.intendedUser = intendedUser;
        this.message = message;
        this.status = status;
    }
}
