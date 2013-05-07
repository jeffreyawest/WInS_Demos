#!/bin/sh

PROXY_SCRIPT="/home/oracle/setProxy.sh"

echo "#!/bin/sh" > ${PROXY_SCRIPT}
echo "export http_proxy=\"\"" >> ${PROXY_SCRIPT}
echo "git config --global http.proxy \"\"" >> ${PROXY_SCRIPT}

chmod +x ${PROXY_SCRIPT}