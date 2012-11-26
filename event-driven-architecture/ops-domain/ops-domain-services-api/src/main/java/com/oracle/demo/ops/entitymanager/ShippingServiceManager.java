package com.oracle.demo.ops.entitymanager;

import com.oracle.demo.ops.domain.ShippingService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
 * Date: Sep 15, 2011
 * Time: 10:03:25 AM
 */
public interface ShippingServiceManager extends Serializable
{
  public List<ShippingService> findAllShippingServices();

  public boolean warmCache();

  public void addShippingService(ShippingService newShippingService);

  public void updateService(ShippingService selectedShippingService);
}
