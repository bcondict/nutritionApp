package edu.ucompensar.codigo.ui;

import edu.ucompensar.codigo.entity.FoodItem;
import edu.ucompensar.codigo.entity.NutritionPlan;
import edu.ucompensar.codigo.entity.Recipe;
import edu.ucompensar.codigo.model.enums.FoodCategory;
import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.service.FoodItemService;
import edu.ucompensar.codigo.service.RecipeService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DailyDietView extends JPanel {
    private final UUID userId;
    private NutritionPlan currentPlan;
    private RecipeService recipeService;
    private FoodItemService foodItemService;

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
        this.foodItemService = new FoodItemService();
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
        // System.out.println("Cantidad actual: " + snacksPanel.getComponentCount());
        mealsPanel.add(snacksPanel);
        // System.out.println("Cantidad después: " + snacksPanel.getComponentCount());

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

        // List<FoodItem> breakfastItems = foodItemService.findByCategory("BREAKFAST");
        // for (FoodItem item : breakfastItems) {
        // addFoodItemToMeal(breakfastPanel, item);
        // }

        // List<FoodItem> lunchItems = foodItemService.findByCategory("LUNCH");
        // for (FoodItem item : lunchItems) {
        // addFoodItemToMeal(lunchPanel, item);
        // }

        // // List<FoodItem> dinnerItems =
        // foodItemService.findByCategory(FoodCategory.);
        // for (FoodItem item : dinnerItems) {
        // addFoodItemToMeal(dinnerPanel, item);
        // }

        // List<FoodItem> snackItems =
        // foodItemService.findByCategory(FoodCategory.SNACK);
        // for (FoodItem item : snackItems) {
        // addFoodItemToMeal(snacksPanel, item);
        // }
    }

    // private void addFoodItemToMeal(JPanel mealPanel, FoodItem item) {
    // JPanel itemCard = new JPanel(new BorderLayout());
    // itemCard.setBorder(BorderFactory.createCompoundBorder(
    // BorderFactory.createLineBorder(Color.LIGHT_GRAY),
    // BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    // JLabel nameLabel = new JLabel(item.getName());
    // nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
    // itemCard.add(nameLabel, BorderLayout.NORTH);

    // JLabel caloriesLabel = new JLabel(item.getCaloriesPer100g() + " kcal/100g");
    // caloriesLabel.setFont(new Font("Arial", Font.PLAIN, 10));
    // itemCard.add(caloriesLabel, BorderLayout.CENTER);

    // JButton addButton = new JButton("Agregar");
    // addButton.addActionListener(e -> addFoodItemToDailyLog(item));
    // itemCard.add(addButton, BorderLayout.EAST);

    // mealPanel.add(itemCard);
    // mealPanel.revalidate();
    // mealPanel.repaint();
    // }
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

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Buscar");
        JComboBox<String> categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todos");
        for (FoodCategory category : FoodCategory.values()) {
            categoryFilter.addItem(category.name());
        }

        JPanel filterPanel = new JPanel(new BorderLayout(5, 5));
        filterPanel.add(new JLabel("Categoría:"), BorderLayout.WEST);
        filterPanel.add(categoryFilter, BorderLayout.CENTER);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(filterPanel, BorderLayout.CENTER);

        // Lista de resultados
        DefaultListModel<FoodItem> listModel = new DefaultListModel<>();
        JList<FoodItem> resultsList = new JList<>(listModel);
        resultsList.setCellRenderer(new FoodItemListRenderer());
        JScrollPane scrollPane = new JScrollPane(resultsList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de cantidad
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel quantityLabel = new JLabel("Cantidad (g):");
        JTextField quantityField = new JTextField(10);
        quantityField.setText("100");
        JButton addButton = new JButton("Agregar seleccionado");
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);
        quantityPanel.add(addButton);
        mainPanel.add(quantityPanel, BorderLayout.SOUTH);

        // Cargar todos los alimentos al inicio
        loadFoodItemsToList(listModel, null, null);

        // Evento de búsqueda
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            String category = (String) categoryFilter.getSelectedItem();
            if ("Todas".equals(category)) {
                category = null;
            }
            loadFoodItemsToList(listModel, keyword, FoodCategory.valueOf(category));
        });

        categoryFilter.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            String category = (String) categoryFilter.getSelectedItem();
            if ("Todas".equals(category)) {
                category = null;
            }
            loadFoodItemsToList(listModel, keyword, FoodCategory.valueOf(category));
        });

        // Evento de agregar
        addButton.addActionListener(e -> {
            FoodItem selected = resultsList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(dialog, "Seleccione un alimento");
                return;
            }

            try {
                BigDecimal grams = new BigDecimal(quantityField.getText());
                if (grams.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(dialog, "La cantidad debe ser mayor a 0");
                    return;
                }

                addFoodItemToDailyLog(selected, grams);
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Ingrese una cantidad válida");
            }
        });

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void loadFoodItemsToList(DefaultListModel<FoodItem> listModel, String keyword, FoodCategory category) {
        listModel.clear();
        FoodItemService foodItemService = new FoodItemService();
        List<FoodItem> items;

        if (keyword != null && !keyword.isEmpty()) {
            items = foodItemService.findByNameContaining(keyword);
            if (category != null) {
                items = items.stream()
                        .filter(item -> category.equals(item.getCategory()))
                        .collect(Collectors.toList());
            }
        } else if (category != null) {
            items = foodItemService.findByCategory(category);
        } else {
            items = foodItemService.findAll();
        }

        for (FoodItem item : items) {
            listModel.addElement(item);
        }
    }

    private void addFoodItemToDailyLog(FoodItem item, BigDecimal grams) {
        BigDecimal factor = grams.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        BigDecimal calories = item.getCaloriesPer100g().multiply(factor);
        BigDecimal protein = item.getProteinPer100g().multiply(factor);
        BigDecimal carbs = item.getCarbsPer100g().multiply(factor);
        BigDecimal fat = item.getFatPer100g().multiply(factor);

        currentCalories = currentCalories.add(calories);
        currentProtein = currentProtein.add(protein);
        currentCarbs = currentCarbs.add(carbs);
        currentFat = currentFat.add(fat);

        updateProgressBars();
        addFoodItemToMealPanel(item, grams);

        String message = String.format(
                "Agregado: %s - %.0fg\nCalorías: %.0f | Proteínas: %.1fg | Carbos: %.1fg | Grasas: %.1fg",
                item.getName(), grams, calories, protein, carbs, fat);
        JOptionPane.showMessageDialog(this, message);
    }

    // Renderer personalizado para JList
    private class FoodItemListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof FoodItem) {
                FoodItem item = (FoodItem) value;
                label.setText(String.format("%s - %.0f kcal/100g | Proteínas: %.1fg | Carbos: %.1fg | Grasas: %.1fg",
                        item.getName(),
                        item.getCaloriesPer100g(),
                        item.getProteinPer100g(),
                        item.getCarbsPer100g(),
                        item.getFatPer100g()));
            }

            return label;
        }
    }

    public void showNoPlanMessage() {
        removeAll();
        JLabel message = new JLabel("No tienes un plan nutricional activo. Crea uno primero.", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 16));
        add(message);
        revalidate();
        repaint();

    }

    private void addFoodItemToMealPanel(FoodItem item, BigDecimal grams) {
        JPanel targetPanel = getMealPanelByCategory(item.getName());
        final JPanel finalTargetPanel = (targetPanel == null) ? snacksPanel : targetPanel; // Por defecto a snacks si no encuentra categoría

        JPanel itemCard = new JPanel(new BorderLayout());
        itemCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));

        JLabel nameLabel = new JLabel(item.getName() + " (" + grams + "g)");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 11));
        infoPanel.add(nameLabel);

        BigDecimal calories = item.getCaloriesPer100g()
                .multiply(grams.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        JLabel caloriesLabel = new JLabel(String.format("%.0f kcal | P:%.1fg | C:%.1fg | G:%.1fg",
                calories,
                item.getProteinPer100g().multiply(grams.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)),
                item.getCarbsPer100g().multiply(grams.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)),
                item.getFatPer100g().multiply(grams.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP))));
        caloriesLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        infoPanel.add(caloriesLabel);

        itemCard.add(infoPanel, BorderLayout.CENTER);

        JButton removeButton = new JButton("❌");
        removeButton.setPreferredSize(new Dimension(30, 30));
        removeButton.addActionListener(e -> {
            finalTargetPanel.remove(itemCard);
            finalTargetPanel.revalidate();
            finalTargetPanel.repaint();
            // Aquí también deberías restar los macros
        });
        itemCard.add(removeButton, BorderLayout.EAST);

        finalTargetPanel.add(itemCard);
        finalTargetPanel.revalidate();
        finalTargetPanel.repaint();
    }

    private JPanel getMealPanelByCategory(String category) {
        switch (category) {
            case "REAKFAST":
                return breakfastPanel;
            case "LUNCH":
                return lunchPanel;
            case "DINNER":
                return dinnerPanel;
            case "SNACK":
                return snacksPanel;
            default:
                // Mapear otras categorías a comidas específicas
                if (category.equals("FRUITS") || category.equals("GRAINS") || category.equals("DAIRY")) {
                    return breakfastPanel;
                } else if (category.equals("MEAT") || category.equals("POULTRY") || category.equals("FISH")) {
                    return lunchPanel;
                } else if (category.equals("VEGETABLES")) {
                    return dinnerPanel;
                } else {
                    return snacksPanel;
                }
        }
    }
}