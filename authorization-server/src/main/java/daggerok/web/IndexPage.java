package daggerok.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class IndexPage {

    @GetMapping("/")
    String index() {
        return "index";
    }


    final ServletContext servletContext;

    /**
     * exclude fallback for paths:
     * - webjars (ie: http://localhost:9999/uaa/webjars/...)
     * - assets  (ie: http://localhost:9999/uaa/assets/...)
     * <p>
     * ps: will fails for matched-based requests like: http://localhost:9999/uaa/webjars/not-found
     */
    @SneakyThrows
    @GetMapping("/{path:(?!oauth|assets|webjars).*$}/**")
    void fallback(final HttpServletResponse res) {
        System.out.println(servletContext.getContextPath());
        res.sendRedirect(servletContext.getContextPath());
    }
}
