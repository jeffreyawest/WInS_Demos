package com.oracle.demo.ops.services;

import com.oracle.demo.ops.domain.*;
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
 *
 * This code is provided under the following licenses:
 *
 * GNU General Public License (GPL-2.0)
 * COMMON DEVELOPMENT AND DISTRIBUTION LICENSE Version 1.0 (CDDL-1.0)
 *
 * <p/>
 * ****************************************************************************
 * User: jeffrey.a.west
 * Date: Feb 16, 2011
 * Time: 4:39:08 PM
 */
@Local
public interface ParcelService
 extends Serializable
{
  GetParcelByIdResponse getParcelById(GetParcelByIdRequest pRequest);

  AddParcelLogEventJMSPROXYResponse addParcelEventJMSPROXY(AddParcelLogEventJMSPROXYRequest pRequest);

  void publishParcelEvent(ParcelEvent event);

  GetParcelEventLogResponse getParcelEvents(GetParcelEventLogRequest pRequest);
}