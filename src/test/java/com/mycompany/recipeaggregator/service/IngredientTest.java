package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.IngredientCreateDTO;
import com.mycompany.recipeaggregator.dto.IngredientUsageDTO;
import com.mycompany.recipeaggregator.models.Ingredient;
import com.mycompany.recipeaggregator.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository repository;

    @InjectMocks
    private IngredientService service;

    @Test
    void shouldListAllIngredients() throws SQLException {
        var ingredients = List.of(
                new Ingredient(1, "Sugar"),
                new Ingredient(2, "Salt")
        );

        when(repository.list()).thenReturn(ingredients);

        var result = service.listAll();

        assertEquals(2, result.size());
        assertEquals("Sugar", result.get(0).getName());
        assertEquals("Salt", result.get(1).getName());

        verify(repository).list();
    }

    @Test
    void shouldListMostUsedIngredients() throws SQLException {
        var expected = List.of(
                new IngredientUsageDTO(1, "Sugar", 10),
                new IngredientUsageDTO(2, "Salt", 5)
        );

        when(repository.listMostUsed()).thenReturn(expected);

        var result = service.listMostUsed();

        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getId());
        assertEquals("Sugar", result.get(0).getName());
        assertEquals(10, result.get(0).getUsageCount());

        verify(repository).listMostUsed();
    }

    @Test
    void shouldCreateIngredient() throws SQLException {
        var dto = new IngredientCreateDTO("Sugar");

        var entity = new Ingredient(0, "Sugar");
        var saved = new Ingredient(1, "Sugar");

        when(repository.insert(any(Ingredient.class))).thenReturn(saved);

        var result = service.create(dto);

        assertNotNull(result);
        assertEquals("Sugar", result.getName());

        verify(repository).insert(any(Ingredient.class));
    }

    @Test
    void shouldDeleteIngredient() throws SQLException {
        service.delete(1);

        verify(repository).delete(1);
    }

    @Test
    void shouldThrowWhenDeleteIdInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> service.delete(0));

        verifyNoInteractions(repository);
    }
}
