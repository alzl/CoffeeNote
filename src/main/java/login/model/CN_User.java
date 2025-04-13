package login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CN_User {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String userType;
    private String profileImg;
    //private String businessRegNum; //CN_Registered_Cafes에서 처리(0830)

    // 역할 필드가 null일 경우 빈 리스트로 초기화
    private List<String> roles = new ArrayList<>();

    // 沅뚰븳 諛섑솚 硫붿꽌�뱶
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }
}
