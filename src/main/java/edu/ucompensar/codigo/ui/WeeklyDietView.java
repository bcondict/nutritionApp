package edu.ucompensar.codigo.ui;

import edu.ucompensar.codigo.entity.NutritionPlan;
import edu.ucompensar.codigo.entity.WeeklyMenu;
import edu.ucompensar.codigo.service.WeeklyMenuService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class WeeklyDietView extends JPanel {
    private final UUID userId;
    private NutritionPlan currentPlan;
    private WeeklyMenuService weeklyMenuService;
    
    private JComboBox<String> weekSelector;
    private JPanel weekPanel;
    private List<WeeklyMenu> weeklyMenus;

    public WeeklyDietView(UUID userId) {
        this.userId = userId;
        this.weeklyMenuService = new WeeklyMenuService();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    public void setNutritionPlan(NutritionPlan plan) {
        this.currentPlan = plan;
        initComponents();
        loadWeeklyMenus();
    }
    
    private void initComponents() {
        removeAll();
        
        // Selector de semana
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Semana:"));
        weekSelector = new JComboBox<>();
        weekSelector.addActionListener(e -> loadSelectedWeek());
        topPanel.add(weekSelector);
        
        JButton generateButton = new JButton("Generar menús semanales");
        generateButton.addActionListener(e -> generateMenus());
        topPanel.add(generateButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Panel de días de la semana
        weekPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        add(weekPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    private void loadWeeklyMenus() {
        if (currentPlan == null) return;
        
        weeklyMenus = weeklyMenuService.findByNutritionPlanId(currentPlan.getId());
        weekSelector.removeAllItems();
        
        for (WeeklyMenu menu : weeklyMenus) {
            String label = menu.getWeekStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                          " - " + menu.getWeekEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            weekSelector.addItem(label);
        }
        
        if (!weeklyMenus.isEmpty()) {
            weekSelector.setSelectedIndex(0);
            loadSelectedWeek();
        } else {
            showNoMenusMessage();
        }
    }
    
    private void loadSelectedWeek() {
        if (weeklyMenus == null || weeklyMenus.isEmpty()) return;
        
        int index = weekSelector.getSelectedIndex();
        if (index >= 0 && index < weeklyMenus.size()) {
            WeeklyMenu selectedMenu = weeklyMenus.get(index);
            displayWeek(selectedMenu);
        }
    }
    
    private void displayWeek(WeeklyMenu menu) {
        weekPanel.removeAll();
        
        LocalDate current = menu.getWeekStart();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        
        for (int i = 0; i < 7; i++) {
            JPanel dayPanel = createDayPanel(current, formatter);
            weekPanel.add(dayPanel);
            current = current.plusDays(1);
        }
        
        weekPanel.revalidate();
        weekPanel.repaint();
    }
    
    private JPanel createDayPanel(LocalDate date, DateTimeFormatter formatter) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        String dayName = date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, new java.util.Locale("es"));
        JLabel dayLabel = new JLabel("<html><b>" + dayName + "</b><br>" + date.format(formatter) + "</html>");
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dayLabel);
        
        // Botón para ver/editar comidas del día
        JButton viewButton = new JButton("Ver menú");
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewButton.addActionListener(e -> showDayMenu(date));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(viewButton);
        
        return panel;
    }
    
    private void showDayMenu(LocalDate date) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                     "Menú del " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Aquí cargarías las comidas para cada tipo
        JPanel breakfastPanel = new JPanel();
        breakfastPanel.add(new JLabel("Desayuno - Aquí van las recetas"));
        tabbedPane.addTab("Desayuno", breakfastPanel);
        
        JPanel lunchPanel = new JPanel();
        lunchPanel.add(new JLabel("Almuerzo - Aquí van las recetas"));
        tabbedPane.addTab("Almuerzo", lunchPanel);
        
        JPanel dinnerPanel = new JPanel();
        dinnerPanel.add(new JLabel("Cena - Aquí van las recetas"));
        tabbedPane.addTab("Cena", dinnerPanel);
        
        JPanel snacksPanel = new JPanel();
        snacksPanel.add(new JLabel("Snacks - Aquí van las recetas"));
        tabbedPane.addTab("Snacks", snacksPanel);
        
        dialog.add(tabbedPane);
        dialog.setVisible(true);
    }
    
    private void generateMenus() {
        if (currentPlan == null) return;
        
        int option = JOptionPane.showConfirmDialog(this,
            "¿Generar menús para las próximas 4 semanas?",
            "Generar menús",
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            weeklyMenuService.generateWeeklyMenus(currentPlan.getId(), LocalDate.now(), 4);
            loadWeeklyMenus();
            JOptionPane.showMessageDialog(this, "Menús generados exitosamente");
        }
    }
    
    private void showNoMenusMessage() {
        weekPanel.removeAll();
        JLabel message = new JLabel("No hay menús semanales generados. Haz clic en 'Generar menús'", SwingConstants.CENTER);
        weekPanel.add(message);
        weekPanel.revalidate();
        weekPanel.repaint();
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