package ldpd.suso.controller;

import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import ldpd.suso.security.MemberCreateForm;
import ldpd.suso.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "member/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {

        //memberCreateForm = new MemberCreateForm();

        if (bindingResult.hasErrors()) {
            return "member/signup_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/signup_form";
        }

        try {
            memberService.create(memberCreateForm.getUsername(),
                    memberCreateForm.getName(), memberCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "member/signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "member/signup_form";
        }

        return "main";
    }

    @GetMapping("/login")   //post 방식 로그인은 SecurityConfig에서 대신 수행한다.
    public String login() {
        return "member/login_form";
    }

    @GetMapping("/user/mypage")
    public String mypage(){

//        String username = member.getName();
//        Optional<Member> _member = memberRepository.findByusername(username);
//        model.addAttribute("member", _member);

        return "member/mypage";
    }
}
