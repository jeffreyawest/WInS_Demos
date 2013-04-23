package com.oracle.example.jms.distributed.queue;

import com.oracle.example.jms.Constants;
import com.oracle.example.jms.producer.WLJMSProducer;

import java.util.Date;

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
public class QueueProducer
    extends WLJMSProducer
{
  public static final String JMS_CF_JNDI = "com/oracle/example/jms/base/cf";
  public static final String JMS_QUEUE_JNDI = "com/oracle/example/jms/base/queue";

  public static void main(String[] args)
  {
    WLJMSProducer sender = null;
    try
    {
      sender = new WLJMSProducer(Constants.WL_INITIAL_CONTEXT, Constants.JMS_ENDPOINT_ADDRESS, Constants.USERNAME, Constants.PASSWORD, JMS_CF_JNDI, JMS_QUEUE_JNDI);
      sender.sendMessageBatch("Batch=[" + Constants.filenameDateFormatter.format(new Date()) + "] Hello World!!", 4, 500);
      sender.sendMessageBatch("Batch=[" + Constants.filenameDateFormatter.format(new Date()) + "] Hello World!!", 4, 500);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}