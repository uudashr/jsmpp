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

import java.io.DataInputStream;
import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.util.OctetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link PDUReader}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class DefaultPDUReader implements PDUReader {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPDUReader.class);

    /* (non-Javadoc)
     * @see org.jsmpp.PDUReader#readPDUHeader(java.io.DataInputStream)
     */
    public Command readPDUHeader(DataInputStream in)
            throws InvalidCommandLengthException, IOException {
		final String DEBUG_STR = ":readPDUHeader: ";
		int available = in.available();
		if (available == 0 ) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				LOG.error("{} ", DEBUG_STR, e);
			}
			return null;
		}

		Command header = new Command();
		int commandLength = in.readInt();
		LOG.trace("{} command length {}, available in stream {}", new Object[] {DEBUG_STR, commandLength, in.available()});
		header.setCommandLength(commandLength);

        if (header.getCommandLength() < 16) {
			LOG.trace("{} command length is too short", DEBUG_STR);            // command length to short, read the left dump anyway
            byte[] dump = new byte[header.getCommandLength()];
            in.read(dump, 4, header.getCommandLength() - 4);

            throw new InvalidCommandLengthException("Command length "
                    + header.getCommandLength() + " is to short");
        }
		int commandId = in.readInt();
		LOG.trace("{} command id {}, available in stream {}", new Object[] {DEBUG_STR, commandId, in.available()});
		header.setCommandId(commandId);
		int commandStatus = in.readInt();
		LOG.trace("{} command status {}, available in stream {}", new Object[] {DEBUG_STR, commandStatus, in.available()});
		header.setCommandStatus(commandStatus);
		int sequenceNumber = in.readInt();
		LOG.trace("{} command number {}, available in stream {}", new Object[] {DEBUG_STR, sequenceNumber, in.available()});
		header.setSequenceNumber(sequenceNumber);
		return header;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUReader#readPDU(java.io.InputStream, org.jsmpp.bean.Command)
     */
    public byte[] readPDU(DataInputStream in, Command pduHeader) throws IOException {
        return readPDU(in, pduHeader.getCommandLength(), pduHeader
                .getCommandId(), pduHeader.getCommandStatus(), pduHeader
                .getSequenceNumber());
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUReader#readPDU(java.io.InputStream, int, int, int, int)
     */
    public byte[] readPDU(DataInputStream in, int commandLength, int commandId,
            int commandStatus, int sequenceNumber) throws IOException {

        byte[] b = new byte[commandLength];
        System.arraycopy(OctetUtil.intToBytes(commandLength), 0, b, 0, 4);
        System.arraycopy(OctetUtil.intToBytes(commandId), 0, b, 4, 4);
        System.arraycopy(OctetUtil.intToBytes(commandStatus), 0, b, 8, 4);
        System.arraycopy(OctetUtil.intToBytes(sequenceNumber), 0, b, 12, 4);

        if (commandLength > 16) {
            synchronized (in) {
                in.readFully(b, 16, commandLength - 16);
            }
        }
        return b;
    }
}
