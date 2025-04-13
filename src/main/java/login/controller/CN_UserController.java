package login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;  // PasswordEncoder import
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import advertising.model.CN_Advertising;
import advertising.model.CN_AdvertisingDetails;
import advertising.service.CN_AdvertisingService;
import advertisingimage.model.CN_Advertising_Image;
import advertisingimage.service.CN_Advertising_ImageService;
import bookmark.model.CN_Bookmark;
import bookmark.service.CN_BookmarkService;
import bookmarkcafes.service.CN_Bookmark_CafesService;
import bookmarklikes.model.CN_Bookmark_Likes;
import bookmarklikes.service.CN_Bookmark_LikesService;
import cafe.model.CN_Cafe;
import cafe.model.CN_CafeDetails;
import cafe.model.CafeRankingDTO;
import cafe.service.CN_CafeService;
import login.model.CN_User;
import login.security.CustomUserDetails;
import login.service.CN_UserService;
import registeredcafes.model.CN_Registered_Cafes;
import registeredcafes.service.CN_Registered_CafesService;
import review.model.CN_Review;
import review.service.CN_ReviewService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class CN_UserController {

    @Autowired
    private CN_UserService userService;

    @Autowired
    private CN_Registered_CafesService registeredCafesService;

    @Autowired
    private CN_CafeService cafeService;
    
    @Autowired
    private CN_ReviewService reviewService;
    
    @Autowired 
    private CN_AdvertisingService advertisingService;
    
    @Autowired
    private CN_Advertising_ImageService advertisingImageService;
    
    @Autowired
    private CN_BookmarkService bookmarkService;

    @Autowired
    private CN_Bookmark_LikesService bookmarkLikesService;

    @Autowired
    private CN_Bookmark_CafesService bookmarkCafesService;

    @Autowired
    private AuthenticationManager authenticationManager;  // AuthenticationManager 주입

    @Autowired
    private PasswordEncoder passwordEncoder;  // PasswordEncoder 주입

    // 홈 페이지로 리다이렉트하는 메서드
    @GetMapping("/")
    public String home() {
        return "redirect:/user/home";
    }

    //0827 - 이름,이메일,사업자번호 수정 시 비번 본인인증 메서드
    @GetMapping("/verifyPassword")
    @ResponseBody
    public Map<String, Boolean> VerifyPassword(@RequestParam("password") String password) {
        System.out.println("VerifyPassword - pw:" + password);
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // 비밀번호 인증 처리 (userService.authenticate 메서드 사용)
        boolean isAuthenticated = userService.authenticate(auth.getName(), password);
        
        // 결과를 Map으로 반환하여 JSON 형식으로 응답
        Map<String, Boolean> result = new HashMap<>();
        result.put("match", isAuthenticated);
        
        return result;
    }
    
    //0827 - 이름 변경하기!
    @PostMapping("/updateUserName")
    public ModelAndView updateUserName(String userName, RedirectAttributes redirectAttributes) {
    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("redirect:/user/home");  //홈으로 리다이렉트
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	try {
			userName = new String(userName.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String userId = auth.getName();
    	System.out.println("updateUserName - userName:"+userName+"userId"+userId);
    	boolean checkUpdateSuccess = userService.setUserName(userId, userName);
    	
    	System.out.println("사용자 이름 업뎃 성공여부:"+checkUpdateSuccess);
    	if (checkUpdateSuccess) {
    		//이걸쓰면 url 주소 변경하려고 리다이렉트 해도 잘 넘어가짐, Flash로 보내면 세션에 메시지가 저장돼 유지돼서 한글도 인코딩 된대
            redirectAttributes.addFlashAttribute("successMessage", "이름이 성공적으로 변경되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "이름 변경에 실패했습니다.");
        }
    	
    	//user/home으로 강제이동
    	return modelAndView;
    }
    //0827 - 이메일 변경하기!
    @PostMapping("/updateUserEmail")
    public ModelAndView updateUserEmail(String userEmail, RedirectAttributes redirectAttributes) {
    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("redirect:/user/home");  //홈으로 리다이렉트
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	try {
			userEmail = new String(userEmail.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String userId = auth.getName();
    	System.out.println("updateUserEmail - userEmail:"+userEmail+"userId"+userId);
    	//이메일 중복 체크 후 수정
        if (userService.emailExists(userEmail)) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 사용 중인 이메일입니다.");
        } else {
            boolean checkUpdateSuccess = userService.setUserEmail(userId, userEmail);
            if (checkUpdateSuccess) {
                redirectAttributes.addFlashAttribute("successMessage", "이메일이 성공적으로 변경되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "이메일 변경에 실패했습니다.");
            }
        }

    	return modelAndView;
    }
    // 사업자 등록번호 추가 메서드
    // 사업자 등록번호 추가 메서드
    @PostMapping("/registerBusiness")
    public ModelAndView registerBusiness(
            @RequestParam("businessRegNum") String businessRegNum,
            @RequestParam("cafeId") String cafeId,
            @RequestParam("cafeX") String cafeX,
            @RequestParam("cafeY") String cafeY,
            @RequestParam("placeName") String placeName,
            RedirectAttributes redirectAttributes) {
    	
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/user/home");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        //입력받은 것 중 placename은 한글이 깨질 수 있다
    	try {
    		placeName = new String(placeName.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // 디버깅: 입력된 사업자 등록번호와 카페 정보 출력
        System.out.println("registerBusiness - 입력된 사업자 등록번호: " + businessRegNum);
        System.out.println("registerBusiness - 입력된 카페 ID: " + cafeId);
        System.out.println("registerBusiness - 입력된 카페 위치 (X, Y): (" + cafeX + ", " + cafeY + ")");
        System.out.println("registerBusiness - 입력된 카페 이름: " + placeName);
        System.out.println("registerBusiness - 현재 사용자 ID: " + userId);

        // 카페가 DB에 등록되어 있는지 확인
        CN_Cafe cafe = cafeService.findCafeById(cafeId);
        if (cafe == null) {
            // 카페가 없다면 새로 추가
            CN_Cafe newCafe = new CN_Cafe(cafeId, cafeX, cafeY, placeName);
            cafeService.createCafe(newCafe);
            System.out.println("registerBusiness - 새 카페 추가됨: " + newCafe);
        } else {
            System.out.println("registerBusiness - 카페가 이미 존재함: " + cafe);
        }

        // 사업자 등록번호 중복 체크
        if (registeredCafesService.findRegisteredCafesById(Integer.parseInt(businessRegNum)) != null) {
            System.out.println("registerBusiness - 이미 등록된 사업자 등록번호입니다: " + businessRegNum);
            redirectAttributes.addFlashAttribute("errorMessage", "이미 등록된 사업자 등록번호입니다.");
            return modelAndView;
        } else {
            System.out.println("registerBusiness - 사업자 등록번호가 유효합니다: " + businessRegNum);
        }

        // 중복 등록 체크 (카페 ID로 등록된 사업장 찾기)
        CN_Registered_Cafes existingCafe = registeredCafesService.findRegisteredCafeByCafeId(cafeId);
        if (existingCafe != null) {
            System.out.println("registerBusiness - 이미 다른 사업자가 등록한 카페입니다: " + cafeId);
            redirectAttributes.addFlashAttribute("errorMessage", "이미 다른 사업자가 등록한 카페입니다.");
            return modelAndView;
        } else {
            System.out.println("registerBusiness - 카페 ID 중복 없음, 등록 가능: " + cafeId);
        }

        // CN_Registered_Cafes 객체 생성 및 등록
        CN_Registered_Cafes newRegisteredCafe = new CN_Registered_Cafes();
        newRegisteredCafe.setRegisteredId(Integer.parseInt(businessRegNum)); // businessRegNum을 int로 변환하여 등록 ID로 사용
        newRegisteredCafe.setCafeId(cafeId);
        newRegisteredCafe.setUserId(userId);

        // 디버깅: 등록될 CN_Registered_Cafes 객체 정보 출력
        System.out.println("registerBusiness - 등록할 새로운 CN_Registered_Cafes 객체: " + newRegisteredCafe);

        registeredCafesService.createRegisteredCafes(newRegisteredCafe);
        redirectAttributes.addFlashAttribute("successMessage", "사업장이 성공적으로 등록되었습니다.");

        System.out.println("registerBusiness - 사업장 등록 완료: " + newRegisteredCafe);
        return modelAndView;
    }

	// 카페 검색하여 보이기
    @PostMapping("/searchmap")
    @ResponseBody // 추가하여 JSON 형태로 응답
    public List<CN_CafeDetails> searchMap(@RequestParam("keyword") String keyword) throws IOException {
        System.out.println("searchMap method start with keyword: " + keyword);

        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String query = URLEncoder.encode(keyword, "UTF-8");
        String category = "CE7";
        int page = 1;
        int size = 15;
        int totalResults = 45;

        List<CN_CafeDetails> allCafeDetails = new ArrayList<>();

        while (allCafeDetails.size() < totalResults) {
            String fullURL = apiURL + "?query=" + query + "&category_group_code=" + category + "&page=" + page;
            URL url = new URL(fullURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa");

            String data = "";
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                data = response.toString();
            } else {
                System.out.println("GET request failed: " + responseCode);
                break;
            }

            int first = data.indexOf('[');
            int end = data.indexOf(']');
            if (first != -1 && end != -1) {
                data = data.substring(first, end + 1);
            }

            ObjectMapper mapper = new ObjectMapper();
            List<CN_CafeDetails> cafeDetailsList = mapper.readValue(data, new TypeReference<List<CN_CafeDetails>>() {});

            allCafeDetails.addAll(cafeDetailsList);
            if (cafeDetailsList.size() < size) {
                break;
            }
            page++;
        }

        System.out.println("searchMap method return " + allCafeDetails.size() + " results.");
        System.out.println("searchMap cafelist: " + allCafeDetails);
        // JSON 형태로 반환
        return allCafeDetails;
    }
    // 홈 페이지를 표시하는 메서드
    @GetMapping("/home")
    public ModelAndView showHomePage(HttpSession session) {
        System.out.println("/user/home start");
        //ModelAndView modelAndView = new ModelAndView("user/home");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = createModelAndView("home.jsp", auth); // main/index를 이용하도록 변경

        System.out.println("auth:" + auth);
        if (session != null) {
            System.out.println("Session ID: " + session.getId());
            System.out.println("Creation Time: " + new java.util.Date(session.getCreationTime()));
            System.out.println("Last Accessed Time: " + new java.util.Date(session.getLastAccessedTime()));
            System.out.println("Max Inactive Interval (seconds): " + session.getMaxInactiveInterval());
        } else {
            System.out.println("Session is null.");
        }

        if (auth != null && auth.isAuthenticated()) {
            String userId = auth.getName();
            CN_User user = userService.findUserById(userId);
            if (user != null) {
                modelAndView.addObject("user", user);
                modelAndView.addObject("profileImg", user.getProfileImg());

                List<CN_Registered_Cafes> registeredCafes = registeredCafesService.findAllRegisteredCafesByUserId(userId);
                List<CN_CafeDetails> cafeDetailsList = new ArrayList<>();

                for (CN_Registered_Cafes registeredCafe : registeredCafes) {
                    CN_Cafe cafe = cafeService.findCafeById(registeredCafe.getCafeId());
                    if (cafe != null) {
                        try {
                            String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
                            String query = URLEncoder.encode(cafe.getPlaceName(), "UTF-8");
                            String fullURL = apiURL + "?query=" + query + "&category_group_code=CE7";
                            
                            URL url = new URL(fullURL);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa");

                            int responseCode = conn.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                String inputLine;
                                StringBuilder response = new StringBuilder();
                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();
                                
                                ObjectMapper mapper = new ObjectMapper();
                                Map<String, Object> resultMap = mapper.readValue(response.toString(), new TypeReference<Map<String, Object>>() {});
                                List<Map<String, Object>> documents = (List<Map<String, Object>>) resultMap.get("documents");

                                for (Map<String, Object> cafeData : documents) {
                                    if (cafe.getCafeId().equals(cafeData.get("id"))) {
                                        CN_CafeDetails cafeDetails = new CN_CafeDetails();
                                        cafeDetails.setCafeId(cafe.getCafeId());
                                        cafeDetails.setX((String) cafeData.get("x"));
                                        cafeDetails.setY((String) cafeData.get("y"));
                                        cafeDetails.setPlaceName((String) cafeData.get("place_name"));
                                        cafeDetails.setAddressName((String) cafeData.get("address_name"));
                                        cafeDetails.setRoadAddressName((String) cafeData.get("road_address_name"));
                                        cafeDetails.setPhone((String) cafeData.get("phone"));
                                        cafeDetails.setPlaceUrl((String) cafeData.get("place_url"));

                                        // 광고(홍보) 가져오기
                                        List<CN_Advertising> advertisingList = advertisingService.findAdvertisingsByCafeId(cafe.getCafeId());
                                        List<CN_AdvertisingDetails> advertisingDetailsList = new ArrayList<>();
                                        
                                        for (CN_Advertising advertising : advertisingList) {
                                            List<CN_Advertising_Image> advertisingImages = advertisingImageService.findImagesByAdvertisingId(advertising.getAdvertisingId());
                                            CN_AdvertisingDetails advertisingDetails = new CN_AdvertisingDetails(advertising, advertisingImages);
                                            advertisingDetailsList.add(advertisingDetails);
                                        }
                                        cafeDetails.setAdvertisings(advertisingDetailsList); // 카페 상세 정보에 광고 목록 추가
                                        
                                        cafeDetailsList.add(cafeDetails);
                                        break;
                                    }
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("Error occurred while processing cafe: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
                // 0903-**북마크 관련 코드 추가 시작**
                // 로그인한 사용자가 추가한 북마크
                List<CN_Bookmark> userBookmarks = bookmarkService.findBookmarksByUserId(userId);
                List<Map<String, Object>> userBookmarkDetails = new ArrayList<>();

                for (CN_Bookmark bookmark : userBookmarks) {
                    Map<String, Object> bookmarkData = new HashMap<>();
                    CN_User bookmarkUser = userService.findUserById(bookmark.getUserId());
                    bookmarkData.put("userId", bookmarkUser.getUserId());
                    bookmarkData.put("userName", bookmarkUser.getUserName());
                    bookmarkData.put("title", bookmark.getTitle());
                    bookmarkData.put("content", bookmark.getContent());
                    bookmarkData.put("bookmarkId", bookmark.getBookmarkId());
                    bookmarkData.put("likes", bookmark.getLikes());
                    // isPublic 값을 boolean 형태로 변환
                    bookmarkData.put("isPublic", bookmark.getIsPublic() == 1 ? "true" : "false");

                    userBookmarkDetails.add(bookmarkData);
                }
                modelAndView.addObject("userBookmarks", userBookmarkDetails);

                // 로그인한 사용자가 좋아요한 북마크
                List<CN_Bookmark> likedBookmarks = bookmarkLikesService.findBookmarksLikedByUserId(userId);
                List<Map<String, Object>> likedBookmarkDetails = new ArrayList<>();

                for (CN_Bookmark bookmark : likedBookmarks) {
                    Map<String, Object> bookmarkData = new HashMap<>();
                    CN_User bookmarkUser = userService.findUserById(bookmark.getUserId());
                    bookmarkData.put("userId", bookmarkUser.getUserId());
                    bookmarkData.put("userName", bookmarkUser.getUserName());
                    bookmarkData.put("title", bookmark.getTitle());
                    bookmarkData.put("content", bookmark.getContent());
                    bookmarkData.put("bookmarkId", bookmark.getBookmarkId());
                    bookmarkData.put("likes", bookmark.getLikes());
                    // isPublic 값을 boolean 형태로 변환
                    bookmarkData.put("isPublic", bookmark.getIsPublic() == 1 ? "true" : "false");

                    likedBookmarkDetails.add(bookmarkData);
                }
                modelAndView.addObject("likedBookmarks", likedBookmarkDetails);

                modelAndView.addObject("cafeDetailsList", cafeDetailsList); // 카페 상세 정보 목록을 모델에 추가               
            } else {
                return new ModelAndView("redirect:/login/login");
            }
        } else {
            return new ModelAndView("redirect:/login/login");
        }
        return modelAndView;
    }
    //0903추가 - 내가 추가한 북마크 삭제하기
    @PostMapping("/deleteBookmark")
    @ResponseBody
    public Map<String, String> deleteBookmark(@RequestParam("bookmarkId") int bookmarkId) {
        Map<String, String> response = new HashMap<>();
        try {
            // 1. 북마크에 연결된 좋아요 먼저 삭제
            bookmarkLikesService.deleteLikesByBookmarkId(bookmarkId);

            // 2. 북마크에 연결된 카페 관계 먼저 삭제
            bookmarkCafesService.deleteCafesByBookmarkId(bookmarkId);

            // 3. 실제 북마크 삭제
            bookmarkService.deleteBookmark(bookmarkId);

            response.put("message", "북마크가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            response.put("message", "북마크 삭제 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return response;
    }

    
    //0902 홍보글 추가 (mapcontroller의 리뷰글 추가를 참고함)
    @PostMapping("/createAdvertising")
    public String createAdvertising(
            @RequestParam("cafeId") String cafeId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("images") MultipartFile[] images,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        System.out.println("createAdvertising method start");

        // 현재 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        if (userId == null || "anonymousUser".equals(userId)) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/user/home";
        }

        // UTF-8 인코딩 처리
        try {
            title = new String(title.getBytes("ISO-8859-1"), "UTF-8");
            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 홍보글 객체 생성 및 저장
        CN_Advertising advertising = new CN_Advertising();
        advertising.setTitle(title);
        advertising.setContent(content);
        advertising.setCafeId(cafeId);
        advertising.setUserId(userId);
        advertising.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        // 광고 생성
        advertisingService.createAdvertising(advertising);
        System.out.println("Advertising created: " + advertising);

        // 광고 ID 확인
        int advertisingId = advertising.getAdvertisingId();
        if (advertisingId == 0) {
            redirectAttributes.addFlashAttribute("message", "광고 생성에 실패했습니다.");
            return "redirect:/user/home";
        }
        System.out.println("광고 id: "+advertisingId);
        // 이미지 디버그: 이미지 배열의 크기와 각각의 이미지 이름을 출력
        System.out.println("Number of images uploaded: " + images.length);
        for (MultipartFile image : images) {
            System.out.println("Image name: " + image.getOriginalFilename());
        }

        // 홍보 이미지 저장 로직
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String fileName = image.getOriginalFilename();
                    System.out.println("Original image file name: " + fileName);
                    
                    fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_"); // 파일 이름 유효한 문자로 처리
                    fileName = advertisingId + "_" + fileName; // 광고 ID 추가
                    System.out.println("Processed image file name: " + fileName);
                    
                    String savePath = session.getServletContext().getRealPath("/resources/advertisingimages/") + fileName;
                    System.out.println("Image save path: " + savePath);
                    
                    File saveFile = new File(savePath);

                    // 디렉토리 존재 여부 확인 및 생성
                    File directory = new File(saveFile.getParent());
                    if (!directory.exists()) {
                        boolean dirsCreated = directory.mkdirs();
                        System.out.println("Directories created: " + dirsCreated);
                    }

                    // 이미지 파일 저장
                    image.transferTo(saveFile);
                    System.out.println("Image saved successfully: " + saveFile.getAbsolutePath());

                    // 광고 이미지 객체 생성 및 저장
                    CN_Advertising_Image advertisingImage = new CN_Advertising_Image();
                    advertisingImage.setAdvertisingId(advertisingId);
                    advertisingImage.setImagePath("/advertisingimages/" + fileName);
                    
                    // 광고 이미지 DB 저장
                    advertisingImageService.createImage(advertisingImage);
                    System.out.println("Saved advertising image to DB: " + advertisingImage);

                } catch (IOException e) {
                    System.out.println("Error saving image: " + image.getOriginalFilename());
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("message", "이미지 저장 중 오류가 발생했습니다.");
                    return "redirect:/user/home";
                }
            } else {
                System.out.println("Image is empty or null.");
            }
        }

        redirectAttributes.addFlashAttribute("message", "홍보가 성공적으로 등록되었습니다.");
        System.out.println("Advertising added successfully.");
        return "redirect:/user/home";
    }
    //0902추가 
    @PostMapping("/deleteAdvertising")
    @ResponseBody
    public Map<String, String> deleteAdvertising(@RequestParam("advertisingId") int advertisingId) {
        Map<String, String> response = new HashMap<>();
        try {
            // 홍보에 연결된 이미지들 삭제
            List<CN_Advertising_Image> images = advertisingImageService.findImagesByAdvertisingId(advertisingId);
            for (CN_Advertising_Image image : images) {
                advertisingImageService.deleteImage(image.getAdvertisingImageId());
            }

            // 홍보글 삭제
            advertisingService.deleteAdvertising(advertisingId);
            response.put("message", "홍보글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            response.put("message", "홍보글 삭제 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return response;
    }
    //0902추가 - 등록카페 삭제
    @PostMapping("/deleteRegisteredCafe")
    @ResponseBody
    public Map<String, String> deleteRegisteredCafe(@RequestParam("cafeId") String cafeId) { // cafeId로 변경
        Map<String, String> response = new HashMap<>();
        try {
            // cafeId로 등록된 카페를 찾습니다.
            CN_Registered_Cafes registeredCafe = registeredCafesService.findRegisteredCafeByCafeId(cafeId);
            if (registeredCafe != null) {
                // 카페에 연결된 모든 홍보글 삭제
                List<CN_Advertising> advertisings = advertisingService.findAdvertisingsByCafeId(cafeId);
                for (CN_Advertising advertising : advertisings) {
                    // 홍보글에 연결된 이미지들 삭제
                    List<CN_Advertising_Image> images = advertisingImageService.findImagesByAdvertisingId(advertising.getAdvertisingId());
                    for (CN_Advertising_Image image : images) {
                        advertisingImageService.deleteImage(image.getAdvertisingImageId());
                    }

                    // 홍보글 삭제
                    advertisingService.deleteAdvertising(advertising.getAdvertisingId());
                }

                // 등록된 카페 삭제
                registeredCafesService.deleteRegisteredCafes(registeredCafe.getRegisteredId()); // 기존의 registeredId를 사용하여 삭제
                response.put("message", "등록된 카페가 성공적으로 삭제되었습니다.");
            } else {
                response.put("message", "등록된 카페를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            response.put("message", "등록된 카페 삭제 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return response;
    }
    
    //0909추가 - 프로필 변경
    @PostMapping("/updateProfileImg")
    @ResponseBody
    public Map<String, String> updateProfileImg(@RequestParam("profileImg") String profileImg) {
        Map<String, String> response = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        // 사용자 정보 가져오기
        CN_User user = userService.findUserById(userId);
        
        if (user != null) {
            // 프로필 이미지 업데이트
            user.setProfileImg(profileImg);
            userService.updateUser(user);
            response.put("message", "프로필 이미지가 성공적으로 변경되었습니다.");
        } else {
            response.put("message", "사용자를 찾을 수 없습니다.");
        }
        
        return response;
    }
    
    //0905추가
    // MainController에서 가져온 createModelAndView 메서드를 CN_UserController로 복사
    //그리고 기능을 살짝 변경, 왜냐면 home.jsp는 main폴더에 있지 않으니까.
    private ModelAndView createModelAndView(String section, Authentication auth) {
        ModelAndView modelAndView = new ModelAndView("main/index");
        //main폴더 뒤로 가기 위해 ../ 을 먼저 쓰고 user폴더로 들어가기
        modelAndView.addObject("section", "../user/" + section);  // 상대 경로로 수정
        
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

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            modelAndView.addObject("profileImg", userDetails.getProfileImg());
            modelAndView.addObject("userId", userDetails.getUsername());
            modelAndView.addObject("userName", userDetails.getUserNickname());
        } else if (auth != null) {
            modelAndView.addObject("userId", auth.getName());
        }

        return modelAndView;
    }
    
    //0905추가 - 카페 순위 계산 공식
    public static double calculateWeightedRating(double R, int v, int m, double C) {
        return (v / (double)(v + m)) * R + (m / (double)(v + m)) * C;
    }
}