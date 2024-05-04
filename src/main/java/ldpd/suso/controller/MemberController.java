package ldpd.suso.controller;

import jakarta.servlet.http.HttpSession;
import ldpd.suso.entity.Member;
import ldpd.suso.entity.Sign;
import ldpd.suso.repository.MemberRepository;
import ldpd.suso.security.MemberCreateForm;
import ldpd.suso.security.MemberSecurityService;
import ldpd.suso.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberSecurityService memberSecurityService;

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "member/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/signup_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/signup_form";
        }

        try {
            // 회원 가입 전 중복 검사
            Member existingMember = memberRepository.findByusername(memberCreateForm.getUsername());
            if (existingMember != null && existingMember.getUsername() != null) {
                bindingResult.reject("signupFailed", "이미 등록된 사용자 아이디입니다.");
                return "member/signup_form";
            }

            existingMember = memberRepository.findByname(memberCreateForm.getName());
            if (existingMember != null && existingMember.getName() != null) {
                bindingResult.reject("signupFailed", "이미 등록된 사용자 이름입니다.");
                return "member/signup_form";
            }

            // 중복이 없을 경우 회원 가입 진행
            memberService.create(memberCreateForm.getUsername(),
                    memberCreateForm.getName(),
                    memberCreateForm.getEmail(),
                    memberCreateForm.getPassword1());
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "회원 가입에 실패하였습니다.");
            return "member/signup_form";
        }
        return "main";
    }

    @GetMapping("/login")   //post 방식 로그인은 SecurityConfig에서 대신 수행한다.
    public String login() {
        return "member/login_form";
    }

    @GetMapping("/user/mypage")
    public String mypage(HttpSession session, Model model) {

        //현재 인증된 사용자의 인증 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 사용자의 정보를 가져옴
        Object principal = authentication.getPrincipal();

        // principal이 UserDetails를 구현한 경우, 사용자의 정보에 접근할 수 있음
        String username = null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            // 사용자의 정보에 접근하여 필요한 값 가져오기
            username = userDetails.getUsername();
            // 사용자의 다른 정보에 접근 가능
        }

        if (memberRepository != null) {
            Member optionalMember = memberRepository.findByusername(username);
            if (optionalMember!=null) {
                Member member = optionalMember;
                model.addAttribute("member", member);
            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("error", "데이터베이스에 접근할 수 없습니다.");
        }

        return "member/my_page";
    }

    @GetMapping("/user/update/username")
    public String id_Update(Model model) {

        return "member/update_id";
    }

    @PostMapping("/user/update/username")
    public String updateUsername(@RequestParam("newUsername") String newUsername, Authentication authentication) {
        // 현재 인증된 사용자의 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 현재 사용자의 아이디 가져오기
        String currentUsername = userDetails.getUsername();

        // 아이디 변경 로직
        memberService.updateUsername(currentUsername, newUsername);

        // 세션 정보 업데이트
        userDetails = memberSecurityService.loadUserByUsername(newUsername);
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        return "redirect:/user/mypage";
    }

    @GetMapping("/user/update/email")
    public String email_Update(Model model) {

        return "member/update_email";
    }

    @PostMapping("/user/update/email")
    public String updateEmail(@RequestParam("newEmail") String newEmail, Authentication authentication) {
        // 현재 인증된 사용자의 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 현재 사용자의 아이디 가져오기
        String currentUsername = userDetails.getUsername();

        // 이메일 변경 로직
        memberService.updateEmail(currentUsername, newEmail);

        return "redirect:/user/mypage";
    }

    @GetMapping("/user/update/name")
    public String name_Update(Model model) {

        return "member/update_name";
    }

    @PostMapping("/user/update/name")
    public String updateName(@RequestParam("newName") String newName, Authentication authentication) {
        // 현재 인증된 사용자의 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 현재 사용자의 아이디 가져오기
        String currentUsername = userDetails.getUsername();

        // 이름 변경 로직
        memberService.updateName(currentUsername, newName);

        return "redirect:/user/mypage";
    }

    @GetMapping("/user/update/password")
    public String password_Update(Model model) {

        return "member/update_password";
    }

    @PostMapping("/user/update/password")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Authentication authentication) {
        // 현재 인증된 사용자의 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 현재 사용자의 아이디 가져오기
        String currentUsername = userDetails.getUsername();

        // 비밀번호 변경 로직
        memberService.updatePassword(currentUsername, currentPassword, newPassword);

        // 세션 정보 업데이트 (비밀번호 변경 시 세션 갱신은 필요하지 않을 수도 있음)

        return "redirect:/user/mypage";
    }
}
