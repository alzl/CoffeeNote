package notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CN_Notice {
    private int noticeId;
    private String userId; // String 타입으로 수정
    private String title;
    private String content;
    private Timestamp createdDate;
}
