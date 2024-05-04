package ldpd.suso.controller;

import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MemberRepository memberRepository;

    @GetMapping("/home")
    public String mainPage(Model model) {

        return "main";
    }
}
