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

import org.jsmpp.PDUStringException;

/**
 * String validator for the SMPP PDU string types.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public final class StringValidator {
    private StringValidator() {
    }

    public static void validateString(String value, StringParameter param)
            throws PDUStringException {
        if (param.getType() == StringType.C_OCTEC_STRING) {
            if (param.isRangeMinAndMax()) {
                if (!isCOctetStringValid(value, param.getMax())) {
                    throw new PDUStringException("C-Octet String value '"
                            + value + "' length must be less than " + param.getMax()
                            + ". Actual length is " + value.length(),
                            param);
                }
            } else if (!isCOctetStringNullOrNValValid(value, param.getMax())) {
                throw new PDUStringException(
                        "C-Octet String value '" + value + "' length should be 1 or " + (param.getMax() - 1)
                                + ". Actual length is "
                                + value.length(), param);
            }
        } else if (param.getType() == StringType.OCTET_STRING
                && !isOctetStringValid(value, param.getMax())) {
            throw new PDUStringException("Octet String value '" + value
                    + "' length must be less than or equal to " + param.getMax()
                    + ". Actual length is " + value.length(), param);
        }
    }

    public static void validateString(byte[] value, StringParameter param)
            throws PDUStringException {
        if (param.getType() == StringType.C_OCTEC_STRING) {
            if (param.isRangeMinAndMax()) {
                if (!isCOctetStringValid(value, param.getMax())) {
                    throw new PDUStringException("C-Octet String value '"
                            + new String(value) + "' length must be less than "
                            + param.getMax() + ". Actual length is "
                            + value.length, param);
                }
            } else if (!isCOctetStringNullOrNValValid(value, param.getMax())) {
                throw new PDUStringException(
                        "C-Octet String value '" + new String(value) + "' length should be 1 or " + (param.getMax() - 1)
                                + ". Actual length is "
                                + value.length, param);
            }
        } else if (param.getType() == StringType.OCTET_STRING
                && !isOctetStringValid(value, param.getMax())) {
            throw new PDUStringException("Octet String value '"
                    + new String(value) + "' length must be less than or equal to "
                    + param.getMax() + ". Actual length is "
                    + value.length, param);
        }
    }

    /**
     * Validate the C-Octet String.
     * 
     * @param value
     * @param maxLength
     * @return
     */
    static boolean isCOctetStringValid(String value, int maxLength) {
        if (value == null)
            return true;
        if (value.length() >= maxLength)
            return false;
        return true;

    }

    static boolean isCOctetStringValid(byte[] value, int maxLength) {
        if (value == null)
            return true;
        if (value.length >= maxLength)
            return false;
        return true;

    }

    /**
     * Validate the C-Octet String
     * 
     * @param value
     * @param length
     * @return
     */
    static boolean isCOctetStringNullOrNValValid(String value,
            int length) {
        if (value == null) {
            return true;
        }
        
        if (value.length() == 0) {
            return true;
        }
        
        if (value.length() == length - 1) {
            return true;
        }
        return false;
    }

    static boolean isCOctetStringNullOrNValValid(byte[] value,
            int length) {
        if (value == null)
            return true;
        if (value.length == 0)
            return true;
        if (value.length == length - 1) {
            return true;
        }
        return false;
    }

    /**
     * Validate the Octet String
     * 
     * @param value
     * @param maxLength
     * @return
     */
    static boolean isOctetStringValid(String value, int maxLength) {
        if (value == null)
            return true;
        if (value.length() > maxLength)
            return false;
        return true;
    }

    static boolean isOctetStringValid(byte[] value, int maxLength) {
        if (value == null)
            return true;
        if (value.length > maxLength)
            return false;
        return true;
    }
}
