/**
 * **************************************************************************
 * <p/>
 * This code is provided for example purposes only.  Oracle does not assume
 * any responsibility or liability for the consequences of using this code.
 * If you choose to use this code for any reason, including but not limited
 * to its use as an example you do so at your own risk and without the support
 * of Oracle.
 * <p/>
 * *************************************************************************** */
package com.oracle.demo.ops.rest.v2;

import com.oracle.demo.ops.entitymanager.ParcelEventManager;
import com.oracle.demo.ops.entitymanager.ShipmentManager;
import com.oracle.demo.ops.entitymanager.ParcelManager;
import com.oracle.demo.ops.services.EventService;

import javax.naming.InitialContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


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
 * Date: Mar 2, 2011
 * Time: 8:13:30 AM
 */
public class OPSRestService
        extends javax.ws.rs.core.Application
{
  protected static final int    JSON_FORMAT = 0;
  protected static final int    XML_FORMAT  = 1;
  private static final   String XML_STR     = "XML";

  protected ParcelManager parcelManager;
  protected ParcelEventManager parcelEventManager;
  protected ShipmentManager shipmentManager;
  protected EventService eventService;
  
  public OPSRestService()
  {
    try
    {
      InitialContext ic = new InitialContext();
      parcelEventManager = (ParcelEventManager) ic.lookup("java:comp/env/ejb/ParcelEventManager");
      parcelManager = (ParcelManager) ic.lookup("java:comp/env/ejb/ParcelManager");
      shipmentManager = (ShipmentManager) ic.lookup("java:comp/env/ejb/ShipmentManager");
      eventService = (EventService) ic.lookup("java:comp/env/ejb/EventJMSClient");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }


  protected Response buildResponse(int format, Object entity)
  {
    if (format == JSON_FORMAT)
    {
      return Response.status(200).type(MediaType.APPLICATION_JSON).entity(entity).build();
    }
    else
    {
      return Response.status(200).type(MediaType.APPLICATION_XML).entity(entity).build();
    }
  }

  protected static int getFormat(final String pFormat)
  {
    if (pFormat != null && pFormat.toUpperCase().contains(XML_STR))
    {
      return XML_FORMAT;
    }

    return JSON_FORMAT;
  }
}
