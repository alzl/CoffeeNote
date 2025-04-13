package review.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import review.model.CN_Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CN_ReviewDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(CN_Review review) {
        String sql = "INSERT INTO CN_Review (review_id, title, content, cafe_id, user_id, rating, likes, created_date) " +
                     "VALUES (cn_review_seq.NEXTVAL, :title, :content, :cafeId, :userId, :rating, :likes, :createdDate)";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("title", review.getTitle())
            .addValue("content", review.getContent())
            .addValue("cafeId", review.getCafeId())
            .addValue("userId", review.getUserId())
            .addValue("rating", review.getRating())
            .addValue("likes", review.getLikes())
            .addValue("createdDate", review.getCreatedDate());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[] {"review_id"});
        review.setReviewId(keyHolder.getKey().intValue()); //여기서 생성한 리뷰객체에 진짜 리뷰 id를 넣어주네
    }

    public void update(CN_Review review) {
        String sql = "UPDATE CN_Review SET title = :title, content = :content, cafe_id = :cafeId, user_id = :userId, rating = :rating, likes = :likes, created_date = :createdDate WHERE review_id = :reviewId";
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", review.getReviewId());
        params.put("title", review.getTitle());
        params.put("content", review.getContent());
        params.put("cafeId", review.getCafeId());
        params.put("userId", review.getUserId());
        params.put("rating", review.getRating());
        params.put("likes", review.getLikes());
        params.put("createdDate", review.getCreatedDate());

        jdbcTemplate.update(sql, params);
    }

    public void delete(int reviewId) {
        String sql = "DELETE FROM CN_Review WHERE review_id = :reviewId";
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", reviewId);
        jdbcTemplate.update(sql, params);
    }

    public CN_Review findById(int reviewId) {
        String sql = "SELECT * FROM CN_Review WHERE review_id = :reviewId";
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", reviewId);
        return jdbcTemplate.queryForObject(sql, params, new ReviewRowMapper());
    }

    public List<CN_Review> findAll() {
        String sql = "SELECT * FROM CN_Review";
        return jdbcTemplate.query(sql, new ReviewRowMapper());
    }
    //0828추가 - 해당 카페의 리뷰만을 가져오는 쿼리문
    // review.dao.CN_ReviewDao
    public List<CN_Review> findByCafeId(String cafeId) {
        String sql = "SELECT * FROM CN_Review WHERE cafe_id = :cafeId";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafeId);

        return jdbcTemplate.query(sql, params, new ReviewRowMapper());
    }

    private static class ReviewRowMapper implements RowMapper<CN_Review> {
        @Override
        public CN_Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            CN_Review review = new CN_Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setTitle(rs.getString("title"));
            review.setContent(rs.getString("content"));
            review.setCafeId(rs.getString("cafe_id"));
            review.setUserId(rs.getString("user_id"));
            review.setRating(rs.getInt("rating"));
            review.setLikes(rs.getInt("likes"));
            review.setCreatedDate(rs.getTimestamp("created_date"));
            return review;
        }
    }
}
