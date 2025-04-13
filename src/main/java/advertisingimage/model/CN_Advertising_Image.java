package advertisingimage.model;

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
public class CN_Advertising_Image {
    private int advertisingImageId;
    private int advertisingId;
    private String imagePath;
}
