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
package org.jsmpp;

/**
 * This exception thrown if we receive unexpected response (such as invalid PDU
 * parameter).
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class InvalidResponseException extends Exception {
    private static final long serialVersionUID = -7149393390064407238L;

    /**
     * Construct with specified message and cause.
     * 
	 * @param message is the detail message.
	 * @param cause is the parent cause.
	 */
	public InvalidResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
     * Construct with specified message.
     * 
	 * @param message is the detail message.
	 */
	public InvalidResponseException(String message) {
		super(message);
	}
}
