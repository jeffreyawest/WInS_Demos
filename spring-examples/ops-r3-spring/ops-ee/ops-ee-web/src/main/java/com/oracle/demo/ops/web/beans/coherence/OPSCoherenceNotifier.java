package com.oracle.demo.ops.web.beans.coherence;

import java.lang.*;
import java.util.*;
import java.util.logging.*;

import com.tangosol.net.*;

import com.oracle.demo.ops.domain.*;

public class OPSCoherenceNotifier {

	private static final Logger logger = Logger.getLogger(OPSCoherenceNotifier.class.getName());
	private static final String CACHE_NAME = "ops-notification-cache";

	public static void publish(Shipment shipment) {
		Cluster cluster = CacheFactory.ensureCluster();
/*
		logger.info("----------------------------------------------------");
		Service svc = cluster.ensureService("OPSDistributedCache"
			, CacheService.TYPE_DISTRIBUTED);
		logger.log(Level.INFO, "Service name: " + svc.getInfo().getServiceName());
		logger.log(Level.INFO, "Serializer name: " + svc.getSerializer().getClass().getName());
*/
		NamedCache cache = CacheFactory.getCache(CACHE_NAME);
		logger.log(Level.INFO, "Publishing shipment: id = {0}", shipment.getId());
		logger.log(Level.INFO, "Cache name: " + cache.getCacheName());
		logger.log(Level.INFO, "Service name: " + cache.getCacheService().getInfo().getServiceName());
		cache.put(shipment.getId(), shipment);
		logger.log(Level.INFO, "After adding to cache");
	}

	public static void publish(Map<Integer, Shipment> shipmentList) {
		CacheFactory.ensureCluster();
		NamedCache cache = CacheFactory.getCache(CACHE_NAME);
		logger.log(Level.INFO, "Bulk publishing shipments: {0}", shipmentList.size());
		cache.putAll(shipmentList);
	}
}
