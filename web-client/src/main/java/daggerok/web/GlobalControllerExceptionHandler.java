package daggerok.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Controller
@ControllerAdvice
@RequiredArgsConstructor
class GlobalControllerExceptionHandlermplements implements ErrorController {

    static final String ENDPOINT = "/error";

    // @org.springframework.beans.factory.annotation.Autowired
    final ServletContext servletContext;

    @SneakyThrows
    @GetMapping(ENDPOINT)
    public String handle404(final HttpServletResponse res, final Exception err) {

        if (NOT_FOUND.value() != res.getStatus()) {


            Optional.ofNullable(err).ifPresent(ex -> {

                if (Optional.ofNullable(err.getMessage()).isPresent()) {

                    log.error("{} fallback: {} {}", err.getClass().getSimpleName(), err.getMessage());

                } else {

                    log.error("{} fallback", err.getClass().getSimpleName());
                }
            });

            if (log.isDebugEnabled()) {
                log.debug(err.getMessage(), err);
            }
        }

        return "index";
    }

    @Override
    public String getErrorPath() {
        return ENDPOINT;
    }
}
