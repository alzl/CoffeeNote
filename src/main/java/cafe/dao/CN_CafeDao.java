package cafe.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import cafe.model.CN_Cafe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CN_CafeDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Cafe
    public void insert(CN_Cafe cafe) {
        String sql = "INSERT INTO CN_Cafe (cafe_id, x, y, place_name) VALUES (:cafeId, :x, :y, :placeName)";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafe.getCafeId());
        params.put("x", cafe.getX());
        params.put("y", cafe.getY());
        params.put("placeName", cafe.getPlaceName());

        jdbcTemplate.update(sql, params);
    }

    // Update an existing CN_Cafe
    public void update(CN_Cafe cafe) {
        String sql = "UPDATE CN_Cafe SET x = :x, y = :y, place_name = :placeName WHERE cafe_id = :cafeId";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafe.getCafeId());
        params.put("x", cafe.getX());
        params.put("y", cafe.getY());
        params.put("placeName", cafe.getPlaceName());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Cafe by ID
    public void delete(String cafeId) {
        String sql = "DELETE FROM CN_Cafe WHERE cafe_id = :cafeId";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafeId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Cafe by ID
    public CN_Cafe findById(String cafeId) {
        String sql = "SELECT * FROM CN_Cafe WHERE cafe_id = :cafeId";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafeId);
        try {
            return jdbcTemplate.queryForObject(sql, params, new CN_CafeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null; // 결과가 없으면 null 반환
        }
    }

    // Find all CN_Cafes
    public List<CN_Cafe> findAll() {
        String sql = "SELECT * FROM CN_Cafe";
        return jdbcTemplate.query(sql, new CN_CafeRowMapper());
    }

    // RowMapper to map ResultSet to CN_Cafe object
    private static class CN_CafeRowMapper implements RowMapper<CN_Cafe> {
        @Override
        public CN_Cafe mapRow(ResultSet rs, int rowNum) throws SQLException {
            CN_Cafe cafe = new CN_Cafe();
            cafe.setCafeId(rs.getString("cafe_id"));
            cafe.setX(rs.getString("x"));
            cafe.setY(rs.getString("y"));
            cafe.setPlaceName(rs.getString("place_name"));  // place_name 추가
            return cafe;
        }
    }
}
