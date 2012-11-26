package sessionbean;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;

/**
 * Interceptor class.  The interceptor method is annotated with the
 *  @AroundInvoke annotation.
 */

public class AuditInterceptor {

  public AuditInterceptor() {}

  @AroundInvoke
  public Object audit(InvocationContext ic) throws Exception {
    System.out.println("Invoking method: "+ic.getMethod());
    return ic.proceed();
  }

  @PostActivate
  public void postActivate(InvocationContext ic) {
    System.out.println("Invoking method: "+ic.getMethod());
  }

  @PrePassivate
  public void prePassivate(InvocationContext ic) {
    System.out.println("Invoking method: "+ic.getMethod());
  }

}
