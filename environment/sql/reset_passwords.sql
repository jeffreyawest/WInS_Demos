-- truncates tables otherwise WLS can get confused...
truncate table weblogic_examples_domain.active drop storage;
truncate table saf_target_domain.active drop storage;
truncate table weblogic_examples_domain.jmsstore1wlstore drop storage;
truncate table weblogic_examples_domain.jmsstore2wlstore drop storage;
truncate table weblogic_examples_domain.safstore1wlstore drop storage;
truncate table weblogic_examples_domain.safstore2wlstore drop storage;

ALTER PROFILE DEFAULT LIMIT
FAILED_LOGIN_ATTEMPTS UNLIMITED
PASSWORD_LIFE_TIME UNLIMITED;

alter user sys identified by welcome1;
alter user system identified by welcome1;
alter user ops_user identified by ops_user;
alter user ops identified by ops;
alter user weblogic_examples_domain identified by weblogic_examples_domain;
alter user saf_target_domain identified by saf_target_domain;

exit;
