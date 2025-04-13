package review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import reviewimage.model.CN_Review_Image;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CN_ReviewDetails {
    private int reviewId;
    private String title;
    private String content;
    private String cafeId;
    private String userId;
    private int rating;
    private int likes;
    private Timestamp createdDate;
    private List<CN_Review_Image> reviewImages; // 리뷰 이미지 목록

    public CN_ReviewDetails(CN_Review review, List<CN_Review_Image> images) {
        this.reviewId = review.getReviewId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.cafeId = review.getCafeId();
        this.userId = review.getUserId();
        this.rating = review.getRating();
        this.likes = review.getLikes();
        this.createdDate = review.getCreatedDate();
        this.reviewImages = images;
    }
}
