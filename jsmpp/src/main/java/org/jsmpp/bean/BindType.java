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
package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * Enum represent bind type of SMPP. There is 3 bind type provide by SMPP:
 * <ul>
 * <li>receiver (BIND_RX) - receive only</li>
 * <li>transmitter (BIND_TX) - transmit only</li>
 * <li>transceiver (BIND_TRX) - receive and transmit</li>
 * </ul>
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public enum BindType {

    /**
     * Bind transmitter.
     */
    BIND_TX(SMPPConstant.CID_BIND_TRANSMITTER),

    /**
     * Bind receiver.
     */
    BIND_RX(SMPPConstant.CID_BIND_RECEIVER),

    /**
     * Bind transceiver.
     */
    BIND_TRX(SMPPConstant.CID_BIND_TRANSCEIVER);

    private int bindCommandId;

    private BindType(int bindCommadId) {
        this.bindCommandId = bindCommadId;
    }

    /**
     * Get the command_id of given bind type.
     * 
     * @return the bind command_id.
     */
    public int commandId() {
        return bindCommandId;
    }

    /**
     * Return the response command_id that should given by this bind type.
     * 
     * @return the bind response command_id.
     */
    public int responseCommandId() {
        return bindCommandId | SMPPConstant.MASK_CID_RESP;
    }

    /**
     * Inform whether the bind type is transmitable.
     * 
     * @return <tt>true</tt> f the bind type is transmitable, otherwise
     *         <tt>false</tt>.
     */
    public boolean isTransmitable() {
        return this == BIND_TX || this == BIND_TRX;
    }

    /**
     * Inform whether the bind type is receivable.
     * 
     * @return <tt>true</tt> if the bind type is receivable, otherwise
     *         <tt>false</tt>.
     */
    public boolean isReceiveable() {
        return this == BIND_RX || this == BIND_TRX;
    }

    /**
     * Get the <tt>BindType</tt> by specified command_id.
     * 
     * @param bindCommandId is the command_id.
     * @return the enum const associated with the specified
     *         <tt>bindCommandId</tt>.
     * @throws IllegalArgumentException if there is no <tt>BindType</tt>
     *         associated with specified <tt>bindCommandId</tt> found.
     */
    public static final BindType valueOf(int bindCommandId)
            throws IllegalArgumentException {
        for (BindType bindType : values()) {
            if (bindType.bindCommandId == bindCommandId) {
                return bindType;
            }
        }
        throw new IllegalArgumentException(
                "No enum const BindType with command id " + bindCommandId);
    }
}
