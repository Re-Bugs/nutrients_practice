package CSDL.spring_ml_practice.repository;

import CSDL.spring_ml_practice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberEmailAndPassword(String memberEmail, String password);
    Member findByMemberEmail(String memberEmail); // 메서드 추가
}
