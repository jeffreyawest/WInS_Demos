package com.oracle.demo.ops.web.beans.services;

import com.oracle.demo.ops.domain.ShippingService;
import com.oracle.demo.ops.web.beans.OPSController;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jeffrey.a.west
 * Date: Oct 11, 2011
 * Time: 11:54:52 AM
 */
@ManagedBean(name = "ServiceControllerBean")
@SessionScoped
public class ServiceControllerBean
    extends OPSController
{
  @Resource(mappedName = "com.oracle.demo.ops.jdbc.cluster-ds")
  private DataSource dataSource;

  private List<ShippingService> allServices = new ArrayList<ShippingService>();
  private ShippingService newShippingService = new ShippingService();
  private ShippingService selectedShippingService;

  public List<ShippingService> getAllServices()
  {
    allServices.clear();
    allServices.addAll(getShippingServiceManager().findAllShippingServices());
    return allServices;
  }

  public void editShippingServiceActionListener(ActionEvent event)
  {
    getShippingServiceManager().updateService(selectedShippingService);
  }

  public void addShippingServiceActionListener(ActionEvent event)
  {
    addService(newShippingService);
    newShippingService = new ShippingService();
  }

  private void addService(ShippingService pService)
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = dataSource.getConnection();
      stmt = conn.prepareStatement("INSERT INTO SHIPPINGSERVICE (ID, NAME, DESCRIPTION) VALUES (?,?,?)");
      stmt.setInt(1, pService.getId());
      stmt.setString(2, pService.getName());
      stmt.setString(3, pService.getDescription());
      stmt.executeUpdate();
      conn.commit();
      conn.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    finally
    {
      if(conn != null)
      {
        try
        {
          conn.close();
        }
        catch(SQLException e)
        {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }

    }
  }


  public List<ShippingService> getShippingServicesListTopLink()
  {
    return getShippingServiceManager().findAllShippingServices();
  }

  public void selectShippingServiceListener(ActionEvent event)
  {
    System.out.println(event.getComponent().getAttributes().get("action"));

    UIComponent tmpComponent = event.getComponent();

    while(null != tmpComponent && !(tmpComponent instanceof UIData))
    {
      tmpComponent = tmpComponent.getParent();
    }

    if(tmpComponent != null)
    {
      final Object tmpRowData = ((UIData) tmpComponent).getRowData();

      if(tmpRowData instanceof ShippingService)
      {
        selectedShippingService = (ShippingService) tmpRowData;

        System.out.println("Selected Shipping Service: "+selectedShippingService.getId());
      }
      else
      {
        System.out.println(this.getClass()+"::selectShippingServiceListener::Wrong Class!!==="+tmpComponent.getClass());
      }
    }
  }

  public void warmCacheActionListner(ActionEvent event)
  {
    System.out.println("WARMING!!");
    System.out.println("WARMING!!");
    getShippingServiceManager().warmCache();
  }

  public String warmCacheAction()
  {
    getShippingServiceManager().warmCache();

    return "/ops/shippingServices/listServices";
  }

  public List<ShippingService> getShippingServicesListSQL()
  {
    List<ShippingService> list = new ArrayList<ShippingService>();

    Connection conn = null;
    Statement stmt = null;

    try
    {
      conn = dataSource.getConnection();
      stmt = conn.createStatement();

      ResultSet rs = stmt.executeQuery("select * from shippingservice");

      while(rs.next())
      {
        ShippingService svc = new ShippingService();
        svc.setId(rs.getInt("ID"));
        svc.setCost(rs.getDouble("COST"));
        svc.setDescription(rs.getString("DESCRIPTION"));
        svc.setName(rs.getString("NAME"));
        list.add(svc);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      if(stmt != null)
      {
        try
        {
          stmt.close();
        }
        catch(SQLException ignore)
        {

        }
      }

      if(conn != null)
      {
        try
        {
          conn.close();
        }
        catch(SQLException ignore)
        {
        }
      }


    }

    return list;
  }

  public void setAllServices(List<ShippingService> allServices)
  {
    this.allServices = allServices;
  }

  public ShippingService getNewShippingService()
  {
    return newShippingService;
  }

  public void setNewShippingService(ShippingService newShippingService)
  {
    this.newShippingService = newShippingService;
  }

  public ShippingService getSelectedShippingService()
  {
    return selectedShippingService;
  }

  public void setSelectedShippingService(ShippingService selectedShippingService)
  {
    this.selectedShippingService = selectedShippingService;
  }
}
