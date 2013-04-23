package com.oracle.example.jdbc;

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
    name = "JDBCHoggerEJB",
    mappedName = "com.oracle.example.jms.wldf.jdbchogger",
    activationConfig = {
        @ActivationConfigProperty(
            propertyName = "connectionFactoryJndiName",
            propertyValue = "com/oracle/example/jms/wldf/cf"),
        @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Topic")
    })
public class JDBCHoggerListenerBean implements MessageListener
{
  @Resource(name = "jdbc.ds.weblogic_examples")
  private javax.sql.DataSource ds;

  public JDBCHoggerListenerBean()
  {
  }

  public void onMessage(Message message)
  {
    Connection conn = null;

    try
    {
      conn = ds.getConnection();
      System.out.println("Thread: " + Thread.currentThread().getName() + " | Got connection... sleeping...");
      Thread.sleep(20000);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (conn != null)
      {
        System.out.println("Thread: " + Thread.currentThread().getName() + " | Releasing connection!");
        try
        {
          conn.close();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }

  }
}
