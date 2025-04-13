package bookmarkcafes.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import bookmarkcafes.model.CN_Bookmark_Cafes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CN_Bookmark_CafesDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Bookmark_Cafes
    public void insert(CN_Bookmark_Cafes bookmarkCafes) {
        String sql = "INSERT INTO CN_Bookmark_Cafes (bookmark_cafes_id, bookmark_id, cafe_id) " +
                     "VALUES (cn_bookmark_cafes_seq.NEXTVAL, :bookmarkId, :cafeId)";

        Map<String, Object> params = new HashMap<>();
        params.put("bookmarkId", bookmarkCafes.getBookmarkId());
        params.put("cafeId", bookmarkCafes.getCafeId());

        jdbcTemplate.update(sql, params);
    }

    // Update an existing CN_Bookmark_Cafes
    public void update(CN_Bookmark_Cafes bookmarkCafes) {
        String sql = "UPDATE CN_Bookmark_Cafes SET bookmark_id = :bookmarkId, cafe_id = :cafeId WHERE bookmark_cafes_id = :bookmarkCafesId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkCafesId", bookmarkCafes.getBookmarkCafesId())
            .addValue("bookmarkId", bookmarkCafes.getBookmarkId())
            .addValue("cafeId", bookmarkCafes.getCafeId());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Bookmark_Cafes by ID
    public void delete(int bookmarkCafesId) {
        String sql = "DELETE FROM CN_Bookmark_Cafes WHERE bookmark_cafes_id = :bookmarkCafesId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkCafesId", bookmarkCafesId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Bookmark_Cafes by ID
    public CN_Bookmark_Cafes findById(int bookmarkCafesId) {
        String sql = "SELECT * FROM CN_Bookmark_Cafes WHERE bookmark_cafes_id = :bookmarkCafesId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkCafesId", bookmarkCafesId);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new CN_Bookmark_Cafes(
            rs.getInt("bookmark_cafes_id"),
            rs.getInt("bookmark_id"),
            rs.getString("cafe_id")
        ));
    }

    // Find all CN_Bookmark_Cafes
    public List<CN_Bookmark_Cafes> findAll() {
        String sql = "SELECT * FROM CN_Bookmark_Cafes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CN_Bookmark_Cafes(
            rs.getInt("bookmark_cafes_id"),
            rs.getInt("bookmark_id"),
            rs.getString("cafe_id")
        ));
    }
    // Find CN_Bookmark_Cafes by bookmarkId
    public List<CN_Bookmark_Cafes> findByBookmarkId(int bookmarkId) {
        String sql = "SELECT * FROM CN_Bookmark_Cafes WHERE bookmark_id = :bookmarkId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkId", bookmarkId);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new CN_Bookmark_Cafes(
            rs.getInt("bookmark_cafes_id"),
            rs.getInt("bookmark_id"),
            rs.getString("cafe_id")
        ));
    }
    //0903추가
    // 특정 북마크 ID에 연결된 모든 카페를 삭제하는 메서드 추가
    public void deleteByBookmarkId(int bookmarkId) {
        String sql = "DELETE FROM CN_Bookmark_Cafes WHERE bookmark_id = :bookmarkId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bookmarkId", bookmarkId);

        jdbcTemplate.update(sql, params);
    }
}
