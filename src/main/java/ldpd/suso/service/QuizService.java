package ldpd.suso.service;

import ldpd.suso.entity.Quiz;
import ldpd.suso.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

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
}