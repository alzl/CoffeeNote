package bookmarkcafes.model;

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
public class CN_Bookmark_Cafes {
    private int bookmarkCafesId;
    private int bookmarkId;
    private String cafeId;
}
