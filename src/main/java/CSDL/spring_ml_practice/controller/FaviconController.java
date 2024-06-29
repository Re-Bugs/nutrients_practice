package CSDL.spring_ml_practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FaviconController {

    @RequestMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        // 빈 응답을 반환
    }
}

