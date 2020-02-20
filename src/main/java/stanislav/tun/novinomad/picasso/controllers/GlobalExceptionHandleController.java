package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandleController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNullPointerException(){
        var mav = new ModelAndView("error");
        mav.addObject("message", "oops :( something was not initialized properly");
        mav.addObject("description", "please check for all fields are correctly filled");
        return mav;
    }
}
