package stanislav.tun.novinomad.picasso.controllers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.util.CustomMailSender;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.FileSystems;

// for internal controllers exceptions
@ControllerAdvice
public class GlobalExceptionHandleController {

    private final CustomMailSender sender;
    private final String s = FileSystems.getDefault().getSeparator();
    private final String path = "."+s+"picasso"+s+"picasso_logs"+s+"picasso_log.log";
    private static final String toEmail = "wong.stanislav@gmail.com";

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandleController.class);

    public GlobalExceptionHandleController(CustomMailSender sender) {
        this.sender = sender;
    }

    // handle all unexpected exceptions
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest req, final Throwable e) {
        var mav = new ModelAndView("errorPage");
        mav.addObject("message", "Unknown error");
        mav.addObject("description", "Don't panic. Looks like you found a BUG.");
        mav.addObject("errorCode", 500);
        logger.error("Unknown error! " + "Requested url: " + req.getRequestURI() + " " + ExceptionUtils.getStackTrace(e));

        try {
            sender.sendEmail(toEmail,"Novinomad picasso thrown an "+e.getMessage()+" exception",
                    "see stack "+ExceptionUtils.getStackTrace(e), path);
        } catch (MessagingException messagingException) {
            logger.error("Unable to send email "+ExceptionUtils.getStackTrace(messagingException));
        }
        return mav;
    }
// this not works for http errors
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(AccessDeniedException.class)
//    public ModelAndView handle403Exception() {
//        var mav = new ModelAndView("errorPage");
//        mav.addObject("message", "Access denied!");
//        mav.addObject("description", "You shall not pass!");
//        mav.addObject("errorCode", 403);
//        return mav;
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(PageNotFoundException.class)
//    public ModelAndView handle404Exception(HttpServletRequest req) {
//        var mav = new ModelAndView("errorPage");
//        mav.addObject("message", "Page not found!");
//        mav.addObject("description", "There is no view for path: "+req.getRequestURI());
//        mav.addObject("errorCode", 404);
//        return mav;
//    }
}
