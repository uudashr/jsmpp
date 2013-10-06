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
package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.CancelSmResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.ReplaceSmResp;
import org.jsmpp.bean.SubmitMultiResp;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is unbound state implementation of {@link SMPPSessionState}. This class give specific response to a
 * transmit related transaction, otherwise it always give negative response.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionBoundTX extends SMPPSessionBound implements SMPPSessionState {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionBoundTX.class);

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TX;
    }

    @Override
    public void processSubmitSmResp(Command pduHeader, byte[] pdu, ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                SubmitSmResp resp = AbstractGenericSMPPSessionBound.pduDecomposer.submitSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                SMPPSessionBoundTX.logger.error("Failed decomposing submit_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
            }
        } else {
            SMPPSessionBoundTX.logger.warn("No request with sequence number " + pduHeader.getSequenceNumber() + " found");
        }
    }

    @Override
    public void processSubmitMultiResp(Command pduHeader, byte[] pdu, ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                SubmitMultiResp resp = AbstractGenericSMPPSessionBound.pduDecomposer.submitMultiResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                SMPPSessionBoundTX.logger.error("Failed decomposing submit_multi_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
            }
        } else {
            SMPPSessionBoundTX.logger.warn("No request with sequence number " + pduHeader.getSequenceNumber() + " found");
        }
    }

    @Override
    public void processQuerySmResp(Command pduHeader, byte[] pdu, ResponseHandler responseHandler) throws IOException {

        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                QuerySmResp resp = AbstractGenericSMPPSessionBound.pduDecomposer.querySmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                SMPPSessionBoundTX.logger.error("Failed decomposing submit_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
            }
        } else {
            SMPPSessionBoundTX.logger.error("No request find for sequence number " + pduHeader.getSequenceNumber());
            responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processCancelSmResp(Command pduHeader, byte[] pdu, ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            CancelSmResp resp = AbstractGenericSMPPSessionBound.pduDecomposer.cancelSmResp(pdu);
            pendingResp.done(resp);
        } else {
            SMPPSessionBoundTX.logger.error("No request find for sequence number " + pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processReplaceSmResp(Command pduHeader, byte[] pdu, ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            ReplaceSmResp resp = AbstractGenericSMPPSessionBound.pduDecomposer.replaceSmResp(pdu);
            pendingResp.done(resp);
        } else {
            SMPPSessionBoundTX.logger.error("No request find for sequence number " + pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processDeliverSm(Command pduHeader, byte[] pdu, ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }

    @Override
    public void processDeliverSmResp(Command pduHeader, byte[] pdu, ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }

    @Override
    public void processAlertNotification(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) {
        SMPPSessionBoundTX.logger.error("Receiving alert_notification while on invalid bound state (transmitter)");
    }
}
