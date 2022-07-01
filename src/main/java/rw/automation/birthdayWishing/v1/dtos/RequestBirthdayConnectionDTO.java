package rw.automation.birthdayWishing.v1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBirthdayConnectionDTO {

    @NotNull
    private UUID userId;

    private String message;

}
