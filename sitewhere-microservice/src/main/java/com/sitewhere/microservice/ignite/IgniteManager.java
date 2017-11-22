package com.sitewhere.microservice.ignite;

import java.util.ArrayList;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSystemProperties;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sitewhere.microservice.MicroserviceEnvironment;
import com.sitewhere.server.lifecycle.LifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.IMicroservice;
import com.sitewhere.spi.microservice.ignite.IIgniteManager;
import com.sitewhere.spi.server.lifecycle.ILifecycleProgressMonitor;

/**
 * Default implementation of {@link IIgniteManager}.
 * 
 * @author Derek
 */
public class IgniteManager extends LifecycleComponent implements IIgniteManager {

    /** Static logger instance */
    private static Logger LOGGER = LogManager.getLogger();

    /** Microservice */
    private IMicroservice microservice;

    /** Ignite configuration */
    private IgniteConfiguration igniteConfiguration;

    /** Ignite instance */
    private Ignite ignite;

    public IgniteManager(IMicroservice microservice) {
	this.microservice = microservice;
    }

    /*
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#initialize(com.sitewhere.
     * spi.server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void initialize(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	System.setProperty(IgniteSystemProperties.IGNITE_QUIET, "true");
	this.igniteConfiguration = createIgniteConfiguration();
    }

    /*
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#start(com.sitewhere.spi.
     * server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void start(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	this.ignite = Ignition.start(getIgniteConfiguration());
    }

    /*
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#stop(com.sitewhere.spi.
     * server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void stop(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	if (getIgnite() != null) {
	    getIgnite().close();
	}
    }

    /**
     * Create configuration for Apache Ignite.
     * 
     * @return
     * @throws SiteWhereException
     */
    protected IgniteConfiguration createIgniteConfiguration() throws SiteWhereException {
	IgniteConfiguration configuration = new IgniteConfiguration();

	// Turn off logging for metrics.
	configuration.setMetricsLogFrequency(0);

	// Redirect logging to SiteWhere.
	configuration.setGridLogger(new MicroserviceIgniteLogger());

	// Use TCP/IP discovery with instance management microservice as hub.
	TcpDiscoverySpi discovery = new TcpDiscoverySpi();
	TcpDiscoveryVmIpFinder finder = new TcpDiscoveryVmIpFinder();
	List<String> addresses = new ArrayList<String>();
	addresses.add(getMicroservice().getIdentifier());
	addresses.add(MicroserviceEnvironment.HOST_INSTANCE_MANAGEMENT);
	finder.setAddresses(addresses);
	discovery.setIpFinder(finder);
	configuration.setDiscoverySpi(discovery);

	return configuration;
    }

    /*
     * @see com.sitewhere.spi.microservice.ignite.IIgniteManager#getIgnite()
     */
    @Override
    public Ignite getIgnite() {
	return ignite;
    }

    protected void setIgnite(Ignite ignite) {
	this.ignite = ignite;
    }

    /*
     * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
     */
    @Override
    public Logger getLogger() {
	return LOGGER;
    }

    protected IMicroservice getMicroservice() {
	return microservice;
    }

    protected void setMicroservice(IMicroservice microservice) {
	this.microservice = microservice;
    }

    public IgniteConfiguration getIgniteConfiguration() {
	return igniteConfiguration;
    }

    public void setIgniteConfiguration(IgniteConfiguration igniteConfiguration) {
	this.igniteConfiguration = igniteConfiguration;
    }
}