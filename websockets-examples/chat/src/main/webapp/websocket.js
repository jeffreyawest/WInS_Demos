var chathandle;
var chatPipe;
var wsUri = "ws://" + document.location.host + document.location.pathname + "ws";
//("ws://localhost:7001/chat-1.0-SNAPSHOT/ws");

function _chatJoin()
{
  chathandle = prompt('Join chat with handle...', 'User-1');
  chatPipe = new WebSocket(wsUri);
  chatPipe.onopen = _chatOpen;
  chatPipe.onmessage = _chatReceive;
  chatPipe.onclose = _chatClose;
}

function _chatOpen(event)
{
  _chatSend('---------- joined ----------');
  document.getElementById('joinchat').disabled = true;
  document.getElementById('leavechat').disabled = false;
}

function _chatClose(event)
{
  _chatLog('---------- WebSocket is now closed ----------');
  document.getElementById('joinchat').disabled = false;
  document.getElementById('leavechat').disabled = true;
}

function _chatLeave()
{
  _chatSend('---------- left ----------');
  chatPipe.close();
}

function _chatReceive(event)
{
  if (event.data.indexOf("---------- joined ----------") != -1)
  {
    userField.innerHTML +=
        event.data.substring(0, event.data.indexOf(": ---------- joined ----------"))
            + "\n";
  }
  else if (event.data.indexOf("---------- left ----------") != -1)
  {
    msg_type_idx = event.data.indexOf("msg_type")

    pipe_idx = event.data.indexof("|", msg_type_idx)

    msg_type = event.data.substring(msg_type_idx + 9, pipe_idx)

    if (msg_type == "join")
    {
      // add the user to the list
    }
    else if (msg_type == "leave")
    {
      // remove the user from the list
    }
    else if (msg_type == "msg")
    {
      // display message
    }

    tstamp_idx = event.data.indexOf("tstamp")
    src_usr_idx = event.data.indexOf("src_user")
    msg_idx = event.data.indexOf("msg")
  }
  else
  {
    chatlogField.innerHTML += event.data + "\n";
  }
  _chatLog(event.data);
}

function _chatSend(msg)
{
  chatPipe.send(chathandle + ": " + msg);
  document.getElementById('msg').select();
}

function _chatLog(msg)
{
  var now = new Date();
  var h = now.getHours();
  var m = now.getMinutes();
  var s = now.getSeconds();
  if (h < 10)
  {
    h = '0' + h;
  }
  if (m < 10)
  {
    m = '0' + m;
  }
  if (s < 10)
  {
    s = '0' + s;
  }

  var la = document.getElementById('logarea');
  if (!la)
  {
    return;
  }
  la.value = la.value + "[" + h + ":" + m + ":" + s + "] " + msg + '\n';
}
