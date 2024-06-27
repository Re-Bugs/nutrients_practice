package CSDL.spring_ml_practice.service;

import CSDL.spring_ml_practice.domain.Member;
import CSDL.spring_ml_practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Member findByEmailAndPassword(String email, String password) {
        return memberRepository.findByMemberEmailAndPassword(email, password);
    }
}
