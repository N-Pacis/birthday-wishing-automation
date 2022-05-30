package rw.automation.birthdayWishing.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rw.automation.birthdayWishing.v1.models.User;
import rw.automation.birthdayWishing.v1.payload.ApiResponse;
import rw.automation.birthdayWishing.v1.services.IBirthdayConnectionService;
import rw.automation.birthdayWishing.v1.services.IUserService;
import rw.automation.birthdayWishing.v1.utils.Constants;


@RestController
@RequestMapping(path = "/birthday-connections")
@PreAuthorize("isAuthenticated()")
public class BirthdayConnectionController {

    private IBirthdayConnectionService birthdayConnectionService;
    private IUserService userService;

    @Autowired
    public BirthdayConnectionController(IBirthdayConnectionService birthdayConnectionService,IUserService userService){
        this.birthdayConnectionService = birthdayConnectionService;
        this.userService = userService;
    }

    @GetMapping(path = "/as-list")
    public ResponseEntity<ApiResponse> listUserBirthdayConnections(){
        User user = userService.getLoggedInUser();
        return ResponseEntity.ok(ApiResponse.success(birthdayConnectionService.listUserBirthdayConnections(user)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getUserBirthdayConnections(
            @RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "limit",defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ){
        Pageable pageable = PageRequest.of(page,limit, Sort.Direction.DESC,"id");
        User user = userService.getLoggedInUser();
        return ResponseEntity.ok(ApiResponse.success(birthdayConnectionService.getUserBirthdayConnections(user,pageable)));
    }

}
