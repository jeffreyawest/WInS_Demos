package com.oracle.demo.ops.services;

import com.oracle.demo.ops.domain.*;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
 * Time: 4:39:15 PM
 */
@Local
public interface ShipmentService
 extends Serializable
{
  CreateShipmentResponse createShipment(CreateShipmentRequest pRequest);

  SendNewShipmentViaJMSResponse sendNewShipmentViaJMS(SendNewShipmentViaJMSRequest pRequest);

  GetShipmentByIdResponse getShipmentById(GetShipmentByIdRequest pShipmentId);

  GetShipmentByExternalIdResponse getShipmentByExternalId(GetShipmentByExternalIdRequest pExternalId);

  Collection<ShippingService> getAllShippingServices(boolean direct);
}