package CSDL.spring_ml_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class MemberSignUpDTO {
    private String memberEmail;
    private String password;
    private String name;
    private int sex;
    private int activityLevel;
    private int weight;
    private int height;
}
