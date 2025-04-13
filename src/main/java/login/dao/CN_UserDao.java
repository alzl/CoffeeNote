package login.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import login.model.CN_User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CN_UserDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_User
    public void insert(CN_User user) {
        String sql = "INSERT INTO CN_User (user_id, user_name, password, email, user_type, profile_img) VALUES (:userId, :userName, :password, :email, :userType, :profileImg)";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("userName", user.getUserName());
        params.put("password", user.getPassword()); // 암호화된 비밀번호 저장
        params.put("email", user.getEmail());
        params.put("userType", user.getUserType());
        params.put("profileImg", user.getProfileImg());

        jdbcTemplate.update(sql, params);
    }

    // Update an existing CN_User
    public void update(CN_User user) {
        String sql = "UPDATE CN_User SET user_name = :userName, password = :password, email = :email, user_type = :userType, profile_img = :profileImg WHERE user_id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("userName", user.getUserName());
        params.put("password", user.getPassword()); // 암호화된 비밀번호 업데이트
        params.put("email", user.getEmail());
        params.put("userType", user.getUserType());
        params.put("profileImg", user.getProfileImg());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_User by ID
    public void delete(String userId) {
        String sql = "DELETE FROM CN_User WHERE user_id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_User by ID
    public CN_User findById(String userId) {
        String sql = "SELECT user_id, user_name, password, email, user_type, profile_img FROM CN_User WHERE user_id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        List<CN_User> users = jdbcTemplate.query(sql, params, new CN_UserRowMapper());
        return users.isEmpty() ? null : users.get(0);
    }

    // Find all CN_Users
    public List<CN_User> findAll() {
        String sql = "SELECT user_id, user_name, password, email, user_type, profile_img FROM CN_User";
        return jdbcTemplate.query(sql, new CN_UserRowMapper());
    }

    // id 중복 검사
    public boolean idExists(String id) {
        String sql = "SELECT COUNT(*) FROM CN_User WHERE user_id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        int count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count > 0;
    }

    // 이메일 중복 검사
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM CN_User WHERE email = :email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        int count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count > 0;
    }

    // 0827 - 사용자 이름 업데이트 문
    public boolean setUserName(String userId, String userName) {
        // 먼저 해당 userId가 있는지 확인합니다.
        String checkSql = "SELECT COUNT(*) FROM CN_User WHERE user_id = :userId";
        Map<String, Object> checkParams = new HashMap<>();
        checkParams.put("userId", userId);
        
        int count = jdbcTemplate.queryForObject(checkSql, checkParams, Integer.class);
        
        // 만약 해당 userId가 존재하지 않는다면 false를 반환합니다.
        if (count == 0) {
            return false;
        }
        
        // userName을 업데이트하는 SQL문을 실행합니다.
        String updateSql = "UPDATE CN_User SET user_name = :userName WHERE user_id = :userId";
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("userId", userId);
        updateParams.put("userName", userName);
        
        try {
            // 업데이트 시도 후, 영향받은 행(row)의 수를 확인합니다.
            int updatedRows = jdbcTemplate.update(updateSql, updateParams);
            
            // 만약 업데이트가 정상적으로 되었다면 true를 반환합니다.
            return updatedRows > 0;
        } catch (Exception e) {
            // 업데이트가 실패한 경우 예외를 처리하고 false를 반환합니다.
            e.printStackTrace();
            return false;
        }
    }

    public boolean setUserEmail(String userId, String userEmail) {
        // 먼저 해당 userId가 있는지 확인합니다.
        String checkSql = "SELECT COUNT(*) FROM CN_User WHERE user_id = :userId";
        Map<String, Object> checkParams = new HashMap<>();
        checkParams.put("userId", userId);
        
        int count = jdbcTemplate.queryForObject(checkSql, checkParams, Integer.class);
        
        // 만약 해당 userId가 존재하지 않는다면 false를 반환합니다.
        if (count == 0) {
            return false;
        }
        
        // userName을 업데이트하는 SQL문을 실행합니다.
        String updateSql = "UPDATE CN_User SET email = :email WHERE user_id = :userId";
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("userId", userId);
        updateParams.put("email", userEmail);
        
        try {
            // 업데이트 시도 후, 영향받은 행(row)의 수를 확인합니다.
            int updatedRows = jdbcTemplate.update(updateSql, updateParams);
            
            // 만약 업데이트가 정상적으로 되었다면 true를 반환합니다.
            return updatedRows > 0;
        } catch (Exception e) {
            // 업데이트가 실패한 경우 예외를 처리하고 false를 반환합니다.
            e.printStackTrace();
            return false;
        }
    }
    
    //0923추가 이메일로 계정찾기 (알려주는건 id만)
    public CN_User findByEmail(String email) {
        String sql = "SELECT user_id, user_name, password, email, user_type, profile_img FROM CN_User WHERE email = :email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        List<CN_User> users = jdbcTemplate.query(sql, params, new CN_UserRowMapper());
        return users.isEmpty() ? null : users.get(0);
    }
    
    // RowMapper to map ResultSet to CN_User object
    private static class CN_UserRowMapper implements RowMapper<CN_User> {
        @Override
        public CN_User mapRow(ResultSet rs, int rowNum) throws SQLException {
            CN_User user = new CN_User();
            user.setUserId(rs.getString("user_id"));
            user.setUserName(rs.getString("user_name"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setUserType(rs.getString("user_type"));
            user.setProfileImg(rs.getString("profile_img"));
            return user;
        }
    }
}
