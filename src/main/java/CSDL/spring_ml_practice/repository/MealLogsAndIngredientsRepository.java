package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.MealLogsAndIngredients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealLogsAndIngredientsRepository extends JpaRepository<MealLogsAndIngredients, Integer> {
}