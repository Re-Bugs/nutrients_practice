package CSDL.spring_ml_practice.service;

import CSDL.spring_ml_practice.domain.Ingredient;
import CSDL.spring_ml_practice.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public List<Ingredient> searchIngredients(String query) {
        return ingredientRepository.findByNameContaining(query);
    }

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }
}
