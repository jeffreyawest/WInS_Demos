package com.oracle.demo.ops.rest;

import com.oracle.demo.ops.entitymanager.AddressManager;
import com.oracle.demo.ops.entitymanager.ParcelEventManager;
import com.oracle.demo.ops.entitymanager.ParcelManager;
import com.oracle.demo.ops.entitymanager.ShipmentManager;
import com.oracle.demo.ops.services.EventService;

import javax.naming.InitialContext;
import javax.naming.NamingException;

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
 * Date: May 16, 2011
 * Time: 3:52:39 PM
 */
public class EJBProxy
{
  public ParcelManager parcelManager;
  public AddressManager addressManager;
  public ParcelEventManager parcelEventManager;
  public ShipmentManager shipmentManager;
  public EventService eventService;

  public EJBProxy()
      throws
      NamingException
  {
    InitialContext ic = new InitialContext();
    parcelManager = (ParcelManager) ic.lookup("java:comp/env/ejb/ParcelManager");
    parcelEventManager = (ParcelEventManager) ic.lookup("java:comp/env/ejb/ParcelEventManager");
    shipmentManager = (ShipmentManager) ic.lookup("java:comp/env/ejb/ShipmentManager");
    eventService = (EventService) ic.lookup("java:comp/env/ejb/EventJMSClient");
  }
}
