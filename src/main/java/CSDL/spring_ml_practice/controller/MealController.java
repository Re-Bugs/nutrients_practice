package CSDL.spring_ml_practice.controller;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.service.MealService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping("/add_meal")
    public String addMeal(Model model) {
        return "add_meal";
    }

    @GetMapping("/search_recipe")
    @ResponseBody
    public List<Recipe> searchRecipe(@RequestParam String query) {
        return mealService.searchRecipes(query);
    }

    @GetMapping("/search_ingredient")
    @ResponseBody
    public List<Ingredient> searchIngredient(@RequestParam String query) {
        return mealService.searchIngredients(query);
    }

    @PostMapping("/add_meal")
    public String addMeal(@RequestParam("selectedRecipeId") int selectedRecipeId,
                          @RequestParam("selectedIngredientIds") List<Integer> selectedIngredientIds,
                          HttpSession session) {
        // 현재 로그인한 사용자 정보 가져오기
        String memberEmail = (String) session.getAttribute("memberEmail");

        // 현재 시간을 이용해 meal_log 생성
        mealService.addMealLog(memberEmail, selectedRecipeId, selectedIngredientIds);

        return "redirect:/main";
    }
}
