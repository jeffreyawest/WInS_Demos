package com.oracle.demo.ops.entitymanager.ejb;

import com.oracle.demo.ops.domain.Address;
import com.oracle.demo.ops.entitymanager.AddressManager;

import javax.ejb.Local;
import javax.ejb.Stateless;

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
 * User: jeffrey.a.west
 * Date: Sep 23, 2011
 * Time: 10:29:31 AM
 */
@Local(AddressManager.class)
@Stateless(name = "AddressManagerBean", mappedName = "ejb/AddressManager")
public class AddressManagerImpl implements AddressManager
{
  @Override
  public Address getAddressById(int pId)
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
