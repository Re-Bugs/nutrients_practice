package CSDL.spring_ml_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class MemberSignInDTO {
    private String memberEmail;
    private String password;
}
