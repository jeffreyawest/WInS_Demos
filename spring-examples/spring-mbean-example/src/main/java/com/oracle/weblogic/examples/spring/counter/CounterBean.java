package com.oracle.weblogic.demo.spring.counter;

/**
 * Created with IntelliJ IDEA.
 * User: jeffrey.a.west
 * Date: 4/3/12
 * Time: 1:57 PM
 */
public class CounterBean
{
  private int value;

  public int increment()
  {
    return ++value;
  }

  public int reset()
  {
    value = 0;

    return value;
  }

  public int getValue()
  {
    return value;
  }

  public void setValue(int value)
  {
    this.value = value;
  }
}
