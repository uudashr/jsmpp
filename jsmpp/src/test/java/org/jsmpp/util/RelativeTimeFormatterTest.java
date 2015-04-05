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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class RelativeTimeFormatterTest {

  @Test(groups = "checkintest")
  public void testStaticRelativeFormatter() {
    String formatted = RelativeTimeFormatter.format(07, 12, 26, 12, 46, 10);
    assertEquals(formatted, "071226124610000R");
  }

  @Test(groups = "checkintest")
  public void formatNullDate() {
    TimeFormatter timeFormatter = new RelativeTimeFormatter();
    assertNull(timeFormatter.format((Date) null));
    assertNull(timeFormatter.format((Calendar) null));
  }

  @Test(groups = "checkintest")
  public void validateRelativeDate() throws Exception {
    String formatted = RelativeTimeFormatter.format(07, 12, 26, 12, 46, 10);
    StringValidator.validateString(formatted, StringParameter.SCHEDULE_DELIVERY_TIME);
    StringValidator.validateString(formatted, StringParameter.VALIDITY_PERIOD);
    StringValidator.validateString(formatted, StringParameter.FINAL_DATE);
  }

  @Test(groups = "checkintest", expectedExceptions = IllegalArgumentException.class)
  public void formatRelativeDateWhenAlreadyPast() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();

    // date in the past
    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"));
    date.set(1998, Calendar.MARCH, 1, 13, 46, 59);

    timeFormatter.format(date);
    fail("Expected IllegalArgumentException not thrown");
  }

  @Test(groups = "checkintest")
  public void formatRelativeDateIgnoreMilliSeconds() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();

    // Set the SMSC date to some future datetime
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"));
    smscDate.set(Calendar.YEAR, 2080);
    smscDate.set(Calendar.MONTH, Calendar.JANUARY);
    smscDate.set(Calendar.DAY_OF_MONTH, 1);
    smscDate.set(Calendar.HOUR_OF_DAY, 13);
    smscDate.set(Calendar.MINUTE, 46);
    smscDate.set(Calendar.SECOND, 59);
    smscDate.set(Calendar.MILLISECOND, 0);

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    // Tenth of seconds should be ignored
    date.set(Calendar.MILLISECOND, 800);

    assertEquals(timeFormatter.format(date, smscDate), "000000000000000R");
  }

  @Test(groups = "checkintest", expectedExceptions = IllegalArgumentException.class)
  public void formatRelativeDateWhenExceedsCentury() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();

    // relative date more then 100 years ahead
    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    date.add(Calendar.YEAR, 101);

    String relativeTime = timeFormatter.format(date);
    fail("Expected IllegalArgumentException not thrown");
  }

  @Test(groups = "checkintest")
  public void formatRelativeDateSame() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    assertEquals(timeFormatter.format(date, date), "000000000000000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeDateDifferentTimeZone() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    smscDate.set(2014, Calendar.JANUARY, 2, 23, 15, 16);
    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles"));
    date.set(2014, Calendar.JANUARY, 2, 23, 15, 16);
    assertEquals(timeFormatter.format(date, smscDate), "000000010000000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeDateMonthFebruary() {
    // for Java 8, the relative time could be calculated better
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("Pacific/Midway"));
    smscDate.set(2015, Calendar.FEBRUARY, 1, 0, 11, 22);

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("Pacific/Midway"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    date.add(Calendar.MONTH, 1);
    assertEquals(timeFormatter.format(date, smscDate), "000028000000000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeDateMonthMay() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    smscDate.set(2015, Calendar.MAY, 1, 13, 14, 15);

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    date.add(Calendar.MONTH, 1);
    assertEquals(timeFormatter.format(date, smscDate), "000100000000000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeDateWeek() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    date.add(Calendar.DAY_OF_MONTH, 7);
    assertEquals(timeFormatter.format(date, smscDate), "000007000000000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeTimeSecond() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    date.add(Calendar.SECOND, 1);
    assertEquals(timeFormatter.format(date, smscDate), "000000000001000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeTimeHours() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    date.add(Calendar.HOUR_OF_DAY, 16);
    assertEquals(timeFormatter.format(date, smscDate), "000000160000000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeTimeMonth() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    smscDate.set(2001, Calendar.JANUARY, 31, 14, 15, 16);

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    date.add(Calendar.MONTH, 1);
    // when using Joda-Time or Java 8 Period class
    //assertEquals(timeFormatter.format(date), "000100000000000R");
    assertEquals(timeFormatter.format(date, smscDate), "000028000000000R");
  }

  @Test(groups = "checkintest")
  public void formatRelativeDateNewYear() {
    RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();
    GregorianCalendar smscDate = new GregorianCalendar(TimeZone.getTimeZone("America/Denver"));
    smscDate.set(2001, Calendar.DECEMBER, 31, 23, 59, 59);

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"));
    date.setTimeInMillis(smscDate.getTimeInMillis());
    date.add(Calendar.SECOND, 2);
    assertEquals(timeFormatter.format(date, smscDate), "000000000002000R");
  }

}
