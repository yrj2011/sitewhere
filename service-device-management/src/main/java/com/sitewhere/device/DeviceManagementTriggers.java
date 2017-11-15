/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.device;

import java.util.Map;

import com.sitewhere.rest.model.device.event.request.DeviceStateChangeCreateRequest;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.IDeviceAssignment;
import com.sitewhere.spi.device.IDeviceManagement;
import com.sitewhere.spi.device.communication.IDeviceCommunication;
import com.sitewhere.spi.device.event.IDeviceEventManagement;
import com.sitewhere.spi.device.event.state.StateChangeCategory;
import com.sitewhere.spi.device.event.state.StateChangeType;
import com.sitewhere.spi.device.request.IDeviceAssignmentCreateRequest;

/**
 * Adds triggers for processing related to device management API calls.
 * 
 * @author Derek
 */
public class DeviceManagementTriggers extends DeviceManagementDecorator {

    /** Device event management */
    private IDeviceEventManagement deviceEventManangement;

    /** Device communication */
    private IDeviceCommunication deviceCommunication;

    public DeviceManagementTriggers(IDeviceManagement delegate, IDeviceEventManagement deviceEventManangement,
	    IDeviceCommunication deviceCommunication) {
	super(delegate);
	this.deviceEventManangement = deviceEventManangement;
	this.deviceCommunication = deviceCommunication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.device.DeviceManagementDecorator#createDeviceAssignment(com
     * .sitewhere .spi.device.request.IDeviceAssignmentCreateRequest)
     */
    @Override
    public IDeviceAssignment createDeviceAssignment(IDeviceAssignmentCreateRequest request) throws SiteWhereException {
	IDeviceAssignment created = super.createDeviceAssignment(request);
	DeviceStateChangeCreateRequest state = new DeviceStateChangeCreateRequest(StateChangeCategory.Assignment,
		StateChangeType.Assignment_Created, null, null);
	getDeviceEventManangement().addDeviceStateChange(created, state);
	return created;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sitewhere.device.DeviceManagementDecorator#
     * updateDeviceAssignmentMetadata(java.lang.String, java.util.Map)
     */
    @Override
    public IDeviceAssignment updateDeviceAssignmentMetadata(String token, Map<String, String> metadata)
	    throws SiteWhereException {
	IDeviceAssignment updated = super.updateDeviceAssignmentMetadata(token, metadata);
	DeviceStateChangeCreateRequest state = new DeviceStateChangeCreateRequest(StateChangeCategory.Assignment,
		StateChangeType.Assignment_Updated, null, null);
	getDeviceEventManangement().addDeviceStateChange(updated, state);
	return updated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.device.DeviceManagementDecorator#endDeviceAssignment(java.
     * lang. String)
     */
    @Override
    public IDeviceAssignment endDeviceAssignment(String token) throws SiteWhereException {
	IDeviceAssignment updated = super.endDeviceAssignment(token);
	DeviceStateChangeCreateRequest state = new DeviceStateChangeCreateRequest(StateChangeCategory.Assignment,
		StateChangeType.Assignment_Released, null, null);
	getDeviceEventManangement().addDeviceStateChange(updated, state);
	return updated;
    }

    public IDeviceEventManagement getDeviceEventManangement() {
	return deviceEventManangement;
    }

    public void setDeviceEventManangement(IDeviceEventManagement deviceEventManangement) {
	this.deviceEventManangement = deviceEventManangement;
    }

    public IDeviceCommunication getDeviceCommunication() {
	return deviceCommunication;
    }

    public void setDeviceCommunication(IDeviceCommunication deviceCommunication) {
	this.deviceCommunication = deviceCommunication;
    }
}