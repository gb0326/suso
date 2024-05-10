package ldpd.suso.controller;

import ldpd.suso.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping("/home")
    public String mainPage(Model model) {

        return "main";
    }
}
