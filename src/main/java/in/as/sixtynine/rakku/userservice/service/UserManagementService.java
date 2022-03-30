package in.as.sixtynine.rakku.userservice.service;


import com.azure.cosmos.implementation.guava25.collect.Sets;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.service.dbservice.UserDataService;
import in.as.sixtynine.rakku.userservice.utils.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserDataService userDataService;

    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 100))
    public User saveUser(User user, String totp) {
        user.setRoles(Sets.newHashSet(ERole.USER.getRoleName()));
        user.setCreatedTime(System.currentTimeMillis());
        user.setLastUpdatedTime(System.currentTimeMillis());
        user.setId(generateUserID(user));
        return userDataService.saveUser(user, totp);
    }

    private String generateUserID(User user) {
        return user.getFirstName().toLowerCase() + "." + user.getLastName().toLowerCase() + "." + user.getPhoneNumber();
    }

    public void generateOtp(long phoneNumber) throws Exception {
        userDataService.saveSMSOtp(phoneNumber);
    }

    public User addFollowing(String name, String customerIds) {
        return userDataService.addFollowing(name, customerIds);
    }

    public User getUserByID(String id) {
        return userDataService.getUserByID(id);
    }

    public User getUserByMobileNumber(String mobilenumber) {
        return userDataService.getUserByMobileNumber(mobilenumber);

    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userDataService.getAllUsers().forEach(users::add);
        return users;

    }

    public User geLoggedInUser(Principal principal) {
        final User userByID = userDataService.getUserByID(principal.getName());
        return userByID;
    }
}
