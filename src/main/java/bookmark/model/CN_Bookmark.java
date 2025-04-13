package bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CN_Bookmark {
    private int bookmarkId;
    private String userId;
    private String title;
    private String content;
    private int likes;
    private int isPublic; //오라클이 bool 몰라서 0, 1로 구분
}
