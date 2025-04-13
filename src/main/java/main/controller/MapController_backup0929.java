//package main.controller;
//   //ctrl + a + shift + c
//   //아직 내 주변 검색이 없던 시절
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import advertising.model.CN_Advertising;
//import advertising.model.CN_AdvertisingDetails;
//import advertising.service.CN_AdvertisingService;
//import advertisingimage.model.CN_Advertising_Image;
//import advertisingimage.service.CN_Advertising_ImageService;
//import cafe.model.CN_Cafe;
//import cafe.model.CN_CafeDetails;
//import cafe.service.CN_CafeService;
//import bookmark.model.CN_Bookmark;
//import bookmark.service.CN_BookmarkService;
//import bookmarkcafes.model.CN_Bookmark_Cafes;
//import bookmarkcafes.service.CN_Bookmark_CafesService;
//import bookmarklikes.model.CN_Bookmark_Likes;
//import bookmarklikes.service.CN_Bookmark_LikesService;
//import login.model.CN_User;
//import login.service.CN_UserService;
//import review.model.CN_Review;
//import review.model.CN_ReviewDetails;
//import review.service.CN_ReviewService;
//import reviewimage.model.CN_Review_Image;
//import reviewimage.service.CN_Review_ImageService;
//import reviewlikes.model.CN_Review_Likes;
//import reviewlikes.service.CN_Review_LikesService;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpSession;
//
//@Controller
//@RequestMapping("/map")
//public class MapController_backup0929 {
//
//    @Autowired
//    private CN_CafeService cafeService;
//
//    @Autowired
//    private CN_BookmarkService bookmarkService;
//
//    @Autowired
//    private CN_Bookmark_CafesService bookmarkCafesService;
//
//    @Autowired
//    private CN_UserService userService;
//    
//    @Autowired
//    private CN_ReviewService reviewService;
//    
//    @Autowired
//    private CN_Review_ImageService reviewImageService;
//    
//    @Autowired
//    private CN_AdvertisingService advertisingService;
//    
//    @Autowired
//    private CN_Advertising_ImageService advertisingImageService;
//    
//    @Autowired
//    private CN_Review_LikesService reviewLikesService;
//    
//    @Autowired
//    private CN_Bookmark_LikesService bookmarkLikesService;
//    
//    //0910추가 - 북마크 제목 검색 (/mapview의 북마크 검색해와서 리스트로 담는 것과 동일해야함)
//    @PostMapping("/searchBookmarks")
//    @ResponseBody
//    public List<Map<String, Object>> searchBookmarks(@RequestParam("keyword") String keyword) {
//    	System.out.println("검색한 북마크의 키워드:"+keyword);
//        // UTF-8 인코딩 처리
//        /* 이건 오히려 이거 안해야함
//    	try {
//        	keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        */
//        List<CN_Bookmark> searchResults = bookmarkService.searchBookmarksByTitle(keyword);
//
//        List<Map<String, Object>> bookmarkList = new ArrayList<>();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInUserId = authentication != null ? authentication.getName() : null;
//
//        for (CN_Bookmark bookmark : searchResults) {
//            Map<String, Object> bookmarkData = new HashMap<>();
//            CN_User user = userService.findUserById(bookmark.getUserId());
//            bookmarkData.put("userId", user.getUserId());
//            bookmarkData.put("userName", user.getUserName());
//            bookmarkData.put("title", bookmark.getTitle());
//            bookmarkData.put("content", bookmark.getContent());
//            bookmarkData.put("bookmarkId", bookmark.getBookmarkId());
//            bookmarkData.put("likes", bookmark.getLikes());
//            bookmarkData.put("liked", loggedInUserId != null && bookmarkLikesService.findByUserIdAndBookmarkId(loggedInUserId, bookmark.getBookmarkId()) != null);
//
//            bookmarkList.add(bookmarkData);
//        }
//        System.out.println("bookmarkList:"+bookmarkList);
//        return bookmarkList;
//    }
//   
//    //0903추가 - 리뷰 좋아요 기능
//    // 리뷰 좋아요 기능
//    @PostMapping("/reviewlike")
//    @ResponseBody
//    public Map<String, Object> toggleReviewLike(@RequestParam("reviewId") int reviewId,
//                                                @RequestParam("action") String action) {
//        Map<String, Object> response = new HashMap<>();
//        
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication != null ? authentication.getName() : null;
//
//        // 로그인 체크
//        if (userId == null || "anonymousUser".equals(authentication.getPrincipal())) {
//            response.put("error", "로그인이 필요합니다.");
//            return response;
//        }
//
//        CN_Review_Likes existingLike = reviewLikesService.findByUserIdAndReviewId(userId, reviewId);
//        CN_Review review = reviewService.findReviewById(reviewId);
//
//        boolean isLiked = false; // 기본값 설정
//
//        if ("like".equals(action)) {
//            if (existingLike == null) {
//                reviewLikesService.createReviewLike(new CN_Review_Likes(0, userId, reviewId));
//                review.setLikes(review.getLikes() + 1);
//                reviewService.updateReview(review);
//                isLiked = true; // 좋아요가 눌린 상태로 변경
//            } else {
//                reviewLikesService.deleteReviewLike(existingLike.getReviewLikeId());
//                review.setLikes(review.getLikes() - 1);
//                reviewService.updateReview(review);
//                isLiked = false; // 좋아요가 취소된 상태로 변경
//            }
//        } else if ("unlike".equals(action)) {
//            if (existingLike != null) {
//                reviewLikesService.deleteReviewLike(existingLike.getReviewLikeId());
//                review.setLikes(review.getLikes() - 1);
//                reviewService.updateReview(review);
//                isLiked = false; // 좋아요가 취소된 상태로 변경
//            } else {
//                response.put("error", "취소할 좋아요가 없습니다.");
//                return response;
//            }
//        }
//
//        response.put("isLiked", isLiked); // 변경된 상태 반환
//        response.put("likes", review.getLikes());
//        response.put("error", null);
//        return response;
//    }
//
//    // 북마크 좋아요 기능
//    @PostMapping("/bookmarklike")
//    @ResponseBody
//    public Map<String, Object> toggleBookmarkLike(@RequestParam("bookmarkId") int bookmarkId,
//                                                  @RequestParam("action") String action) {
//        Map<String, Object> response = new HashMap<>();
//        
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication != null ? authentication.getName() : null;
//
//        // 로그인 체크
//        if (userId == null || "anonymousUser".equals(authentication.getPrincipal())) {
//            response.put("error", "로그인이 필요합니다.");
//            return response;
//        }
//
//        CN_Bookmark_Likes existingLike = bookmarkLikesService.findByUserIdAndBookmarkId(userId, bookmarkId);
//        CN_Bookmark bookmark = bookmarkService.findBookmarkById(bookmarkId);
//
//        boolean isLiked = false; // 기본값 설정
//
//        if ("like".equals(action)) {
//            if (existingLike == null) {
//                bookmarkLikesService.createBookmarkLike(new CN_Bookmark_Likes(0, userId, bookmarkId));
//                bookmark.setLikes(bookmark.getLikes() + 1);
//                bookmarkService.updateBookmark(bookmark);
//                isLiked = true; // 좋아요가 눌린 상태로 변경
//            } else {
//                bookmarkLikesService.deleteBookmarkLike(existingLike.getBookmarkLikeId());
//                bookmark.setLikes(bookmark.getLikes() - 1);
//                bookmarkService.updateBookmark(bookmark);
//                isLiked = false; // 좋아요가 취소된 상태로 변경
//            }
//        } else if ("unlike".equals(action)) {
//            if (existingLike != null) {
//                bookmarkLikesService.deleteBookmarkLike(existingLike.getBookmarkLikeId());
//                bookmark.setLikes(bookmark.getLikes() - 1);
//                bookmarkService.updateBookmark(bookmark);
//                isLiked = false; // 좋아요가 취소된 상태로 변경
//            } else {
//                response.put("error", "취소할 좋아요가 없습니다.");
//                return response;
//            }
//        }
//
//        response.put("isLiked", isLiked); // 변경된 상태 반환
//        response.put("likes", bookmark.getLikes());
//        response.put("error", null);
//        return response;
//    }
//
//    //지도 보이기
//    //0911 추가 - 지도 페이지 로드시 서울 역 주변 1km 내의 카페 검색해 보여주기
//    @GetMapping("/mapview")
//    public ModelAndView showMapView(HttpSession session) {
//        System.out.println("showMapView method start");
//
//        List<CN_Bookmark> publicBookmarks = bookmarkService.findPublicBookmarks();
//        List<Map<String, Object>> bookmarkList = new ArrayList<>();
//
//        // 현재 로그인 사용자 확인
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInUserId = authentication != null ? authentication.getName() : null;
//        //북마크 리스트는 페이지가 열릴때부터 이미 불러와져있음
//        for (CN_Bookmark bookmark : publicBookmarks) {
//            Map<String, Object> bookmarkData = new HashMap<>();
//            CN_User user = userService.findUserById(bookmark.getUserId());
//            bookmarkData.put("userId", user.getUserId());
//            bookmarkData.put("userName", user.getUserName());
//            bookmarkData.put("title", bookmark.getTitle());
//            bookmarkData.put("content", bookmark.getContent());
//            bookmarkData.put("bookmarkId", bookmark.getBookmarkId());
//            bookmarkData.put("likes", bookmark.getLikes());
//            // 로그인한 사용자가 이 북마크를 좋아요 했는지 확인
//            bookmarkData.put("liked", loggedInUserId != null && bookmarkLikesService.findByUserIdAndBookmarkId(loggedInUserId, bookmark.getBookmarkId()) != null);
//            
//            bookmarkList.add(bookmarkData);
//        }
//        
//        ModelAndView modelAndView = new ModelAndView("map/mapview");
//        modelAndView.addObject("bookmarkList", bookmarkList);
//        //로그인 여부로 하트를 어떻게 보일지 판별하기 위해서
//        //로그인 안한 사람은 하트가 개수는 보여도 무조건 흰색이어야 하고, 누를 수도 없어야 해
//        modelAndView.addObject("loggedInUserId", loggedInUserId); // 로그인 상태 추가
//        
//        // 세션에서 검색 결과 확인
//        @SuppressWarnings("unchecked")
//        List<CN_CafeDetails> searchResults = (List<CN_CafeDetails>) session.getAttribute("searchResults");
//
//        if (searchResults != null && !searchResults.isEmpty()) {
//            // 검색 결과가 있으면 검색 결과를 보여줌
//            modelAndView.addObject("cafeDetailsList", searchResults);
//            session.removeAttribute("searchResults"); // 검색 결과를 사용한 후에는 세션에서 제거
//        } else {
//            // 검색 결과가 없으면 서울역 주변 카페를 검색
//            double x = 126.972341; // 서울역 경도
//            double y = 37.556008;  // 서울역 위도
//            int radius = 1000;     // 1km 반경
//            List<CN_CafeDetails> cafeDetailsList = searchCafesByLocation(x, y, radius);
//            modelAndView.addObject("cafeDetailsList", cafeDetailsList);
//        }
//        
//        modelAndView.getModelMap().addAttribute("contentType", "text/html; charset=UTF-8");
//        System.out.println("showMapView method return");
//        return modelAndView;
//    }
//    // Kakao API를 사용하여 서울역 주변 카페 검색 및 상세 정보 조회
//    private List<CN_CafeDetails> searchCafesByLocation(double x, double y, int radius) {
//        List<CN_CafeDetails> allCafeDetails = new ArrayList<>();
//        try {
//            String apiURL = "https://dapi.kakao.com/v2/local/search/category.json";
//            String query = "?category_group_code=CE7&x=" + x + "&y=" + y + "&radius=" + radius;
//            String fullURL = apiURL + query;
//            
//            URL url = new URL(fullURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa");
//            
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                
//                ObjectMapper mapper = new ObjectMapper();
//                Map<String, Object> resultMap = mapper.readValue(response.toString(), new TypeReference<Map<String, Object>>() {});
//                List<Map<String, Object>> documents = (List<Map<String, Object>>) resultMap.get("documents");
//
//                for (Map<String, Object> cafeData : documents) {
//                    CN_CafeDetails cafeDetails = new CN_CafeDetails();
//                    cafeDetails.setCafeId((String) cafeData.get("id"));
//                    cafeDetails.setX((String) cafeData.get("x"));
//                    cafeDetails.setY((String) cafeData.get("y"));
//                    cafeDetails.setPlaceName((String) cafeData.get("place_name"));
//                    cafeDetails.setAddressName((String) cafeData.get("address_name"));
//                    cafeDetails.setRoadAddressName((String) cafeData.get("road_address_name"));
//                    cafeDetails.setPhone((String) cafeData.get("phone"));
//                    cafeDetails.setPlaceUrl((String) cafeData.get("place_url"));
//
//                    // 각 카페에 대한 리뷰 및 홍보 정보 추가
//                    addCafeAdditionalDetails(cafeDetails);
//                    allCafeDetails.add(cafeDetails);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return allCafeDetails;
//    }
//    // 각 카페에 대한 추가 정보를 가져와서 설정하는 메서드
//    private void addCafeAdditionalDetails(CN_CafeDetails cafeDetails) {
//        // 해당 카페의 리뷰 목록을 가져옴
//        List<CN_Review> reviews = reviewService.findReviewsByCafeId(cafeDetails.getCafeId());
//        List<CN_ReviewDetails> reviewDetailsList = new ArrayList<>();
//
//        for (CN_Review review : reviews) {
//            // 각 리뷰에 대한 이미지 목록을 가져옴
//            List<CN_Review_Image> reviewImages = reviewImageService.findImagesByReviewId(review.getReviewId());
//
//            // 리뷰와 이미지를 포함한 CN_ReviewDetails 객체 생성
//            CN_ReviewDetails reviewDetails = new CN_ReviewDetails(review, reviewImages);
//            reviewDetailsList.add(reviewDetails);
//        }
//        // 카페 세부 정보에 리뷰 목록 추가
//        cafeDetails.setReviews(reviewDetailsList);
//
//        // 해당 카페의 홍보 목록을 가져옴
//        List<CN_Advertising> advertisings = advertisingService.findAdvertisingsByCafeId(cafeDetails.getCafeId());
//        List<CN_AdvertisingDetails> advertisingDetailsList = new ArrayList<>();
//
//        for (CN_Advertising advertising : advertisings) {
//            List<CN_Advertising_Image> advertisingImages = advertisingImageService.findImagesByAdvertisingId(advertising.getAdvertisingId());
//            CN_AdvertisingDetails advertisingDetails = new CN_AdvertisingDetails(advertising, advertisingImages);
//            advertisingDetailsList.add(advertisingDetails);
//        }
//
//        // 카페 세부 정보에 홍보 목록 추가
//        cafeDetails.setAdvertisings(advertisingDetailsList);
//    }
//    
//    //마커를 생성할 때, 미리 카페 정보를 넣어놓는데, 그거는 검색이 실행 됐을 때 생성됨
//    //미리 그냥 내가 선택할 수 있는 마커들의 모든 홍보와 리뷰를 불러오고 사용함
//    //0911-기본 mapview url로 이동하면 서울역 1km 내의 카페가 검색되는데, 이거랑 겹치지 않도록 세션을 사용함
//    @PostMapping("/searchmap")
//    public String searchMap(@RequestParam("keyword") String keyword, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
//        System.out.println("searchMap method start with keyword: " + keyword);
//        keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
//        System.out.println("UTF-8 encoding keyword: " + keyword);
//
//        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
//        String query = URLEncoder.encode(keyword, "UTF-8");
//        String category = "CE7";
//        int page = 1;
//        int size = 15;
//        int totalResults = 45;
//
//        List<CN_CafeDetails> allCafeDetails = new ArrayList<>();
//
//        while (allCafeDetails.size() < totalResults) {
//            String fullURL = apiURL + "?query=" + query + "&category_group_code=" + category + "&page=" + page;
//            URL url = new URL(fullURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa");
//
//            String data = "";
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                data = response.toString();
//            } else {
//                System.out.println("GET request failed: " + responseCode);
//                break;
//            }
//
//            int first = data.indexOf('[');
//            int end = data.indexOf(']');
//            if (first != -1 && end != -1) {
//                data = data.substring(first, end + 1);
//            }
//
//            //카페의 디테일 리스트를 생성
//            ObjectMapper mapper = new ObjectMapper();
//            List<CN_CafeDetails> cafeDetailsList = mapper.readValue(data, new TypeReference<List<CN_CafeDetails>>() {});
//            
//            // 리뷰와 홍보 정보를 추가
//            for (CN_CafeDetails cafeDetails : cafeDetailsList) {
//                addCafeAdditionalDetails(cafeDetails);
//            }
//            
//            allCafeDetails.addAll(cafeDetailsList);
//            if (cafeDetailsList.size() < size) {
//                break;
//            }
//            page++;
//        }
//
//        List<CN_CafeDetails> finalCafeDetails = allCafeDetails.size() > totalResults ? allCafeDetails.subList(0, totalResults) : allCafeDetails;
//
//        // 검색 결과를 세션에 저장
//        session.setAttribute("searchResults", finalCafeDetails);
//
//        System.out.println("searchMap method return");
//        return "redirect:/map/mapview"; // 검색 결과를 가지고 mapview로 이동
//    }
//
//    /* 0910 이전 옛날의 북마크 검색 방식 
//    @PostMapping("/filterByBookmark")
//    public String filterByBookmark(@RequestParam("bookmarkId") int bookmarkId, RedirectAttributes redirectAttributes) {
//        System.out.println("filterByBookmark method start with bookmarkId: " + bookmarkId);
//
//        List<CN_Bookmark_Cafes> bookmarkCafesList = bookmarkCafesService.findCafesByBookmarkId(bookmarkId);
//        System.out.println("Found " + bookmarkCafesList.size() + " cafes in the bookmark.");
//
//        List<CN_CafeDetails> allCafeDetails = new ArrayList<>();
//
//        for (CN_Bookmark_Cafes bookmarkCafes : bookmarkCafesList) {
//            CN_Cafe cafe = cafeService.findCafeById(bookmarkCafes.getCafeId());
//            if (cafe != null) {
//                System.out.println("Processing cafe: " + cafe.getPlaceName() + " with ID: " + cafe.getCafeId());
//                try {
//                    String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
//                    String query = URLEncoder.encode(cafe.getPlaceName(), "UTF-8");
//                    String fullURL = apiURL + "?query=" + query + "&category_group_code=CE7";
//                    System.out.println("Sending request to Kakao API with URL: " + fullURL);
//
//                    URL url = new URL(fullURL);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa");
//
//                    int responseCode = conn.getResponseCode();
//                    System.out.println("Kakao API response code: " + responseCode);
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                        String inputLine;
//                        StringBuilder response = new StringBuilder();
//                        while ((inputLine = in.readLine()) != null) {
//                            response.append(inputLine);
//                        }
//                        in.close();
//                        String data = response.toString();
//                        System.out.println("Received data from Kakao API: " + data);
//
//                        ObjectMapper mapper = new ObjectMapper();
//                        Map<String, Object> resultMap = mapper.readValue(data, new TypeReference<Map<String, Object>>() {});
//                        List<Map<String, Object>> documents = (List<Map<String, Object>>) resultMap.get("documents");
//
//                        for (Map<String, Object> cafeData : documents) {
//                            if (cafe.getCafeId().equals(cafeData.get("id"))) {
//                                System.out.println("Found matching cafe ID in Kakao API response.");
//                                CN_CafeDetails cafeDetails = new CN_CafeDetails();
//                                cafeDetails.setCafeId(cafe.getCafeId());
//                                cafeDetails.setX((String) cafeData.get("x"));
//                                cafeDetails.setY((String) cafeData.get("y"));
//                                cafeDetails.setPlaceName((String) cafeData.get("place_name"));
//                                cafeDetails.setAddressName((String) cafeData.get("address_name"));
//                                cafeDetails.setRoadAddressName((String) cafeData.get("road_address_name"));
//                                cafeDetails.setPhone((String) cafeData.get("phone"));
//                                cafeDetails.setPlaceUrl((String) cafeData.get("place_url"));
//                                allCafeDetails.add(cafeDetails);
//                                break; // 일치하는 첫 번째 카페 정보를 찾으면 루프 종료
//                            }
//                        }
//                    } else {
//                        System.out.println("Kakao API request failed: " + responseCode);
//                    }
//                } catch (IOException e) {
//                    System.out.println("Error occurred while processing cafe: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            } else {
//                System.out.println("No cafe found with ID: " + bookmarkCafes.getCafeId());
//            }
//        }
//
//        redirectAttributes.addFlashAttribute("cafeDetailsList", allCafeDetails);
//        System.out.println("filterByBookmark method end with " + allCafeDetails.size() + " cafes.");
//        return "redirect:/map/mapview";
//    }
//    */
//    //0905추가 - 북마크 상세보기 페이지로 이동 (위의 메서드 활용)
//    //0910추가 - 북마크의 상세정보도 포함하여 전송
//    // 북마크 상세 정보를 가져오는 API (POST 요청)
//    @PostMapping("/bookmarkdetails")
//    @ResponseBody
//    public Map<String, Object> showMapViewForBookmark(@RequestParam("bookmarkId") int bookmarkId) {
//        Map<String, Object> response = new HashMap<>();
//        // 현재 로그인 사용자 확인
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInUserId = authentication != null ? authentication.getName() : null;
//
//        try {
//            CN_Bookmark bookmark = bookmarkService.findBookmarkById(bookmarkId);
//            CN_User user = userService.findUserById(bookmark.getUserId()); // 작성자 정보 가져오기
//
//            List<CN_Bookmark_Cafes> bookmarkCafesList = bookmarkCafesService.findCafesByBookmarkId(bookmarkId);
//            List<CN_CafeDetails> allCafeDetails = new ArrayList<>();
//            
//            // 북마크에 저장된 카페가 없는 경우
//            if (bookmarkCafesList.isEmpty()) {
//                response.put("error", "noCafes"); // 에러 메시지 전송
//                return response;
//            }
//
//            for (CN_Bookmark_Cafes bookmarkCafes : bookmarkCafesList) {
//                CN_Cafe cafe = cafeService.findCafeById(bookmarkCafes.getCafeId());
//                if (cafe != null) {
//                    try {
//                        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
//                        String query = URLEncoder.encode(cafe.getPlaceName(), "UTF-8");
//                        String fullURL = apiURL + "?query=" + query + "&category_group_code=CE7";
//                        URL url = new URL(fullURL);
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestMethod("GET");
//                        conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa");
//
//                        int responseCode = conn.getResponseCode();
//                        if (responseCode == HttpURLConnection.HTTP_OK) {
//                            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                            String inputLine;
//                            StringBuilder apiResponse = new StringBuilder();
//                            while ((inputLine = in.readLine()) != null) {
//                                apiResponse.append(inputLine);
//                            }
//                            in.close();
//                            String data = apiResponse.toString();
//
//                            ObjectMapper mapper = new ObjectMapper();
//                            Map<String, Object> resultMap = mapper.readValue(data, new TypeReference<Map<String, Object>>() {});
//                            List<Map<String, Object>> documents = (List<Map<String, Object>>) resultMap.get("documents");
//
//                            for (Map<String, Object> cafeData : documents) {
//                                if (cafe.getCafeId().equals(cafeData.get("id"))) {
//                                    CN_CafeDetails cafeDetails = new CN_CafeDetails();
//                                    cafeDetails.setCafeId(cafe.getCafeId());
//                                    cafeDetails.setX((String) cafeData.get("x"));
//                                    cafeDetails.setY((String) cafeData.get("y"));
//                                    cafeDetails.setPlaceName((String) cafeData.get("place_name"));
//                                    cafeDetails.setAddressName((String) cafeData.get("address_name"));
//                                    cafeDetails.setRoadAddressName((String) cafeData.get("road_address_name"));
//                                    cafeDetails.setPhone((String) cafeData.get("phone"));
//                                    cafeDetails.setPlaceUrl((String) cafeData.get("place_url"));
//                                    allCafeDetails.add(cafeDetails);
//                                    break;
//                                }
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            // 북마크와 카페 정보가 담긴 JSON 응답 데이터 반환
//            response.put("bookmarkDetails", bookmark);
//            response.put("userName", user.getUserName());
//            response.put("cafeDetailsList", allCafeDetails);
//            System.out.println("cafeDetailsList:\n"+allCafeDetails);
//            //(북마크 상세 정보의 좋아요)로그인 여부로 하트를 어떻게 보일지 판별하기 위해서
//            //로그인 안한 사람은 하트가 개수는 보여도 무조건 흰색이어야 하고, 누를 수도 없어야 해
//            response.put("loggedInUserId", loggedInUserId); // 로그인 사용자 ID를 추가
//            
//            // 로그인한 사용자가 해당 북마크를 좋아요 했는지 확인하는 로직 추가
//            boolean isLiked = false;
//            if (loggedInUserId != null) {
//                CN_Bookmark_Likes existingLike = bookmarkLikesService.findByUserIdAndBookmarkId(loggedInUserId, bookmarkId);
//                isLiked = (existingLike != null);
//            }
//            // 좋아요 상태를 응답에 추가
//            response.put("liked", isLiked);
//            System.out.println("liked 출력"+isLiked);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", "북마크 정보를 가져오는 중 오류가 발생했습니다.");
//        }
//
//        return response;
//    }
//    //외부페이지 접근용
//    @PostMapping("/bookmarkdetails2")
//    public String showMapViewForBookmark2(@RequestParam("bookmarkId") int bookmarkId, RedirectAttributes redirectAttributes) {
//        // bookmarkId를 redirectAttributes에 추가하여 리다이렉션 중 데이터 전달
//        redirectAttributes.addFlashAttribute("bookmarkdetails2_bookmarkId", bookmarkId);
//        // mapview.jsp로 리디렉션
//        return "redirect:/map/mapview";
//    }
//    
//    @PostMapping("/bookmarkcreate")
//    public String createBookmark(@RequestParam("title") String title,
//                                 @RequestParam("content") String content,
//                                 @RequestParam(value = "isPublic", required = false, defaultValue = "off") String isPublicStr, 
//                                 @RequestParam("selectedCafes") String selectedCafesJson,
//                                 HttpSession session, RedirectAttributes redirectAttributes) {
//        System.out.println("createBookmark method start");
//
//        // UTF-8 인코딩 처리
//        try {
//            title = new String(title.getBytes("ISO-8859-1"), "UTF-8");
//            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        // SecurityContextHolder를 사용하여 현재 인증된 사용자 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//        System.out.println("User ID from security context: " + userId);
//
//        if (userId == null || "anonymousUser".equals(userId)) {
//            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
//            System.out.println("User not logged in. Redirecting to map view.");
//            return "redirect:/map/mapview";
//        }
//
//        System.out.println("Received parameters - title: " + title + ", content: " + content + ", isPublic: " + isPublicStr + ", selectedCafes: " + selectedCafesJson);
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            List<Map<String, String>> cafes = mapper.readValue(selectedCafesJson, new TypeReference<List<Map<String, String>>>() {});
//            System.out.println("Parsed selected cafes JSON: " + cafes);
//
//            // isPublic 변환 로직
//            int isPublic = "on".equals(isPublicStr) ? 1 : 0;
//            System.out.println("Converted isPublic value: " + isPublic);
//
//            // 1. 북마크 생성
//            CN_Bookmark newBookmark = new CN_Bookmark(0, userId, title, content, 0, isPublic);
//            System.out.println("Creating new bookmark: " + newBookmark);
//            bookmarkService.createBookmark(newBookmark);
//
//            // 새로 생성한 북마크 ID 가져오기
//            CN_Bookmark latestBookmark = bookmarkService.findLatestBookmarkByUserId(userId);
//            int newBookmarkId = latestBookmark.getBookmarkId();
//            System.out.println("Latest bookmark ID for user: " + newBookmarkId);
//
//            // 2. 카페 존재 여부 확인 및 추가, 그리고 북마크-카페 매핑 추가
//            for (Map<String, String> cafeData : cafes) {
//                String cafeId = cafeData.get("cafeId");
//                String x = cafeData.get("x");
//                String y = cafeData.get("y");
//                String placeName = cafeData.get("placeName");
//                placeName = new String(placeName.getBytes("ISO-8859-1"), "UTF-8");
//                System.out.println("Processing cafe - ID: " + cafeId + ", X: " + x + ", Y: " + y + ", Place Name: " + placeName);
//
//                CN_Cafe existingCafe = cafeService.findCafeById(cafeId);
//                
//                if (existingCafe == null) {
//                    // 카페가 존재하지 않으면 추가
//                    CN_Cafe newCafe = new CN_Cafe(cafeId, x, y, placeName);
//                    cafeService.createCafe(newCafe);
//                    System.out.println("Created new cafe: " + newCafe);
//                } else {
//                    System.out.println("Cafe already exists: " + existingCafe);
//                }
//
//                // 북마크-카페 매핑 추가
//                CN_Bookmark_Cafes bookmarkCafes = new CN_Bookmark_Cafes();
//                bookmarkCafes.setBookmarkId(newBookmarkId);
//                bookmarkCafes.setCafeId(cafeId);
//                bookmarkCafesService.createBookmarkCafes(bookmarkCafes);
//                System.out.println("Added bookmark-cafe mapping: " + bookmarkCafes);
//            }
//
//            redirectAttributes.addFlashAttribute("message", "북마크가 생성되었습니다.");
//            System.out.println("Bookmark creation successful. Redirecting to map view.");
//            return "redirect:/map/mapview";
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("message", "북마크 생성 중 오류가 발생했습니다.");
//            System.out.println("Exception occurred during bookmark creation.");
//            return "redirect:/map/mapview";
//        } finally {
//            System.out.println("createBookmark method end");
//        }
//    }
//    //0905 수정 - 리뷰쓰기
//    @PostMapping("/addreview")
//    @ResponseBody // JSON 형식으로 응답 반환
//    public Map<String, Object> addReview(@RequestParam("title") String title,
//                                         @RequestParam("content") String content,
//                                         @RequestParam("rating") int rating,
//                                         @RequestParam("cafeId") String cafeId,
//                                         @RequestParam("cafeX") String cafeX,
//                                         @RequestParam("cafeY") String cafeY,
//                                         @RequestParam("cafeName") String cafeName,
//                                         @RequestParam("images") MultipartFile[] images,
//                                         HttpSession session) {
//        // 응답을 저장할 Map 객체 생성
//        Map<String, Object> response = new HashMap<>();
//        System.out.println("addReview method start");
//
//        // 사용자 확인
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//        if (userId == null || "anonymousUser".equals(userId)) {
//            response.put("error", "로그인이 필요합니다.");
//            System.out.println("User not logged in.");
//            return response;
//        }
//
//        // UTF-8 변환 처리
//        try {
//            title = new String(title.getBytes("ISO-8859-1"), "UTF-8");
//            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
//            cafeName = new String(cafeName.getBytes("ISO-8859-1"), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        // 디버깅: 요청 파라미터 로그 출력
//        System.out.println("Received parameters - title: " + title + ", content: " + content + ", rating: " + rating
//                + ", cafeId: " + cafeId + ", cafeX: " + cafeX + ", cafeY: " + cafeY + ", cafeName: " + cafeName);
//        for (MultipartFile image : images) {
//            System.out.println("Image name: " + image.getOriginalFilename() + ", Size: " + image.getSize());
//        }
//
//        try {
//            // 1. 카페가 저장돼있는지 확인하고, 없다면 추가한다.
//            CN_Cafe existingCafe = cafeService.findCafeById(cafeId);
//            if (existingCafe == null) {
//                CN_Cafe newCafe = new CN_Cafe(cafeId, cafeX, cafeY, cafeName);
//                cafeService.createCafe(newCafe);
//                System.out.println("Created new cafe: " + newCafe);
//            } else {
//                System.out.println("Cafe already exists: " + existingCafe);
//            }
//
//            // 2. 리뷰 저장
//            CN_Review review = new CN_Review();
//            review.setTitle(title);
//            review.setContent(content);
//            review.setCafeId(cafeId);
//            review.setUserId(userId);
//            review.setRating(rating);
//            review.setLikes(0); // 새로운 리뷰의 좋아요 수는 0으로 초기화
//            review.setCreatedDate(new Timestamp(System.currentTimeMillis())); // 현재 시간을 설정
//
//            reviewService.createReview(review);
//            System.out.println("Created new review: " + review);
//            int reviewId = review.getReviewId(); // 미리 받아두기
//
//            // 3. 리뷰 이미지 저장
//            for (MultipartFile image : images) {
//                if (!image.isEmpty()) {
//                    try {
//                    	//리뷰 하나를 올릴 때 여러개의 사진을 올릴 수 있는데, 다른 리뷰와 사진명이 같아도 고유할 수 있도록
//                    	//사진이름 앞에 리뷰 id를 앞에 입력하여 저장하게 하자.
//                    	//같은 이름의 사진을 한번에 올릴 수는 없을테니 그냥 리뷰 아이디를 쓰면 될 거 같다!
//                        String fileName = image.getOriginalFilename();
//                        // 파일 이름에 유효한 문자가 아닌 경우를 대비하여 파일 이름을 처리합니다.
//                        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
//                        // 파일 이름 앞에 "reviewId_" 추가
//                        fileName = reviewId+"_"+fileName;
//                        //저장할 위치 (로컬 프로젝트 경로에 저장되지 않고 그 프로젝트가 있는 워크스페이스의....
//                        //D:\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\프로젝트명\resources\reviewimages
//                        //에 저장된다. workspace의 위치또한 내가 설정한 것에 따라 바뀔 수 있음.
//                        String savePath = session.getServletContext().getRealPath("/resources/reviewimages/") + fileName;
//                        File saveFile = new File(savePath);
//
//                        // 디렉토리 존재 여부 확인 및 생성
//                        File directory = new File(saveFile.getParent());
//                        if (!directory.exists()) {
//                            directory.mkdirs();
//                        }
//
//                        // 파일을 저장합니다.
//                        image.transferTo(saveFile);
//
//                        // 리뷰 이미지 객체 생성 및 저장
//                        CN_Review_Image reviewImage = new CN_Review_Image();
//                        reviewImage.setReviewId(review.getReviewId());
//                        //dispatcher-servlet.xml에서 <mvc:resources location="/resources/" mapping="/**"/> 
//                        //리소스 맵핑 해줬기에 resources/reviewimages/... 안의 사진들은 그냥
//                        //reviewimages/... 라고 경로를 쓰면 불러와짐
//                        //근데 그냥 /reviewimages/ 해라. reviewimages/ 은 상대 경로라 옆에 추가 경로 있으면 그거에 추가돼서 안불러와져
//                        reviewImage.setImagePath("/reviewimages/" + fileName);
//                        reviewImageService.createReviewImage(reviewImage);
//                        System.out.println("Saved review image: " + reviewImage);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        System.out.println("Failed to save image: " + image.getOriginalFilename());
//                        response.put("success", false);
//                        response.put("error", "이미지 저장 중 오류가 발생했습니다.");
//                        return response;
//                    }
//                }
//            }
//            response.put("success", true);
//            response.put("message", "리뷰가 성공적으로 등록되었습니다.");
//            System.out.println("Review added successfully.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("success", false);
//            response.put("error", "리뷰 등록 중 오류가 발생했습니다.");
//            System.out.println("Exception occurred while adding review.");
//        } finally {
//            System.out.println("addReview method end");
//        }
//
//        return response;
//    }
//    //리뷰보이기 메서드("/getCafeReviews")가 삭제되고 아래로 통합됨
//    //0902추가 - 해당 카페에 추가된 모든 이미지(홍보, 리뷰의)를 작성일 순서별로 보이기
//    //0903추가 - 해당 카페에 추가된 리뷰의 좋아요 좋아요 정보 전송
//    @GetMapping("/getCafeMedia")
//    @ResponseBody
//    public List<Map<String, Object>> getCafeMedia(@RequestParam("cafeId") String cafeId) {
//    	System.out.println("getCafeMedia method start with cafeId: " + cafeId);
//
//        List<Map<String, Object>> mediaDetails = new ArrayList<>();
//
//        // 현재 로그인 사용자 확인
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInUserId = authentication != null ? authentication.getName() : null;
//        
//        // 날짜 형식 지정 (ISO 8601 형식으로 변환)
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//
//        // 리뷰 가져오기
//        List<CN_Review> reviews = reviewService.findReviewsByCafeId(cafeId);
//        for (CN_Review review : reviews) {
//            CN_User user = userService.findUserById(review.getUserId());
//            List<CN_Review_Image> reviewImages = reviewImageService.findImagesByReviewId(review.getReviewId());
//
//            Map<String, Object> reviewData = new HashMap<>();
//            reviewData.put("type", "review");
//            reviewData.put("title", review.getTitle());
//            reviewData.put("content", review.getContent());
//            reviewData.put("rating", review.getRating()); // 별점 추가
//            reviewData.put("likes", review.getLikes()); // 좋아요 추가
//            reviewData.put("createdDate", sdf.format(review.getCreatedDate())); // 문자열로 변환된 날짜
//            reviewData.put("userName", user.getUserName());
//            reviewData.put("userId", user.getUserId());
//            reviewData.put("reviewId", review.getReviewId()); // 리뷰 ID 추가
//            // 로그인한 사용자가 이 리뷰를 좋아요 했는지 확인
//            reviewData.put("liked", loggedInUserId != null && reviewLikesService.findByUserIdAndReviewId(loggedInUserId, review.getReviewId()) != null);
//
//            List<String> imagePaths = reviewImages.stream()
//                .map(CN_Review_Image::getImagePath)
//                .collect(Collectors.toList());
//            reviewData.put("images", imagePaths); // 이미지 목록 추가
//
//            mediaDetails.add(reviewData);
//        }
//
//        // 홍보 가져오기
//        List<CN_Advertising> advertisings = advertisingService.findAdvertisingsByCafeId(cafeId);
//        for (CN_Advertising advertising : advertisings) {
//            CN_User user = userService.findUserById(advertising.getUserId());
//            List<CN_Advertising_Image> advertisingImages = advertisingImageService.findImagesByAdvertisingId(advertising.getAdvertisingId());
//
//            Map<String, Object> advertisingData = new HashMap<>();
//            advertisingData.put("type", "advertising");
//            advertisingData.put("title", advertising.getTitle());
//            advertisingData.put("content", advertising.getContent());
//            advertisingData.put("createdDate", sdf.format(advertising.getCreatedDate())); // 문자열로 변환된 날짜
//            advertisingData.put("userName", user.getUserName());
//            advertisingData.put("userId", user.getUserId());
//
//            List<String> imagePaths = advertisingImages.stream()
//                .map(CN_Advertising_Image::getImagePath)
//                .collect(Collectors.toList());
//            advertisingData.put("images", imagePaths); // 이미지 목록 추가
//
//            mediaDetails.add(advertisingData);
//        }
//
//        // 생성일을 기준으로 이미지 정렬 (최신순)
//        mediaDetails.sort((m1, m2) -> ((String) m2.get("createdDate")).compareTo((String) m1.get("createdDate")));
//
//        System.out.println("getCafeMedia method return " + mediaDetails.size() + " media items.");
//        return mediaDetails;
//    }
//    
//}
