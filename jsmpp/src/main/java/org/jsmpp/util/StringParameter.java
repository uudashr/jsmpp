/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.jsmpp.util;

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public enum StringParameter {
	/**
	 * system_id string parameter.
	 */
	SYSTEM_ID(StringType.C_OCTEC_STRING, 0, 16, true, SMPPConstant.STAT_ESME_RINVSYSID),
    
	/**
	 * password string parameter.
	 */
	PASSWORD(StringType.C_OCTEC_STRING, 0, 9, true, SMPPConstant.STAT_ESME_RINVPASWD),
    
	/**
	 * system_type string parameter.
	 */
	SYSTEM_TYPE(StringType.C_OCTEC_STRING, 0, 13, true, SMPPConstant.STAT_ESME_RINVSYSTYP),
    
	/**
	 * Invalid address range assume should return the error of invalid source address.
	 */
	ADDRESS_RANGE(StringType.C_OCTEC_STRING, 0, 41, true, SMPPConstant.STAT_ESME_RINVSRCADR), 
    
	SERVICE_TYPE(StringType.C_OCTEC_STRING, 0, 6, true, SMPPConstant.STAT_ESME_RINVSERTYP),
	SOURCE_ADDR(StringType.C_OCTEC_STRING, 0, 21, true, SMPPConstant.STAT_ESME_RINVSRCADR), 
	DESTINATION_ADDR(StringType.C_OCTEC_STRING, 0, 21, true, SMPPConstant.STAT_ESME_RINVSRCADR), 
	SCHEDULE_DELIVERY_TIME(StringType.C_OCTEC_STRING, 0, 17, false, SMPPConstant.STAT_ESME_RINVSCHED), 
	VALIDITY_PERIOD(StringType.C_OCTEC_STRING, 0, 17, false, SMPPConstant.STAT_ESME_RINVEXPIRY),
	
	/**
	 * Validator for DL_NAME (Distribution List)
	 */
	DL_NAME(StringType.C_OCTEC_STRING, 0, 21, true, SMPPConstant.STAT_ESME_RINVDLNAME),
	
	/**
	 * When validating final date is failed, then we should throw error with status 
	 * STAT_ESME_RINVDFTMSGID, means that predefined message are not exist.
	 */
	FINAL_DATE(StringType.C_OCTEC_STRING, 0, 17, false, SMPPConstant.STAT_ESME_RINVDFTMSGID),
	SHORT_MESSAGE(StringType.OCTET_STRING, 0, 254, true, SMPPConstant.STAT_ESME_RINVMSGLEN),  
	MESSAGE_ID(StringType.C_OCTEC_STRING, 0, 65, true, SMPPConstant.STAT_ESME_RINVMSGID), 
	DEL_MESSAGE_ID(StringType.C_OCTEC_STRING, 0, 0, true, SMPPConstant.STAT_ESME_RINVMSGID), 
	/**
	 * ESME_ADDR has error code OK because there is not response to alert_notification
	 * (ESME_ADDR only use on alert_notification), so there is no error code to return.
	 */
	ESME_ADDR(StringType.C_OCTEC_STRING, 0, 65, true, SMPPConstant.STAT_ESME_ROK);
	
	private StringType type;
	private final int min;
	private final int max;
	private final boolean rangeMinAndMax;
	private final int errCode;
	
	/**
	 * @param type
	 * @param min
	 * @param max
	 * @param rangeMinAndMax
	 * @param errCode AKA error code on command_status.
	 */
	private StringParameter(StringType type, int min, int max, boolean rangeMinAndMax, int errCode) {
		this.type = type;
		this.min = min;
		this.max = max;
		this.rangeMinAndMax = rangeMinAndMax;
		this.errCode = errCode;
	}

	/**
	 * @return Returns the max.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @return Returns the min.
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @return Returns the rangeMinAndMax.
	 */
	public boolean isRangeMinAndMax() {
		return rangeMinAndMax;
	}

	public StringType getType() {
		return type;
	}
	
	/**
	 * Get the corresponding error code if the rule has broken.
	 * @return
	 */
	public int getErrCode() {
		return errCode;
	}
}
