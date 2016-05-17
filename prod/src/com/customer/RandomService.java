package com.customer;

import java.util.Random;

import com.sterlingcommerce.woodstock.util.frame.log.LogService;
import com.sterlingcommerce.woodstock.util.frame.log.Logger;
import com.sterlingcommerce.woodstock.workflow.WFCBase;
import com.sterlingcommerce.woodstock.services.IService;
import com.sterlingcommerce.woodstock.workflow.WorkFlowContext;
import com.sterlingcommerce.woodstock.workflow.WorkFlowException;

public class RandomService implements IService{
	/*
	 * this logger logs to the system logger that is defined in the
	 * properties/log*properties* files
	 */
	private static Logger logger = LogService.getLogger("systemlogger");
	public final static long DEFAULT_SEED = 10;

	public final static String SCOPE = "Service";
	public final static String SUBSYSTEM = "RandomService";
	public final static String TYPE_DOUBLE = "DOUBLE";
	public final static String TYPE_FLOAT = "FLOAT";
	public final static String TYPE_INT = "INT";
	public final static String TYPE_LONG = "LONG";

	/*
	 * the following parameters are the ones that have been defined in the
	 * servicedefinition file for this Service
	 */
	public final static String SEED_PARAMETER_NAME = "seed";
	public final static String RETURN_TYPE_PARAMETER_NAME = "returnType";
	public final static String RANDOM_VALUE = "randomValue";

	public RandomService() {
	}

	// Every service needs to have this method and this is the entry point of
	// the service.
	//
	public WorkFlowContext processData(WorkFlowContext wfc) throws WorkFlowException {
		try {
			wfc.harnessRegister();
			long seed = DEFAULT_SEED;
			String randomValueToReturn = "";
			String seedString = wfc.getParm(SEED_PARAMETER_NAME);
			if (seedString != null && seedString.length() > 0) {
				// Found a String lets try to convert it to long
				try {
					seed = Long.parseLong(seedString);
				} catch (NumberFormatException nfe) {
					/* Not able to read the parameter, go back to the default */
					Object[] arguments = { seedString };
					logger.log(SCOPE, SUBSYSTEM, "parameter seed with value {0} can not be parsed into a long value",
							arguments);
				}
			}

			Object[] arguments = { seed };
			logger.log(SCOPE, SUBSYSTEM, "random seed is {0}", arguments);
			Random randomNumberGenerator = new Random(seed);

			String returnTypeString = wfc.getParm(RETURN_TYPE_PARAMETER_NAME);
			if (returnTypeString != null && returnTypeString.length() > 0) {
				/*
				 * Looks like we have a desired type for the random Number, lets
				 * see if we can identify it The option list in the service
				 * definition file does contain these constants DOUBLE FLOAT INT
				 * LONG
				 * 
				 * we will fall back to Long, just in case there is nothing
				 * meaningfull specified
				 */
				Object[] args = { returnTypeString };
				logger.log(SCOPE, SUBSYSTEM, "return type parameter read as {0} ", args);
				if (TYPE_DOUBLE.equalsIgnoreCase(returnTypeString)) {
					randomValueToReturn = Double.toString(randomNumberGenerator.nextDouble());
				} else if (TYPE_FLOAT.equalsIgnoreCase(returnTypeString)) {
					randomValueToReturn = Float.toString(randomNumberGenerator.nextFloat());
				} else if (TYPE_INT.equalsIgnoreCase(returnTypeString)) {
					randomValueToReturn = Integer.toString(randomNumberGenerator.nextInt());
				} else {
					randomValueToReturn = Long.toString(randomNumberGenerator.nextLong());
				}
			} else {
				/*
				 * if nothing has been specified at all we will also fall back
				 * to LONG
				 */
				logger.log(SCOPE, SUBSYSTEM, "No return type provided, fall back to LONG");
				randomValueToReturn = Long.toString(randomNumberGenerator.nextLong());
			}
			Object[] resultargs = { randomValueToReturn };
			logger.log(SCOPE, SUBSYSTEM, "random Number generator generated {0} as Value", resultargs);
			wfc.setWFContent(RANDOM_VALUE, randomValueToReturn);
			wfc.setBasicStatus(WFCBase.SUCCESS);
		} catch (Exception e) {
			wfc.setBasicStatus(WFCBase.ERROR);
			throw new WorkFlowException(e.toString());
		} finally {
			wfc.unregisterThread();
		}
		return wfc;
	}

}
