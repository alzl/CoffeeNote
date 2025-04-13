package cafe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class CN_Cafe {
    @JsonProperty("id")
    private String cafeId;

    @JsonProperty("x")
    private String x;

    @JsonProperty("y")
    private String y;
    
    @JsonProperty("place_name")
    private String placeName;  // 새로 추가된 필드
}
