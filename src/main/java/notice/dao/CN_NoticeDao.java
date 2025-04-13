package notice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import notice.model.CN_Notice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CN_NoticeDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    // Insert a new CN_Notice
    public void insert(CN_Notice notice) {
        String sql = "INSERT INTO CN_Notice (notice_id, user_id, title, content, created_date) " +
                     "VALUES (cn_notice_seq.NEXTVAL, :userId, :title, :content, :createdDate)";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", notice.getUserId())
            .addValue("title", notice.getTitle())
            .addValue("content", notice.getContent())
            .addValue("createdDate", notice.getCreatedDate());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[] {"notice_id"});
        notice.setNoticeId(keyHolder.getKey().intValue()); // Setting the generated notice_id to the notice object
    }

    // Update an existing CN_Notice
    public void update(CN_Notice notice) {
        String sql = "UPDATE CN_Notice SET title = :title, content = :content, created_date = :createdDate WHERE notice_id = :noticeId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("noticeId", notice.getNoticeId())
            .addValue("title", notice.getTitle())
            .addValue("content", notice.getContent())
            .addValue("createdDate", notice.getCreatedDate());

        jdbcTemplate.update(sql, params);
    }

    // Delete a CN_Notice by ID
    public void delete(int noticeId) {
        String sql = "DELETE FROM CN_Notice WHERE notice_id = :noticeId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("noticeId", noticeId);

        jdbcTemplate.update(sql, params);
    }

    // Find a CN_Notice by ID
    public CN_Notice findById(int noticeId) {
        String sql = "SELECT * FROM CN_Notice WHERE notice_id = :noticeId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("noticeId", noticeId);

        return jdbcTemplate.queryForObject(sql, params, new CN_NoticeRowMapper());
    }

    // Find all CN_Notices
    public List<CN_Notice> findAll() {
        String sql = "SELECT * FROM CN_Notice ORDER BY notice_id DESC"; // 최신순 정렬
        return jdbcTemplate.query(sql, new CN_NoticeRowMapper());
    }

    // RowMapper to map ResultSet to CN_Notice object
    private static class CN_NoticeRowMapper implements RowMapper<CN_Notice> {
        @Override
        public CN_Notice mapRow(ResultSet rs, int rowNum) throws SQLException {
            CN_Notice notice = new CN_Notice();
            notice.setNoticeId(rs.getInt("notice_id"));
            notice.setUserId(rs.getString("user_id"));
            notice.setTitle(rs.getString("title"));
            notice.setContent(rs.getString("content"));
            notice.setCreatedDate(rs.getTimestamp("created_date"));
            return notice;
        }
    }
}
