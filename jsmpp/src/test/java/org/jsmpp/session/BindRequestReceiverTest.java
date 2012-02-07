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

import java.util.concurrent.TimeoutException;

import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindType;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class BindRequestReceiverTest {
    private BindRequestReceiver requestReceiver;
    
    @BeforeMethod
    public void setUp() {
        requestReceiver = new BindRequestReceiver(new DummyResponseHandler());
    }
    
    @Test(groups="checkintest")
    public void testWaitTimeout() {
        
        try {
            BindRequest request = requestReceiver.waitForRequest(1000);
            fail("Should fail since no request for 1000 millis");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            fail("Should not fail waitForRequest");
        } catch (TimeoutException e) {
        }
    }
    
    @Test(groups="checkintest")
    public void testReceiveRequest() {
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
            BindRequest request = requestReceiver.waitForRequest(1000);
        } catch (IllegalStateException e) {
            fail("Should not fail waitForRequest and success accepting request");
        } catch (TimeoutException e) {
            fail("Should not fail waitForRequest and success accepting request");
        }
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
        
        try {
            requestReceiver.waitForRequest(1000);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        } catch (TimeoutException e) {
            fail("Should throw IllegalStateException");
        }
    }
    
    @Test(groups="checkintest")
    public void testNoSingleAccept() {
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
        } catch (IllegalStateException e) {
            fail("Should not fail waitForRequest and success accepting request");
        }
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }
    
    @Test(groups="checkintest")
    public void testNonSingleWait() {
        
        try {
            BindRequest request = requestReceiver.waitForRequest(1000);
            fail("Should throw TimeoutException");
        } catch (IllegalStateException e) {
            fail("Should throw TimeoutException");
        } catch (TimeoutException e) {
        }
        
        try {
            requestReceiver.waitForRequest(1000);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        } catch (TimeoutException e) {
            fail("Should throw IllegalStateException");
        }
    }
    
    private static final Bind dummyBind() {
        Bind bind = new Bind();
        bind.setCommandId(BindType.BIND_RX.commandId());
        return bind;
    }
}
