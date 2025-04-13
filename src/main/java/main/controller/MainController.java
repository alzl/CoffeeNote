package main.controller;

import login.model.CN_User;
import login.security.CustomUserDetails;
import login.service.CN_UserService;
import notice.model.CN_Notice;
import notice.service.CN_NoticeService;
import review.model.CN_Review;
import review.service.CN_ReviewService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import advertisingimage.model.CN_Advertising_Image;
import advertisingimage.service.CN_Advertising_ImageService;
import bookmark.model.CN_Bookmark;
import bookmark.service.CN_BookmarkService;
import bookmarkcafes.model.CN_Bookmark_Cafes;
import bookmarkcafes.service.CN_Bookmark_CafesService;
import cafe.model.CN_Cafe;
import cafe.model.CN_CafeDetails;
import cafe.model.CafeRankingDTO;
import cafe.service.CN_CafeService;

@Controller
public class MainController {
	
    @Autowired
    private CN_NoticeService noticeService;
    
    @Autowired
    private CN_CafeService cafeService;

    @Autowired
    private CN_ReviewService reviewService;

    @Autowired
    private CN_BookmarkService bookmarkService;
    
    @Autowired
    private CN_Advertising_ImageService advertisingImageService;

	// "/error" url을 숨기기 위해서 일부러 
	// /index로 보내는 url과 같이 "/"로 분류하여 예외처리함
    //0905추가 - 카페, 북마크 순위 보이기
    @GetMapping("/")
    public ModelAndView showHome(HttpServletRequest request) {
        ModelAndView modelAndView = createModelAndView("home.jsp");
        // 0909 - home.jsp에 있던 카페 북마크 순위를 index.jsp로 옮기기 위해서 "/"말고 다른 대서도 적용되게함
        // createModelAndView에서 불러오게 함
     // 요청에서 플래시 속성 가져오기
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null && "true".equals(flashMap.get("error"))) {
            modelAndView.setViewName("main/error");
        }

        // 모든 카페 가져오기
        List<CN_Cafe> cafes = cafeService.findAllCafes();
        List<Map<String, Object>> cafesWithImages = new ArrayList<>();

        //0924: 이미지를 가진 카페 필터링
        for (CN_Cafe cafe : cafes) {
            // 해당 카페의 광고 이미지 가져오기
            List<CN_Advertising_Image> images = advertisingImageService.findImagesByCafeId(cafe.getCafeId());
            if (!images.isEmpty()) {
                // 가장 최근의 이미지 선택
                CN_Advertising_Image latestImage = images.get(0);

                // CN_CafeDetails 가져오기 (placeName을 사용하여 Kakao API 호출)
                CN_CafeDetails cafeDetails = getCafeDetailsByPlaceName(cafe.getPlaceName());
                if (cafeDetails != null) {
                    Map<String, Object> cafeInfo = new HashMap<>();
                    cafeInfo.put("cafeId", cafe.getCafeId());
                    cafeInfo.put("placeName", cafe.getPlaceName());
                    cafeInfo.put("roadAddressName", cafeDetails.getRoadAddressName());
                    cafeInfo.put("imagePath", latestImage.getImagePath());
                    cafesWithImages.add(cafeInfo);
                }
            }
        }

        // 이미지가 있는 카페가 4개 미만인 경우 처리
        int numberOfCafes = Math.min(cafesWithImages.size(), 4);

        // 랜덤하게 카페 선택
        Collections.shuffle(cafesWithImages);
        List<Map<String, Object>> selectedCafes = cafesWithImages.stream()
                .limit(numberOfCafes)
                .collect(Collectors.toList());

        // 모델에 추가
        modelAndView.addObject("randomCafes", selectedCafes);

        return modelAndView;
    }
 // Kakao API를 사용하여 CN_CafeDetails를 가져오는 메서드
    private CN_CafeDetails getCafeDetailsByPlaceName(String placeName) {
        try {
            String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
            String query = URLEncoder.encode(placeName, "UTF-8");
            String fullURL = apiURL + "?query=" + query + "&category_group_code=CE7";
            URL url = new URL(fullURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa"); // 실제 REST API 키로 대체

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder apiResponse = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    apiResponse.append(inputLine);
                }
                in.close();
                String data = apiResponse.toString();

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> resultMap = mapper.readValue(data, new TypeReference<Map<String, Object>>() {});
                List<Map<String, Object>> documents = (List<Map<String, Object>>) resultMap.get("documents");

                for (Map<String, Object> cafeData : documents) {
                    if (placeName.equals(cafeData.get("place_name"))) {
                        CN_CafeDetails cafeDetails = new CN_CafeDetails();
                        cafeDetails.setCafeId((String) cafeData.get("id"));
                        cafeDetails.setX((String) cafeData.get("x"));
                        cafeDetails.setY((String) cafeData.get("y"));
                        cafeDetails.setPlaceName((String) cafeData.get("place_name"));
                        cafeDetails.setAddressName((String) cafeData.get("address_name"));
                        cafeDetails.setRoadAddressName((String) cafeData.get("road_address_name"));
                        cafeDetails.setPhone((String) cafeData.get("phone"));
                        cafeDetails.setPlaceUrl((String) cafeData.get("place_url"));
                        return cafeDetails;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //0905추가 - 카페 순위 계산 공식
    public static double calculateWeightedRating(double R, int v, int m, double C) {
        return (v / (double)(v + m)) * R + (m / (double)(v + m)) * C;
    }
    
	@GetMapping("/error")
	public ModelAndView handleError(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    ModelAndView modelAndView = new ModelAndView("main/error");

	    if (status != null) {
	        int statusCode = Integer.parseInt(status.toString());
	        modelAndView.addObject("errorCode", statusCode);

	        switch (statusCode) {
            	case 400:
            		modelAndView.addObject("errorMessage", "잘못된 요청을 받았습니다.");
                break;
            	case 401:
            		modelAndView.addObject("errorMessage", "접근 권한이 없는 페이지입니다");
                break;
	            case 404:
	                modelAndView.addObject("errorMessage", "페이지를 찾을 수 없습니다.");
	                break;
	            case 405:
	            	modelAndView.addObject("errorMessage", "지원하지 않는 페이지입니다");
	            	break;
	            case 500:
	                modelAndView.addObject("errorMessage", "서버 내부 오류가 발생했습니다.");
	                break;
	            default:
	                modelAndView.addObject("errorMessage", "예기치 못한 오류가 발생했습니다.");
	                break;
	        }
	    } else {
	        // 오류 상태 코드가 없는 경우 (예: 직접 "/error"로 접근한 경우)
	        modelAndView.addObject("errorCode", "Unknown");
	        modelAndView.addObject("errorMessage", "예기치 못한 오류가 발생했습니다.");
	    }

	    return modelAndView;
	}
	/*
    @GetMapping("/a")
    public ModelAndView showPageA() {
        return createModelAndView("a.jsp");
    }

    @GetMapping("/b")
    public ModelAndView showPageB() {
        return createModelAndView("b.jsp");
    }
    */
	//입점안내
    @GetMapping("/storeGuide")
    public ModelAndView showStoreGuide() {
        return createModelAndView("storeGuide.jsp");
    }
    //회사소개
    @GetMapping("/comInfo")
    public ModelAndView showComInfo() {
        return createModelAndView("comInfo.jsp");
    }
    //이용약관
    @GetMapping("/terms")
    public ModelAndView showTerms() {
        return createModelAndView("terms.jsp");
    }
    
    @GetMapping("/noticeList")
    public ModelAndView showNoticeList(@RequestParam(defaultValue = "1") int page) {
        ModelAndView modelAndView = createModelAndView("noticeList.jsp"); // 수정: createModelAndView 사용
        int pageSize = 5;
        List<CN_Notice> notices = noticeService.findAllNotices();
        int totalNotices = notices.size();
        int totalPages = (int) Math.ceil((double) totalNotices / pageSize);

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalNotices);

        List<CN_Notice> pagedNotices = notices.subList(startIndex, endIndex);
        modelAndView.addObject("notices", pagedNotices);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", totalPages);
        return modelAndView;
    }

    @GetMapping("/noticeView")
    public ModelAndView viewNotice(@RequestParam int noticeId, @RequestParam(defaultValue = "1") int page) {
        ModelAndView modelAndView = createModelAndView("noticeView.jsp"); // 수정: createModelAndView 사용
        CN_Notice notice = noticeService.findNoticeById(noticeId);
        modelAndView.addObject("notice", notice);
        modelAndView.addObject("currentPage", page);
        return modelAndView;
    }

    @GetMapping("/noticeWrite")
    public ModelAndView writeNotice() {
        ModelAndView modelAndView = createModelAndView("noticeWrite.jsp");//new ModelAndView("main/index");
        //modelAndView.addObject("section", "noticeWrite.jsp");
        return modelAndView;
    }

    @PostMapping("/saveNotice")
    public String saveNotice(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        RedirectAttributes redirectAttributes) {
    	//admin만 글쓰기 가능
    	// 현재 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String userType = userDetails.getUserType();
        //어드민 아니면 ㄴㄴ
        if(!userType.equals("ADMIN")) {
        	redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login/login"; // 로그인 페이지로 리다이렉트
        }
        //얜 없어도 되긴 함
        if(!(auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails)) {
        	redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login/login"; // 로그인 페이지로 리다이렉트
        }
        // UTF-8 인코딩 처리
        try {
            title = new String(title.getBytes("ISO-8859-1"), "UTF-8");
            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    	// 잘 가져왔으니 저장
        CN_Notice notice = new CN_Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setCreatedDate(new Timestamp(System.currentTimeMillis())); // 현재 시간을 설정
        notice.setUserId(userDetails.getUsername()); // userId로 설정

        noticeService.createNotice(notice);

        redirectAttributes.addFlashAttribute("message", "공지사항이 저장되었습니다.");
        return "redirect:/noticeList";
    }
    
    @PostMapping("/deleteNotice")
    public String deleteNotice(@RequestParam int noticeId, RedirectAttributes redirectAttributes) {
    	//admin만 글삭제 가능
    	// 현재 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String userType = userDetails.getUserType();
        //어드민 아니면 ㄴㄴ
        if(!userType.equals("ADMIN")) {
        	redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login/login"; // 로그인 페이지로 리다이렉트
        }
        //얜 없어도 되긴 함
        if(!(auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails)) {
        	redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login/login"; // 로그인 페이지로 리다이렉트
        }
        //잘되면 삭제
        noticeService.deleteNotice(noticeId);
        redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 삭제되었습니다.");
        return "redirect:/noticeList";
    }

    private ModelAndView createModelAndView(String section) {
        ModelAndView modelAndView = new ModelAndView("main/index");
        modelAndView.addObject("section", section);
        
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
        	//유저디테일은 getUsername이 user_id 가져오는거니까 사용에 조심
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            modelAndView.addObject("profileImg", userDetails.getProfileImg());
            modelAndView.addObject("userId", userDetails.getUsername()); //헷갈리지 말것!!!!
            modelAndView.addObject("userName", userDetails.getUserNickname());            
        } else if (auth != null) {
            modelAndView.addObject("userId", auth.getName());
        }

        return modelAndView;
    }
}
