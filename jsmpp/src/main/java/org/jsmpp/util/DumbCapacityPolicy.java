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

/**
 * It's a dumb capacity policy. It calculate nothing, just return the new
 * capacity same as requeiredCapacity.
 * 
 * @author uudashr
 * 
 */
public class DumbCapacityPolicy implements CapacityPolicy {
    
    /* (non-Javadoc)
     * @see org.jsmpp.util.CapacityPolicy#ensureCapacity(int, int)
     */
    public int ensureCapacity(int requiredCapacity, int currentCapacity) {
        return requiredCapacity;
    }
}
