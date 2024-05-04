package ldpd.suso.service;

import ldpd.suso.entity.Sign;
import ldpd.suso.repository.SignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@Slf4j  //로그 찍을 때 필요
public class SignService {

    @Autowired
    private SignRepository signRepository;

    public void registerSign(Sign sign, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {

            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  //파일 저장 경로

            UUID uuid = UUID.randomUUID();  //파일 이름에 붙일 랜덤 이름 생성

            String fileName = uuid + "_" + file.getOriginalFilename();  //랜덤 이름과 원래 파일 이름을 이어서 저장될 파일 이름 생성

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            sign.setFilename(fileName);
            sign.setFilepath("/files/" + fileName);
            signRepository.save(sign);
        } else {
            signRepository.save(sign);
        }

    }

    public void deleteFile(String filePath) {
        File deleteFile = new File("src/main/resources/static/" + filePath);

        if(deleteFile.exists()) {   //파일이 존재하면 삭제
            deleteFile.delete();
            log.info("파일을 삭제 했습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }


    // 게시글 리스트 처리
    public Page<Sign> signList(Pageable pageable){

        return signRepository.findAll(pageable);
    }

    public Page<Sign> signSearchList(String searchKeyword, Pageable pageable) {

        return signRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시물 불러오기
    public Sign signDetail(Integer id){
        return signRepository.findById(id).get();
    }

    //특정 게시물 삭제
    public void signDelete(Integer id) {

        deleteFile(signRepository.findById(id).get().getFilepath());
        signRepository.deleteById(id);
    }
}
