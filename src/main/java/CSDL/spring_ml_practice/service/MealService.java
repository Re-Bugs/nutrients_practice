package CSDL.spring_ml_practice.service;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.MealLog;
import CSDL.spring_ml_practice.domain.MealLogsAndIngredients;
import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.repository.IngredientRepository;
import CSDL.spring_ml_practice.repository.MealLogRepository;
import CSDL.spring_ml_practice.repository.MealLogsAndIngredientsRepository;
import CSDL.spring_ml_practice.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealLogRepository mealLogRepository;
    private final MealLogsAndIngredientsRepository mealLogsAndIngredientsRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public void addMealLog(String memberEmail, int selectedRecipeId, List<Integer> selectedIngredientIds) {
        // 오늘 날짜의 식사 기록 확인
        List<MealLog> todayLogs = mealLogRepository.findByMemberEmailAndToday(memberEmail);
        int meal = determineMealType(todayLogs);

        // 식사 로그 생성
        MealLog mealLog = new MealLog();
        mealLog.setMemberEmail(memberEmail);
        mealLog.setLogDate(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
        mealLog.setMeal(meal);
        mealLog = mealLogRepository.save(mealLog);

        // 식사 로그에 재료 추가
        for (Integer ingredientId : selectedIngredientIds) {
            MealLogsAndIngredients mealLogsAndIngredients = new MealLogsAndIngredients();
            mealLogsAndIngredients.setMealLogId(mealLog.getMealLogId());
            mealLogsAndIngredients.setIngredientId(ingredientId);
            mealLogsAndIngredientsRepository.save(mealLogsAndIngredients);
        }
    }

    private int determineMealType(List<MealLog> todayLogs) {
        boolean hasBreakfast = false;
        boolean hasLunch = false;
        boolean hasDinner = false;

        for (MealLog log : todayLogs) {
            if (log.getMeal() == 1) {
                hasBreakfast = true;
            } else if (log.getMeal() == 2) {
                hasLunch = true;
            } else if (log.getMeal() == 3) {
                hasDinner = true;
            }
        }

        if (!hasBreakfast) {
            return 1;
        } else if (!hasLunch) {
            return 2;
        } else if (!hasDinner) {
            return 3;
        } else {
            throw new IllegalStateException("All meals for today have been logged.");
        }
    }

    public Map<Integer, Integer> getCaloriesByMealType(String memberEmail) {
        List<MealLog> todayLogs = mealLogRepository.findByMemberEmailAndToday(memberEmail);
        return todayLogs.stream()
                .collect(Collectors.toMap(
                        MealLog::getMeal,
                        log -> mealLogsAndIngredientsRepository.findByMealLogId(log.getMealLogId()).stream()
                                .mapToInt(ingredient -> ingredientRepository.findById(ingredient.getIngredientId())
                                        .map(ing -> (int) ing.getCalorie())
                                        .orElse(0))
                                .sum()
                ));
    }

    public int getTotalCalories(String memberEmail) {
        return getCaloriesByMealType(memberEmail).values().stream().mapToInt(Integer::intValue).sum();
    }

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.findByNameContaining(query);
    }

    public List<Ingredient> searchIngredients(String query) {
        return ingredientRepository.findByNameContaining(query);
    }
}
