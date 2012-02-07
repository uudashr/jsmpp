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
 * @author uudashr
 * 
 */
public class SarTotalSegments {
    private byte value;

    public SarTotalSegments(byte value) {
        this.value = value;
    }

    public SarTotalSegments(short value) {
        this((byte)value);
    }

    public short getTag() {
        return SMPPConstant.TAG_SAR_TOTAl_SEGMENTS;
    }

    public short getLength() {
        return 2;
    }

    public short getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SarTotalSegments other = (SarTotalSegments)obj;
        if (value != other.value)
            return false;
        return true;
    }
    
    
}
