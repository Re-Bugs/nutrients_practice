package CSDL.spring_ml_practice.controller;

import CSDL.spring_ml_practice.domain.Member;
import CSDL.spring_ml_practice.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/sign_up")
    public String signUp(Model model) {
        model.addAttribute("member", new Member());
        return "sign_up";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Member member, Model model) {
        memberService.saveMember(member);
        model.addAttribute("message", "회원가입이 완료되었습니다.");
        return "sign_in";  // 회원가입 후 로그인 페이지로 리디렉션
    }

    @GetMapping("/sign_in")
    public String signIn(Model model) {
        model.addAttribute("member", new Member());
        return "sign_in";
    }

    @PostMapping("/login")
    public String login(@RequestParam("memberEmail") String memberEmail,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        Member member = memberService.findByEmailAndPassword(memberEmail, password);
        if (member != null) {
            session.setAttribute("memberEmail", memberEmail);  // 이메일 세션에 저장
            return "redirect:/main";  // 로그인 성공 시 이동할 페이지
        } else {
            model.addAttribute("error", "이메일 또는 패스워드가 잘못되었습니다.");
            model.addAttribute("member", new Member());
            return "sign_in";
        }
    }

    @GetMapping("/error")
    public String handleError(Model model) {
        return "error";
    }
}
