package login.controller;

import login.model.CN_User;
import login.security.CustomUserDetailsService;
import login.service.CN_UserService;
import review.model.CN_Review;
import review.service.CN_ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import bookmark.model.CN_Bookmark;
import bookmark.service.CN_BookmarkService;
import cafe.model.CN_Cafe;
import cafe.model.CafeRankingDTO;
import cafe.service.CN_CafeService;

import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class CN_LoginController {

    @Autowired
    private CN_UserService cnUserService;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CN_BookmarkService bookmarkService;
    
    @Autowired
    private CN_ReviewService reviewService;
    
    @Autowired
    private CN_CafeService cafeService;
    
    //0923 추가, 아디 비번찾기
    // 아이디 찾기 페이지로 이동하는 메서드
    @GetMapping("/findId")
    public ModelAndView showFindIdPage() {
        // 변경된 부분: createModelAndView 메서드를 사용하여 아이디 찾기 페이지를 로드
        return createModelAndView("findId.jsp");
    }

    // 비밀번호 찾기 페이지로 이동하는 메서드
    @GetMapping("/findPassword")
    public ModelAndView showFindPasswordPage() {
        // 변경된 부분: createModelAndView 메서드를 사용하여 비밀번호 찾기 페이지를 로드
        return createModelAndView("findPassword.jsp");
    }
    
    // 아이디 찾기 처리 메서드 (이메일로 사용자 아이디 찾기)
    @PostMapping("/findIdProcess")
    @ResponseBody
    public Map<String, Object> findIdByEmail(@RequestParam("email") String email) {
    	System.out.println("findIdByEmail");
        Map<String, Object> response = new HashMap<>();
        CN_User user = cnUserService.findUserByEmail(email); // 이메일로 유저 검색
        if (user != null) {
            response.put("exists", true);
            response.put("userId", user.getUserId()); // 유저 아이디 반환
        } else {
            response.put("exists", false); // 존재하지 않음
        }
        return response;
    }

    // 비밀번호 찾기 처리 메서드 (userId와 email로 비밀번호 재설정)
    @PostMapping("/findPasswordProcess")
    @ResponseBody
    public Map<String, Object> findPasswordByIdAndEmail(@RequestParam("userId") String userId, @RequestParam("email") String email) {
    	System.out.println("findPasswordByIdAndEmail");
    	Map<String, Object> response = new HashMap<>();
        CN_User user = cnUserService.findUserById(userId); // userId로 유저 검색
        if (user != null && user.getEmail().equals(email)) {
            // 임시 비밀번호 생성 (5자리 영어 대소문자 및 숫자)
            String tempPassword = generateTempPassword(5);
            
            // 비밀번호 암호화 후 업데이트
            user.setPassword(tempPassword); // 임시 비밀번호 설정
            cnUserService.updateUser(user); // DB에 암호화된 비밀번호로 저장

            response.put("success", true);
            response.put("tempPassword", tempPassword); // 임시 비밀번호 반환
        } else {
            response.put("success", false); // 일치하는 유저 없음
        }
        return response;
    }

    // 임시 비밀번호 생성 메서드
    private String generateTempPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            tempPassword.append(chars.charAt(randomIndex));
        }
        return tempPassword.toString();
    }
    ////
    
    // 로그인 페이지로 리다이렉트하는 메서드
    @GetMapping("/") // "/login/" == "/login/login"
    public String showLoginPage_root() {
        return "redirect:/login/login";
    }

    // 로그인 페이지를 표시하는 메서드
    @GetMapping("/login") // "/login/login"
    public ModelAndView showLoginPage() {
        System.out.println("/login/login start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> user_type = authentication.getAuthorities();
        String userId = authentication.getName();
        System.out.println("auth:" + authentication);
        System.out.println("userId:" + userId);
        System.out.println("usertype:" + user_type);
        
        // 변경된 부분: createModelAndView 메서드를 사용하여 로그인 페이지를 로드
        return createModelAndView("login.jsp");
    }

    // 로그인 오류 페이지를 표시하는 메서드
    @GetMapping("/login-error")
    public ModelAndView loginError() {
        ModelAndView modelAndView = createModelAndView("login.jsp"); // 로그인 페이지를 로드하도록 설정
        modelAndView.addObject("error", "Invalid username or password.");
        return modelAndView;
    }

    // 로그인 처리 메서드
    @PostMapping("/loginAttempt")
    public ModelAndView handleLogin(@RequestParam("user_id") String userId,
                                    @RequestParam("password") String password,
                                    HttpServletRequest request) {
        System.out.println("handleLogin: login attempt started for user: " + userId);
        try {
            // 사용자 ID 존재 여부 확인
            if (!cnUserService.idExists(userId)) {
                return new ModelAndView("redirect:/login/login-error")
                        .addObject("error", "Invalid username or password.");
            }
            System.out.println("----로그인 시도 start----");

            // UserDetails를 사용하여 Authentication 생성
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

            // WebAuthenticationDetails를 통해 Details 설정
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 사용자 정보 및 권한 출력
            Collection<? extends GrantedAuthority> user_type = authentication.getAuthorities();
            String authuserId = authentication.getName();
            System.out.println("auth:" + authentication);
            System.out.println("입력한userId:" + userId);
            System.out.println("저장한userId:" + authuserId);
            System.out.println("usertype:" + user_type);
            System.out.println("----로그인 성공 end----");
            
            return new ModelAndView("redirect:/");
            //return new ModelAndView("redirect:/user/home");
        } catch (BadCredentialsException e) {
            return new ModelAndView("redirect:/login/login-error")
                    .addObject("error", "Invalid username or password.");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/login/login-error");
        }
    }

    // 회원가입 페이지를 표시하는 메서드
    @GetMapping("/register")
    public ModelAndView showRegistrationPage() {
        // 변경된 부분: createModelAndView 메서드를 사용하여 회원가입 페이지를 로드
        return createModelAndView("register.jsp");
    }

    // 회원가입 처리 메서드
    @PostMapping("/register")
    public ModelAndView handleRegistration(CN_User user) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            // 유저 이름 강제 변경
            String userName = user.getUserName();
            try {
                user.setUserName(new String(userName.getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            cnUserService.createUser(user);

            // 자바스크립트를 통해 알림을 표시하고 로그인 페이지로 리다이렉트
            String successMessage = "<script>alert('회원가입에 성공하셨습니다. 로그인해주세요.'); window.location.href='/login/login';</script>";
            modelAndView.setViewName("login/register-success");
            modelAndView.addObject("message", successMessage);
        } catch (DuplicateKeyException e) {
            // 변경된 부분: createModelAndView 메서드를 사용하여 회원가입 페이지를 로드
            modelAndView = createModelAndView("register.jsp");
            modelAndView.addObject("error", "이미 존재하는 아이디입니다. 다른 아이디를 선택해주세요.");
        }

        return modelAndView;
    }

    // 사용자 ID 중복 체크를 위한 메서드
    @GetMapping("/check-id-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkIdDuplicate(@RequestParam("id") String id) {
        boolean exists = cnUserService.idExists(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 이메일 중복 체크를 위한 메서드
    @GetMapping("/check-email-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkEmailDuplicate(@RequestParam("email") String email) {
        boolean exists = cnUserService.emailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 로그아웃 처리 메서드
    @GetMapping("/logout")
    public String logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.clearContext();
        }
        return "redirect:/";
    }

    // main/index.jsp 안에 login 관련 페이지가 보일 수 있도록 createModelAndView 메서드 추가
    private ModelAndView createModelAndView(String section) {
        ModelAndView modelAndView = new ModelAndView("main/index");
        // 상대 경로로 수정하여 login 디렉토리의 JSP를 로드하도록 설정
        modelAndView.addObject("section", "../login/" + section);
        
        // 카페 순위 계산
        List<CN_Cafe> cafes = cafeService.findAllCafes();
        List<CafeRankingDTO> cafeRankings = cafes.stream()
            .map(cafe -> {
                List<CN_Review> reviews = reviewService.findReviewsByCafeId(cafe.getCafeId());
                double avgRating = reviews.stream().mapToInt(CN_Review::getRating).average().orElse(0);
                int reviewCount = reviews.size();
                double adjustedScore = calculateWeightedRating(avgRating, reviewCount, 5, 3.8);
                return new CafeRankingDTO(cafe, adjustedScore, avgRating, reviewCount);
            })
            .filter(ranking -> ranking.getReviewCount() > 5) // 리뷰 개수가 5개 초과인 경우만 포함
            .sorted(Comparator.comparingDouble(CafeRankingDTO::getAdjustedScore).reversed())
            .limit(10) // 상위 10개 카페
            .collect(Collectors.toList());

        // 북마크 순위 계산
        List<CN_Bookmark> bookmarks = bookmarkService.findPublicBookmarks();
        List<CN_Bookmark> topBookmarks = bookmarks.stream()
            .sorted(Comparator.comparingInt(CN_Bookmark::getLikes).reversed())
            .limit(10) // 상위 10개 북마크
            .collect(Collectors.toList());

        modelAndView.addObject("cafeRankings", cafeRankings);
        modelAndView.addObject("topBookmarks", topBookmarks);

        // 인증된 사용자 정보가 필요할 때는 개별 메서드에서 직접 가져와서 사용
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            modelAndView.addObject("userId", userDetails.getUsername());
        }

        return modelAndView;
    }
    //0905추가 - 카페 순위 계산 공식
    public static double calculateWeightedRating(double R, int v, int m, double C) {
        return (v / (double)(v + m)) * R + (m / (double)(v + m)) * C;
    }
}
