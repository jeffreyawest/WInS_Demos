package com.oracle.demo.ops.services.ejb;

import com.oracle.demo.ops.domain.*;
import com.oracle.demo.ops.domain.WebServiceResponseHeader;
import com.oracle.demo.ops.entitymanager.ParcelEventManager;
import com.oracle.demo.ops.entitymanager.ParcelManager;
import com.oracle.demo.ops.entitymanager.ShipmentManager;
import com.oracle.demo.ops.services.EventService;
import com.oracle.demo.ops.services.ParcelService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.io.Serializable;
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
 * Date: Feb 22, 2011
 * Time: 11:20:29 AM
 */
@Local(ParcelService.class)
@Stateless(name="ParcelServiceBean", mappedName="ejb/ParcelService")
public class ParcelServiceImpl
        implements ParcelService,
                   Serializable
{
  @EJB(beanName="ParcelManagerBean")
  private ParcelManager parcelManager;

  @EJB(beanName="ParcelEventManagerBean")
  private ParcelEventManager parcelEventManager;

  @EJB(beanName="ShipmentManagerBean")
  private ShipmentManager shipmentManager;

  @EJB(beanName="EventServiceBean")
  private EventService EventService;


  public ParcelServiceImpl()
  {
  }

  public GetParcelByIdResponse getParcelById(GetParcelByIdRequest pRequest)
  {
    //System.out.println("ParcelServiceImpl.getParcelById");
    GetParcelByIdResponse resp = new GetParcelByIdResponse();
    resp.setResponseHeader(new WebServiceResponseHeader());    
    resp.getResponseHeader().setRequestHeader(pRequest.getRequestHeader());
    resp.setParcel(parcelManager.getParcelById(pRequest.getParcelId()));
    return resp;
  }

  @Override
  public AddParcelLogEventJMSPROXYResponse addParcelEventJMSPROXY(AddParcelLogEventJMSPROXYRequest pRequest)
  {
    EventService.sendEventToQueue(pRequest.getParcelLogEvent());

    AddParcelLogEventJMSPROXYResponse resp = new AddParcelLogEventJMSPROXYResponse();
    resp.setResponseHeader(new WebServiceResponseHeader());
    resp.getResponseHeader().setRequestHeader(pRequest.getRequestHeader());
    resp.setParcelLogEvent(pRequest.getParcelLogEvent());
    return resp;
  }

  @Override
  public void publishParcelEvent(com.oracle.demo.ops.domain.ParcelEvent event)
  {
    EventService.sendEventToQueue(event);
  }

  @Override
  public GetParcelEventLogResponse getParcelEvents(GetParcelEventLogRequest pRequest)
  {
    //System.out.println("ParcelServiceImpl.getParcelEvents");

    GetParcelEventLogResponse resp = new GetParcelEventLogResponse();
    List<com.oracle.demo.ops.domain.ParcelEvent> list = parcelEventManager.getParcelLogByParcelId(pRequest.getParcelId());
    resp.setResponseHeader(new WebServiceResponseHeader());
    resp.getResponseHeader().setRequestHeader(pRequest.getRequestHeader());
    resp.getParcelLogEvents().addAll(list);
    return resp;
  }

  public ParcelManager getParcelManager()
  {
    return parcelManager;
  }

  public void setParcelManager(ParcelManager parcelManager)
  {
    this.parcelManager = parcelManager;
  }

  public ShipmentManager getShipmentManager()
  {
    return shipmentManager;
  }

  public void setShipmentManager(ShipmentManager shipmentManager)
  {
    this.shipmentManager = shipmentManager;
  }

  public EventService getEventService()
  {
    return EventService;
  }

  public void setEventService(EventService eventService)
  {
    EventService = eventService;
  }


}
