package registeredcafes.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import registeredcafes.model.CN_Registered_Cafes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CN_Registered_CafesDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Registered_Cafes
    public void insert(CN_Registered_Cafes registeredCafes) {
        String sql = "INSERT INTO CN_Registered_Cafes (registered_id, cafe_id, user_id) " +
                     "VALUES (:registeredId, :cafeId, :userId)";
        Map<String, Object> params = new HashMap<>();
        params.put("registeredId", registeredCafes.getRegisteredId()); // businessRegNum을 사용
        params.put("cafeId", registeredCafes.getCafeId());
        params.put("userId", registeredCafes.getUserId());

        jdbcTemplate.update(sql, params);
    }

    // Update an existing CN_Registered_Cafes
    public void update(CN_Registered_Cafes registeredCafes) {
        String sql = "UPDATE CN_Registered_Cafes SET cafe_id = :cafeId, user_id = :userId WHERE registered_id = :registeredId";
        Map<String, Object> params = new HashMap<>();
        params.put("registeredId", registeredCafes.getRegisteredId());
        params.put("cafeId", registeredCafes.getCafeId());
        params.put("userId", registeredCafes.getUserId());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Registered_Cafes by ID
    public void delete(int registeredId) {
        String sql = "DELETE FROM CN_Registered_Cafes WHERE registered_id = :registeredId";
        Map<String, Object> params = new HashMap<>();
        params.put("registeredId", registeredId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Registered_Cafes by ID
    public CN_Registered_Cafes findById(int registeredId) {
        String sql = "SELECT * FROM CN_Registered_Cafes WHERE registered_id = :registeredId";
        Map<String, Object> params = new HashMap<>();
        params.put("registeredId", registeredId);

        return jdbcTemplate.queryForObject(sql, params, new CN_Registered_CafesRowMapper());
    }

    // Find all CN_Registered_Cafes
    public List<CN_Registered_Cafes> findAll() {
        String sql = "SELECT * FROM CN_Registered_Cafes";
        return jdbcTemplate.query(sql, new CN_Registered_CafesRowMapper());
    }

    // 특정 사용자 ID로 등록된 모든 카페 찾기
    public List<CN_Registered_Cafes> findAllByUserId(String userId) {
        String sql = "SELECT * FROM CN_Registered_Cafes WHERE user_id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        return jdbcTemplate.query(sql, params, new CN_Registered_CafesRowMapper());
    }
    
    //0829추가 카페 ID로 등록된 사업장을 찾는 메서드 (중복 체크 용도)
    public CN_Registered_Cafes findByCafeId(String cafeId) {
        String sql = "SELECT * FROM CN_Registered_Cafes WHERE cafe_id = :cafeId";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafeId);

        List<CN_Registered_Cafes> results = jdbcTemplate.query(sql, params, new CN_Registered_CafesRowMapper());
        return results.isEmpty() ? null : results.get(0);
    }
    //0829추가 중복된 사업자번호가 있는지 찾는 메서드 (중복 체크)
    public CN_Registered_Cafes findByRegisteredId(int registeredId) {
        String sql = "SELECT * FROM CN_Registered_Cafes WHERE registered_id = :registeredId";
        Map<String, Object> params = new HashMap<>();
        params.put("registeredId", registeredId);

        List<CN_Registered_Cafes> results = jdbcTemplate.query(sql, params, new CN_Registered_CafesRowMapper());
        return results.isEmpty() ? null : results.get(0);
    }

    // RowMapper to map ResultSet to CN_Registered_Cafes object
    private static class CN_Registered_CafesRowMapper implements RowMapper<CN_Registered_Cafes> {
        @Override
        public CN_Registered_Cafes mapRow(ResultSet rs, int rowNum) throws SQLException {
            CN_Registered_Cafes registeredCafes = new CN_Registered_Cafes();
            registeredCafes.setRegisteredId(rs.getInt("registered_id"));
            registeredCafes.setCafeId(rs.getString("cafe_id"));
            registeredCafes.setUserId(rs.getString("user_id"));
            return registeredCafes;
        }
    }
}
