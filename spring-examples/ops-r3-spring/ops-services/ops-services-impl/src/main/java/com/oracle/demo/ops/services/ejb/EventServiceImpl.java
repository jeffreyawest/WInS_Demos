package com.oracle.demo.ops.services.ejb;

import com.oracle.demo.ops.Constants;
import com.oracle.demo.ops.services.EventService;
import com.oracle.demo.ops.xml.MyMarshaller;
//import weblogic.jms.extensions.WLMessageProducer;
//import weblogic.jms.extensions.WLSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.xml.bind.JAXBException;
import java.io.Serializable;

/**
 * **************************************************************************
 * <p/>
 * This code is provided for example purposes only.  Oracle does not assume
 * any responsibility or liability for the consequences of using this code.
 * If you choose to use this code for any reason, including but not limited
 * to its use as an example you do so at your own risk and without the support
 * of Oracle.
 * <p/>
 * ****************************************************************************
 * User: jeffrey.a.west
 * Date: 4/4/11
 * Time: 10:57 AM
 */
@Local(EventService.class)
@Stateless(name="EventServiceBean", mappedName="ejb/EventService")
public class EventServiceImpl
    implements EventService,

               Serializable
{
  @Resource(name = Constants.CONNECTION_FACTORY_JNDI, type = ConnectionFactory.class)
  private ConnectionFactory connectionFactory;

  @Resource(name = Constants.EVENT_QUEUE_JNDI, type = Queue.class)
  private Queue queue;

  @Resource(name = Constants.EVENT_TOPIC_JNDI, type = Topic.class)
  private Topic topic;

  private Connection connection;
  private Session session;

  private MessageProducer queueProducer;
  private MessageProducer topicProducer;

  @PostConstruct
  public void initialize()
  {
    try
    {
      connection = connectionFactory.createConnection();
      session = (Session) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      queueProducer = (MessageProducer) session.createProducer(queue);
      topicProducer = (MessageProducer) session.createProducer(topic);
    }
    catch (JMSException e)
    {
      if (connection != null)
      {
        try
        {
          connection.close();
        }
        catch (JMSException f)
        {
          e.printStackTrace();
        }
      }
    }
  }

  @PreDestroy
  public void closeConnection()
  {
    try
    {
      if (connection != null)
      {
        connection.close();
      }
    }
    catch (JMSException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void sendEventToQueue(final com.oracle.demo.ops.domain.ParcelEvent event)
  {
    try
    {
      //queueProducer.setUnitOfOrder(getUnitOfOrder(event));
      queueProducer.send(session.createTextMessage(MyMarshaller.marshallObjectToString(event)));
    }
    catch (JMSException e)
    {
      e.printStackTrace();
    }
    catch (JAXBException e)
    {
      e.printStackTrace();
    }
  }

  private String getUnitOfOrder(com.oracle.demo.ops.domain.ParcelEvent event)
  {
    return "ParcelId_"+event.getParcelId();
  }

  @Override
  public void publishEventToTopic(final com.oracle.demo.ops.domain.ParcelEvent event)
  {
    try
    {
      String eventXmlString = MyMarshaller.marshallObjectToString(event);
      TextMessage newMessage = session.createTextMessage(eventXmlString);

      setJmsMessageProperties(event, newMessage);
//      topicProducer.setUnitOfOrder(getUnitOfOrder(event));
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
  }

  private void setJmsMessageProperties(final com.oracle.demo.ops.domain.ParcelEvent event, final TextMessage newMessage)
      throws
      JMSException
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
