<html>

<head>
  <title>WebSocket Chat</title>
</head>

<body>
<H1>WebSocket Chat Room</H1>

<input type="button" value="Join chat" id="joinchat" onClick="_chatJoin()"/>
<input type="button" value="Leave chat" id="leavechat" disabled="true" onClick="_chatLeave()"/>
</br>

<table>
  <tr>
    <td>
      Users<br/>
      <textarea name="userField" readonly="true" rows="6" cols="20" id="userField"></textarea>
    </td>
    <td>
      Chat<br/>
      <textarea name="chatlogField" readonly="true" rows="6" cols="50" id="chatlogField"></textarea>
    </td>
  </tr>
</table>

Complete log<br/>
<textarea name="logarea" id="logarea" rows="10" cols="65" readonly="true"></textarea>
</br>

<input type="text" size="80" name="msg" id="msg" value="hello"
       onkeydown="if(event.keyCode==13)_chatSend(document.getElementById('msg').value)"/>

<script type="text/javascript" src="websocket.js"></script>

</body>

</html>