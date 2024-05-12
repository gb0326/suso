package ldpd.suso.service;

import ldpd.suso.entity.Quiz;
import ldpd.suso.entity.Result;
import ldpd.suso.entity.Sign;
import ldpd.suso.repository.QuizRepository;
import ldpd.suso.repository.ResultRepository;
import ldpd.suso.repository.SignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private SignRepository signRepository;
    @Autowired
    private ResultRepository resultRepository;

    public List<Sign> getRandomChoices(Integer id) {
        List<Sign> allSigns = signRepository.findAll(); //모든 수어 속성을 가져옴
        Quiz quiz = quizRepository.findById(id).orElse(null); //주어진 퀴즈를 가져옴
        if (quiz == null || quiz.getSign() == null) //퀴즈나 정답이 없는 경우
            return Collections.emptyList();

        //오답으로 제공할 수어 표지 목록
        List<Sign> wrongAnswers = new ArrayList<>(allSigns);
        wrongAnswers.remove(quiz.getSign()); //정답을 제거

        //랜덤하게 오답 선택지를 선택
        Collections.shuffle(wrongAnswers);

        //오답 중에서 2개 선택
        List<Sign> randomChoices = wrongAnswers.subList(0, 3);

        //정답을 추가하여 리스트 생성
        List<Sign> choices = new ArrayList<>();
        choices.add(quiz.getSign()); // 정답 추가
        choices.addAll(randomChoices); // 오답 추가

        //선택지를 랜덤하게 섞음
        Collections.shuffle(choices);

        return choices;
    }


    public void registerQuiz(Quiz quiz) throws Exception {

        quizRepository.save(quiz);
    }

    public void quizDelete(Integer id) {       //데이터베이스에서 CASCADE 제약조건으로 수어 테이블과 같이 삭제된다.
        quizRepository.deleteById(id);
    }

    public List<Quiz> quizList() {
        return quizRepository.findAll();
    }

    public Quiz quizDetail(Integer id){
        return quizRepository.findById(id).get();
    }

    public List<Result> getQuizResultsByUsername(String username) {
        return resultRepository.findByMemberUsername(username);
    }
}