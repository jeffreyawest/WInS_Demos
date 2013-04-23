package com.oracle.demo.ops.web.beans.shipment;

import com.oracle.demo.ops.domain.*;
import com.oracle.demo.ops.web.beans.OPSController;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.primefaces.event.FlowEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
  * **************************************************************************
 * <p/>
 * This code is provided for example purposes only.  Oracle does not assume
 * any responsibility or liability for the consequences of using this code.
 * If you choose to use this code for any reason, including but not limited
 * to its use as an example you do so at your own risk and without the support
 * of Oracle.
 *
 * This code is provided under the following licenses:
 *
 * GNU General Public License (GPL-2.0)
 * COMMON DEVELOPMENT AND DISTRIBUTION LICENSE Version 1.0 (CDDL-1.0)
 *
 * <p/>
 * ****************************************************************************
 * User: jeffrey.a.west
 * Date: Feb 26, 2011
 * Time: 1:30:28 PM
 */
@ManagedBean(name = "shipmentWizard")
@SessionScoped
public class ShipmentWizard
    extends OPSController
{
  static final long serialVersionUID = 42L;

  public static final String CACHE_NAME = "ops-notification-cache";

  private Address toAddress = new Address();
  private Address fromAddress = new Address();
  private List<Parcel> parcelList = new ArrayList<Parcel>();


  private ShippingService selectedService = new ShippingService();
  private String selectedServiceName = "foobar";

  private String parcelContents;
  private int parcelWeight;
  private int parcelHeight;
  private int parcelLength;
  private int parcelWidth;

  private String testString = "foo";

  private boolean skip;

  private Shipment finalShipment;

  private static transient Logger logger = Logger.getLogger(ShipmentWizard.class.getName());

  public ShipmentWizard()
  {
  }

  public String save()
  {
    logger.info("FINISH!!");
    logger.info("FINISH!!");
    logger.info("FINISH!!");
    Shipment shipment = new Shipment();
    shipment.setFromAddress(fromAddress);
    shipment.setToAddress(toAddress);
    shipment.setShippingService(selectedService);
    shipment.getParcels().addAll(parcelList);

    finalShipment = getShipmentManager().createShipment(shipment);

    parcelList = new ArrayList<Parcel>();

    FacesMessage msg = new FacesMessage("Shipment ID:" + finalShipment.getId(), "Shipment ID:" + finalShipment.getId());
    FacesContext.getCurrentInstance().addMessage(null, msg);

    parcelList.clear();
    toAddress = new Address();
    fromAddress = new Address();

//    NamedCache cache = CacheFactory.getCache(CACHE_NAME);
//    cache.put(finalShipment.getId(), finalShipment);


    return "success";
  }

  public String finish()
  {
    Shipment shipment = new Shipment();
    shipment.setFromAddress(fromAddress);
    shipment.setToAddress(toAddress);
    shipment.setShippingService(selectedService);
    shipment.getParcels().addAll(parcelList);

    finalShipment = getShipmentManager().createShipment(shipment);

    parcelList = new ArrayList<Parcel>();

    FacesMessage msg = new FacesMessage("Shipment ID:" + finalShipment.getId(), "Shipment ID:" + finalShipment.getId());
    FacesContext.getCurrentInstance().addMessage(null, msg);

    parcelList.clear();
    toAddress = new Address();
    fromAddress = new Address();
    return "success";
  }

  public String deleteParcelAction(ActionEvent event)
  {
    Map<String, Object> params = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

    Parcel selectedParcel = (Parcel) params.get("parcel");

    parcelList.remove(selectedParcel);

    return null;
  }

  public void addParcelActionListener(ActionEvent actionEvent)
  {
    Parcel p = new Parcel();

    p.setContents(parcelContents);
    parcelContents = null;
    p.setWeight(parcelWeight);
    parcelWeight = 0;
    p.setHeight(parcelHeight);
    parcelHeight = 0;
    p.setWidth(parcelWidth);
    parcelWidth = 0;
    p.setLength(parcelLength);
    parcelLength = 0;

    parcelList.add(p);
  }

  public void save(ActionEvent actionEvent)
  {
  }

  public boolean isSkip()
  {
    return skip;
  }

  public void setSkip(boolean skip)
  {
    this.skip = skip;
  }

  public String onFlowProcess(FlowEvent event)
  {
    logger.info("Current wizard step:" + event.getOldStep());
    logger.info("Next step:" + event.getNewStep());

    if (skip)
    {
      skip = false;   //reset in case user goes back
      return "confirm";
    }
    else
    {
      return event.getNewStep();
    }
  }

  public void ajaxListener()
  {
    System.out.println("ajaxListener!");
  }

  public void selectServiceValueChangeListener(ValueChangeEvent pEvent)
  {
    System.out.println("selectServiceValueChangeListener");
    selectServiceListener(pEvent);
  }

  public void selectServiceActionListener(ActionEvent pEvent)
  {
    System.out.println("selectServiceActionListener");
    selectServiceListener(pEvent);
  }

  public void selectServiceListener(FacesEvent event)
  {
    System.out.println("selectServiceActionListener");
    System.out.println(event.getComponent().getAttributes().get("action"));

    UIComponent tmpComponent = event.getComponent();

    while (null != tmpComponent && !(tmpComponent instanceof UIData))
    {
      tmpComponent = tmpComponent.getParent();
    }

    if (tmpComponent != null)
    {
      final Object tmpRowData = ((UIData) tmpComponent).getRowData();

      if (tmpRowData instanceof ShippingService)
      {
        setSelectedService((ShippingService) tmpRowData);

        System.out.println("Selected Shipping Service: " + selectedService.getId());
      }
      else
      {
        System.out.println(this.getClass() + "::selectServiceListener::Wrong Class!!===" + tmpComponent.getClass());
      }
    }

  }

  public Address getToAddress()
  {
    return toAddress;
  }

  public void setToAddress(Address toAddress)
  {
    this.toAddress = toAddress;
  }

  public Address getFromAddress()
  {
    return fromAddress;
  }

  public void setFromAddress(Address fromAddress)
  {
    this.fromAddress = fromAddress;
  }

  public List<Parcel> getParcelList()
  {
    return parcelList;
  }

  public void setParcelList(List<Parcel> parcelList)
  {
    this.parcelList = parcelList;
  }

  public String getParcelContents()
  {
    return parcelContents;
  }

  public void setParcelContents(String parcelContents)
  {
    this.parcelContents = parcelContents;
  }

  public int getParcelWeight()
  {
    return parcelWeight;
  }

  public void setParcelWeight(int parcelWeight)
  {
    this.parcelWeight = parcelWeight;
  }

  public int getParcelHeight()
  {
    return parcelHeight;
  }

  public void setParcelHeight(int parcelHeight)
  {
    this.parcelHeight = parcelHeight;
  }

  public int getParcelLength()
  {
    return parcelLength;
  }

  public void setParcelLength(int parcelLength)
  {
    this.parcelLength = parcelLength;
  }

  public int getParcelWidth()
  {
    return parcelWidth;
  }

  public void setParcelWidth(int parcelWidth)
  {
    this.parcelWidth = parcelWidth;
  }

  public Shipment getFinalShipment()
  {
    return finalShipment;
  }

  public void setFinalShipment(Shipment finalShipment)
  {
    this.finalShipment = finalShipment;
  }

  public List<ShippingService> getShippingServiceList()
  {
    getShippingServiceManager().warmCache();
    return getShippingServiceManager().findAllShippingServices();
  }

  public ShippingService getSelectedService()
  {
    System.out.println("getSelectedService name=" + (selectedService == null ? "NULL" : selectedService.getName()));
    return selectedService;
  }

  public String getSelectedServiceName()
  {
    System.out.println("getSelectedServiceName");
    return selectedServiceName;
  }

  public void setSelectedServiceName(final String selectedServiceName)
  {
    System.out.println("setSelectedServiceName " + selectedServiceName);
    this.selectedServiceName = selectedServiceName;
  }

  public void setSelectedService(ShippingService pService)
  {
    this.selectedService = pService;
    try
    {
      System.out.println("setSelectedService name=" + pService.getName());
    }
    catch (Exception ignore)
    {
    }
  }

  public String getTestString()
  {
    return testString;
  }

  public void setTestString(final String testString)
  {
    this.testString = testString;
  }

  public static Logger getLogger()
  {
    return logger;
  }

  public static void setLogger(final Logger logger)
  {
    ShipmentWizard.logger = logger;
  }
}