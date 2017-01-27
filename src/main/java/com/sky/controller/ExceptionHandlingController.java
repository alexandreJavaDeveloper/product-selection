package com.sky.controller;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.sky.exception.CustomerNotFoundException;
import com.sky.exception.InvalidLocationException;
import com.sky.i18n.StringsI18N;

@ControllerAdvice
public class ExceptionHandlingController
{
    private final String GENERAL_VIEW_NAME = "error/general";

    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView exception(final Exception exception)
    {
        this.printLog(exception);

        final ModelAndView modelAndView = new ModelAndView(this.GENERAL_VIEW_NAME);
        modelAndView.addObject("message", StringsI18N.PROBLEM_READING_NUMBERS);
        return modelAndView;
    }

    @ExceptionHandler({ CustomerNotFoundException.class, InvalidLocationException.class })
    public ModelAndView handleCustomerNotFoundAndInvalidLocationException(final Exception exception)
    {
        this.printLog(exception);

        final ModelAndView modelAndView = new ModelAndView(this.GENERAL_VIEW_NAME);
        modelAndView.addObject("message", StringsI18N.PROBLEM_RETRIEVING_CUSTOMER_INFORMATION);
        return modelAndView;
    }

    @ExceptionHandler({ SQLException.class, DataAccessException.class })
    public ModelAndView handleDatabaseException(final Exception exception)
    {
        this.printLog(exception);

        final ModelAndView modelAndView = new ModelAndView(this.GENERAL_VIEW_NAME);
        modelAndView.addObject("message", StringsI18N.PROBLEM_RETRIEVING_DATABASE_INFORMATION);
        return modelAndView;
    }

    /**
     *  Just simulation for Jog4j API.
     * @param exception
     */
    private void printLog(final Exception exception)
    {
        exception.printStackTrace();
    }
}