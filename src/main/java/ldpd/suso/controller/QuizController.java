package ldpd.suso.controller;

import ldpd.suso.entity.Quiz;
import ldpd.suso.entity.Sign;
import ldpd.suso.repository.QuizRepository;
import ldpd.suso.repository.SignRepository;
import ldpd.suso.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final SignRepository signRepository;

    @GetMapping("/quiz/list")
    public String quizList(Model model) {

        List<Quiz> quiz = null;
        quiz = quizService.quizList();
        model.addAttribute("list", quiz);

        return "quiz/quiz_list";
    }

    @GetMapping("/quiz")
    public String quizDetail(Model model, @RequestParam Integer id) {

        model.addAttribute("quiz", quizService.quizDetail(id));
        model.addAttribute("wrongAnswers", quizService.getRandomWrongAnswers(id));

        return "quiz/quiz_detail";
    }

    @PostMapping("/quiz/submit")
    public String submitQuiz(@RequestParam Integer quiz_id, @RequestParam Integer choiceId, Model model) {
        Quiz quiz = quizService.quizDetail(quiz_id);

        if (quiz == null) {
            //퀴즈가 없는 경우
            model.addAttribute("error", "퀴즈를 찾을 수 없습니다.");
            return "quiz/quiz_error";
        }

        Sign selectedAnswer = signRepository.findById(choiceId).orElse(null);
        if (selectedAnswer == null) {
            //선택한 답변이 유효하지 않은 때
            model.addAttribute("error", "유효하지 않은 답변입니다.");
            return "quiz/quiz_error";
        }

        if (selectedAnswer.equals(quiz.getSign())) {
            //정답인 경우
            quizService.markQuizAsCorrect(quiz);
            model.addAttribute("message", "정답입니다!");
        } else {
            model.addAttribute("message", "오답입니다.");
        }

        return "quiz/quiz_result";
    }
}