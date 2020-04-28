package com.insd.study.springboot.web;

import com.insd.study.springboot.config.auth.LoginUser;
import com.insd.study.springboot.config.auth.dto.SessionUser;
import com.insd.study.springboot.service.PostsService;
import com.insd.study.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        /* Model : 서버 템플릿 엔진(index.mustache)에서 사용할 수 있는 객체를 저장
         * postsService.fineAllDesc()를 호출해서 얻은 List<PostsListResponseDto> 객체를
         * posts로 index.mustache에 전달한다.
         */
        model.addAttribute("posts", postsService.fineAllDesc());

        /* model.addAttribute() : userName을 사용할 수 있게 user가 null이 아닌 경우 userName을 Model에 저장
         * index.mustache :
         *  1. {{#userName}}을 통해 존재할 경우 : logout 버튼 노출
         *  2. {{^userName}}을 통해 존재하지 않을 경우 : 로그인 버튼 노출
         * @LoginUser를 사용함으로써 httpSession.getAttribute("user")를 사용하지 않아도 됨
         *  - LoginUserArgumentResolver.resolveArgument() 참조
         */
        // SessionUser user = (SessionUser)httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        /* 머스테치 스타터 덕분에 컨트롤러에서 문자열을 반환할 때
         * 앞의 경로(기본 경로- src/main/resources/templates)와 뒤의 파일 확장자(.mustache)는 자동으로 지정됨
         * 즉, src/main/resources/templates/index.mustache 로 전환되어 View Resolver가 처리하게 됨
         */
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
