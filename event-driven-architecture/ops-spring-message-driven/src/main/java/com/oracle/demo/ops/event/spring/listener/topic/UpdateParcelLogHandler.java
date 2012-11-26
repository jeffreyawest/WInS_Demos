package com.oracle.demo.ops.event.spring.listener.topic;

import com.oracle.demo.ops.domain.ParcelEvent;
import com.oracle.demo.ops.string.StringUtils;

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
 * Date: Feb 15, 2011
 * Time: 10:19:39 AM
 */
public class UpdateParcelLogHandler
    extends AbstractEventHandler
{
  protected void handleEventInternal(ParcelEvent event)
  {
    String theString = StringUtils.toString(event);
    System.out.println("Spring:: Add Event to Parcel Log:: " + theString);
  }
}