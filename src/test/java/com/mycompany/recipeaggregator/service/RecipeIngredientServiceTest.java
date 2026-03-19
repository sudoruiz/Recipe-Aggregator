package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.RecipeIngredientPatchDTO;
import com.mycompany.recipeaggregator.repository.RecipeIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

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

        service.patchIngredients(10, dto);

        verify(repository).addIngredient(10, 1, 2, "kg");
    }

    @Test
    void shouldRemoveIngredient() throws SQLException {
        dto.setAction("remove");
        dto.setIngredientId(1);

        service.patchIngredients(10, dto);

        verify(repository).removeIngredient(10, 1);
    }

    @Test
    void shouldUpdateIngredient() throws SQLException {
        dto.setAction("update");
        dto.setIngredientId(1);
        dto.setQuantity(5);
        dto.setUnit("g");

        service.patchIngredients(10, dto);

        verify(repository).updateIngredient(10, 1, 5, "g");
    }

    @Test
    void shouldThrowWhenRecipeIdInvalid() {
        dto.setAction("remove");
        dto.setIngredientId(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.patchIngredients(0, dto));

        verifyNoInteractions(repository);
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
