package edu.ucompensar.codigo.model.interfaces;

import edu.ucompensar.codigo.entity.DailyMenu;
import java.util.List;
import java.util.UUID;

public interface IDailyMenuDAO {
    void save(DailyMenu dailyMenu);
    DailyMenu findById(UUID id);
    List<DailyMenu> findAll();
    void update(DailyMenu dailyMenu);
    void delete(UUID id);
    
    List<DailyMenu> findByWeeklyMenuId(UUID weeklyMenuId);
    DailyMenu findByWeeklyMenuIdAndDayOfWeek(UUID weeklyMenuId, String dayOfWeek);
    List<DailyMenu> findByDayOfWeek(String dayOfWeek);
    boolean existsByWeeklyMenuIdAndDayOfWeek(UUID weeklyMenuId, String dayOfWeek);
    void deleteByWeeklyMenuId(UUID weeklyMenuId);
    int countByWeeklyMenuId(UUID weeklyMenuId);
}