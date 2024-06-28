package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Integer> {
    @Query("SELECT m FROM MealLog m WHERE m.memberEmail = :memberEmail AND CAST(m.logDate AS date) = CURRENT_DATE")
    List<MealLog> findByMemberEmailAndToday(@Param("memberEmail") String memberEmail);
}
