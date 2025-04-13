package login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
    	System.out.println("SecurityConfig: passwordEncoder start");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	System.out.println("SecurityConfig: authenticationManager start");
    	return authenticationConfiguration.getAuthenticationManager();
    }
    /*
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(5242880); // 5MB
        return resolver;
    }
    */
    /*
     * 권한 설정할 때 hasAnyRole() 사용하면 자꾸 뭐 접두사 ROLE_ 이 빠져서 그런건지 인식이 제대로 안된다.
     * 실제로 로그인한 유저에게 저장되는 권한 명은 ROLE_이 빠진 채 저장됨을 유의
     * 그래서 hasAnyAuthority() 를 사용함.
     * 어느 url에 대해서 단 하나의 권한만 허용하려면 hasAuthority()
     * 어느 url에 대해서 여러명의 권한을 허용하려면 hasAnyAuthority()
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
	            .authorizeRequests()
	            //리소스 좌표 허용
	            .antMatchers("/css/**", "/js/**", "/profile_img/**", "/reviewimages/**", "/advertisingimages/**", "/images/**", "/fonts/**").permitAll()
	            //0827 - 비번 확인 url 허용 추가
	            .antMatchers("/*", "/login/*", "/test/*").permitAll()
	            //reviewlike랑 bookmarklike는 로그인 여부를 알기 위해서
	            .antMatchers("/map/mapview", "/map/searchmap", "/map/filterByBookmark", "/map/getCafeMedia", 
	            		"/map/reviewlike", "/map/bookmarklike", "/map/bookmarkdetails", "/map/bookmarkdetails2", 
	            		"/map/searchBookmarks", "/map/searchNearbyCafes").permitAll()
	            //.antMatchers(HttpMethod.GET, "/map/*").permitAll()
	            .antMatchers("/map/addreview", "/map/bookmarkcreate").hasAnyAuthority("USER", "ADMIN", "BUSINESS")       
	            .antMatchers("/user/*").hasAnyAuthority("USER", "ADMIN", "BUSINESS")  // hasAnyAuthority 사용
	            .antMatchers("/noticeWrite","/saveNotice","/deleteNotice").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login/login")
                //로그인 성공, 실패 여부에 따른 url 이동은 LoginController의 handleLogin으로 처리
                //.defaultSuccessUrl("/user/home", true) //로그인 성공시 보일 페이지
                .defaultSuccessUrl("/", true) 
                .failureUrl("/login/login-error")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/login/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login/login")
                .sessionFixation().migrateSession()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/");

        System.out.println("Security configuration loaded successfully");
        return http.build();
    }

}
