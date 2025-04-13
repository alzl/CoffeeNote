package reviewlikes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reviewlikes.dao.CN_Review_LikesDao;
import reviewlikes.model.CN_Review_Likes;

import java.util.List;

@Service
public class CN_Review_LikesService {
    @Autowired
    private CN_Review_LikesDao reviewLikesDao;

    public void createReviewLike(CN_Review_Likes reviewLike) {
        reviewLikesDao.insert(reviewLike);
    }

    public void deleteReviewLike(int reviewLikeId) {
        reviewLikesDao.delete(reviewLikeId);
    }

    public CN_Review_Likes findReviewLikeById(int reviewLikeId) {
        return reviewLikesDao.findById(reviewLikeId);
    }
    
    public CN_Review_Likes findByUserIdAndReviewId(String userId, int reviewId) {
        return reviewLikesDao.findByUserIdAndReviewId(userId, reviewId);
    }

    public List<CN_Review_Likes> findAllReviewLikes() {
        return reviewLikesDao.findAll();
    }
}
