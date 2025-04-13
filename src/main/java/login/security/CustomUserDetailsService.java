package login.security;

import login.model.CN_User;
import login.service.CN_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CN_UserService cnUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CN_User user = cnUserService.findUserById(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // 권한 설정 시 "ROLE_"을 추가
        String role = /*"ROLE_" +*/ user.getUserType().toUpperCase();
        user.getRoles().add(role);

        return new CustomUserDetails(user); // CustomUserDetails로 반환
    }
}
