/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * @author uudashr
 * 
 */
public class DeliverSm extends MessageRequest {

    public DeliverSm() {
        super();
    }

    /**
     * Get the short message as {@link DeliveryReceipt}. This method will be valid if the parsed short message valid and
     * Message Type (esm_class) contains SMSC Delivery Receipt.
     * 
     * @return the {@link DeliveryReceipt}.
     * @throws InvalidDeliveryReceiptException
     *             if there is an error found while parsing delivery receipt.
     */
    public DeliveryReceipt getShortMessageAsDeliveryReceipt()
            throws InvalidDeliveryReceiptException {
        return getDeliveryReceipt(DefaultDeliveryReceiptStripper.getInstance());
    }

    /**
     * Create a response object for deliver_sm message synchronized with the sequence number.
     * 
     * @return DeliverSmResp to wrap deliver_sm_resp message.
     */
    public DeliverSmResp createResponse() {
        DeliverSmResp resp = new DeliverSmResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * Get delivery receipt based on specified strategy/stripper.
     * 
     * @param <T>
     * @param stripper
     *            is the stripper.
     * @return the delivery receipt as an instance of T.
     * @throws InvalidDeliveryReceiptException
     *             if there is an error found while parsing delivery receipt.
     */
    public <T> T getDeliveryReceipt(DeliveryReceiptStrip<T> stripper)
            throws InvalidDeliveryReceiptException {
        return stripper.strip(this);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isSmscDeliveryReceipt() {
        return DeliverSm.isSmscDeliveryReceipt(esmClass);
    }

    /**
     * Message Type.
     * 
     * @param value
     */
    public void setSmscDeliveryReceipt() {
        esmClass = DeliverSm.composeSmscDeliveryReceipt(esmClass);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isSmeManualAcknowledgment() {
        return DeliverSm.isSmeManualAcknowledgment(esmClass);
    }

    /**
     * Message Type.
     */
    public void setSmeManualAcknowledgment() {
        esmClass = DeliverSm.composeSmeManualAcknowledment(esmClass);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isConversationAbort() {
        return DeliverSm.isConversationAbort(esmClass);
    }

    /**
     * Message Type.
     */
    public void setConversationAbort() {
        esmClass = DeliverSm.composeConversationAbort(esmClass);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isIntermedietDeliveryNotification() {
        return DeliverSm.isIntermedietDeliveryNotification(esmClass);
    }

    /**
     * Message Type.
     */
    public void setIntermedietDeliveryNotification() {
        esmClass = DeliverSm.composeIntermedietDeliveryNotification(esmClass);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @return
     */
    public boolean isSmeAckNotRequested() {
        return DeliverSm.isSmeAckNotRequested(registeredDelivery);
    }

    /**
     * SME originated Acknowledgment.
     */
    public void setSmeAckNotRequested() {
        registeredDelivery = DeliverSm.composeSmeAckNotRequested(registeredDelivery);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @return
     */
    public boolean isSmeDeliveryAckRequested() {
        return DeliverSm.isSmeDeliveryAckRequested(registeredDelivery);
    }

    /**
     * SME originated Acknowledgment.
     */
    public void setSmeDeliveryAckRequested() {
        registeredDelivery = DeliverSm.composeSmeDeliveryAckRequested(registeredDelivery);
    }

    /**
     * SME originated Acknowledgement.
     * 
     * @return
     */
    public boolean isSmeManualAckRequested() {
        return DeliverSm.isSmeManualAckRequested(registeredDelivery);
    }

    /**
     * 
     * SME originated Acknowledgement.
     */
    public void setSmeManualAckRequested() {
        registeredDelivery = DeliverSm.composeSmeManualAckRequested(registeredDelivery);
    }

    /**
     * SME originated Acknowledgement.
     * 
     * @return
     */
    public boolean isSmeDeliveryAndManualAckRequested() {
        return DeliverSm.isSmeDeliveryAndManualAckRequested(registeredDelivery);
    }

    /**
     * SME originated Acknowledgement.
     */
    public void setSmeDeliveryAndManualAckRequested() {
        registeredDelivery = DeliverSm.composeSmeDeliveryAndManualAckRequested(registeredDelivery);
    }

    /**
     * Message Type. Return esm_class of deliver_sm or data_sm indicate delivery receipt or not.
     * 
     * @param esmClass
     * @return <tt>true</tt> if esmClass indicate delivery receipt
     */
    public static final boolean isSmscDeliveryReceipt(byte esmClass) {
        return AbstractSmCommand.isMessageType(esmClass, SMPPConstant.ESMCLS_SMSC_DELIV_RECEIPT);
    }

    /**
     * Message Type. Set esm_class as delivery receipt
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeSmscDeliveryReceipt(byte esmClass) {
        return AbstractSmCommand.composeMessageType(esmClass, SMPPConstant.ESMCLS_SMSC_DELIV_RECEIPT);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isSmeDeliveryAcknowledgment(byte esmClass) {
        return AbstractSmCommand.isMessageType(esmClass, SMPPConstant.ESMCLS_SME_DELIV_ACK);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeSmeDeliveryAcknowledment(byte esmClass) {
        return AbstractSmCommand.composeMessageType(esmClass, SMPPConstant.ESMCLS_SME_DELIV_ACK);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isSmeManualAcknowledgment(byte esmClass) {
        return AbstractSmCommand.isMessageType(esmClass, SMPPConstant.ESMCLS_SME_MANUAL_ACK);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeSmeManualAcknowledment(byte esmClass) {
        return AbstractSmCommand.composeMessageType(esmClass, SMPPConstant.ESMCLS_SME_MANUAL_ACK);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isConversationAbort(byte esmClass) {
        return AbstractSmCommand.isMessageType(esmClass, SMPPConstant.ESMCLS_CONV_ABORT);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeConversationAbort(byte esmClass) {
        return AbstractSmCommand.composeMessageType(esmClass, SMPPConstant.ESMCLS_CONV_ABORT);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isIntermedietDeliveryNotification(byte esmClass) {
        return AbstractSmCommand.isMessageType(esmClass, SMPPConstant.ESMCLS_INTRMD_DELIV_NOTIF);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeIntermedietDeliveryNotification(byte esmClass) {
        return AbstractSmCommand.composeMessageType(esmClass, SMPPConstant.ESMCLS_INTRMD_DELIV_NOTIF);
    }

    /*
     * SME originated Acknowledgement.
     */

    /**
     * SME originated Acknowledgement.
     * 
     * @param registeredDeliery
     * @return
     */
    public static final boolean isSmeAckNotRequested(byte registeredDeliery) {
        return AbstractSmCommand.isSmeAck(registeredDeliery, SMPPConstant.REGDEL_SME_ACK_NO);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final byte composeSmeAckNotRequested(byte registeredDelivery) {
        return AbstractSmCommand.composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_ACK_NO);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @param registeredDeliery
     * @return
     */
    public static final boolean isSmeDeliveryAckRequested(byte registeredDeliery) {
        return AbstractSmCommand.isSmeAck(registeredDeliery, SMPPConstant.REGDEL_SME_DELIVERY_ACK_REQUESTED);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final byte composeSmeDeliveryAckRequested(byte registeredDelivery) {
        return AbstractSmCommand.composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_ACK_REQUESTED);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final boolean isSmeManualAckRequested(byte registeredDelivery) {
        return AbstractSmCommand.isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_MANUAL_ACK_REQUESTED);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final byte composeSmeManualAckRequested(byte registeredDelivery) {
        return AbstractSmCommand.composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_MANUAL_ACK_REQUESTED);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final boolean isSmeDeliveryAndManualAckRequested(byte registeredDelivery) {
        return AbstractSmCommand.isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED);
    }

    /**
     * SME originated Acknowledgment.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final byte composeSmeDeliveryAndManualAckRequested(byte registeredDelivery) {
        return AbstractSmCommand.composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED);
    }
}
