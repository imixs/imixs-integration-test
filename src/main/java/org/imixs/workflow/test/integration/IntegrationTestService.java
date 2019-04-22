/*******************************************************************************
 *  Imixs IX Workflow Technology
 *  Copyright (C) 2001, 2008 Imixs Software Solutions GmbH,  
 *  http://www.imixs.com
 *  
 *  This program is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU General Public License 
 *  as published by the Free Software Foundation; either version 2 
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 *  General Public License for more details.
 *  
 *  You can receive a copy of the GNU General Public
 *  License at http://www.gnu.org/licenses/gpl.html
 *  
 *  Contributors:  
 *  	Imixs Software Solutions GmbH - initial API and implementation
 *  	Ralph Soika
 *******************************************************************************/
package org.imixs.workflow.test.integration;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.imixs.workflow.exceptions.AccessDeniedException;
import org.imixs.workflow.exceptions.ModelException;
import org.imixs.workflow.exceptions.QueryException;

/**
 * The IntegrationTestService runs on deployment and starts the tests.
 * <p>
 * The service starts a EJB Timer Service which runs the tests
 * 
 * @author rsoika
 * 
 */
@DeclareRoles({ "org.imixs.ACCESSLEVEL.MANAGERACCESS" })
@RunAs("org.imixs.ACCESSLEVEL.MANAGERACCESS")
@Startup
@Singleton
public class IntegrationTestService {
	
	private static final long START_DELAY_SECONDS = 5 * 1000L;

	@Resource
	private TimerService timerService;

	
	@EJB
	LuceneTest luceneTest;
	@EJB
	ModelTest modelTest;
	
	private static Logger logger = Logger.getLogger(IntegrationTestService.class.getName());

	/**
	 * This method start the system setup during deployment
	 * 
	 * @throws AccessDeniedException
	 */
	@PostConstruct
	public void startup() {
		logger.info("...intializing Integration Tests in " + START_DELAY_SECONDS + " seconds...");
		TimerConfig config = new TimerConfig();
		config.setPersistent(false);
		timerService.createIntervalTimer(START_DELAY_SECONDS, START_DELAY_SECONDS, config);
	}

	/**
	 * Using EJB TimerService in current project for tasks like periodic data
	 * pruning, or back-end data synchronization. It allows not only single time
	 * execution, but also interval timers and timers with calendar based schedule.
	 * @throws QueryException 
	 * @throws ModelException 
	 */
	@Timeout
	private synchronized void onTimer() throws QueryException, ModelException {
		
		logger.info("...run Integration Tests...");
	
		
		if (modelTest.run()) {
			logger.info("====> ModelTest OK");
		}
		
		
		if (luceneTest.run()) {
			logger.info("====> LuceneTest OK");
		}
	}
	
	
	
	
	
	
}






