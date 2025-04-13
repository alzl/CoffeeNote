package registeredcafes.model;

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
public class CN_Registered_Cafes {
    private int registeredId; // businessRegNum을 저장하는 필드로 사용 (시퀀스아님)
    private String cafeId;
    private String userId;
}
