package com.mycompany.recipeaggregator.dao;

import com.mycompany.recipeaggregator.config.HibernateUtil;
import com.mycompany.recipeaggregator.models.Ingredient;
import com.mycompany.recipeaggregator.models.Recipe;
import com.mycompany.recipeaggregator.models.RecipeIngredient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.mycompany.recipeaggregator.repository.CrudRepository;
import java.util.List;

public class RecipeDAO implements CrudRepository<Recipe> {

    @Override
    public Recipe insert(Recipe recipe) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (recipe.getIngredients() != null) {
                for (RecipeIngredient ri : recipe.getIngredients()) {
                    Ingredient managed = session.get(Ingredient.class, ri.getIngredient().getId());
                    ri.setIngredient(managed);
                    ri.setRecipe(recipe);
                }
            }

            session.persist(recipe);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error inserting recipe", e);
        }

        return recipe;
    }

    @Override
    public List<Recipe> list() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Recipe> recipes = session
                    .createQuery("FROM Recipe r LEFT JOIN FETCH r.ingredients ri LEFT JOIN FETCH ri.ingredient", Recipe.class)
                    .list();

            System.out.println("Listing recipes");
            return recipes;

        } catch (Exception e) {
            throw new RuntimeException("Error listing recipes", e);
        }
    }

    @Override
    public Recipe update(Recipe recipe) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(recipe);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating recipe", e);
        }

        return recipe;
    }

    @Override
    public void delete(int id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Recipe recipe = session.get(Recipe.class, id);
            if (recipe != null) {
                session.remove(recipe);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error deleting recipe", e);
        }
    }
}
