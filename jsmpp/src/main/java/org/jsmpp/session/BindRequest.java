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
package org.jsmpp.session;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsmpp.PDUStringException;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.StringParameter;
import org.jsmpp.util.StringValidator;

/**
 * @author uudashr
 *
 */
public class BindRequest {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final BindType bindType;
    private final String systemId;
    private final String password;
    private final String systemType;
    private final TypeOfNumber addrTon;
    private final NumberingPlanIndicator addrNpi;
    private final String addressRange;
    
    private final int originalSequenceNumber;
    private boolean done;
    
    private final ServerResponseHandler responseHandler;
    public BindRequest(int sequenceNumber, BindType bindType, String systemId, String password, 
            String systemType, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, 
            String addressRange, ServerResponseHandler responseHandler) {
        this.originalSequenceNumber = sequenceNumber;
        this.responseHandler = responseHandler;
        
        this.bindType = bindType;
        this.systemId = systemId;
        this.password = password;
        this.systemType = systemType;
        this.addrTon = addrTon;
        this.addrNpi = addrNpi;
        this.addressRange = addressRange;
    }
    
    public BindRequest(Bind bind, ServerResponseHandler responseHandler) {
        this(bind.getSequenceNumber(), BindType.valueOf(bind.getCommandId()), bind.getSystemId(), 
                bind.getPassword(), bind.getSystemType(), 
                TypeOfNumber.valueOf(bind.getAddrTon()), 
                NumberingPlanIndicator.valueOf(bind.getAddrNpi()), 
                bind.getAddressRange(), responseHandler);
    }
    
    @Deprecated
    public BindParameter getBindParameter() {
        return new BindParameter(bindType, systemId, password, systemType, addrTon, addrNpi, addressRange);
    }
    
    public BindType getBindType() {
        return bindType;
    }
    
    public String getSystemId() {
        return systemId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getSystemType() {
        return systemType;
    }
    
    public TypeOfNumber getAddrTon() {
        return addrTon;
    }
    
    public NumberingPlanIndicator getAddrNpi() {
        return addrNpi;
    }
    
    public String getAddressRange() {
        return addressRange;
    }
    
    /**
     * Accept the bind request.
     * 
     * @param systemId is the system identifier that will be send to ESME.
     * @throws PDUStringException if the system id is not valid.
     * @throws IllegalStateException if the acceptance or rejection has been made.
     * @throws IOException is the connection already closed.
     * @see #reject(ProcessRequestException)
     */
    public void accept(String systemId) throws PDUStringException, IllegalStateException, IOException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        lock.lock();
        try {
            if (!done) {
                done = true;
                try {
                    responseHandler.sendBindResp(systemId, bindType, originalSequenceNumber);
                } finally {
                    condition.signal();
                }
            } else {
                throw new IllegalStateException("Response already initiated");
            }
        } finally {
            lock.unlock();
        }
        done = true;
    }
    
    /**
     * Reject the bind request.
     * 
     * @param errorCode is the reason of rejection.
     * @throws IllegalStateException if the acceptance or rejection has been made.
     * @throws IOException if the connection already closed.
     * @see {@link #accept()}
     */
    public void reject(int errorCode) throws IllegalStateException, IOException {
        lock.lock();
        try {
            if (done) {
                throw new IllegalStateException("Response already initiated");
            } else {
                done = true;
                try {
                    responseHandler.sendNegativeResponse(bindType.commandId(), errorCode, originalSequenceNumber);
                } finally {
                    condition.signal();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
