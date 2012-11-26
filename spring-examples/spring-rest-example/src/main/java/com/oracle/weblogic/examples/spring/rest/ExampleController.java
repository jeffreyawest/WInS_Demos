package com.oracle.weblogic.examples.spring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jeffrey.a.west
 * Date: 4/3/12
 * Time: 1:40 PM
 */
@Controller
public class ExampleController
{
  private String message;

  @InitBinder
  public void initBinder(WebDataBinder binder)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
  }

  @RequestMapping(value = "/returnMessage", method = RequestMethod.GET)
  @ResponseBody
  public String returnMessage()
  {
    return message;
  }

  @RequestMapping(value = "/sayHello/{name}", method = RequestMethod.GET)
  @ResponseBody
  public String sayHello(@PathVariable("name") String name)
  {
    return "Hello, " + name + "!!";
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }
}