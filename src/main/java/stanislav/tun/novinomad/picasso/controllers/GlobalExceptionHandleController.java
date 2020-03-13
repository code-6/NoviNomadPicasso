package stanislav.tun.novinomad.picasso.controllers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.exceptions.AccessDeniedException;
import stanislav.tun.novinomad.picasso.exceptions.PageNotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandleController {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandleController.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest req, final Throwable e) {
        var mav = new ModelAndView("errorPage");
        mav.addObject("message", "Unknown error");
        mav.addObject("description", "Don't panic. Looks like you found a BUG.");
        mav.addObject("errorCode", 500);
        logger.error("Unknown error! " + "Requested url: " + req.getRequestURI() + " " + ExceptionUtils.getStackTrace(e));
        return mav;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handle403Exception() {
        var mav = new ModelAndView("errorPage");
        mav.addObject("message", "Access denied!");
        mav.addObject("description", "You shall not pass!");
        mav.addObject("errorCode", 403);
        return mav;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PageNotFoundException.class)
    public ModelAndView handle404Exception(HttpServletRequest req) {
        var mav = new ModelAndView("errorPage");
        mav.addObject("message", "Page not found!");
        mav.addObject("description", "There is no view for path: "+req.getRequestURI());
        mav.addObject("errorCode", 404);
        return mav;
    }
}
