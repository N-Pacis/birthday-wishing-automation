package rw.automation.birthdayWishing.v1.dtos;

import lombok.Data;
import rw.automation.birthdayWishing.v1.security.ValidPassword;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDTO {
    @NotBlank
    private String currentPassword;

    @NotBlank
    @ValidPassword
    private String newPassword;
}
