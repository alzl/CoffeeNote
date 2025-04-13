package advertising.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import advertising.model.CN_Advertising;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CN_AdvertisingDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Advertising using a sequence and capture generated ID
    public void insert(CN_Advertising advertising) {
        String sql = "INSERT INTO CN_Advertising (advertising_id, title, content, cafe_id, user_id, created_date) " +
                     "VALUES (cn_advertising_seq.NEXTVAL, :title, :content, :cafeId, :userId, :createdDate)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", advertising.getTitle());
        params.addValue("content", advertising.getContent());
        params.addValue("cafeId", advertising.getCafeId());
        params.addValue("userId", advertising.getUserId());
        params.addValue("createdDate", advertising.getCreatedDate());

        // Use KeyHolder to capture the generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[] {"advertising_id"});
        
        // Set the generated ID back to the advertising object
        advertising.setAdvertisingId(keyHolder.getKey().intValue());
    }

    // Update an existing CN_Advertising
    public void update(CN_Advertising advertising) {
        String sql = "UPDATE CN_Advertising SET title = :title, content = :content, cafe_id = :cafeId, user_id = :userId, created_date = :createdDate WHERE advertising_id = :advertisingId";
        Map<String, Object> params = new HashMap<>();
        params.put("advertisingId", advertising.getAdvertisingId());
        params.put("title", advertising.getTitle());
        params.put("content", advertising.getContent());
        params.put("cafeId", advertising.getCafeId());
        params.put("userId", advertising.getUserId());
        params.put("createdDate", advertising.getCreatedDate());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Advertising by ID
    public void delete(int advertisingId) {
        String sql = "DELETE FROM CN_Advertising WHERE advertising_id = :advertisingId";
        Map<String, Object> params = new HashMap<>();
        params.put("advertisingId", advertisingId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Advertising by ID
    public CN_Advertising findById(int advertisingId) {
        String sql = "SELECT * FROM CN_Advertising WHERE advertising_id = :advertisingId";
        Map<String, Object> params = new HashMap<>();
        params.put("advertisingId", advertisingId);

        return jdbcTemplate.queryForObject(sql, params, new CN_AdvertisingRowMapper());
    }

    // Find all CN_Advertisings
    public List<CN_Advertising> findAll() {
        String sql = "SELECT * FROM CN_Advertising";
        return jdbcTemplate.query(sql, new CN_AdvertisingRowMapper());
    }
    
    //0902추가 - cafeid에 해당하는 홍보 가져오기
    public List<CN_Advertising> findAdvertisingsByCafeId(String cafeId) {
        String sql = "SELECT * FROM CN_Advertising WHERE cafe_id = :cafeId";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafeId);
        
        return jdbcTemplate.query(sql, params, new CN_AdvertisingRowMapper());
    }

    // RowMapper to map ResultSet to CN_Advertising object
    private static class CN_AdvertisingRowMapper implements RowMapper<CN_Advertising> {
        @Override
        public CN_Advertising mapRow(ResultSet rs, int rowNum) throws SQLException {
            CN_Advertising advertising = new CN_Advertising();
            advertising.setAdvertisingId(rs.getInt("advertising_id"));
            advertising.setTitle(rs.getString("title"));
            advertising.setContent(rs.getString("content"));
            advertising.setCafeId(rs.getString("cafe_id"));
            advertising.setUserId(rs.getString("user_id"));
            advertising.setCreatedDate(rs.getTimestamp("created_date"));
            return advertising;
        }
    }
}
