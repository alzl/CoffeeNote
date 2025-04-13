package advertisingimage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import advertisingimage.model.CN_Advertising_Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CN_Advertising_ImageDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert
    public void insert(CN_Advertising_Image image) {
        String sql = "INSERT INTO CN_Advertising_Image (advertising_image_id, advertising_id, image_path) " +
                     "VALUES (cn_advertising_image_seq.NEXTVAL, :advertisingId, :imagePath)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("advertisingId", image.getAdvertisingId())
            .addValue("imagePath", image.getImagePath());

        jdbcTemplate.update(sql, params);
    }

    // Update
    public void update(CN_Advertising_Image image) {
        String sql = "UPDATE CN_Advertising_Image SET advertising_id = :advertisingId, image_path = :imagePath WHERE advertising_image_id = :advertisingImageId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("advertisingId", image.getAdvertisingId())
            .addValue("imagePath", image.getImagePath())
            .addValue("advertisingImageId", image.getAdvertisingImageId());

        jdbcTemplate.update(sql, params);
    }

    // Delete
    public void delete(int advertisingImageId) {
        String sql = "DELETE FROM CN_Advertising_Image WHERE advertising_image_id = :advertisingImageId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("advertisingImageId", advertisingImageId);

        jdbcTemplate.update(sql, params);
    }

    // Find by ID
    public CN_Advertising_Image findById(int advertisingImageId) {
        String sql = "SELECT * FROM CN_Advertising_Image WHERE advertising_image_id = :advertisingImageId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("advertisingImageId", advertisingImageId);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new CN_Advertising_Image(
            rs.getInt("advertising_image_id"),
            rs.getInt("advertising_id"),
            rs.getString("image_path")
        ));
    }
    
    // Find all
    public List<CN_Advertising_Image> findAll() {
        String sql = "SELECT * FROM CN_Advertising_Image";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CN_Advertising_Image(
            rs.getInt("advertising_image_id"),
            rs.getInt("advertising_id"),
            rs.getString("image_path")
        ));
    }
    
    //0902추가 - 홍보id에 해당하는 홍보이미지 가져오기
    public List<CN_Advertising_Image> findImagesByAdvertisingId(int advertisingId) {
        String sql = "SELECT * FROM CN_Advertising_Image WHERE advertising_id = :advertisingId";
        Map<String, Object> params = new HashMap<>();
        params.put("advertisingId", advertisingId);
        
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            CN_Advertising_Image image = new CN_Advertising_Image();
            image.setAdvertisingImageId(rs.getInt("advertising_image_id"));
            image.setAdvertisingId(rs.getInt("advertising_id"));
            image.setImagePath(rs.getString("image_path"));
            return image;
        });
    }
    
    //0924추가 - 해당 카페의 모든 광고이미지를 리스트로 반환
    public List<CN_Advertising_Image> findImagesByCafeId(String cafeId) {
        String sql = "SELECT ai.* FROM CN_Advertising_Image ai " +
                     "JOIN CN_Advertising a ON ai.advertising_id = a.advertising_id " +
                     "WHERE a.cafe_id = :cafeId ORDER BY ai.advertising_image_id DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("cafeId", cafeId);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            CN_Advertising_Image image = new CN_Advertising_Image();
            image.setAdvertisingImageId(rs.getInt("advertising_image_id"));
            image.setAdvertisingId(rs.getInt("advertising_id"));
            image.setImagePath(rs.getString("image_path"));
            return image;
        });
    }
}
