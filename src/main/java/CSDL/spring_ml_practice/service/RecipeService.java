package CSDL.spring_ml_practice.service;

import CSDL.spring_ml_practice.domain.Recipe;
import CSDL.spring_ml_practice.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.findByNameContaining(query);
    }
}
