package bookmark.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import bookmark.model.CN_Bookmark;
import java.util.List;

@Repository
public class CN_BookmarkDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Bookmark
    public void insert(CN_Bookmark bookmark) {
        String sql = "INSERT INTO CN_Bookmark (bookmark_id, user_id, title, content, likes, is_public) " +
                     "VALUES (cn_bookmark_seq.NEXTVAL, :userId, :title, :content, :likes, :isPublic)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", bookmark.getUserId())
            .addValue("title", bookmark.getTitle())
            .addValue("content", bookmark.getContent())
            .addValue("likes", bookmark.getLikes())
            .addValue("isPublic", bookmark.getIsPublic());

        jdbcTemplate.update(sql, params);
    }

    // Update an existing CN_Bookmark
    public void update(CN_Bookmark bookmark) {
        String sql = "UPDATE CN_Bookmark SET user_id = :userId, title = :title, content = :content, likes = :likes, is_public = :isPublic WHERE bookmark_id = :bookmarkId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", bookmark.getUserId())
            .addValue("title", bookmark.getTitle())
            .addValue("content", bookmark.getContent())
            .addValue("likes", bookmark.getLikes())
            .addValue("isPublic", bookmark.getIsPublic())
            .addValue("bookmarkId", bookmark.getBookmarkId());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Bookmark by ID
    public void delete(int bookmarkId) {
        String sql = "DELETE FROM CN_Bookmark WHERE bookmark_id = :bookmarkId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkId", bookmarkId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Bookmark by ID
    public CN_Bookmark findById(int bookmarkId) {
        String sql = "SELECT * FROM CN_Bookmark WHERE bookmark_id = :bookmarkId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkId", bookmarkId);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new CN_Bookmark(
            rs.getInt("bookmark_id"),
            rs.getString("user_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getInt("likes"),
            rs.getInt("is_public")
        ));
    }

    // Find all CN_Bookmarks
    public List<CN_Bookmark> findAll() {
        String sql = "SELECT * FROM CN_Bookmark";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CN_Bookmark(
            rs.getInt("bookmark_id"),
            rs.getString("user_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getInt("likes"),
            rs.getInt("is_public")
        ));
    }

    // 주어진 사용자 ID에 대한 가장 최근에 생성된 북마크 가져오기
    public CN_Bookmark findLatestBookmarkByUserId(String userId) {
        String sql = "SELECT * FROM (SELECT * FROM CN_Bookmark WHERE user_id = :userId ORDER BY bookmark_id DESC) WHERE ROWNUM = 1";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new CN_Bookmark(
            rs.getInt("bookmark_id"),
            rs.getString("user_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getInt("likes"),
            rs.getInt("is_public")
        ));
    }
    
    // 추가: 공개된 CN_Bookmarks 찾기
    public List<CN_Bookmark> findPublicBookmarks() {
        String sql = "SELECT * FROM CN_Bookmark WHERE is_public = 1";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CN_Bookmark(
            rs.getInt("bookmark_id"),
            rs.getString("user_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getInt("likes"),
            rs.getInt("is_public")
        ));
    }
    
    //0903추가
    // 특정 사용자 ID의 모든 북마크를 찾는 메서드 추가
    public List<CN_Bookmark> findByUserId(String userId) {
        String sql = "SELECT * FROM CN_Bookmark WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new CN_Bookmark(
            rs.getInt("bookmark_id"),
            rs.getString("user_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getInt("likes"),
            rs.getInt("is_public")
        ));
    }
    
    //0910추가 - 북마크 제목 검색 (공개된 북마크만 검색되도록 함)
    public List<CN_Bookmark> searchByTitle(String keyword) {
        String sql = "SELECT * FROM CN_Bookmark WHERE title LIKE :keyword AND is_public = 1";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("keyword", "%" + keyword + "%");

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new CN_Bookmark(
            rs.getInt("bookmark_id"),
            rs.getString("user_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getInt("likes"),
            rs.getInt("is_public")
        ));
    }

}
