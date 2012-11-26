package com.oracle.weblogic.demo.spring.counter;

import com.oracle.weblogic.demo.spring.jmx.WebLogicJMXWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: jeffrey.a.west
 * Date: 4/3/12
 * Time: 1:59 PM
 */

@Controller
@RequestMapping(value = "/counter")
public class CounterController
{
  @Autowired
  private CounterBean counter;

  @RequestMapping(value = "/value", method = RequestMethod.GET)
  @ResponseBody
  public int getValue()
  {
    return counter.getValue();
  }

  @RequestMapping(value = "/increment", method = RequestMethod.GET)
  @ResponseBody
  public int incrementCounter()
  {
    return counter.increment();
  }

  @RequestMapping(value = "/reset", method = RequestMethod.GET)
  @ResponseBody
  public int resetCounter()
  {
    return counter.reset();
  }
}
