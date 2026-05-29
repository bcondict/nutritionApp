package edu.ucompensar.codigo.ui;

import edu.ucompensar.codigo.entity.NutritionPlan;
import edu.ucompensar.codigo.entity.Recipe;
import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.service.RecipeService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class DailyDietView extends JPanel {
    private final UUID userId;
    private NutritionPlan currentPlan;
    private RecipeService recipeService;
    
    private JLabel caloriesLabel;
    private JProgressBar caloriesProgress;
    private JLabel proteinLabel;
    private JProgressBar proteinProgress;
    private JLabel carbsLabel;
    private JProgressBar carbsProgress;
    private JLabel fatLabel;
    private JProgressBar fatProgress;
    
    private JPanel breakfastPanel;
    private JPanel lunchPanel;
    private JPanel dinnerPanel;
    private JPanel snacksPanel;
    
    private BigDecimal currentCalories = BigDecimal.ZERO;
    private BigDecimal currentProtein = BigDecimal.ZERO;
    private BigDecimal currentCarbs = BigDecimal.ZERO;
    private BigDecimal currentFat = BigDecimal.ZERO;

    public DailyDietView(UUID userId) {
        this.userId = userId;
        this.recipeService = new RecipeService();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    public void setNutritionPlan(NutritionPlan plan) {
        this.currentPlan = plan;
        initComponents();
        loadDailyMeals();
    }
    
    private void initComponents() {
        removeAll();
        
        // Panel superior - Macros del día
        JPanel macrosPanel = createMacrosPanel();
        add(macrosPanel, BorderLayout.NORTH);
        
        // Panel central - Comidas del día
        JPanel mealsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        breakfastPanel = createMealPanel("🌅 Desayuno");
        lunchPanel = createMealPanel("☀️ Almuerzo");
        dinnerPanel = createMealPanel("🌙 Cena");
        snacksPanel = createMealPanel("🍎 Snacks");
        
        mealsPanel.add(breakfastPanel);
        mealsPanel.add(lunchPanel);
        mealsPanel.add(dinnerPanel);
        mealsPanel.add(snacksPanel);
        
        add(mealsPanel, BorderLayout.CENTER);
        
        // Botón para agregar comidas
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addMealButton = new JButton("+ Agregar comida al día");
        addMealButton.addActionListener(e -> showAddFoodDialog());
        bottomPanel.add(addMealButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        revalidate();
        repaint();
    }
    
    private JPanel createMacrosPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Progreso del día",
            TitledBorder.CENTER,
            TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Calorías
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Calorías:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        caloriesProgress = new JProgressBar(0, currentPlan.getTargetCalories().intValue());
        caloriesProgress.setStringPainted(true);
        panel.add(caloriesProgress, gbc);
        
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        caloriesLabel = new JLabel("0 / " + currentPlan.getTargetCalories().intValue() + " kcal");
        panel.add(caloriesLabel, gbc);
        
        // Proteínas
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Proteínas:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        int proteinTarget = currentPlan.getTargetProteinPct().multiply(currentPlan.getTargetCalories())
            .divide(new BigDecimal("100"), 0, RoundingMode.HALF_UP).intValue();
        proteinProgress = new JProgressBar(0, proteinTarget);
        proteinProgress.setStringPainted(true);
        panel.add(proteinProgress, gbc);
        
        gbc.gridx = 3;
        proteinLabel = new JLabel("0 / " + proteinTarget + " g");
        panel.add(proteinLabel, gbc);
        
        // Carbohidratos
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Carbohidratos:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        int carbsTarget = currentPlan.getTargetCarbsPct().multiply(currentPlan.getTargetCalories())
            .divide(new BigDecimal("100"), 0, RoundingMode.HALF_UP).intValue();
        carbsProgress = new JProgressBar(0, carbsTarget);
        carbsProgress.setStringPainted(true);
        panel.add(carbsProgress, gbc);
        
        gbc.gridx = 3;
        carbsLabel = new JLabel("0 / " + carbsTarget + " g");
        panel.add(carbsLabel, gbc);
        
        // Grasas
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Grasas:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        int fatTarget = currentPlan.getTargetFatPct().multiply(currentPlan.getTargetCalories())
            .divide(new BigDecimal("100"), 0, RoundingMode.HALF_UP).intValue();
        fatProgress = new JProgressBar(0, fatTarget);
        fatProgress.setStringPainted(true);
        panel.add(fatProgress, gbc);
        
        gbc.gridx = 3;
        fatLabel = new JLabel("0 / " + fatTarget + " g");
        panel.add(fatLabel, gbc);
        
        return panel;
    }
    
    private JPanel createMealPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setPreferredSize(new Dimension(400, 300));
        return panel;
    }
    
    private void loadDailyMeals() {
        // Aquí cargarías las comidas del día desde la base de datos
        // Por ahora es un ejemplo
        addSampleRecipes();
    }
    
    private void addSampleRecipes() {
        // Ejemplo de cómo agregar recetas
        List<Recipe> breakfastRecipes = recipeService.findByMealType(MealType.BREAKFAST);
        for (Recipe recipe : breakfastRecipes) {
            addRecipeToMeal(breakfastPanel, recipe);
        }
    }
    
    private void addRecipeToMeal(JPanel mealPanel, Recipe recipe) {
        JPanel recipeCard = new JPanel(new BorderLayout());
        recipeCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        JLabel nameLabel = new JLabel(recipe.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        recipeCard.add(nameLabel, BorderLayout.NORTH);
        
        JLabel infoLabel = new JLabel(recipe.getPrepTimeFormatted() + " | " + recipe.getDifficulty());
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        recipeCard.add(infoLabel, BorderLayout.CENTER);
        
        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(e -> addRecipeToDailyLog(recipe));
        recipeCard.add(addButton, BorderLayout.EAST);
        
        mealPanel.add(recipeCard);
        mealPanel.revalidate();
        mealPanel.repaint();
    }
    
    private void addRecipeToDailyLog(Recipe recipe) {
        // Aquí calcularías y actualizarías los macros del día
        JOptionPane.showMessageDialog(this, "Receta agregada: " + recipe.getName());
        updateProgress(recipe);
    }
    
    private void updateProgress(Recipe recipe) {
        // Ejemplo de actualización de macros
        // Esto debería venir de los alimentos de la receta
        currentCalories = currentCalories.add(new BigDecimal("500"));
        currentProtein = currentProtein.add(new BigDecimal("30"));
        currentCarbs = currentCarbs.add(new BigDecimal("45"));
        currentFat = currentFat.add(new BigDecimal("20"));
        
        updateProgressBars();
    }
    
    private void updateProgressBars() {
        caloriesProgress.setValue(currentCalories.intValue());
        caloriesLabel.setText(currentCalories.intValue() + " / " + caloriesProgress.getMaximum() + " kcal");
        
        proteinProgress.setValue(currentProtein.intValue());
        proteinLabel.setText(currentProtein.intValue() + " / " + proteinProgress.getMaximum() + " g");
        
        carbsProgress.setValue(currentCarbs.intValue());
        carbsLabel.setText(currentCarbs.intValue() + " / " + carbsProgress.getMaximum() + " g");
        
        fatProgress.setValue(currentFat.intValue());
        fatLabel.setText(currentFat.intValue() + " / " + fatProgress.getMaximum() + " g");
    }
    
    private void showAddFoodDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agregar comida", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Buscar");
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        JList<String> resultsList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(resultsList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        searchButton.addActionListener(e -> {
            // Buscar alimentos
            String[] results = {"Pechuga de pollo - 200g", "Arroz integral - 150g", "Ensalada - 100g"};
            resultsList.setListData(results);
        });
        
        JButton addButton = new JButton("Agregar seleccionado");
        addButton.addActionListener(e -> {
            // Agregar alimento seleccionado
            dialog.dispose();
        });
        panel.add(addButton, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    public void showNoPlanMessage() {
        removeAll();
        JLabel message = new JLabel("No tienes un plan nutricional activo. Crea uno primero.", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 16));
        add(message);
        revalidate();
        repaint();
    }
}