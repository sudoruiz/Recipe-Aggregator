package com.mycompany.recipeaggregator;

public class Main {

    public static void main(String[] args) {
        RecipeDAO dao = new RecipeDAO();

        //Criando receita
        Recipe recipe = new Recipe(0, "Bolo de Cenoura",
                "Um bolo delicioso com cobertura de chocolate",
                "Cenoura, ovos, farinha, açúcar, chocolate", 60, 8);

        dao.insert(recipe);
        System.out.println("Receita inserida com sucesso!");

        //Listar receita
        System.out.println("Lista de receitas:");
        for (Recipe r : dao.list()) {
            System.out.println(r.getId() + " - " + r.getName());
        }
        
        //Atualizar 
        recipe.setDescription("Um bolo simples e delicioso com cobertura de chocolate caseiro");
        dao.update(recipe);
        System.out.println("Receita atualizada");
        
        //Deletar receita
        dao.delete(recipe.getId());
        System.out.println("Receita deletada!");
    }
}
