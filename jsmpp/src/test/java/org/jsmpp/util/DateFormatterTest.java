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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.testng.Assert.*;

import org.testng.annotations.Test;


/**
 * @author uudashr
 *
 */
public class DateFormatterTest {
    
    @Test(groups="checkintest")
    public void testStaticAbsoluteFormatter() {
        String formatted = AbsoluteTimeFormatter.format(07, 12, 26, 11, 37, 03, 8, 45, '+');
        assertEquals(formatted, "071226113703845+");
    }
    
    @Test(groups="checkintest")
    public void testStaticRelativeFormatter() {
        String formatted = RelativeTimeFormatter.format(07, 12, 26, 12, 46, 10);
        assertEquals(formatted, "071226124610000R");
    }
    
    @Test(groups="checkintest")
    public void formatNullDate() {
        TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
        assertNull(timeFormatter.format((Date)null));
        assertNull(timeFormatter.format((Calendar)null));
        
        timeFormatter = new RelativeTimeFormatter();
        assertNull(timeFormatter.format((Date)null));
        assertNull(timeFormatter.format((Calendar)null));
    }
    
    @Test(groups="checkintest")
    public void validateAbsoluteDate() throws Exception {
        String formatted = AbsoluteTimeFormatter.format(07, 12, 26, 11, 37, 03, 8, 45, '+');
        StringValidator.validateString(formatted, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(formatted, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(formatted, StringParameter.FINAL_DATE);
    }
    
    @Test(groups="checkintest")
    public void validateRelativeDate() throws Exception {
        String formatted = RelativeTimeFormatter.format(07, 12, 26, 12, 46, 10);
        StringValidator.validateString(formatted, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(formatted, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(formatted, StringParameter.FINAL_DATE);
    }

    @Test(groups="checkintest")
    public void formatAbsoluteDate() {
        TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

        GregorianCalendar date = new GregorianCalendar(Locale.GERMANY);
        date.set(Calendar.YEAR, 2013);
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR, 1);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        assertEquals(timeFormatter.format(date), "130101130000004+");

        date.set(Calendar.MONTH, Calendar.JULY);

        // because of daylight saving time, we have a different offset
        assertEquals(timeFormatter.format(date), "130701130000008+");
    }

    @Test(groups="checkintest")
    public void formatRelativeDate() {
        RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter(TimeZone.getTimeZone("America/Denver"));

        // at this date neither Denver nor Germany has daylight saving time
        GregorianCalendar date = new GregorianCalendar(Locale.GERMANY);
        date.set(Calendar.YEAR, 2013);
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR, 1);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        assertEquals(timeFormatter.format(date), "130101050000000R");
        
        // at this date Denver has already daylight saving time but not Germany
        date.set(Calendar.MONTH, Calendar.MARCH);
        date.set(Calendar.DAY_OF_MONTH, 20);
        
        assertEquals(timeFormatter.format(date), "130320060000000R");

        // at this date Denver and Germany has daylight saving time
        date.set(Calendar.MONTH, Calendar.APRIL);
        date.set(Calendar.DAY_OF_MONTH, 1);

        assertEquals(timeFormatter.format(date), "130401050000000R");
    }
}
