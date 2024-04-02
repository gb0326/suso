package ldpd.suso.service;

import ldpd.suso.entity.Quiz;
import ldpd.suso.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public void register(Quiz quiz, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {

            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  //파일 저장 경로

            UUID uuid = UUID.randomUUID();  //파일 이름에 붙일 랜덤 이름 생성

            String fileName = uuid + "_" + file.getOriginalFilename();  //랜덤 이름과 원래 파일 이름을 이어서 저장될 파일 이름 생성

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            quiz.setFilename(fileName);
            quiz.setFilepath("/files/" + fileName);
            quizRepository.save(quiz);
        } else {
            quizRepository.save(quiz);
        }


    }
}
