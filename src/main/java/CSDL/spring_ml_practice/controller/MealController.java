package CSDL.spring_ml_practice.controller;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.Recipe;
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
            return "redirect:/sign_in";
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
            return "redirect:/sign_in";
        }

        List<Integer> selectedIngredientIds = Arrays.stream(selectedIngredientIdsString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        log.debug("Adding meal log for memberEmail: {}, selectedRecipeId: {}, selectedIngredientIds: {}", memberEmail, selectedRecipeId, selectedIngredientIds);
        mealService.addMealLog(memberEmail, selectedRecipeId, selectedIngredientIds, mealImage);

        log.debug("Exiting addMeal");
        return "redirect:/main";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        log.error("An error occurred: ", exception);
        model.addAttribute("errorMessage", exception.getMessage());
        return "error";
    }
}
