package rw.automation.birthdayWishing.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rw.automation.birthdayWishing.v1.enums.ERole;
import rw.automation.birthdayWishing.v1.enums.EUserStatus;
import rw.automation.birthdayWishing.v1.fileHandling.File;
import rw.automation.birthdayWishing.v1.fileHandling.FileStorageService;
import rw.automation.birthdayWishing.v1.models.Role;
import rw.automation.birthdayWishing.v1.models.User;
import rw.automation.birthdayWishing.v1.payload.ApiResponse;
import rw.automation.birthdayWishing.v1.services.IFileService;
import rw.automation.birthdayWishing.v1.services.IRoleService;
import rw.automation.birthdayWishing.v1.services.IUserService;
import rw.automation.birthdayWishing.v1.utils.Formatter;
import rw.automation.birthdayWishing.v1.dtos.ChangePasswordDTO;
import rw.automation.birthdayWishing.v1.dtos.SignUpDTO;

import javax.validation.Valid;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final IUserService userService;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileStorageService fileStorageService;
    private final IFileService fileService;

    @Value("${uploads.directory.user_profiles}")
    private String directory;

    @Autowired
    public UserController(IUserService userService, IRoleService iRoleService, BCryptPasswordEncoder bCryptPasswordEncoder, FileStorageService fileStorageService, IFileService fileService) {
        this.userService = userService;
        this.roleService = iRoleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileService = fileService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(path = "/current-user")
    public ResponseEntity<ApiResponse> currentlyLoggedInUser() {
        return ResponseEntity.ok(new ApiResponse(true, userService.getLoggedInUser()));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody SignUpDTO dto) {

        User user = new User();

        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        Role role = roleService.findByName(ERole.STANDARD);
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setPhoneNumber(dto.getMobile());
        user.setPassword(encodedPassword);
        user.setStatus(EUserStatus.WAIT_EMAIL_VERIFICATION);
        user.setRoles(Collections.singleton(role));
        user.setDOB(dto.getDOB());

        User entity = this.userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, entity));
    }

    @PutMapping(path = "/{id}/upload-profile")
    public ResponseEntity<ApiResponse> uploadProfileImage(
            @PathVariable(value = "id") UUID id,
            @RequestParam("file") MultipartFile document
    ) {
        this.userService.findById(id);
        File file = this.fileService.create(document, directory);

        User updated = this.userService.changeProfileImage(id, file);

        return ResponseEntity.ok(new ApiResponse(true, "File saved successfully", updated));

    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(ChangePasswordDTO dto){
        userService.changePassword(dto);

        return Formatter.done();
    }

    @GetMapping("/files/load-file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadProfileImage(@PathVariable String filename) {

        Resource file = this.fileStorageService.load(directory, filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}