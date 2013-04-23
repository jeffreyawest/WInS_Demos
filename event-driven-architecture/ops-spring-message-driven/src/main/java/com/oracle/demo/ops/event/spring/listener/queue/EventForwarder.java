package com.oracle.demo.ops.event.spring.listener.queue;

import com.oracle.demo.ops.Constants;
import com.oracle.demo.ops.domain.ParcelEvent;
import com.oracle.demo.ops.xml.MyMarshaller;

import javax.annotation.Resource;
import javax.jms.*;
import javax.xml.bind.JAXBException;

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
 * Date: Feb 15, 2011
 * Time: 9:58:46 PM
 */
public class EventForwarder
{
  @Resource(name = Constants.CONNECTION_FACTORY_JNDI, type = ConnectionFactory.class)
  private ConnectionFactory connectionFactory;

  @Resource(name = Constants.FOREIGN_EVENT_TOPIC_JNDI, type = Topic.class)
  private Topic topic;

  public void publishEventToTopic(final ParcelEvent event)
  {
    System.out.println("SPRING:: Forwarding event...");

    Connection connection = null;

    try
    {
      connection = connectionFactory.createConnection();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer topicProducer = session.createProducer(topic);

      String eventXmlString = MyMarshaller.marshallObjectToString(event);
      TextMessage newMessage = session.createTextMessage(eventXmlString);

      setJmsMessageProperties(event, newMessage);
      topicProducer.send(newMessage);
    }
    catch (JMSException e)
    {
      e.printStackTrace();
    }
    catch (JAXBException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (connection != null)
      {
        try
        {
          connection.close();
        }
        catch (JMSException f)
        {
          f.printStackTrace();
        }
      }
    }
  }

  private void setJmsMessageProperties(final ParcelEvent event, final TextMessage newMessage)
      throws JMSException
  {
    newMessage.setStringProperty("location", event.getLocation());

    if (event.getDate() != null)
    {
      newMessage.setStringProperty("date", event.getDate().toString());
    }

    newMessage.setStringProperty("parcelId", String.valueOf(event.getParcelId()));

    if (event.getParcelStatus() != null)
    {
      newMessage.setStringProperty("parcelStatus", event.getParcelStatus().value());
    }
  }
}