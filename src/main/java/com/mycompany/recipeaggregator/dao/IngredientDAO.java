package com.mycompany.recipeaggregator.dao;

import com.mycompany.recipeaggregator.config.HibernateUtil;
import com.mycompany.recipeaggregator.dto.IngredientUsageDTO;
import com.mycompany.recipeaggregator.models.Ingredient;
import com.mycompany.recipeaggregator.repository.IngredientRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.*;
import java.util.List;

public class IngredientDAO implements IngredientRepository {

    @Override
    public List<Ingredient> list() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery("FROM Ingredient", Ingredient.class)
                    .list();

        } catch (Exception e) {
            throw new RuntimeException("Error listing ingredients", e);
        }
    }

    @Override
    public Ingredient insert(Ingredient ingredient) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(ingredient);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error inserting ingredient", e);
        }

        return ingredient;
    }

    @Override
    public Ingredient update(Ingredient ingredient) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(ingredient);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating ingredient", e);
        }

        return ingredient;
    }

    @Override
    public void delete(int id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Ingredient ingredient = session.get(Ingredient.class, id);
            if (ingredient != null) {
                session.remove(ingredient);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error deleting ingredient", e);
        }
    }

    public List<IngredientUsageDTO> listMostUsed() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("""
                SELECT new com.mycompany.recipeaggregator.dto.IngredientUsageDTO(
                    i.id,
                    i.name,
                    COUNT(ri.ingredient.id)
                )
                FROM RecipeIngredient ri
                JOIN ri.ingredient i
                GROUP BY i.id, i.name
                ORDER BY COUNT(ri.ingredient.id) DESC
            """, IngredientUsageDTO.class).list();

        } catch (Exception e) {
            throw new RuntimeException("Error listing most used ingredients", e);
        }
    }
}
