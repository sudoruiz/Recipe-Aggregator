package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.RecipeIngredientPatchDTO;
import com.mycompany.recipeaggregator.models.Ingredient;
import com.mycompany.recipeaggregator.models.Recipe;
import com.mycompany.recipeaggregator.repository.RecipeIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.recipeaggregator.models.RecipeIngredient;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeIngredientServiceTest {

    @Mock
    private RecipeIngredientRepository repository;

    @InjectMocks
    private RecipeIngredientService service;

    private RecipeIngredientPatchDTO dto;

    @BeforeEach
    void setup() {
        dto = new RecipeIngredientPatchDTO();
    }

    @Test
    void shouldAddIngredient() throws SQLException {
        dto.setAction("add");
        dto.setIngredientId(1);
        dto.setQuantity(2);
        dto.setUnit("kg");

        Recipe recipe = new Recipe();
        recipe.setId(10);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);

        var expected = List.of(
                new RecipeIngredient(recipe, ingredient, 2, "kg")
        );

        when(repository.findByRecipeId(10)).thenReturn(expected);

        var result = service.patchIngredients(10, dto);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getIngredient().getId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals("kg", result.get(0).getUnit());

        verify(repository).insert(any(RecipeIngredient.class));
        verify(repository).findByRecipeId(10);
    }

    @Test
    void shouldRemoveIngredient() throws SQLException {
        dto.setAction("remove");
        dto.setIngredientId(1);

        List<RecipeIngredient> expected = List.of();

        when(repository.findByRecipeId(10)).thenReturn(expected);

        var result = service.patchIngredients(10, dto);

        assertTrue(result.isEmpty());

        verify(repository).removeIngredient(10, 1);
        verify(repository).findByRecipeId(10);
    }

    @Test
    void shouldUpdateIngredient() throws SQLException {
        dto.setAction("update");
        dto.setIngredientId(1);
        dto.setQuantity(5);
        dto.setUnit("g");

        Recipe recipe = new Recipe();
        recipe.setId(10);

        Ingredient ingredientEntity = new Ingredient();
        ingredientEntity.setId(1);

        var expected = List.of(
                new RecipeIngredient(recipe, ingredientEntity, 5, "g")
        );

        when(repository.findByRecipeId(10)).thenReturn(expected);

        var result = service.patchIngredients(10, dto);

        var ingredient = result.get(0);

        assertEquals(5, ingredient.getQuantity());
        assertEquals("g", ingredient.getUnit());

        verify(repository).update(any(RecipeIngredient.class));
        verify(repository).findByRecipeId(10);
    }

    @Test
    void shouldThrowWhenDtoIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.patchIngredients(1, null));

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenActionIsNull() {
        dto.setIngredientId(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.patchIngredients(1, dto));

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenIngredientIdInvalid() {
        dto.setAction("remove");
        dto.setIngredientId(0);

        assertThrows(IllegalArgumentException.class,
                () -> service.patchIngredients(1, dto));

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenActionIsInvalid() {
        dto.setAction("invalid");
        dto.setIngredientId(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.patchIngredients(1, dto));

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenQuantityInvalidOnAdd() {
        dto.setAction("add");
        dto.setIngredientId(1);
        dto.setQuantity(0);
        dto.setUnit("kg");

        assertThrows(IllegalArgumentException.class,
                () -> service.patchIngredients(1, dto));

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenUnitIsBlankOnUpdate() {
        dto.setAction("update");
        dto.setIngredientId(1);
        dto.setQuantity(2);
        dto.setUnit("");

        assertThrows(IllegalArgumentException.class,
                () -> service.patchIngredients(1, dto));

        verifyNoInteractions(repository);
    }
}
