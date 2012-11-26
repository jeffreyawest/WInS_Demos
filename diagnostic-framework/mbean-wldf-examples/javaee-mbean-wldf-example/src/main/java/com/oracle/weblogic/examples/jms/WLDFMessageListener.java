package com.oracle.weblogic.examples.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: jeffreyawest
 * Date: 6/20/12
 * Time: 8:35 PM
 */
@MessageDriven(
    messageListenerInterface = javax.jms.MessageListener.class,
    name = "WLDFListener",
    mappedName = "jms/wldf/notification",
    activationConfig = {
        @ActivationConfigProperty(
            propertyName = "connectionFactoryJndiName",
            propertyValue = "weblogic.jms.XAConnectionFactory"),
        @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Queue")
    })
public class WLDFMessageListener implements MessageListener
{
  @Override
  public void onMessage(Message pMessage)
  {
    if (pMessage instanceof MapMessage)
    {
      MapMessage msg = (MapMessage) pMessage;

      try
      {
        Enumeration theEnum = msg.getMapNames();
        int x = 0;

        while (theEnum.hasMoreElements())
        {
          x++;
          String name = (String) theEnum.nextElement();

          System.out.println("Name=[" + name + "] Value=[" + msg.getString(name) + "]");

          if (x > 100)
          {
            break;
          }
        }

        System.out.println("==============================================");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

      }
      catch (JMSException e)
      {
        e.printStackTrace();
      }
    }

    else

    {
      System.out.println("Not Text Message: " + pMessage.getClass().getName());
    }
  }
}
