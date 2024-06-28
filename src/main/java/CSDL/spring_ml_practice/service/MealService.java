package CSDL.spring_ml_practice.service;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.MealLog;
import CSDL.spring_ml_practice.domain.MealLogsAndIngredients;
import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.repository.MealLogRepository;
import CSDL.spring_ml_practice.repository.MealLogsAndIngredientsRepository;
import CSDL.spring_ml_practice.repository.RecipeRepository;
import CSDL.spring_ml_practice.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealLogRepository mealLogRepository;
    private final MealLogsAndIngredientsRepository mealLogsAndIngredientsRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public void addMealLog(String memberEmail, int selectedRecipeId, List<Integer> selectedIngredientIds, MultipartFile mealImage) throws IOException {
        // 이미지 저장 경로 설정
        String uploadDir = "uploaded_images/";
        String fileName = mealImage.getOriginalFilename();
        File uploadFile = new File(uploadDir + fileName);
        mealImage.transferTo(uploadFile);

        // 오늘 날짜의 식사 기록 확인
        List<MealLog> todayLogs = mealLogRepository.findByMemberEmailAndToday(memberEmail);
        int meal = determineMealType(todayLogs);

        // 식사 로그 생성
        MealLog mealLog = new MealLog();
        mealLog.setMemberEmail(memberEmail);
        mealLog.setLogDate(LocalDateTime.now());
        mealLog.setMeal(meal);
        mealLog.setPicture(uploadDir + fileName);
        mealLog = mealLogRepository.save(mealLog);

        // 레시피의 재료를 찾아서 식사 로그에 추가
        List<Integer> ingredientIds = ingredientRepository.findIngredientsByRecipeId(selectedRecipeId);
        for (Integer ingredientId : ingredientIds) {
            MealLogsAndIngredients mealLogsAndIngredients = new MealLogsAndIngredients();
            mealLogsAndIngredients.setMealLogId(mealLog.getMealLogId());
            mealLogsAndIngredients.setIngredientId(ingredientId);
            mealLogsAndIngredientsRepository.save(mealLogsAndIngredients);
        }

        // 선택된 추가 재료도 식사 로그에 추가
        for (Integer ingredientId : selectedIngredientIds) {
            if (!ingredientIds.contains(ingredientId)) {
                MealLogsAndIngredients mealLogsAndIngredients = new MealLogsAndIngredients();
                mealLogsAndIngredients.setMealLogId(mealLog.getMealLogId());
                mealLogsAndIngredients.setIngredientId(ingredientId);
                mealLogsAndIngredientsRepository.save(mealLogsAndIngredients);
            }
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

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.findByNameContaining(query);
    }

    public List<Ingredient> searchIngredients(String query) {
        return ingredientRepository.findByNameContaining(query);
    }
}
