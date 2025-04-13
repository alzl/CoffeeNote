package reviewimage.model;

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
public class CN_Review_Image {
    private int reviewImageId;
    private int reviewId;
    private String imagePath;
}
