package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.RecipesAndIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipesAndIngredientsRepository extends JpaRepository<RecipesAndIngredients, Integer> {
    @Query("SELECT ri.ingredientId FROM RecipesAndIngredients ri WHERE ri.recipeId = :recipeId")
    List<Integer> findIngredientsByRecipeId(int recipeId);
}
