package edu.ucompensar.codigo.ui;

import edu.ucompensar.codigo.entity.NutritionPlan;
import edu.ucompensar.codigo.entity.WeeklyMenu;
import edu.ucompensar.codigo.service.NutritionPlanService;
import edu.ucompensar.codigo.service.UserProfileService;
import edu.ucompensar.codigo.service.WeeklyMenuService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.UUID;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
    private JButton createPlanButton;
    private JButton refreshButton;
    private JLabel planInfoLabel;
    
    private final UUID userId;
    private final NutritionPlanService planService;
    private final UserProfileService profileService;
    private final WeeklyMenuService weeklyMenuService;
    
    private NutritionPlan currentPlan;
    private DailyDietView dailyDietView;
    private WeeklyDietView weeklyDietView;

    public MainView(UUID userId) {
        this.userId = userId;
        this.planService = new NutritionPlanService();
        this.profileService = new UserProfileService();
        this.weeklyMenuService = new WeeklyMenuService();
        
        setTitle("Nutrition App - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        planInfoLabel = new JLabel("Cargando plan nutricional...");
        planInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(planInfoLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createPlanButton = new JButton("+ Nuevo Plan");
        refreshButton = new JButton("🔄 Actualizar");
        buttonPanel.add(createPlanButton);
        buttonPanel.add(refreshButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Tabbed pane para vista diaria/semanal
        tabbedPane = new JTabbedPane();
        dailyDietView = new DailyDietView(userId);
        weeklyDietView = new WeeklyDietView(userId);
        
        tabbedPane.addTab("Vista Diaria", dailyDietView);
        tabbedPane.addTab("Vista Semanal", weeklyDietView);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Eventos
        createPlanButton.addActionListener(e -> createNewPlan());
        refreshButton.addActionListener(e -> loadData());
    }
    
    private void loadData() {
        currentPlan = planService.findByUserId(userId);
        
        if (currentPlan != null && currentPlan.isActive()) {
            planInfoLabel.setText(String.format("Plan activo: %.0f kcal/día | Proteínas: %.0f%% | Carbos: %.0f%% | Grasas: %.0f%%",
                currentPlan.getTargetCalories(),
                currentPlan.getTargetProteinPct(),
                currentPlan.getTargetCarbsPct(),
                currentPlan.getTargetFatPct()));
            createPlanButton.setText("Cambiar Plan");
            
            // Generar menús semanales si no existen
            WeeklyMenu currentWeek = weeklyMenuService.findCurrentWeekMenu(currentPlan.getId());
            if (currentWeek == null) {
                weeklyMenuService.generateWeeklyMenus(currentPlan.getId(), LocalDate.now(), 4);
            }
            
            // Actualizar vistas
            dailyDietView.setNutritionPlan(currentPlan);
            weeklyDietView.setNutritionPlan(currentPlan);
            
        } else {
            planInfoLabel.setText("No tienes un plan nutricional activo");
            createPlanButton.setText("+ Crear Plan");
            dailyDietView.showNoPlanMessage();
            weeklyDietView.showNoPlanMessage();
        }
    }
    
    private void createNewPlan() {
        if (profileService.hasCompleteProfile(userId)) {
            SelectGoalView goalView = new SelectGoalView(userId);
            goalView.setVisible(true);
            dispose();
        } else {
            int option = JOptionPane.showConfirmDialog(this,
                "Para crear un plan necesitas completar tu perfil. ¿Deseas hacerlo ahora?",
                "Perfil incompleto",
                JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                CompleteProfileView profileView = new CompleteProfileView(userId);
                profileView.setVisible(true);
                dispose();
            }
        }
    }
}