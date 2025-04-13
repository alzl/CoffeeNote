package review.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import review.dao.CN_ReviewDao;
import review.model.CN_Review;

import java.util.List;

@Service
public class CN_ReviewService {
    @Autowired
    private CN_ReviewDao reviewDao;

    public void createReview(CN_Review review) {
        reviewDao.insert(review);
    }

    public void updateReview(CN_Review review) {
        reviewDao.update(review);
    }

    public void deleteReview(int reviewId) {
        reviewDao.delete(reviewId);
    }

    public CN_Review findReviewById(int reviewId) {
        return reviewDao.findById(reviewId);
    }

    public List<CN_Review> findAllReviews() {
        return reviewDao.findAll();
    }
    //0828추가, 해당 카페의 리뷰만 가져오는 쿼리문
    // review.service.CN_ReviewService
    public List<CN_Review> findReviewsByCafeId(String cafeId) {
        return reviewDao.findByCafeId(cafeId);
    }
}
