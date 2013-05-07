package beans;

import com.oracle.example.jms.distributed.queue.QueueProducerEJB;
import com.oracle.example.jms.distributed.topic.ClearScreenEJB;
import com.oracle.example.jms.distributed.topic.PDTProducerEJB;
import com.oracle.example.jms.distributed.topic.RDTProducerEJB;
import com.oracle.example.jms.saf.SAFServerProducerEJB;
import com.oracle.example.jms.uoo.UOOProducerEJB;
import com.oracle.example.jms.uow.UOWProducerEJB;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
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
 * <p/>
 * This code is provided under the following licenses:
 * <p/>
 * GNU General Public License (GPL-2.0)
 * COMMON DEVELOPMENT AND DISTRIBUTION LICENSE Version 1.0 (CDDL-1.0)
 * <p/>
 * <p/>
 * ****************************************************************************
 * User: jeffrey.a.west
 * Date: Jul 27, 2011
 * Time: 10:06:52 AM
 */

@ManagedBean
@SessionScoped
public class JmsExamplesController implements Serializable
{
  static final long serialVersionUID = 43L;
  private static final Logger logger = Logger.getLogger(JmsExamplesController.class.getName());

  @EJB
  private QueueProducerEJB queueProducerEJB;
  @EJB
  private ClearScreenEJB clearScreenEJB;
  @EJB
  private PDTProducerEJB pdtProducerEJB;
  @EJB
  private RDTProducerEJB rdtProducerEJB;
  @EJB
  private UOOProducerEJB uooProducerEJB;
  @EJB
  private UOWProducerEJB uowProducerEJB;
  @EJB
  private SAFServerProducerEJB safProducerEJB;

  public JmsExamplesController()
  {
  }

  public void removeSelectedAttribute(ActionEvent pEvent)
  {

  }


  public void removeEntry(String pKey)
  {
    getSession().removeAttribute(pKey);
  }

  public HttpSession getSession()
  {
    return (HttpSession) getFacesContext().getExternalContext().getSession(true);
  }

  public String getSessionId()
  {
    return getSession().getId();
  }

  public String getWebLogicServerName()
  {
    return System.getProperty("weblogic.Name");
  }

  public FacesContext getFacesContext()
  {
    FacesContext ctx = FacesContext.getCurrentInstance();

    //ctx.get

    return ctx;
  }


  public List<Map.Entry> getSessionAttributes()
  {
    List<Map.Entry> atts = new ArrayList();

    Enumeration<String> attributes = getSession().getAttributeNames();

    //logger.info("Session Attributes:");
    //logger.info("======================================");
    while (attributes.hasMoreElements())
    {
      String att = attributes.nextElement();

      if (att.equals("com.sun.faces.renderkit.ServerSideStateHelper.LogicalViewMap")
          || att.equals("javax.faces.request.charset"))
      {
        continue;
      }

      Object value = getSession().getAttribute(att);
      atts.add(new AbstractMap.SimpleEntry(att, value));
      //logger.info("Att name=[" + att + "] ObjectType=[" + value.getClass() + "] value=[" + value + "]");
    }
    //logger.info("======================================");

    return atts;
  }

  public String invalidateSession()
  {
    invalidate();
    return "/index";
  }

  public void invalidateSessionListener(ActionEvent event)
  {
    invalidate();
  }

  private void invalidate()
  {
    logger.info("INVALIDATING SESSION!! ID=[" + getSessionId() + "]");

    getSession().invalidate();
  }


  public void invalidateSessionActionListener(ActionEvent event)
  {
    invalidateSession();
    getFacesContext().addMessage(null, new FacesMessage("Session Invalidated", "User Request"));
  }

  public String queueDemo()
  {
    queueProducerEJB.doIt();

    return "success";
  }

  public String pdtDemo()
  {
    pdtProducerEJB.doIt();
    return "success";
  }

  public String rdtDemo()
  {
    rdtProducerEJB.doIt();
    return "success";
  }

  public String uowDemo()
  {
    uowProducerEJB.doIt();

    return "success";
  }

  public String safDemo()
  {
    safProducerEJB.doIt();

    return "success";
  }

  public String uooDemoMixed()
  {
    uooProducerEJB.sendMixedUOO();
    return "success";
  }

  public String uooDemoDiscrete()
  {
    uooProducerEJB.sendDiscreteUOO();
    return "success";
  }

  public String uooDemoNonUOO()
  {
    uooProducerEJB.sendRegularMessageBatch();
    return "success";
  }

  public String uowDemoNonUOW()
  {
    uowProducerEJB.sendNonUOW();
    return "success";
  }

  public String uowDemoMixed()
  {
    uowProducerEJB.sendMixedUOW();
    return "success";
  }

  public String uowDemoDiscrete()
  {
    uowProducerEJB.sendDiscreteUOW();
    return "success";
  }

  public String uowDemoIncomplete()
  {
    uowProducerEJB.sendIncompleteUOW();
    return "success";
  }

  public String uowDemoSlow()
  {
    uowProducerEJB.sendSlowUOW();
    return "success";
  }
}
