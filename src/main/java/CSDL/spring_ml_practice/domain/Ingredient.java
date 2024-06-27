package CSDL.spring_ml_practice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ingredients")
@Data
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ingredientId;
    private String name;
    private int exchangeGroup;
    private float exchangeAmount;
    private float carbohydrates;
    private float proteins;
    private float fats;
    private float calorie;
}
