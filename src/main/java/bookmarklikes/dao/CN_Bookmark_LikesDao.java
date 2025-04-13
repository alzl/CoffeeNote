package bookmarklikes.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import bookmark.model.CN_Bookmark;
import bookmarklikes.model.CN_Bookmark_Likes;

import java.util.List;

@Repository
public class CN_Bookmark_LikesDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Bookmark_Likes
    public void insert(CN_Bookmark_Likes bookmarkLike) {
        String sql = "INSERT INTO CN_Bookmark_Likes (bookmark_like_id, user_id, bookmark_id) " +
                     "VALUES (cn_bookmark_likes_seq.NEXTVAL, :userId, :bookmarkId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", bookmarkLike.getUserId())
            .addValue("bookmarkId", bookmarkLike.getBookmarkId());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Bookmark_Likes by ID
    public void delete(int bookmarkLikeId) {
        String sql = "DELETE FROM CN_Bookmark_Likes WHERE bookmark_like_id = :bookmarkLikeId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkLikeId", bookmarkLikeId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Bookmark_Likes by ID
    public CN_Bookmark_Likes findById(int bookmarkLikeId) {
        String sql = "SELECT * FROM CN_Bookmark_Likes WHERE bookmark_like_id = :bookmarkLikeId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkLikeId", bookmarkLikeId);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new CN_Bookmark_Likes(
            rs.getInt("bookmark_like_id"),
            rs.getString("user_id"),
            rs.getInt("bookmark_id")
        ));
    }
    
    // 0903추가: Find CN_Bookmark_Likes by userId and bookmarkId
    public CN_Bookmark_Likes findByUserIdAndBookmarkId(String userId, int bookmarkId) {
        String sql = "SELECT * FROM CN_Bookmark_Likes WHERE user_id = :userId AND bookmark_id = :bookmarkId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("bookmarkId", bookmarkId);

        List<CN_Bookmark_Likes> likes = jdbcTemplate.query(sql, params, (rs, rowNum) -> new CN_Bookmark_Likes(
            rs.getInt("bookmark_like_id"),
            rs.getString("user_id"),
            rs.getInt("bookmark_id")
        ));

        return likes.isEmpty() ? null : likes.get(0);
    }

    // Find all CN_Bookmark_Likes
    public List<CN_Bookmark_Likes> findAll() {
        String sql = "SELECT * FROM CN_Bookmark_Likes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CN_Bookmark_Likes(
            rs.getInt("bookmark_like_id"),
            rs.getString("user_id"),
            rs.getInt("bookmark_id")
        ));
    }
    //0903추가
    // 추가: 사용자가 좋아요한 북마크 목록 가져오기
    public List<CN_Bookmark> findBookmarksLikedByUserId(String userId) {
        String sql = "SELECT b.* FROM CN_Bookmark b " +
                     "JOIN CN_Bookmark_Likes bl ON b.bookmark_id = bl.bookmark_id " +
                     "WHERE bl.user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);
        
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new CN_Bookmark(
            rs.getInt("bookmark_id"),
            rs.getString("user_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getInt("likes"),
            rs.getInt("is_public")
        ));
    }
    public void deleteByBookmarkId(int bookmarkId) {
        String sql = "DELETE FROM CN_Bookmark_Likes WHERE bookmark_id = :bookmarkId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkId", bookmarkId);

        jdbcTemplate.update(sql, params);
    }
}
