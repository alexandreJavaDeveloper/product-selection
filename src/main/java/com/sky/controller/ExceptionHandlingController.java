package com.sky.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionHandlingController
{
    @ExceptionHandler({ SQLException.class, DataAccessException.class })
    public String databaseError()
    {
        // Nothing to do.  Returns the logical view name of an error page, passed
        // to the view-resolver(s) in usual way.
        // Note that the exception is NOT available to this view (it is not added
        // to the model) but see "Extending ExceptionHandlerExceptionResolver"
        // below.
        return "databaseError";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(final HttpServletRequest req, final Exception ex)
    {
        // Log4j would be interesting

        final ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @RequestMapping("/404.html")
    public String render404()
    {
        return "404";
    }
}