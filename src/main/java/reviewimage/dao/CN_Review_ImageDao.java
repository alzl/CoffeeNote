package reviewimage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import reviewimage.model.CN_Review_Image;

import java.util.List;

@Repository
public class CN_Review_ImageDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Review_Image
    public void insert(CN_Review_Image reviewImage) {
        String sql = "INSERT INTO CN_Review_Image (review_image_id, review_id, image_path) " +
                     "VALUES (cn_review_image_seq.NEXTVAL, :reviewId, :imagePath)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("reviewId", reviewImage.getReviewId())
            .addValue("imagePath", reviewImage.getImagePath());

        jdbcTemplate.update(sql, params);
    }

    // Update an existing CN_Review_Image
    public void update(CN_Review_Image reviewImage) {
        String sql = "UPDATE CN_Review_Image SET review_id = :reviewId, image_path = :imagePath WHERE review_image_id = :reviewImageId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("reviewImageId", reviewImage.getReviewImageId())
            .addValue("reviewId", reviewImage.getReviewId())
            .addValue("imagePath", reviewImage.getImagePath());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Review_Image by ID
    public void delete(int reviewImageId) {
        String sql = "DELETE FROM CN_Review_Image WHERE review_image_id = :reviewImageId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("reviewImageId", reviewImageId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Review_Image by ID
    public CN_Review_Image findById(int reviewImageId) {
        String sql = "SELECT * FROM CN_Review_Image WHERE review_image_id = :reviewImageId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("reviewImageId", reviewImageId);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new CN_Review_Image(
            rs.getInt("review_image_id"),
            rs.getInt("review_id"),
            rs.getString("image_path")
        ));
    }
    //0828 추가 - cafeid와 reviewid가 둘 다 같은 이미지 리스트를 반환하는 쿼리
    public List<CN_Review_Image> findImagesByReviewId(int reviewId) {
        String sql = "SELECT * FROM CN_Review_Image WHERE review_id = :reviewId ORDER BY review_image_id DESC";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("reviewId", reviewId);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new CN_Review_Image(
            rs.getInt("review_image_id"),
            rs.getInt("review_id"),
            rs.getString("image_path")
        ));
    }

    // Find all CN_Review_Images
    public List<CN_Review_Image> findAll() {
        String sql = "SELECT * FROM CN_Review_Image";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CN_Review_Image(
            rs.getInt("review_image_id"),
            rs.getInt("review_id"),
            rs.getString("image_path")
        ));
    }
}
