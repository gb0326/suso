package ldpd.suso.controller;

import ldpd.suso.entity.Quiz;
import ldpd.suso.entity.Sign;
import ldpd.suso.service.QuizService;
import ldpd.suso.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RequiredArgsConstructor
@Controller
public class SignController {

    private final SignService signService;
    private final QuizService quizService;

    @GetMapping("/admin/sign")
    public String registerSign(){
        return "sign/sign_register";
    }

    @PostMapping("/admin/sign")
    public String registerSign(Sign sign, Model model, MultipartFile file) throws Exception {

        final long MAX_VIDEO_LENGTH = 100 * 1024 * 1024;

        if(file.getSize() > MAX_VIDEO_LENGTH) {
            throw new IllegalArgumentException("업로드된 영상의 길이가 너무 깁니다.");
        }

        signService.registerSign(sign, file);

        Quiz quiz = new Quiz(sign);
        quizService.registerQuiz(quiz);

        model.addAttribute("message", "수어 사전 등록이 완료됐습니다.");
        model.addAttribute("searchUrl", "/sign/list");

        return "message";
    }

    @GetMapping("/sign/list")
    public String signList(Model model,
                           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                           String searchKeyword) {   //0페이지부터 시작하고 글은 10개씩, 내림차순으로 페이지 보여주기

        Page<Sign> list = null;

        if(searchKeyword == null){  //검색 단어가 안들어왔을 때는 list 보여주고
            list = signService.signList(pageable);
        }else{
            list = signService.signSearchList(searchKeyword, pageable);   //검색 기능이 들어오면 검색 단어를 보여주기
        }

        int nowPage = list.getPageable().getPageNumber() + 1;     //현재 페이지 가져오기, 페이지가 0부터 시작하므로 +1
        int startPage = Math.max(nowPage - 4, 1);   //nowPage-4가 1보다 크면 앞을 반환 아니면 1반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        if(endPage == 0 && list == null) {    //endPage가 0이라는 것은 검색어에 해당하는 결과가 없다는 것
            model.addAttribute("message", "찾으시는 수어가 없습니다.");
            model.addAttribute("searchUrl", "/sign/list");
            return "message";
        }

        model.addAttribute("list", list);   //list라는 이름으로 boardService.boardList()함수를 넘긴다.
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "sign/sign_list";
    }

    @GetMapping("/sign") //localhost:8080/board/view?id=1
    public String signDetail(Model model, Integer id){

        model.addAttribute("sign", signService.signDetail(id));
        return "sign/sign_detail";

    }

    @GetMapping("/sign/delete")
    public String signDelete(Integer id, Model model){

        signService.signDelete(id);

        model.addAttribute("message", "수어 삭제가 완료됐습니다.");
        model.addAttribute("searchUrl", "/sign/list");

        return "message";
    }

    @GetMapping("/sign/modify/{id}")
    public String signModify(@PathVariable("id") Integer id, Model model){
        //PathVariable은 {id} 부분이 인식돼서 Integer id로 들어온다, 그리고 url이 깔끔해짐

        model.addAttribute("sign", signService.signDetail(id));

        return "sign/sign_modify";
    }

    @PostMapping("/sign/update/{id}")
    public String signUpdate(@PathVariable("id") Integer id, Sign sign, Model model, MultipartFile file) throws Exception{

        Sign signTemp = signService.signDetail(id);   //기존에 담긴 board 데이터가 boardTemp로 들어옴

        signService.deleteFile(signTemp.getFilepath());

        signTemp.setTitle(sign.getTitle());
        signTemp.setSign_desc(sign.getSign_desc());
        signTemp.setFilename(sign.getFilename());
        signTemp.setFilepath(sign.getFilepath());

        signService.registerSign(signTemp, file);  //수정이 완료됐으므로 write를 이용해서 다시 쓰기


        model.addAttribute("message", "수어 사전 수정이 완료됐습니다.");
        model.addAttribute("searchUrl", "/sign/list");

        return "message";
    }
}