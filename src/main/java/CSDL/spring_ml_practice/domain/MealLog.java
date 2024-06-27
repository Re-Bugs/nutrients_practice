package CSDL.spring_ml_practice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "meal_logs")
@Data
public class MealLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mealLogId;
    private String memberEmail;
    private LocalDateTime logDate;
    private int meal;
    private String picture;
}
