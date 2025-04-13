package bookmarklikes.model;

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
public class CN_Bookmark_Likes {
    private int bookmarkLikeId;
    private String userId;
    private int bookmarkId;
}
