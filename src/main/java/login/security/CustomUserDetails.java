package login.security;

import login.model.CN_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails { //이 쓸모없는 유저 디테일 같으니..
	@Autowired
    private CN_User user;

    public CustomUserDetails(CN_User user) {
        this.user = user;
    }
    
    public String getUserType() {
    	return user.getUserType();
    }

    public String getProfileImg() {
        return user.getProfileImg(); //프로필 이미지 가져오는거
    }
    
    public String getUserNickname() {
    	return user.getUserName(); //이게 username 가져오는거
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId(); //이거 헷갈리지 않게 조심
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
