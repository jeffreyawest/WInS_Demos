#!/bin/sh

PROXY_SCRIPT="/home/oracle/setProxy.sh"
ORACLE_HTTP_PROXY="http://www-proxy.us.oracle.com:80"

echo "#!/bin/sh" > ${PROXY_SCRIPT}
echo "export http_proxy=\"${ORACLE_HTTP_PROXY}\"" >> ${PROXY_SCRIPT}
echo "git config --global http.proxy \"${ORACLE_HTTP_PROXY}\"" >> ${PROXY_SCRIPT}

chmod +x ${PROXY_SCRIPT}