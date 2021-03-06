/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.batch.configuration;

import com.sitewhere.spi.microservice.configuration.model.IRoleKey;

public enum BatchOperationsRoleKeys implements IRoleKey {

    /** Batch operations */
    BatchOperations("batch_ops"),

    /** Batch operation manager */
    BatchOperationManager("batch_op_mgr");

    private String id;

    private BatchOperationsRoleKeys(String id) {
	this.id = id;
    }

    /*
     * @see com.sitewhere.spi.microservice.configuration.model.IRoleKey#getId()
     */
    @Override
    public String getId() {
	return id;
    }
}