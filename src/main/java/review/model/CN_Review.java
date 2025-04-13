package review.model;

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
public class CN_Review {
    private int reviewId;
    private String title;
    private String content;
    private String cafeId;
    private String userId;
    private int rating;
    private int likes;
    private Timestamp createdDate;
}
