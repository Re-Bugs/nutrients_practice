package CSDL.spring_ml_practice.controller;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.service.MealService;
import CSDL.spring_ml_practice.service.RecipeService;
import CSDL.spring_ml_practice.service.IngredientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MealController {

    private final MealService mealService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @PostMapping("/add_meal")
    public String addMeal(@RequestParam("selectedRecipeId") int selectedRecipeId,
                          @RequestParam("selectedIngredientIds") List<Integer> selectedIngredientIds,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        String memberEmail = (String) session.getAttribute("memberEmail");
        if (memberEmail == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/sign_in";
        }

        mealService.addMealLog(memberEmail, selectedRecipeId, selectedIngredientIds);
        return "redirect:/main";
    }

    @GetMapping("/meal/calories")
    @ResponseBody
    public Map<String, Integer> getCalories(HttpSession session) {
        String memberEmail = (String) session.getAttribute("memberEmail");
        if (memberEmail == null) {
            return Map.of(
                    "totalCalories", 0,
                    "breakfastCalories", 0,
                    "lunchCalories", 0,
                    "dinnerCalories", 0
            );
        }

        Map<Integer, Integer> caloriesByMealType = mealService.getCaloriesByMealType(memberEmail);
        int totalCalories = mealService.getTotalCalories(memberEmail);

        return Map.of(
                "totalCalories", totalCalories,
                "breakfastCalories", caloriesByMealType.getOrDefault(1, 0),
                "lunchCalories", caloriesByMealType.getOrDefault(2, 0),
                "dinnerCalories", caloriesByMealType.getOrDefault(3, 0)
        );
    }

    @GetMapping("/main")
    public String welcome(HttpSession session, Model model) {
        String memberEmail = (String) session.getAttribute("memberEmail");

        if (memberEmail == null) {
            return "redirect:/login";
        }

        int totalCalories = mealService.getTotalCalories(memberEmail);
        Map<Integer, Integer> caloriesByMeal = mealService.getCaloriesByMealType(memberEmail);

        model.addAttribute("totalCalories", totalCalories);
        model.addAttribute("caloriesByMeal", caloriesByMeal);

        return "main";
    }

    @GetMapping("/add_meal")
    public String showAddMealForm(@RequestParam(value = "mealType", required = false) Integer mealType, Model model) {
        model.addAttribute("mealType", mealType);
        return "add_meal";
    }

    @GetMapping("/search_recipes")
    @ResponseBody
    public List<Recipe> searchRecipes(@RequestParam("query") String query) {
        return recipeService.searchRecipes(query);
    }

    @GetMapping("/search_ingredients")
    @ResponseBody
    public List<Ingredient> searchIngredients(@RequestParam("query") String query) {
        return ingredientService.searchIngredients(query);
    }
}
