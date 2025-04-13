package cafe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CafeRankingDTO {
	//카페 정보와, 점수를 포함하는 dto클래스 (랭킹에 씀)
	//DTO는 VO와 달리 계층간 데이터를 전달하는 역할, 테이블과 1:1 필드값이 매칭되는 vo와 다르다
    private CN_Cafe cafe;
    private double adjustedScore;
    private double avgRating; // 리뷰 평점 추가
    private int reviewCount;  // 리뷰 개수 추가
    /*
    public CafeRankingDTO(CN_Cafe cafe, double adjustedScore, double avgRating, int reviewCount) {
        this.cafe = cafe;
        this.adjustedScore = adjustedScore;
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
    }
    */

    public CN_Cafe getCafe() {
        return cafe;
    }

    public double getAdjustedScore() {
        return adjustedScore;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }
}
