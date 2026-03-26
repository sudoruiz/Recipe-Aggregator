package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.RecipeCreateDTO;
import com.mycompany.recipeaggregator.dto.RecipeResponseDTO;
import com.mycompany.recipeaggregator.models.Recipe;
import com.mycompany.recipeaggregator.repository.CrudRepository;
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
class RecipeServiceTest {

    @Mock
    private CrudRepository repository;

    @InjectMocks
    private RecipeService service;

    @Test
    void shouldReturnListOfRecipeDTOs() throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Bolo");
        recipe.setIngredients(List.of());

        when(repository.list()).thenReturn(List.of(recipe));

        List<RecipeResponseDTO> result = service.listRecipes();

        assertEquals(1, result.size());
        assertEquals("Bolo", result.get(0).getName());

        verify(repository).list();
    }

   @Test
    void shouldCreateRecipeWhenValid() throws SQLException {
        RecipeCreateDTO dto = new RecipeCreateDTO();
        dto.setName("Pizza");

        Recipe result = service.createRecipe(dto);

        assertEquals("Pizza", result.getName());
        verify(repository).insert(any(Recipe.class));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() throws SQLException {
        RecipeCreateDTO dto = new RecipeCreateDTO();
        dto.setName("");

        assertThrows(IllegalArgumentException.class,
                () -> service.createRecipe(dto));

        verify(repository, never()).insert(any());
    }

   @Test
    void shouldUpdateRecipeWhenValid() throws SQLException {
        RecipeCreateDTO dto = new RecipeCreateDTO();
        dto.setName("Lasanha");

        service.updateRecipe(1, dto);

        verify(repository).update(any(Recipe.class));
    }

  @Test
    void shouldThrowExceptionWhenUpdateIdIsInvalid() throws SQLException {
        RecipeCreateDTO dto = new RecipeCreateDTO();
        dto.setName("Teste");

        assertThrows(IllegalArgumentException.class,
                () -> service.updateRecipe(0, dto));

        verify(repository, never()).update(any());
    }

   @Test
    void shouldThrowExceptionWhenUpdateDtoIsNull() throws SQLException {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateRecipe(1, null));

        verify(repository, never()).update(any());
    }

  @Test
    void shouldDeleteRecipeWhenIdIsValid() throws SQLException {
        service.deleteRecipe(1);

        verify(repository).delete(1);
    }

   @Test
    void shouldThrowExceptionWhenDeleteIdIsInvalid() throws SQLException {
        assertThrows(IllegalArgumentException.class,
                () -> service.deleteRecipe(0));

        verify(repository, never()).delete(anyInt());
    }
}



