package sessionbean;

/**
 * Business interface for the Account stateful session EJB.
 */

public interface Account {

  public void deposit(int amount);
  public void withdraw(int amount);
  public void sayHelloFromAccountBean();

}