package CSDL.spring_ml_practice.api;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.service.IngredientService;
import CSDL.spring_ml_practice.service.MealService;
import CSDL.spring_ml_practice.service.RecipeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class APIMealController {

    private final MealService mealService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;


    @GetMapping("/calories/today")
    public ResponseEntity<Map<String, String>> getTodayCalories(HttpSession session) {
        String memberEmail = (String) session.getAttribute("memberEmail");
        Map<String, String> response = new HashMap<>();

        Map<Integer, Integer> caloriesByMeal = mealService.getCaloriesByMealType(memberEmail);
        int totalCalories = mealService.getTotalCalories(memberEmail);

        response.put("totalCalories", totalCalories != 0 ? String.valueOf(totalCalories) : "none");
        response.put("breakfastCalories", caloriesByMeal.getOrDefault(1, 0) != 0 ? String.valueOf(caloriesByMeal.get(1)) : "none");
        response.put("lunchCalories", caloriesByMeal.getOrDefault(2, 0) != 0 ? String.valueOf(caloriesByMeal.get(2)) : "none");
        response.put("dinnerCalories", caloriesByMeal.getOrDefault(3, 0) != 0 ? String.valueOf(caloriesByMeal.get(3)) : "none");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Map<String, Object>>> searchRecipes(@RequestParam String keyword) {
        List<Recipe> recipes = recipeService.searchRecipes(keyword);
        List<Map<String, Object>> response = recipes.stream()
                .map(recipe -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", recipe.getRecipeId());
                    map.put("name", recipe.getName());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Map<String, Object>>> searchIngredients(@RequestParam String keyword) {
        List<Ingredient> ingredients = ingredientService.searchIngredients(keyword);
        List<Map<String, Object>> response = ingredients.stream()
                .map(ingredient -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", ingredient.getIngredientId());
                    map.put("name", ingredient.getName());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add_meal")
    public ResponseEntity<Map<String, String>> addMixedMealLog(
            @RequestParam("mealType") int mealType,
            @RequestParam(value = "recipes", required = false) List<Integer> recipeIds,
            @RequestParam(value = "ingredients", required = false) List<Integer> ingredientIds,
            HttpSession session) {

        String memberEmail = (String) session.getAttribute("memberEmail");
        Map<String, String> response = new HashMap<>();
        if (memberEmail == null) {
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        mealService.addMixedMealLog(memberEmail, mealType, recipeIds, ingredientIds);
        response.put("message", "식사 기록이 저장되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/nutrition/today")
    public ResponseEntity<Map<String, Object>> getTodayNutrientAnalysis(HttpSession session) {
        String memberEmail = (String) session.getAttribute("memberEmail");
        if (memberEmail == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }

        Map<String, Object> analysis = mealService.getTodayNutrientAnalysis(memberEmail);
        return ResponseEntity.ok(analysis);
    }
}
