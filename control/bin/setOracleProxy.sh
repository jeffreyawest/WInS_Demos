#!/bin/sh

PROXY_SCRIPT="/home/oracle/setProxy.sh"
ORACLE_HTTP_PROXY="http://www-proxy.us.oracle.com:80"

PROXY_MESSAGE="Proxy Configured for Oracle Network!!!"

echo "#!/bin/sh" > ${PROXY_SCRIPT}
echo "export http_proxy=\"${ORACLE_HTTP_PROXY}\"" >> ${PROXY_SCRIPT}
echo "sudo git config --global http.proxy \"${ORACLE_HTTP_PROXY}\"" >> ${PROXY_SCRIPT}
echo "echo \"${PROXY_MESSAGE}\"" >> ${PROXY_SCRIPT}
echo "echo \"${PROXY_MESSAGE}\"" >> ${PROXY_SCRIPT}
echo "echo \"${PROXY_MESSAGE}\"" >> ${PROXY_SCRIPT}

echo ${PROXY_MESSAGE}

chmod +x ${PROXY_SCRIPT}

if [ "$1" == "wait" ]; then
  read -p "Checkout complete. Press [Enter] to close the window"
 fi
