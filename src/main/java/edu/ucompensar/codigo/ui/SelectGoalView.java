package edu.ucompensar.codigo.ui;

import edu.ucompensar.codigo.service.NutritionPlanService;
import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class SelectGoalView extends JFrame {
    private JButton weightLossBtn;
    private JButton weightGainBtn;
    private JButton muscleGainBtn;
    private JButton maintenanceBtn;
    
    private final UUID userId;
    private final NutritionPlanService planService;

    public SelectGoalView(UUID userId) {
        this.userId = userId;
        this.planService = new NutritionPlanService();
        
        setTitle("Selecciona tu objetivo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Título
        JLabel title = new JLabel("¿Cuál es tu objetivo?", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(title, BorderLayout.NORTH);
        
        // Botones de objetivos
        JPanel goalsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        
        weightLossBtn = createGoalButton("Pérdida de peso", "⚖️⬇️", "WEIGHT_LOSS");
        weightGainBtn = createGoalButton("Ganancia de peso", "⚖️⬆️", "WEIGHT_GAIN");
        muscleGainBtn = createGoalButton("Ganancia muscular", "💪", "MUSCLE_GAIN");
        maintenanceBtn = createGoalButton("Mantenimiento", "🎯", "MAINTENANCE");
        
        goalsPanel.add(weightLossBtn);
        goalsPanel.add(weightGainBtn);
        goalsPanel.add(muscleGainBtn);
        goalsPanel.add(maintenanceBtn);
        
        mainPanel.add(goalsPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        pack();
    }
    
    private JButton createGoalButton(String text, String icon, String goalType) {
        JButton button = new JButton("<html><div style='text-align:center;padding:20px;'>" +
                                     "<span style='font-size:32px;'>" + icon + "</span><br>" +
                                     "<span style='font-size:14px;'>" + text + "</span></div></html>");
        button.setPreferredSize(new Dimension(150, 120));
        button.addActionListener(e -> createPlan(goalType));
        return button;
    }
    
    private void createPlan(String goalType) {
        try {
            planService.createPlanForUser(userId, goalType);
            JOptionPane.showMessageDialog(this, "Plan nutricional creado exitosamente");
            
            dispose();
            MainView mainView = new MainView(userId);
            mainView.setVisible(true);
            
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            dispose();
            CompleteProfileView profileView = new CompleteProfileView(userId);
            profileView.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el plan: " + ex.getMessage());
        }
    }
}