package org.sample.chat;

import weblogic.websocket.*;
import weblogic.websocket.annotation.*;

import java.util.HashMap;
import java.util.StringTokenizer;

@WebSocket
    (
        timeout = -1,
        pathPatterns = {"/ws/*"}
    )
public class ChatEndpoint extends WebSocketAdapter
{
  private HashMap<String, String> users;

  public ChatEndpoint()
  {
    users = new HashMap<String, String>(10);
  }

  public boolean accept(WSHandshakeRequest request, WSHandshakeResponse response)
  {
    return true;
  }

  public void onMessage(WebSocketConnection connection, String payload)
  {
    HashMap<String, String> messageMap = getMessageMap(payload);

    switch (messageMap.get("msg_type"))
    {
      case("join"):
        users.put(messageMap.get("user"), messageMap.get("tstamp"));

      case("leave"):
        users.remove(messageMap.get("user"));

    }

    for (WebSocketConnection wsConn : getWebSocketContext().getWebSocketConnections())
    {
      try
      {
        wsConn.send(getUserListMessage(users));
        wsConn.send(payload);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private String getUserListMessage(HashMap<String, String> users)
  {
    return null;
  }

  private HashMap<String, String> getMessageMap(String payload)
  {
    HashMap map = new HashMap(7);
    StringTokenizer st = new StringTokenizer("|");

    return null;  //To change body of created methods use File | Settings | File Templates.
  }

  public void onTimeout(WebSocketConnection connection)
  {
    try
    {
      connection.send("The connection is closed because of timeout!");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void onError(WebSocketConnection connection, Throwable error)
  {
    try
    {
      connection.send("The connection is closed because of error: " + error.getMessage());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}