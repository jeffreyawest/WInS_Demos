package com.oracle.example.jms.uoo;

import com.oracle.example.jms.Constants;
import com.oracle.example.jms.producer.WLJMSProducer;

import javax.jms.*;
import javax.naming.NamingException;

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
 */
public class UOOProducer extends WLJMSProducer
{
  public static final String JMS_CF_JNDI = "com/oracle/example/jms/uoo/cf";
  public static final String JMS_QUEUE_JNDI = "com/oracle/example/jms/uoo/queue";

  public static void main(String[] args)
  {
    UOOProducer sender = null;

    if (args.length == 0 || args[0].toUpperCase().equals("NORMAL"))
    {
      try
      {
        sender = new UOOProducer(Constants.WL_INITIAL_CONTEXT, Constants.JMS_ENDPOINT_ADDRESS, Constants.USERNAME, Constants.PASSWORD, JMS_CF_JNDI, JMS_QUEUE_JNDI);
        sender.sendMessageBatch("Hello World!!", 10, 1000);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (sender != null)
        {
          try
          {
            sender.close();
          }
          catch (JMSException e)
          {
            e.printStackTrace();
          }
        }
      }
    }

    if (args.length == 0 || args[0].toUpperCase().equals("DISCRETE"))
    {
      try
      {
        sender = new UOOProducer(Constants.WL_INITIAL_CONTEXT, Constants.JMS_ENDPOINT_ADDRESS, Constants.USERNAME, Constants.PASSWORD, JMS_CF_JNDI, JMS_QUEUE_JNDI);
        sender.sendDiscreteUnitOfOrder("Discrete UOO", 11, 1000);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (sender != null)
        {
          try
          {
            sender.close();
          }
          catch (JMSException e)
          {
            e.printStackTrace();
          }
        }
      }
    }

    if (args.length == 0 || args[0].toUpperCase().equals("PARALLEL"))
    {
      try
      {
        sender = new UOOProducer(Constants.WL_INITIAL_CONTEXT, Constants.JMS_ENDPOINT_ADDRESS, Constants.USERNAME, Constants.PASSWORD, JMS_CF_JNDI, JMS_QUEUE_JNDI);
        sender.sendParallelUnitsOfOrder("Intermixed UOO", 11, 1000);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (sender != null)
        {
          try
          {
            sender.close();
          }
          catch (JMSException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private void sendDiscreteUnitOfOrder(String pMessage, int pMessageCount, int pIntervalTimeInMillis) throws JMSException
  {
    beginSession(false);

    String uooName = java.util.UUID.randomUUID().toString();
    mProducer.setUnitOfOrder(uooName);

    System.out.println("Using Unit of Order=[" + uooName + "]");

    for (int x = 1; x <= pMessageCount; x++)
    {
      String text = pMessage + " (" + x + ")";
      sendMessage(text);

      sleep(pIntervalTimeInMillis);
    }

    endSession();
  }

  private void sendParallelUnitsOfOrder(String pMessage, int pMessageCount, int pIntervalTimeInMillis) throws JMSException
  {
    beginSession(false);

    String uooName1 = java.util.UUID.randomUUID().toString();
    String uooName2 = java.util.UUID.randomUUID().toString();
    String uooName3 = java.util.UUID.randomUUID().toString();

    for (int x = 1; x <= pMessageCount; x++)
    {
      String text = pMessage + " (" + x + ")";

      mProducer.setUnitOfOrder(uooName1);
      sendMessage(text);

      mProducer.setUnitOfOrder(uooName2);
      sendMessage(text);

      mProducer.setUnitOfOrder(uooName3);
      sendMessage(text);

      sleep(pIntervalTimeInMillis);
    }

    endSession();
  }

  public UOOProducer(String pInitialContext, String pURL, String pUsername, String pPassword, String pCFName, String pQueueJNDIName) throws NamingException, JMSException
  {
    super(pInitialContext, pURL, pUsername, pPassword, pCFName, pQueueJNDIName);
  }
}