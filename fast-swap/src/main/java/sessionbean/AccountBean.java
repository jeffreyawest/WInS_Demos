package sessionbean;

import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.Remote;
import javax.ejb.EJB;

import javax.annotation.PreDestroy;

import javax.interceptor.Interceptors;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.InvocationContext;

/**
 * Bean file that implements the Account business interface.
 * Uses the following EJB annotations:
 *    -  @Stateful: specifies that this is a stateful session EJB
 *    -  @TransactionAttribute - specifies that this EJB never runs
 *         in a transaction
 *    -  @Remote - specifies the Remote interface for this EJB
 *    -  @EJB - specifies a dependency on the ServiceBean stateless
 *         session ejb
 *    -  @Interceptors - Specifies that the bean file is associated with an
 *         Interceptor class; by default all business methods invoke the
 *         method in the interceptor class annotated with @AroundInvoke.
 *    -  @ExcludeClassInterceptors - Specifies that the interceptor methods
 *         defined for the bean class should NOT fire for the annotated
 *         method.
 *    -  @PreDestroy - Specifies lifecycle method that is invoked when the
 *         bean is about to be destoryed by EJB container.
 *
 */

@Stateful
@TransactionAttribute(NEVER)
@Remote({sessionbean.Account.class})
@Interceptors({sessionbean.AuditInterceptor.class})
public class AccountBean
 implements Account
{

  private int balance = 0;

  public void deposit(int amount) {
    balance += amount;
    System.out.println("deposited: "+amount+" balance: "+balance);
  }

  public void withdraw(int amount) {
    balance -= amount;
    System.out.println("withdrew: "+amount+" balance: "+balance);
  }
  
  public int getBalance(){
	  int bogus;
	  return balance;
  }
  
  @ExcludeClassInterceptors
  public void sayHelloFromAccountBean() {
    
  }
  

  @PreDestroy
  public void preDestroy() {
   System.out.println("Invoking method: preDestroy()");  
  }

}