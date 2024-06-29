package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MealLogRepository extends JpaRepository<MealLog, Integer> {
    @Query("SELECT m FROM MealLog m WHERE m.memberEmail = :memberEmail AND CAST(m.logDate AS date) = CURRENT_DATE")
    List<MealLog> findByMemberEmailAndToday(@Param("memberEmail") String memberEmail);

    @Query("SELECT ml FROM MealLog ml WHERE ml.memberEmail = :memberEmail AND ml.meal = :meal AND CAST(ml.logDate AS date) = CURRENT_DATE")
    Optional<MealLog> findTodayMealLogByMemberEmailAndMeal(@Param("memberEmail") String memberEmail, @Param("meal") int meal);

    @Query("SELECT m FROM MealLog m WHERE m.memberEmail = :memberEmail AND m.logDate >= :startDate AND m.logDate < :endDate")
    List<MealLog> findByMemberEmailAndLogDate(String memberEmail, LocalDateTime startDate, LocalDateTime endDate);
}
