package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.MealLogsAndIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealLogsAndIngredientsRepository extends JpaRepository<MealLogsAndIngredients, Integer> {
    List<MealLogsAndIngredients> findByMealLogId(int mealLogId);
}
