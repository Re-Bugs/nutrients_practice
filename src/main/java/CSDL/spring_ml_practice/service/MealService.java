package CSDL.spring_ml_practice.service;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.MealLog;
import CSDL.spring_ml_practice.domain.MealLogsAndIngredients;
import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.repository.IngredientRepository;
import CSDL.spring_ml_practice.repository.MealLogRepository;
import CSDL.spring_ml_practice.repository.RecipeRepository;
import CSDL.spring_ml_practice.repository.MealLogsAndIngredientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MealLogRepository mealLogRepository;
    private final MealLogsAndIngredientsRepository mealLogsAndIngredientsRepository;

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.findByNameContaining(query);
    }

    public List<Ingredient> searchIngredients(String query) {
        return ingredientRepository.findByNameContaining(query);
    }

    public void addMealLog(String memberEmail, int selectedRecipeId, List<Integer> selectedIngredientIds) {
        // MealLog 엔티티 생성 및 저장
        MealLog mealLog = new MealLog();
        mealLog.setMemberEmail(memberEmail);
        mealLog.setLogDate(LocalDateTime.now());
        mealLog.setMeal(determineMealType(memberEmail));
        mealLog.setPicture(null); // 사진 업로드 로직 추가 필요
        MealLog savedMealLog = mealLogRepository.save(mealLog);

        // MealLogsAndIngredients 엔티티 생성 및 저장
        selectedIngredientIds.forEach(ingredientId -> {
            MealLogsAndIngredients mealLogsAndIngredients = new MealLogsAndIngredients();
            mealLogsAndIngredients.setMealLogId(savedMealLog.getMealLogId());
            mealLogsAndIngredients.setIngredientId(ingredientId);
            mealLogsAndIngredientsRepository.save(mealLogsAndIngredients);
        });
    }

    private int determineMealType(String memberEmail) {
        // 해당 사용자가 아침을 먹지 않았다면 1, 먹었다면 2를 반환하는 로직 구현
        LocalDateTime now = LocalDateTime.now();
        // 여기서는 간단하게 현재 시간 기준으로 식사 유형을 결정하는 예시
        int currentHour = now.getHour();
        if (currentHour < 10) {
            return 1; // 아침
        } else if (currentHour < 15) {
            return 2; // 점심
        } else {
            return 3; // 저녁
        }
    }
}
