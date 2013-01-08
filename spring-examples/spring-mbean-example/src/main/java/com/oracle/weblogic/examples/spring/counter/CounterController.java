package com.oracle.weblogic.examples.spring.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "counter")
public class CounterController
{
  @Autowired
  private CounterBean counter;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  public int getValueRoot()
  {
    return counter.getValue();
  }

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

  @RequestMapping(value = "/set/{newValue}", method = RequestMethod.GET)
  @ResponseBody
  public int setValue(@PathVariable("newValue") int pNewValue)
  {
    return counter.setValue(pNewValue);
  }
}
