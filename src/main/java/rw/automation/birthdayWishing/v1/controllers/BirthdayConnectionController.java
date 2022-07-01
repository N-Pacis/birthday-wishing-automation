package rw.automation.birthdayWishing.v1.controllers;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.automation.birthdayWishing.v1.dtos.RequestBirthdayConnectionDTO;
import rw.automation.birthdayWishing.v1.enums.EBirthdayConnectionStatus;
import rw.automation.birthdayWishing.v1.models.BirthdayConnection;
import rw.automation.birthdayWishing.v1.models.User;
import rw.automation.birthdayWishing.v1.payload.ApiResponse;
import rw.automation.birthdayWishing.v1.services.IBirthdayConnectionService;
import rw.automation.birthdayWishing.v1.services.IUserService;
import rw.automation.birthdayWishing.v1.utils.Constants;

import javax.validation.Valid;
import java.util.UUID;


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

    @GetMapping(path = "/by-status/{status}/as-list")
    public ResponseEntity<ApiResponse> listUserBirthdayConnectionsByStatus(
            @PathVariable(value = "status") EBirthdayConnectionStatus status
    ){
        User user = userService.getLoggedInUser();
        return ResponseEntity.ok(ApiResponse.success(birthdayConnectionService.listUserBirthdayConnectionsByStatus(user,status)));
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<ApiResponse> getUserBirthdayConnectionsByStatus(
            @RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "limit",defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit,
            @PathVariable(value = "status") EBirthdayConnectionStatus status
    ){
        Pageable pageable = PageRequest.of(page,limit, Sort.Direction.DESC,"id");
        User user = userService.getLoggedInUser();
        return ResponseEntity.ok(ApiResponse.success(birthdayConnectionService.getUserBirthdayConnectionsByStatus(user,status,pageable)));
    }

    @GetMapping(path = "/my-requests/as-list")
    public ResponseEntity<ApiResponse> listMyBirthdayConnectionRequests(){
        User user = userService.getLoggedInUser();
        return ResponseEntity.ok(ApiResponse.success(birthdayConnectionService.listMyBirthdayConnectionsRequests(user)));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<ApiResponse> getMyBirthdayConnectionRequests(
            @RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "limit",defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ){
        Pageable pageable = PageRequest.of(page,limit, Sort.Direction.DESC,"id");
        User user = userService.getLoggedInUser();
        return ResponseEntity.ok(ApiResponse.success(birthdayConnectionService.getMyBirthdayConnectionsRequests(user,pageable)));
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse> requestABirthdayConnection(
            @Valid @RequestBody RequestBirthdayConnectionDTO requestDTO
    ){
        User requestor = userService.getLoggedInUser();
        User intendedUser = userService.findById(requestDTO.getUserId());

        BirthdayConnection birthdayConnection = new BirthdayConnection(requestor,intendedUser,requestDTO.getMessage(), EBirthdayConnectionStatus.PENDING);
        BirthdayConnection createdBirthdayConnection = birthdayConnectionService.saveBirthdayConnection(birthdayConnection);
        return ResponseEntity.ok(new ApiResponse(true,createdBirthdayConnection));
    }

    @PutMapping("/{birthdayConnectionId}/approve")
    public ResponseEntity<ApiResponse> approveBirthdayConnectionRequest(
            @PathVariable(value = "birthdayConnectionId") UUID birthdayConnectionId
    ){
        User user = userService.getLoggedInUser();
        BirthdayConnection birthdayConnection = birthdayConnectionService.findById(birthdayConnectionId);
        if(birthdayConnectionService.checkIfUserIsIntendedBirthdayConnectionUser(user,birthdayConnection)){
            birthdayConnectionService.approveBirthdayConnection(birthdayConnection);
            return ResponseEntity.ok(ApiResponse.success("Approved Birthday connection successfully"));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized to perform this request"));
        }
    }

    @PutMapping("/{birthdayConnectionId}/reject")
    public ResponseEntity<ApiResponse> rejectBirthdayConnectionRequest(
            @PathVariable(value = "birthdayConnectionId") UUID birthdayConnectionId
    ){
        User user = userService.getLoggedInUser();
        BirthdayConnection birthdayConnection = birthdayConnectionService.findById(birthdayConnectionId);
        if(birthdayConnectionService.checkIfUserIsIntendedBirthdayConnectionUser(user,birthdayConnection)){
            birthdayConnectionService.rejectBirthdayConnection(birthdayConnection);
            return ResponseEntity.ok(ApiResponse.success("Rejected Birthday connection successfully"));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized to perform this request"));
        }
    }

    @PutMapping("/{birthdayConnectionId}/archive")
    public ResponseEntity<ApiResponse> archiveBirthdayConnectionRequest(
            @PathVariable(value = "birthdayConnectionId") UUID birthdayConnectionId
    ){
        User user = userService.getLoggedInUser();
        BirthdayConnection birthdayConnection = birthdayConnectionService.findById(birthdayConnectionId);
        if(birthdayConnectionService.checkIfUserIsIntendedBirthdayConnectionUser(user,birthdayConnection)){
            birthdayConnectionService.archiveBirthdayConnection(birthdayConnection);
            return ResponseEntity.ok(ApiResponse.success("Archived Birthday connection successfully"));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized to perform this request"));
        }
    }
}
