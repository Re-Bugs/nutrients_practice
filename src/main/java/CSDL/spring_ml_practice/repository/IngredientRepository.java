package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByNameContaining(String name);

    @Query("SELECT ri.ingredientsId FROM RecipesAndIngredients ri WHERE ri.recipesId = :recipeId")
    List<Integer> findIngredientsByRecipeId(int recipeId);

}
