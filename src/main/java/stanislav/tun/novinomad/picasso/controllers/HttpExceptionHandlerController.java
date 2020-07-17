package stanislav.tun.novinomad.picasso.controllers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.util.CustomMailSender;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.FileSystems;

@Controller
public class HttpExceptionHandlerController implements ErrorController {
    private final CustomMailSender sender;
    private final String s = FileSystems.getDefault().getSeparator();
    private final String path = "."+s+"picasso"+s+"picasso_logs"+s+"picasso_log.log";
    private static final String toEmail = "wong.stanislav@gmail.com";

    Logger logger = LoggerFactory.getLogger(HttpExceptionHandlerController.class);

    private ModelAndView mav = new ModelAndView("errorPage");

    public HttpExceptionHandlerController(CustomMailSender sender) {
        this.sender = sender;
    }

    @RequestMapping("/error")
    public ModelAndView getErrorPage(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 403:
                return get403ErrorView();
            case 404:
                return get404ErrorView(request);
            default:
                return getDefaultErrorView(request);
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private ModelAndView get403ErrorView() {
        mav.addObject("message", "Access denied!");
        mav.addObject("description", "You shall not pass!");
        mav.addObject("errorCode", 403);
        return mav;
    }

    private ModelAndView getDefaultErrorView(HttpServletRequest request) {
        Throwable e = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        mav.addObject("message", "Unknown error");
        mav.addObject("description", "Don't panic. Looks like you found a BUG.");
        mav.addObject("errorCode", 500);
        logger.error("Unknown error! " + "Requested url: " + request.getRequestURI() + " " + ExceptionUtils.getStackTrace(e));
        try {
            sender.sendEmail(toEmail,"Novinomad picasso thrown an "+e.getMessage()+" exception",
                    "see stack "+ExceptionUtils.getStackTrace(e), path);
        } catch (MessagingException messagingException) {
            logger.error("Unable to send email "+ExceptionUtils.getStackTrace(messagingException));
        }
        return mav;
    }

    private ModelAndView get404ErrorView(HttpServletRequest req) {
        mav.addObject("message", "Page not found!");
        mav.addObject("description", "There is no view for path: " + req.getRequestURI());
        mav.addObject("errorCode", 404);
        return mav;
    }
}
