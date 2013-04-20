package com.oracle.weblogic.demo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * **************************************************************************
 * <p/>
 * This code is provided for example purposes only.  Oracle does not assume
 * any responsibility or liability for the consequences of using this code.
 * If you choose to use this code for any reason, including but not limited
 * to its use as an example you do so at your own risk and without the support
 * of Oracle.
 * <p/>
 * ****************************************************************************
 * Created with IntelliJ IDEA because its awesome.
 * User: jeffreyawest
 * Date: 4/18/13
 * Time: 11:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "AddressServiceBean", mappedName = "ejb/AddressService")
@LocalBean
public class AddingEJB
{

}
