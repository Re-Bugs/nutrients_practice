package CSDL.spring_ml_practice.controller;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.service.IngredientService;
import CSDL.spring_ml_practice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @GetMapping("/search_recipe")
    public List<Recipe> searchRecipe(@RequestParam String query) {
        return recipeService.searchRecipes(query);
    }

    @GetMapping("/search_ingredient")
    public List<Ingredient> searchIngredient(@RequestParam String query) {
        return ingredientService.searchIngredients(query);
    }
}
