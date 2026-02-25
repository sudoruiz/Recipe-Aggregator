package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.IngredientCreateDTO;
import com.mycompany.recipeaggregator.dto.IngredientResponseDTO;
import com.mycompany.recipeaggregator.dto.IngredientUsageDTO;
import com.mycompany.recipeaggregator.dto.Mapper;
import com.mycompany.recipeaggregator.models.Ingredient;
import com.mycompany.recipeaggregator.repository.IngredientRepository;

import java.sql.SQLException;
import java.util.List;

public class IngredientService {

    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public List<IngredientResponseDTO> listAll() throws SQLException {

        List<Ingredient> ingredients = repository.listAll();

        return ingredients.stream()
                .map(Mapper::toDTO)
                .toList();
    }

    public List<IngredientUsageDTO> listMostUsed() throws SQLException {
        return repository.listMostUsed();
    }

    public IngredientResponseDTO create(IngredientCreateDTO dto)
            throws SQLException {

        Ingredient entity = Mapper.toEntity(dto);

        Ingredient saved = repository.create(entity);

        return Mapper.toDTO(saved);
    }

    public void delete(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        repository.delete(id);
    }
}
