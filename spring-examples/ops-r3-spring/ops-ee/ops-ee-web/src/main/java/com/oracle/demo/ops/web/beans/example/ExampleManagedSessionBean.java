package com.oracle.demo.ops.web.beans.example;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import java.io.Serializable;

/**
  * **************************************************************************
 * <p/>
 * This code is provided for example purposes only.  Oracle does not assume
 * any responsibility or liability for the consequences of using this code.
 * If you choose to use this code for any reason, including but not limited
 * to its use as an example you do so at your own risk and without the support
 * of Oracle.
 *
 * This code is provided under the following licenses:
 *
 * GNU General Public License (GPL-2.0)
 * COMMON DEVELOPMENT AND DISTRIBUTION LICENSE Version 1.0 (CDDL-1.0)
 *
 * <p/>
 * ****************************************************************************
 * User: jeffrey.a.west
 * Date: Feb 23, 2011
 * Time: 7:36:54 PM
 */
@ManagedBean(name = "user")
@SessionScoped
public class ExampleManagedSessionBean   implements Serializable
{
  static final long serialVersionUID = 42L;
  
  private static final String DISPLAY = "display";

  private String paramValue;
  public  String textbox1;
  public  String textbox2;

  public void validateTextBox(ComponentSystemEvent event)
  {

    FacesContext fc = FacesContext.getCurrentInstance();

    UIComponent components = event.getComponent();

    //get textbox1 value
    UIInput uiText1 = (UIInput) components.findComponent("textbox1");
    String text1 = uiText1.getLocalValue().toString();

    //get textbox2 value
    UIInput uiText2 = (UIInput) components.findComponent("textbox2");
    String text2 = uiText2.getLocalValue().toString();

    if (!"MKYONG".equals(text1.toUpperCase()) || (!"123".equals(text2)))
    {

      FacesMessage msg =
              new FacesMessage("Textbox validation failed.",
                      "Please enter 'mkyong' in Textbox1, '123' in Textbox2.");

      msg.setSeverity(FacesMessage.SEVERITY_ERROR);

      //components.getClientId() = textPanel
      fc.addMessage(components.getClientId(), msg);

      //passed to the Render Response phase
      fc.renderResponse();
    }
  }

  public String getTextbox1()
  {
    return textbox1;
  }

  public void setTextbox1(String textbox1)
  {
    this.textbox1 = textbox1;
  }

  public String getTextbox2()
  {
    return textbox2;
  }

  public void setTextbox2(String textbox2)
  {
    this.textbox2 = textbox2;
  }

  public String editActionWithStringParam(String id)
  {
    paramValue = id;
    return DISPLAY;
  }

  public void attrListener(ActionEvent event)
  {
    paramValue = (String) event.getComponent().getAttributes().get("action");
  }

  public String editAction()
  {
    return DISPLAY;
  }

  public String defaultAction()
  {
    return DISPLAY;
  }

  public String getParamValue()
  {
    return paramValue;
  }

  public void setParamValue(String paramValue)
  {
    this.paramValue = paramValue;
  }
}
