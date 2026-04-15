package com.mycompany.recipeaggregator.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.mycompany.recipeaggregator.config.HibernateUtil;
import com.mycompany.recipeaggregator.models.RecipeIngredient;
import com.mycompany.recipeaggregator.repository.RecipeIngredientRepository;

import java.sql.*;
import java.util.List;

public class RecipeIngredientDAO implements RecipeIngredientRepository {

    @Override
    public List<RecipeIngredient> list() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery("FROM RecipeIngredient", RecipeIngredient.class)
                    .list();

        } catch (Exception e) {
            throw new RuntimeException("Error listing recipe ingredients", e);
        }
    }

    @Override
    public RecipeIngredient insert(RecipeIngredient ri) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(ri);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error inserting recipe ingredient", e);
        }

        return ri;
    }

    @Override
    public RecipeIngredient update(RecipeIngredient ri) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(ri);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating recipe ingredient", e);
        }

        return ri;
    }

    @Override
    public void delete(int recipeId) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createMutationQuery("""
                DELETE FROM RecipeIngredient ri
                WHERE ri.recipe.id = :recipeId
            """)
                    .setParameter("recipeId", recipeId)
                    .executeUpdate();

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error deleting recipe ingredients", e);
        }
    }

    @Override
    public List<RecipeIngredient> findByRecipeId(int recipeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("""
                FROM RecipeIngredient ri
                WHERE ri.recipe.id = :recipeId
            """, RecipeIngredient.class)
                    .setParameter("recipeId", recipeId)
                    .list();

        } catch (Exception e) {
            throw new RuntimeException("Error finding recipe ingredients", e);
        }
    }

    @Override
    public void removeIngredient(int recipeId, int ingredientId) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createMutationQuery("""
                DELETE FROM RecipeIngredient ri
                WHERE ri.recipe.id = :recipeId
                AND ri.ingredient.id = :ingredientId
            """)
                    .setParameter("recipeId", recipeId)
                    .setParameter("ingredientId", ingredientId)
                    .executeUpdate();

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error removing ingredient from recipe", e);
        }
    }
}