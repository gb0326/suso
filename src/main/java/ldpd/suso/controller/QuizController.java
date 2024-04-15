package ldpd.suso.controller;

import ldpd.suso.entity.Quiz;
import ldpd.suso.repository.QuizRepository;
import ldpd.suso.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/quiz/list")
    public String quizList(Model model) {

        List<Quiz> quiz = null;
        quiz = quizService.quizList();
        model.addAttribute("list", quiz);

        return "quiz/quiz_list";
    }

    @GetMapping("/quiz")
    public String quizDetail(Model model, Integer id){

        model.addAttribute("quiz", quizService.quizDetail(id));
        return "quiz/quiz_detail";

    }
}