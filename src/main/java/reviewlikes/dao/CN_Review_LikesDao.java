package reviewlikes.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import reviewlikes.model.CN_Review_Likes;

import java.util.List;

@Repository
public class CN_Review_LikesDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Review_Likes
    public void insert(CN_Review_Likes reviewLike) {
        String sql = "INSERT INTO CN_Review_Likes (review_like_id, user_id, review_id) " +
                     "VALUES (cn_review_likes_seq.NEXTVAL, :userId, :reviewId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", reviewLike.getUserId())
            .addValue("reviewId", reviewLike.getReviewId());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Review_Likes by ID
    public void delete(int reviewLikeId) {
        String sql = "DELETE FROM CN_Review_Likes WHERE review_like_id = :reviewLikeId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("reviewLikeId", reviewLikeId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Review_Likes by ID
    public CN_Review_Likes findById(int reviewLikeId) {
        String sql = "SELECT * FROM CN_Review_Likes WHERE review_like_id = :reviewLikeId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("reviewLikeId", reviewLikeId);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new CN_Review_Likes(
            rs.getInt("review_like_id"),
            rs.getString("user_id"),
            rs.getInt("review_id")
        ));
    }
    
    //0903추가: Find CN_Review_Likes by userId and reviewId
    public CN_Review_Likes findByUserIdAndReviewId(String userId, int reviewId) {
        String sql = "SELECT * FROM CN_Review_Likes WHERE user_id = :userId AND review_id = :reviewId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("reviewId", reviewId);

        List<CN_Review_Likes> likes = jdbcTemplate.query(sql, params, (rs, rowNum) -> new CN_Review_Likes(
            rs.getInt("review_like_id"),
            rs.getString("user_id"),
            rs.getInt("review_id")
        ));

        return likes.isEmpty() ? null : likes.get(0);
    }
    
    // Find all CN_Review_Likes
    public List<CN_Review_Likes> findAll() {
        String sql = "SELECT * FROM CN_Review_Likes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CN_Review_Likes(
            rs.getInt("review_like_id"),
            rs.getString("user_id"),
            rs.getInt("review_id")
        ));
    }
}
