package com.oracle.example.wldf.stuck;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.lang.Exception;
import java.lang.Thread;
import java.sql.Connection;

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
 * Date: Jan 15, 2011
 * Time: 7:35:04 AM
 */
@MessageDriven(
    messageListenerInterface = MessageListener.class,
    name = "StuckThreadGeneratorListenerEJB",
    mappedName = "com.oracle.example.jms.wldf.stuck_thread_generator",
    activationConfig = {
        @ActivationConfigProperty(
            propertyName = "connectionFactoryJndiName",
            propertyValue = "com/oracle/example/jms/wldf/cf"),
        @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Topic")
    })
public class StuckThreadGeneratorListener implements MessageListener
{

  public StuckThreadGeneratorListener()
  {
  }

  public void onMessage(Message message)
  {
    long TOTAL_SLEEP_TIME = 610000L;
    long SLEEP_TIME = 6L;
    long TIME_SLEPT = 0;

    try
    {
      System.out.println("Thread: " + Thread.currentThread().getName() +
                         " | Generating stuck thread by sleeping for " + SLEEP_TIME + "ms");
      Thread.sleep(SLEEP_TIME);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
