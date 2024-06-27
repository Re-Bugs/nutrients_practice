package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealLogRepository extends JpaRepository<MealLog, Integer> {
}
