package advertising.model;

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
public class CN_Advertising {
    private int advertisingId;
    private String title;
    private String content;
    private String cafeId;
    private String userId;
    private Timestamp createdDate;
}
