package hello.board.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView entityNotFoundException(EntityNotFoundException ex, Model model) {
        return createErrorModelAndView("error/404", ex.getMessage(), HttpStatus.NOT_FOUND, model);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView allException(Exception ex, Model model) {
        return createErrorModelAndView("error/500", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, model);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(ResponseStatusException ex, Model model) {
        return createErrorModelAndView("error/403", ex.getReason(), ex.getStatusCode(), model);
    }

    private ModelAndView createErrorModelAndView(String viewName, String errorMessage, HttpStatusCode status, Model model) {
        ModelAndView mav = new ModelAndView(viewName);
        model.addAttribute("errorMessage", errorMessage);
        mav.setStatus(status);
        return mav;
    }
}
