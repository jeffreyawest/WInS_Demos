package com.oracle.demo.ops.web.beans;

import com.oracle.demo.ops.entitymanager.*;
import com.oracle.demo.ops.entitymanager.ParcelEventManager;
import com.oracle.demo.ops.entitymanager.ParcelManager;
import com.oracle.demo.ops.entitymanager.ShippingServiceManager;
import com.oracle.demo.ops.services.*;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * **************************************************************************
 * <p/>
 * This code is provided for example purposes only.  Oracle does not assume
 * any responsibility or liability for the consequences of using this code.
 * If you choose to use this code for any reason, including but not limited
 * to its use as an example you do so at your own risk and without the support
 * of Oracle.
 * <p/>
 * ****************************************************************************`
 * User: jeffrey.a.west
 * Date: Jul 28, 2011
 * Time: 7:30:50 AM
 */
public class OPSController
    implements Serializable
{
  static final long serialVersionUID = 1001L;
  private static final Logger logger = Logger.getLogger(OPSController.class.getName());

  private static final boolean USE_LOCAL = true;

  private static final String EJB_PARCEL_MANAGER_LOCAL_JNDI = "ejb/ParcelManager";
  private static final String EJB_PARCEL_LOG_EVENT_MANAGER_LOCAL_JNDI = "ejb/ParcelEventManager";
  private static final String EJB_SHIPMENT_MANAGER_LOCAL_JNDI = "ejb/ShipmentManager";
  private static final String EJB_SHIPPING_SERVICE_MANAGER_LOCAL_JNDI = "ejb/ShippingServiceManager";
  private static final String EJB_SHIPMENT_SERVICE_LOCAL_JNDI = "ejb/ShipmentService";
  private static final String EJB_PARCEL_SERVICE_LOCAL_JNDI = "ejb/ParcelService";
  private static final String EJB_EVENT_SERVICE_LOCAL_JNDI = "ejb/EventService";

  @EJB(name = EJB_SHIPPING_SERVICE_MANAGER_LOCAL_JNDI)
  private transient ShippingServiceManager shippingServiceManager;

  @EJB(name = EJB_PARCEL_MANAGER_LOCAL_JNDI)
  private transient ParcelManager parcelManager;

  @EJB(name = EJB_PARCEL_LOG_EVENT_MANAGER_LOCAL_JNDI)
  private transient ParcelEventManager parcelLogEventManager;

  @EJB(name = EJB_SHIPMENT_MANAGER_LOCAL_JNDI)
  private transient ShipmentManager shipmentManager;

  @EJB(name = EJB_SHIPMENT_SERVICE_LOCAL_JNDI)
  private transient ShipmentService shipmentService;

  @EJB(name = EJB_PARCEL_SERVICE_LOCAL_JNDI)
  private transient ParcelService parcelService;

  @EJB(name = EJB_EVENT_SERVICE_LOCAL_JNDI)
  private transient EventService eventService;

  public ParcelManager getParcelManager()
  {
    if (parcelManager == null)
    {
      parcelManager = (ParcelManager) refreshEJB(EJB_PARCEL_MANAGER_LOCAL_JNDI);
    }
    return parcelManager;
  }

  public ParcelEventManager getParcelEventManager()
  {
    if (parcelLogEventManager == null)
    {
      parcelLogEventManager = (ParcelEventManager) refreshEJB(EJB_PARCEL_LOG_EVENT_MANAGER_LOCAL_JNDI);
    }
    return parcelLogEventManager;
  }

  public ShipmentManager getShipmentManager()
  {
    if (shipmentManager == null)
    {
      shipmentManager = (ShipmentManager) refreshEJB(EJB_SHIPMENT_MANAGER_LOCAL_JNDI);
    }
    return shipmentManager;
  }

  public ShippingServiceManager getShippingServiceManager()
  {
    if (shippingServiceManager == null)
    {
      shippingServiceManager = (ShippingServiceManager) refreshEJB(EJB_SHIPPING_SERVICE_MANAGER_LOCAL_JNDI);
    }
    return shippingServiceManager;
  }

  public ShipmentService getShipmentService()
  {
    if (shipmentService == null)
    {
      shipmentService = (ShipmentService) refreshEJB(EJB_SHIPMENT_SERVICE_LOCAL_JNDI);
    }
    return shipmentService;
  }

  public ParcelService getParcelService()
  {
    if (parcelService == null)
    {
      parcelService = (ParcelService) refreshEJB(EJB_PARCEL_SERVICE_LOCAL_JNDI);
    }
    return parcelService;
  }

  public EventService getEventService()
  {
    if (eventService == null)
    {
      eventService = (EventService) refreshEJB(EJB_EVENT_SERVICE_LOCAL_JNDI);
    }
    return eventService;
  }

  public static Object refreshEJB(String jndi)
  {
    if (USE_LOCAL)
    {
      jndi = "java:comp/env/" + jndi;
    }

    logger.info("////////////// looking up JNDI:" + jndi);

    InitialContext ctx = null;
    try
    {
      ctx = new InitialContext();
      return ctx.lookup(jndi);
    }
    catch (NamingException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  public HttpSession getSession()
  {
    return (HttpSession) getFacesContext().getExternalContext().getSession(false);
  }

  public String getSessionId()
  {
    return getSession().getId();
  }

  public String getWebLogicServerName()
  {
    return System.getProperty("weblogic.Name");
  }

  public FacesContext getFacesContext()
  {
    FacesContext ctx = FacesContext.getCurrentInstance();

    //ctx.get

    return ctx;
  }

  public void setParcelManager(ParcelManager parcelManager)
  {
    this.parcelManager = parcelManager;
  }

  public void setShipmentManager(ShipmentManager ShipmentManager)
  {
    this.shipmentManager = ShipmentManager;
  }

  public void setShipmentService(ShipmentService shipmentService)
  {
    this.shipmentService = shipmentService;
  }

  public void setParcelService(ParcelService parcelService)
  {
    this.parcelService = parcelService;
  }

  public void setEventService(EventService eventService)
  {
    this.eventService = eventService;
  }

}
