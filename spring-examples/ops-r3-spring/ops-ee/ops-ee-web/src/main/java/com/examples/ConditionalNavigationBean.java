package com.examples;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Created by IntelliJ IDEA.
 * User: jeffrey.a.west
 * Date: 3/6/11
 * Time: 12:37 PM
 */
@ManagedBean(name = "ConditionalNavBean")
@SessionScoped
public class ConditionalNavigationBean
{
  private boolean whichCase;

  public boolean getWhichCase()
  {
    return whichCase;
  }

  public void setWhichCase(boolean whichCase)
  {
    this.whichCase = whichCase;
  }

  public String submit()
  {
    return "success";
  }
}
