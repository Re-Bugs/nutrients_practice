package CSDL.spring_ml_practice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "members")
@Getter @Setter
public class Member {
    @Id
    private String memberEmail;
    private String password;
    private String name;
    private int sex;
    private int activityLevel;
    private int weight;
    private int height;
}