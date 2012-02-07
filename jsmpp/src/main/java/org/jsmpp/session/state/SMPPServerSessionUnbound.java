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
package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 *
 */
public class SMPPServerSessionUnbound implements SMPPServerSessionState {
    
    public SessionState getSessionState() {
        return SessionState.UNBOUND;
    }

    public void processBind(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
    
    public void processSubmitMulti(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
    
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
    
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
    
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
    
    public void processCancelSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
    
    public void processReplaceSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
}
