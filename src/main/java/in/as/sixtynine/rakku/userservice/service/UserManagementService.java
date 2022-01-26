package in.as.sixtynine.rakku.userservice.service;


import com.azure.cosmos.implementation.guava25.collect.Sets;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.service.dbservice.UserDataService;
import in.as.sixtynine.rakku.userservice.utils.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserDataService userDataService;

    public User saveUser(User user, String totp) {
        user.setRoles(Sets.newHashSet(ERole.USER.getRoleName()));
        user.setCreatedTime(System.currentTimeMillis());
        user.setLastUpdatedTime(System.currentTimeMillis());
        return userDataService.saveUser(user, totp);
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
}
