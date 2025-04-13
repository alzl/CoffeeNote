package advertising.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import advertisingimage.model.CN_Advertising_Image;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CN_AdvertisingDetails {
    private int advertisingId;
    private String title;
    private String content;
    private String cafeId;
    private String userId;
    private Timestamp createdDate;
    private List<CN_Advertising_Image> advertisingImages; // 광고 이미지 목록

    public CN_AdvertisingDetails(CN_Advertising advertising, List<CN_Advertising_Image> images) {
        this.advertisingId = advertising.getAdvertisingId();
        this.title = advertising.getTitle();
        this.content = advertising.getContent();
        this.cafeId = advertising.getCafeId();
        this.userId = advertising.getUserId();
        this.createdDate = advertising.getCreatedDate();
        this.advertisingImages = images;
    }
}
