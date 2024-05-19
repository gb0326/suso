package ldpd.suso.video;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideoController {
    @GetMapping("/flask")
    public String signTranslate() {
        return "flask_video";
    }
}
