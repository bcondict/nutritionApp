package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.DailyMenuDAO;
import edu.ucompensar.codigo.entity.DailyMenu;
import edu.ucompensar.codigo.model.interfaces.IDailyMenuDAO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DailyMenuService {
    private final IDailyMenuDAO dailyMenuDAO;

    public DailyMenuService() {
        this.dailyMenuDAO = new DailyMenuDAO();
    }

    public void save(DailyMenu dailyMenu) {
        dailyMenuDAO.save(dailyMenu);
    }

    public DailyMenu findById(UUID id) {
        return dailyMenuDAO.findById(id);
    }

    public List<DailyMenu> findAll() {
        return dailyMenuDAO.findAll();
    }

    public void update(DailyMenu dailyMenu) {
        dailyMenuDAO.update(dailyMenu);
    }

    public void delete(UUID id) {
        dailyMenuDAO.delete(id);
    }

    public List<DailyMenu> findByWeeklyMenuId(UUID weeklyMenuId) {
        return dailyMenuDAO.findByWeeklyMenuId(weeklyMenuId);
    }

    public DailyMenu findByWeeklyMenuIdAndDayOfWeek(UUID weeklyMenuId, String dayOfWeek) {
        return dailyMenuDAO.findByWeeklyMenuIdAndDayOfWeek(weeklyMenuId, dayOfWeek);
    }

    public List<DailyMenu> findByDayOfWeek(String dayOfWeek) {
        return dailyMenuDAO.findByDayOfWeek(dayOfWeek);
    }

    public boolean existsByWeeklyMenuIdAndDayOfWeek(UUID weeklyMenuId, String dayOfWeek) {
        return dailyMenuDAO.existsByWeeklyMenuIdAndDayOfWeek(weeklyMenuId, dayOfWeek);
    }

    public void deleteByWeeklyMenuId(UUID weeklyMenuId) {
        dailyMenuDAO.deleteByWeeklyMenuId(weeklyMenuId);
    }

    public int countByWeeklyMenuId(UUID weeklyMenuId) {
        return dailyMenuDAO.countByWeeklyMenuId(weeklyMenuId);
    }

    public void createDailyMenusForWeek(UUID weeklyMenuId, BigDecimal[] dailyCalories) {
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < days.length; i++) {
            if (!existsByWeeklyMenuIdAndDayOfWeek(weeklyMenuId, days[i])) {
                DailyMenu dailyMenu = new DailyMenu(
                    UUID.randomUUID(),
                    weeklyMenuId,
                    days[i],
                    dailyCalories[i],
                    now,
                    now
                );
                save(dailyMenu);
            }
        }
    }
}