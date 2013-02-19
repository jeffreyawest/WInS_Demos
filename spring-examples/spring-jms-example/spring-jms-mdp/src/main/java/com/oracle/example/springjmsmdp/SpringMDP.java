/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.example.springjmsmdp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author ankitpan
 */
public class SpringMDP implements MessageListener {
public void onMessage(Message message) {
 try {
   System.out.println(((TextMessage) message).getText());
 } catch (JMSException ex) {
   throw new RuntimeException(ex);
 }
}
}
