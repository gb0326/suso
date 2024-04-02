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

@Controller
@RequiredArgsConstructor
public class QuizController {

    QuizService quizService;

    @GetMapping("/admin/quiz")
    public String registerQuiz(){
        return "quiz/registerQuiz";
    }

    @PostMapping("/admin/quiz")
    public String registerQuiz(Quiz quiz, Model model, MultipartFile file) throws Exception {

        final long MAX_VIDEO_LENGTH = 100 * 1024 * 1024;

        if(file.getSize() > MAX_VIDEO_LENGTH) {
            throw new IllegalArgumentException("업로드된 영상의 길이가 너무 깁니다.");
        }

        quizService.register(quiz, file);

        model.addAttribute("message", "퀴즈 등록이 완료됐습니다.");
        model.addAttribute("searchUrl", "/home");

        return "message";
    }
}
