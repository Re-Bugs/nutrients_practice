package CSDL.spring_ml_practice.controller;

import CSDL.spring_ml_practice.service.MealService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MealController {

    private final MealService mealService;

    @GetMapping("/add_meal")
    public String addMealPage(Model model, HttpSession session) {
        log.debug("Entering addMealPage");

        String memberEmail = (String) session.getAttribute("memberEmail");

        if (memberEmail == null) {
            log.warn("Member email is null, redirecting to login");
            return "redirect:/login";
        }

        model.addAttribute("recipes", mealService.searchRecipes(""));
        model.addAttribute("ingredients", mealService.searchIngredients(""));
        log.debug("Exiting addMealPage");
        return "add_meal";
    }

    @PostMapping("/add_meal")
    public String addMeal(@RequestParam("selectedRecipeId") int selectedRecipeId,
                          @RequestParam("selectedIngredientIds") String selectedIngredientIdsString,
                          @RequestParam("mealImage") MultipartFile mealImage,
                          HttpSession session) throws IOException {
        log.debug("Entering addMeal");

        String memberEmail = (String) session.getAttribute("memberEmail");

        if (memberEmail == null) {
            log.warn("Member email is null, redirecting to login");
            return "redirect:/login";
        }

        List<Integer> selectedIngredientIds = Arrays.stream(selectedIngredientIdsString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        log.debug("Adding meal log for memberEmail: {}, selectedRecipeId: {}, selectedIngredientIds: {}", memberEmail, selectedRecipeId, selectedIngredientIds);
        mealService.addMealLog(memberEmail, selectedRecipeId, selectedIngredientIds, mealImage);

        log.debug("Exiting addMeal");
        return "redirect:/main";
    }

    @GetMapping("/api/meal/calories")
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

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        log.error("An error occurred: ", exception);
        model.addAttribute("errorMessage", exception.getMessage());
        return "error";
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
}
