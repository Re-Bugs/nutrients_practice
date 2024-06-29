package CSDL.spring_ml_practice.api;

import CSDL.spring_ml_practice.domain.Member;
import CSDL.spring_ml_practice.dto.MemberSignInDTO;
import CSDL.spring_ml_practice.dto.MemberSignUpDTO;
import CSDL.spring_ml_practice.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class APILoginController {

    private final MemberService memberService;

    @PostMapping("/sign_up")
    public ResponseEntity<Map<String, String>> register(@RequestBody MemberSignUpDTO request) {
        Member existingMember = memberService.findByEmail(request.getMemberEmail());
        Map<String, String> response = new HashMap<>();

        if (existingMember != null) {
            response.put("message", "이미 존재하는 이메일");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        memberService.registerMember(request);
        response.put("message", "회원가입 성공");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sign_in")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberSignInDTO request, HttpSession session) {
        Member member = memberService.findByEmailAndPassword(request.getMemberEmail(), request.getPassword());
        Map<String, String> response = new HashMap<>();

        if (member != null) {
            session.setAttribute("memberEmail", request.getMemberEmail());
            response.put("message", "로그인 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "잘못된 이메일 또는 비밀번호");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checkSession(HttpSession session) {
        String memberEmail = (String) session.getAttribute("memberEmail");
        Map<String, String> response = new HashMap<>();

        if (memberEmail != null) {
            response.put("message", "세션 유효");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "세션 비유효");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
