package CSDL.spring_ml_practice.service;

import CSDL.spring_ml_practice.domain.Member;
import CSDL.spring_ml_practice.dto.MemberSignUpDTO;
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

    public Member findByEmail(String email) {
        return memberRepository.findByMemberEmail(email);
    }

    public Member registerMember(MemberSignUpDTO request) {
        Member member = new Member();
        member.setMemberEmail(request.getMemberEmail());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setSex(request.getSex());
        member.setActivityLevel(request.getActivityLevel());
        member.setWeight(request.getWeight());
        member.setHeight(request.getHeight());
        return memberRepository.save(member);
    }
}
