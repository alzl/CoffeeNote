package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cafe.model.CN_Cafe;
import cafe.service.CN_CafeService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private CN_CafeService cafeService;

    // 카카오 API를 호출하여 카페 데이터를 가져와서 처리하는 메서드
    @GetMapping("/testmap")
    public ModelAndView showMap() throws IOException {
        System.out.println("showMap method start");

        ModelAndView modelAndView = new ModelAndView("test/testmap");

        // 카카오 API URL과 쿼리 설정
        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String query = URLEncoder.encode("상봉 카페", "UTF-8");
        //String query = URLEncoder.encode("도봉구 카페", "UTF-8");
        String fullURL = apiURL + "?query=" + query;

        // 카카오 API에 요청을 보내기 위한 HTTP 연결 설정
        URL url = new URL(fullURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "KakaoAK 6f798491fed9e702ef07d3a1fd44aaff");

        String data = "";

        // 응답 처리
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); // UTF-8로 인코딩 설정
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            data = response.toString();
        } else {
            System.out.println("GET request failed: " + responseCode);
        }

        // 응답 데이터에서 유효한 JSON 배열만 추출
        int first = data.indexOf('[');
        int end = data.indexOf(']');
        if (first != -1 && end != -1) {
            data = data.substring(first, end + 1);
        }

        // JSON 데이터를 List<CN_Cafe>로 변환
        ObjectMapper mapper = new ObjectMapper();
        List<CN_Cafe> cafeList = mapper.readValue(data, new TypeReference<List<CN_Cafe>>() {});

        // 디버그용 카페 리스트 출력
        cafeList.forEach(cafe -> System.out.println(cafe));

        // 모델에 카페 리스트 추가
        modelAndView.addObject("cafeList", cafeList);
        
        // Content-Type을 UTF-8로 설정
        modelAndView.getModelMap().addAttribute("contentType", "text/html; charset=UTF-8");

        System.out.println("showMap method return");
        return modelAndView;
    }
    
    // 서울역 중심으로 기본 지도를 표시하는 메서드
    @GetMapping("/testmap2")
    public ModelAndView showSeoulStationMap() {
        System.out.println("showSeoulStationMap method start");

        ModelAndView modelAndView = new ModelAndView("test/testmap2");
        // Content-Type을 UTF-8로 설정
        modelAndView.getModelMap().addAttribute("contentType", "text/html; charset=UTF-8");
        
        System.out.println("showSeoulStationMap method return");
        return modelAndView;
    }
    
    // 사용자가 입력한 키워드로 카페를 검색하고 결과를 표시하는 메서드
    @PostMapping("/searchmap")
    public ModelAndView searchMap(@RequestParam("keyword") String keyword) throws IOException {
        System.out.println("searchMap method start with keyword: " + keyword);
        
        keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
        System.out.println("UTF-8 encoding keyword: " + keyword);
        
        ModelAndView modelAndView = new ModelAndView("test/testmap2");

        // 카카오 API URL과 쿼리 설정
        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String query = URLEncoder.encode(keyword, "UTF-8");
        String fullURL = apiURL + "?query=" + query;

        // 카카오 API에 요청을 보내기 위한 HTTP 연결 설정
        URL url = new URL(fullURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "KakaoAK 6f798491fed9e702ef07d3a1fd44aaff");

        String data = "";

        // 응답 처리
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
        }

        // 응답 데이터에서 유효한 JSON 배열만 추출
        int first = data.indexOf('[');
        int end = data.indexOf(']');
        if (first != -1 && end != -1) {
            data = data.substring(first, end + 1);
        }

        // JSON 데이터를 List<CN_Cafe>로 변환
        ObjectMapper mapper = new ObjectMapper();
        List<CN_Cafe> cafeList = mapper.readValue(data, new TypeReference<List<CN_Cafe>>() {});

        // 모델에 카페 리스트 추가
        modelAndView.addObject("cafeList", cafeList);

        System.out.println("searchMap method return");
        return modelAndView;
    }

    // testmap3.jsp와 연결된 메서드 추가
    @GetMapping("/testmap3")
    public ModelAndView showSeoulStationMapForTestMap3() {
        System.out.println("showSeoulStationMapForTestMap3 method start");

        ModelAndView modelAndView = new ModelAndView("test/testmap3");
        // Content-Type을 UTF-8로 설정
        modelAndView.getModelMap().addAttribute("contentType", "text/html; charset=UTF-8");
        
        System.out.println("showSeoulStationMapForTestMap3 method return");
        return modelAndView;
    }

    @PostMapping("/searchmap2")
    public ModelAndView searchMapForTestMap3(@RequestParam("keyword") String keyword) throws IOException {
        System.out.println("searchMapForTestMap3 method start with keyword: " + keyword);
        
        keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
        System.out.println("UTF-8 encoding keyword: " + keyword);
        
        ModelAndView modelAndView = new ModelAndView("test/testmap3");

        // 카카오 API URL과 쿼리 설정, 카페 카테고리 그룹 코드 'CE7'을 포함
        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String query = URLEncoder.encode(keyword, "UTF-8");
        String category = "CE7";
        int page = 1;  // 첫 페이지부터 시작
        int size = 15; // 한 번에 가져올 최대 갯수
        int totalResults = 45; // 가져올 총 결과 수 (예: 45개의 결과를 원할 경우)
        
        List<CN_Cafe> allCafes = new ArrayList<>();  // 모든 카페 결과를 담을 리스트

        while (allCafes.size() < totalResults) {
            String fullURL = apiURL + "?query=" + query + "&category_group_code=" + category + "&page=" + page;

            // 카카오 API에 요청을 보내기 위한 HTTP 연결 설정
            URL url = new URL(fullURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK d62ba25ad3592252ed507300e683b0aa");

            String data = "";

            // 응답 처리
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

            // 응답 데이터에서 유효한 JSON 배열만 추출
            int first = data.indexOf('[');
            int end = data.indexOf(']');
            if (first != -1 && end != -1) {
                data = data.substring(first, end + 1);
            }

            // JSON 데이터를 List<CN_Cafe>로 변환
            ObjectMapper mapper = new ObjectMapper();
            List<CN_Cafe> cafeList = mapper.readValue(data, new TypeReference<List<CN_Cafe>>() {});

            // 모든 결과를 합친다.
            allCafes.addAll(cafeList);

            // 만약 결과가 더 이상 없으면 종료
            if (cafeList.size() < size) {
                break;
            }

            // 다음 페이지로 이동
            page++;
        }

        // 필요한 수의 결과만 리스트에 저장
        List<CN_Cafe> finalCafes = allCafes.size() > totalResults ? allCafes.subList(0, totalResults) : allCafes;

        // 모델에 카페 리스트 추가
        modelAndView.addObject("cafeList", finalCafes);

        System.out.println("searchMapForTestMap3 method return");
        return modelAndView;
    }

    // DB에서 카페 정보를 조회하여 표시하는 메서드
    @GetMapping("/cafe")
    public ModelAndView showTestCafePage() {
        System.out.println("showTestCafePage method start");

        ModelAndView modelAndView = new ModelAndView("test/testmap2");
        List<CN_Cafe> cafeList = cafeService.findAllCafes();
        modelAndView.addObject("cafeList", cafeList);
        // Content-Type을 UTF-8로 설정
        modelAndView.getModelMap().addAttribute("contentType", "text/html; charset=UTF-8");

        System.out.println("showTestCafePage method return");
        return modelAndView;
    }

    // 새 카페 정보를 DB에 삽입하는 메서드
    @PostMapping("/cafeinsert")
    public String insertCafe(
            @RequestParam("cafeId") String cafeId,
            @RequestParam("x") String x,
            @RequestParam("y") String y,
            @RequestParam("placeName") String placeName,
            RedirectAttributes redirectAttributes) {

        System.out.println("insertCafe method start");

        if (cafeService.findCafeById(cafeId) != null) {
            redirectAttributes.addFlashAttribute("error", "Cafe ID already exists!");
            System.out.println("insertCafe method return (ID already exists)");
            return "redirect:/test/cafe";
        }

        CN_Cafe newCafe = new CN_Cafe(cafeId, x, y, placeName);
        cafeService.createCafe(newCafe);
        redirectAttributes.addFlashAttribute("message", "Cafe created successfully!");

        System.out.println("insertCafe method return (Cafe created)");
        return "redirect:/test/cafe";
    }

    // 카페를 삭제하는 메서드
    @PostMapping("/cafedelete")
    public String deleteCafe(
            @RequestParam("cafeId") String cafeId,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("deleteCafe method start");

        cafeService.deleteCafe(cafeId);
        redirectAttributes.addFlashAttribute("message", "Cafe deleted successfully!");

        System.out.println("deleteCafe method return");
        return "redirect:/test/cafe";
    }
}
