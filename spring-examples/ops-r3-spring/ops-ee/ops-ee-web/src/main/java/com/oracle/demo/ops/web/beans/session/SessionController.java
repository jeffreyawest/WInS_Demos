package com.oracle.demo.ops.web.beans.session;

import com.oracle.demo.ops.domain.*;
import com.oracle.demo.ops.web.beans.OPSController;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

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
 * Date: Jul 27, 2011
 * Time: 10:06:52 AM
 */

@ManagedBean(name = "SessionControllerBean")
@SessionScoped
public class SessionController
    extends OPSController
{
  static final long serialVersionUID = 42L;
  private static final Logger logger = Logger.getLogger(SessionController.class.getName());


  public List<Map.Entry> getSessionAttributes()
  {
    List atts = new ArrayList();

    Enumeration<String> attributes = getSession().getAttributeNames();

    logger.info("Session Attributes:");
    logger.info("======================================");
    while (attributes.hasMoreElements())
    {
      String att = attributes.nextElement();
      Object value = getSession().getAttribute(att);
      atts.add(new AbstractMap.SimpleEntry(att, value));
      logger.info("Att name=[" + att + "] ObjectType=[" + value.getClass() + "] value=[" + value + "]");
    }
    logger.info("======================================");

    return atts;
  }

  public String invalidateSession()
  {
    logger.info("INVALIDATING SESSION!! ID=[" + getSessionId() + "]");

    Enumeration<String> attributes = getSession().getAttributeNames();
    logger.info("Session Attributes:");
    logger.info("======================================");
    while (attributes.hasMoreElements())
    {
      String att = attributes.nextElement();
      Object value = getSession().getAttribute(att);
      logger.info("Att name=[" + att + "] ObjectType=[" + value.getClass() + "] value=[" + value + "]");
    }
    logger.info("======================================");

    getSession().invalidate();
    return "/index";
  }


  public void invalidateSessionActionListener(ActionEvent event)
  {
    invalidateSession();
    getFacesContext().addMessage(null, new FacesMessage("Session Invalidated", "User Request"));
  }


}
