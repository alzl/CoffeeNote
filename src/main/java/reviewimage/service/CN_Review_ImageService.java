package reviewimage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reviewimage.dao.CN_Review_ImageDao;
import reviewimage.model.CN_Review_Image;

import java.util.List;

@Service
public class CN_Review_ImageService {
    @Autowired
    private CN_Review_ImageDao reviewImageDao;

    // Create a new CN_Review_Image
    public void createReviewImage(CN_Review_Image reviewImage) {
        reviewImageDao.insert(reviewImage);
    }

    // Update an existing CN_Review_Image
    public void updateReviewImage(CN_Review_Image reviewImage) {
        reviewImageDao.update(reviewImage);
    }

    // Delete a CN_Review_Image by ID
    public void deleteReviewImage(int reviewImageId) {
        reviewImageDao.delete(reviewImageId);
    }

    // Find a CN_Review_Image by ID
    public CN_Review_Image findReviewImageById(int reviewImageId) {
        return reviewImageDao.findById(reviewImageId);
    }

    // Find all CN_Review_Images
    public List<CN_Review_Image> findAllReviewImages() {
        return reviewImageDao.findAll();
    }

	public List<CN_Review_Image> findImagesByReviewId(int reviewId) {
		return reviewImageDao.findImagesByReviewId(reviewId);
	}
}
