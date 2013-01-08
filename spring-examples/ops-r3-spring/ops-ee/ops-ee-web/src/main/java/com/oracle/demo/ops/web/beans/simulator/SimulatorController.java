package com.oracle.demo.ops.web.beans.simulator;

import com.oracle.demo.ops.domain.*;
import com.oracle.demo.ops.web.beans.OPSController;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
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
 * ****************************************************************************
 * User: jeffrey.a.west
 * Date: Jul 27, 2011
 * Time: 10:06:13 AM
 */

@ManagedBean(name = "SimulatorControllerBean")
@SessionScoped
public class SimulatorController
    extends OPSController
{
  private static final Logger logger = Logger.getLogger(SimulatorController.class.getName());

  static final long serialVersionUID = 42L;
  public static final String CACHE_NAME = "ops-notification-cache";

  private int inputNumberOfShipments = 1;
  private int inputNumberOfParcelsPerShipment = 1;
  private long inputShipmentDelay = 500;
  private long inputParcelEventDelay = 100;
  private int currentShipmentNumber;

  public void beginSimulation(ActionEvent event)
  {
    final List<Shipment> listOfShipments = new ArrayList<Shipment>(inputNumberOfShipments);

    for(int x = 1; x<=inputNumberOfShipments; x++)
    {
      List<ShippingService> list = getShippingServiceManager().findAllShippingServices();

      ShippingService shippingService;

      if(list.size()<=0)
      {
        shippingService = new ShippingService();
        shippingService.setCost(1.0);
        shippingService.setCreatedDate(Calendar.getInstance());
        shippingService.setModifiedDate(Calendar.getInstance());
        shippingService.setDescription("Example Service");
        shippingService.setName("BASIC");

        getShippingServiceManager().addShippingService(shippingService);
      }
      else
      {
        shippingService = list.get(0);
      }

      currentShipmentNumber = x;
      logger.info("Creating Shipment "+currentShipmentNumber+" of "+inputNumberOfShipments);

      Shipment shipment = new Shipment();

      shipment.setFromAddress(createFromAddress());
      shipment.setToAddress(createToAddress());
      shipment.setShippingService(shippingService);

      shipment.getParcels().addAll(createParcels(inputNumberOfParcelsPerShipment));

      Shipment finalShipment = createShipment(shipment);
      listOfShipments.add(finalShipment);

/*
      try
      {
        CacheFactory.ensureCluster();
        NamedCache cache = CacheFactory.getCache(CACHE_NAME);

        if (cache != null)
        {
          logger.info("Putting shipment into notification cache...");
          cache.put(finalShipment.getId(), finalShipment);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
*/

      logger.info("Finished creating Shipment "+currentShipmentNumber);

      if(inputNumberOfShipments>1)
      {
        logger.info("Sleeping for Shipment Delay: "+inputShipmentDelay+"ms");
        sleep(inputShipmentDelay);
      }
    }

    int counter = 0;

    for(Shipment shipment : listOfShipments)
    {
      counter++;
      logger.info("Simulating events for Shipment "+counter+" of "+inputNumberOfShipments);

      simulateEvents(shipment, 5);
    }
  }

  private void sleep(long pDelay)
  {
    try
    {
      Thread.sleep(pDelay);
    }
    catch(InterruptedException e)
    {
      e.printStackTrace();
    }
  }

  private void simulateEvents(Shipment shipment, int numberOfEvents)
  {
    int counter = 0;
    for(Parcel parcel : shipment.getParcels())
    {
      counter++;
      logger.info("Simulating events for Parcel "+counter+" of "+inputNumberOfParcelsPerShipment);
      simulateEvents(parcel, numberOfEvents);
    }
  }

  private void simulateEvents(Parcel parcel, int numberOfStops)
  {
//    getFacesContext().addMessage(null, new FacesMessage("Starting Parcel Events", "Parcel ID: " + parcel.getId()));
    getEventService().sendEventToQueue(createEvent("Billing Information Received",
                                                   ParcelStatus.BILLING_INFO_RECEIVED,
                                                   Calendar.getInstance(),
                                                   "B2C Portal",
                                                   parcel.getId()));
    sleep(inputParcelEventDelay);

    getEventService().sendEventToQueue(createEvent("Parcel Received by Shipper",
                                                   ParcelStatus.PARCEL_RECEIVED,
                                                   Calendar.getInstance(),
                                                   "Retail Store #123",
                                                   parcel.getId()));
    sleep(inputParcelEventDelay);

    for(int x = 1; x<=numberOfStops; x++)
    {
      getEventService().sendEventToQueue(createEvent("Parcel arrived at location "+x,
                                                     ParcelStatus.IN_TRANSIT,
                                                     Calendar.getInstance(),
                                                     "Location "+x,
                                                     parcel.getId()));

      sleep(inputParcelEventDelay);

      getEventService().sendEventToQueue(createEvent("Parcel left location "+x,
                                                     ParcelStatus.IN_TRANSIT,
                                                     Calendar.getInstance(),
                                                     "Location "+x,
                                                     parcel.getId()));
      sleep(inputParcelEventDelay);

    }

    getEventService().sendEventToQueue(createEvent("Delivered",
                                                   ParcelStatus.DELIVERED,
                                                   Calendar.getInstance(),
                                                   "Final Destination",
                                                   parcel.getId()));
    sleep(inputParcelEventDelay);
//    getFacesContext().addMessage(null, new FacesMessage("Events complete for Parcel ID: " + parcel.getId()));
  }

  private ParcelEvent createEvent(String message, ParcelStatus status, Calendar date, String location, int parcelId)
  {
    ParcelEvent event = new ParcelEvent();
    event.setMessage(message);
    event.setParcelStatus(status);
    event.setDate(date);
    event.setLocation(location);
    event.setParcelId(parcelId);

    return event;
  }

  private Shipment createShipment(Shipment shipment)
  {
    CreateShipmentRequest request = new CreateShipmentRequest();
    request.setShipment(shipment);

    CreateShipmentResponse response = getShipmentService().createShipment(request);

    logger.info("Created Shipment ID="+response.getShipment().getId());
    getFacesContext().addMessage(null, new FacesMessage("Created Shipment ID:"+response.getShipment().getId(), ""));

    for(Parcel parcel : response.getShipment().getParcels())
    {
      getFacesContext().addMessage(null, new FacesMessage("Created Parcel ID:"+parcel.getId(), ""));
    }


    return response.getShipment();
  }

  private Collection<? extends Parcel> createParcels(int pNumberOfParcel)
  {
    List<Parcel> list = new ArrayList<Parcel>(pNumberOfParcel);

    for(int x = 1; x<=pNumberOfParcel; x++)
    {
      logger.info("Creating Parcel "+x+" of "+inputNumberOfParcelsPerShipment+" for shipment "+currentShipmentNumber+" of "+inputNumberOfShipments);

      Parcel p = createParcel(x);
      list.add(p);
    }

    return list;
  }

  private Parcel createParcel(int parcelNumber)
  {
    Parcel parcel = new Parcel();
    parcel.setContents("WebLogic Awesome Sauce - Parcel #"+parcelNumber);
    parcel.setHeight(1);
    parcel.setLength(2);
    parcel.setWidth(3);
    parcel.setWeight(5);
    parcel.setParcelStatus(ParcelStatus.BILLING_INFO_RECEIVED);

    return parcel;
  }

  private Address createToAddress()
  {
    Address address = new Address();
    address.setAddressee("Jeff West");
    address.setAddressLine1("123 Main Street");
    address.setAddressLine2("Example Line 2");
    address.setCity("Kinston");
    address.setState("NC");
    address.setPostalCode("12345");
    return address;
  }

  private Address createFromAddress()
  {
    Address address = new Address();
    address.setAddressee("Example Recipient Addressee");
    address.setAddressLine1("321 Main Street");
    address.setAddressLine2("Example Line 2");
    address.setCity("Redwood Shores");
    address.setState("CA");
    address.setPostalCode("54321");

    return address;
  }

  public int getInputNumberOfShipments()
  {
    return inputNumberOfShipments;
  }

  public void setInputNumberOfShipments(int inputNumberOfShipments)
  {
    this.inputNumberOfShipments = inputNumberOfShipments;
  }

  public int getInputNumberOfParcelsPerShipment()
  {
    return inputNumberOfParcelsPerShipment;
  }

  public void setInputNumberOfParcelsPerShipment(int inputNumberOfParcelsPerShipment)
  {
    this.inputNumberOfParcelsPerShipment = inputNumberOfParcelsPerShipment;
  }

  public long getInputShipmentDelay()
  {
    return inputShipmentDelay;
  }

  public void setInputShipmentDelay(long inputShipmentDelay)
  {
    this.inputShipmentDelay = inputShipmentDelay;
  }

  public long getInputParcelEventDelay()
  {
    return inputParcelEventDelay;
  }

  public void setInputParcelEventDelay(long inputParcelEventDelay)
  {
    this.inputParcelEventDelay = inputParcelEventDelay;
  }
}
