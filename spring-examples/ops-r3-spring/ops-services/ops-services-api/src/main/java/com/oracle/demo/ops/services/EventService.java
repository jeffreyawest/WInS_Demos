package com.oracle.demo.ops.services;

import com.oracle.demo.ops.domain.ParcelEvent;

import javax.ejb.Local;
import java.io.Serializable;

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
 * Date: 4/4/11
 * Time: 10:57 AM
 */
@Local
public interface EventService
 extends Serializable
{
  public void sendEventToQueue(ParcelEvent event);

  public void publishEventToTopic(ParcelEvent event);
}
