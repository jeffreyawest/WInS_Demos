package com.oracle.demo.ops.string;

import com.oracle.demo.ops.domain.ParcelEvent;

/**
 * Created with IntelliJ IDEA.
 * User: jeffreyawest
 * Date: 4/30/12
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils
{
  public static String toString(ParcelEvent event)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Date: ").append(event.getDate());
    sb.append(" | Parcel ID: ").append(event.getParcelId());
    sb.append(" | Location: ").append(event.getLocation());
    sb.append(" | Status: ").append(event.getParcelStatus().value());
    sb.append(" | Message: ").append(event.getMessage());

    return sb.toString();
  }
}
