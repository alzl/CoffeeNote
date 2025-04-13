package cafe.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import advertising.model.CN_AdvertisingDetails;
import review.model.CN_ReviewDetails; // CN_ReviewDetails import 추가
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CN_CafeDetails {
    @JsonProperty("id")
    private String cafeId;

    @JsonProperty("x")
    private String x;

    @JsonProperty("y")
    private String y;
    //JsonProperty에 적은 이름은 실제 api에서 제공하는 변수명
    //String placeName은 내가 재정의한 것
    @JsonProperty("place_name")
    private String placeName;
    //구주소
    @JsonProperty("address_name")
    private String addressName;
    //도로명주소
    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("place_url")
    private String placeUrl;
    
    // 광고 세부 정보를 저장하기 위한 필드 추가
    private List<CN_AdvertisingDetails> advertisings; // CN_AdvertisingDetails 리스트 추가

    // 리뷰 세부 정보를 저장하기 위한 필드 추가
    private List<CN_ReviewDetails> reviews; // CN_ReviewDetails 리스트 추가
}
