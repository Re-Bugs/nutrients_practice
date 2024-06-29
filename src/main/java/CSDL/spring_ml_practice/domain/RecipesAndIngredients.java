package CSDL.spring_ml_practice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recipes_and_ingredients")
@Data
public class RecipesAndIngredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "recipe_id")
    private int recipeId;

    @Column(name = "ingredient_id")
    private int ingredientId;
}
