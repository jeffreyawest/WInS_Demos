#!/bin/sh

PROXY_SCRIPT="/home/oracle/setProxy.sh"

cat "#!/bin/sh" > ${PROXY_SCRIPT}
cat "export http_proxy=\"\"" >> ${PROXY_SCRIPT}
cat "git config --global http.proxy \"\"" >> ${PROXY_SCRIPT}

chmod +x ${PROXY_SCRIPT}