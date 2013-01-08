package com.oracle.demo.jsf.converter;

import com.oracle.demo.ops.domain.ParcelStatus;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;

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
 * Date: Mar 3, 2011
 * Time: 4:33:00 PM
 */
@FacesConverter(forClass = com.oracle.demo.ops.domain.ParcelStatus.class)
public class ParcelStatusStringConverter
        implements Converter
{
  private static final Map<String, Object> parcelStatusMap;
  private static final Map<Object, String> reverseMap;

  static
  {
    parcelStatusMap = new HashMap<String, Object>();
    parcelStatusMap.put("Billing Information Received", ParcelStatus.BILLING_INFO_RECEIVED);
    parcelStatusMap.put("Parcel Delivered", ParcelStatus.DELIVERED);
    parcelStatusMap.put("In Transit", ParcelStatus.IN_TRANSIT);
    parcelStatusMap.put("Parcel Received By Shipping Facility", ParcelStatus.PARCEL_RECEIVED);
    parcelStatusMap.put("Delivery Failed", ParcelStatus.DELIVERY_EXCEPTION);

    reverseMap = new HashMap<Object, String>();

    for (Map.Entry<String, Object> statusEntry : parcelStatusMap.entrySet())
    {
      reverseMap.put(statusEntry.getValue(), statusEntry.getKey());
    }
  }

  @Override
  public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
  {
    return parcelStatusMap.get(s);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
  {
    return reverseMap.get(o);
  }

  public static Map<String, Object> getParcelStatusMap()
  {
    return parcelStatusMap;
  }
}
