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
package org.jsmpp.examples.receipts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsmpp.bean.DeliveryReceiptInterface;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pmoerenhout
 */
public class CustomDeliveryReceipt implements DeliveryReceiptInterface<CustomDeliveryReceiptState> {
  private static final Logger LOG = LoggerFactory.getLogger(CustomDeliveryReceipt.class);

  private static final Pattern PATTERN = Pattern.compile(
      "^id:([0-9]*) sub:([0-9]{3}) dlvrd:([0-9]{3}) submit date:([0-9]{10}) done date:([0-9]{10}) err:([0-9]{3}) stat:([a-zA-Z_]*) text:(.*)$");

  // attributes of the custom delivery receipt
  private static final String DELREC_ID = "id";
  private static final String DELREC_SUB = "sub";
  private static final String DELREC_DLVRD = "dlvrd";
  private static final String DELREC_SUBMIT_DATE = "submit date";
  private static final String DELREC_DONE_DATE = "done date";
  private static final String DELREC_ERR = "err";
  private static final String DELREC_STAT = "stat";
  private static final String DELREC_TEXT = "text";

  /**
   * Date format for the <b>submit date</b> and <b>done date</b> attribute
   */
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");

  private String id;
  private Integer submitted;
  private Integer delivered;
  private Date submitDate;
  private Date doneDate;
  private CustomDeliveryReceiptState finalStatus;
  private String error;
  private String text;

  public CustomDeliveryReceipt() {
  }

  public CustomDeliveryReceipt(String formattedDeliveryReceipt)
      throws InvalidDeliveryReceiptException {
        /*
         * id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done
         * date:YYMMDDhhmm stat:DDDDDDD err:E text: ..........
         */
    try {
      Matcher matcher = PATTERN.matcher(formattedDeliveryReceipt);
      if (matcher.find()) {
        id = matcher.group(1);
        submitted = Integer.parseInt(matcher.group(2));
        delivered = Integer.parseInt(matcher.group(3));
        submitDate = string2Date(matcher.group(4));
        doneDate = string2Date(matcher.group(5));
        error = matcher.group(6);
        finalStatus = CustomDeliveryReceiptState.valueOf(matcher.group(7));
        text = matcher.group(8);
      }
    } catch (Exception e) {
      LOG.error("Parse error", e);
      throw new InvalidDeliveryReceiptException("There is an error found when parsing custom delivery receipt", e);
    }
  }

  public CustomDeliveryReceipt(String id, int submitted, int delivered,
                               Date submitDate, Date doneDate, CustomDeliveryReceiptState finalStatus,
                               String error, String text) {
    this.id = id;
    this.submitted = submitted;
    this.delivered = delivered;
    this.submitDate = submitDate;
    this.doneDate = doneDate;
    this.finalStatus = finalStatus;
    this.error = error;
    if (text.length() > 20) {
      this.text = text.substring(0, 20);
    } else {
      this.text = text;
    }
  }

  /**
   * Create String representation of integer. Preceding 0 will be add as needed.
   *
   * @param value is the value.
   * @param digit is the digit should be shown.
   * @return the String representation of int value.
   */

  private static String intToString(int value, int digit) {
    StringBuilder stringBuilder = new StringBuilder(digit);
    stringBuilder.append(Integer.toString(value));
    while (stringBuilder.length() < digit) {
      stringBuilder.insert(0, "0");
    }
    return stringBuilder.toString();
  }

  /**
   * YYMMDDhhmm where: <ul> <li>YY = last two digits of the year (00-99)</li> <li>MM = month (01-12)</li> <li>DD = day (01-31)</li> <li>hh = hour (00-23)</li>
   * <li>mm = minute (00-59)</li> </ul>
   *
   * Java format is (yyMMddHHmm).
   *
   * @param date in <tt>String</tt> format.
   * @throws NumberFormatException     if there is contains non number on <code>date</code> parameter.
   * @throws IndexOutOfBoundsException if the date length in <tt>String</tt> format is less than 10.
   */
  private static Date string2Date(String date) {
    int year = Integer.parseInt(date.substring(0, 2));
    int month = Integer.parseInt(date.substring(2, 4));
    int day = Integer.parseInt(date.substring(4, 6));
    int hour = Integer.parseInt(date.substring(6, 8));
    int minute = Integer.parseInt(date.substring(8, 10));
    int second = 0;
    if (date.length() >= 12) {
      second = Integer.parseInt(date.substring(10, 12));
    }
    Calendar cal = Calendar.getInstance();
    cal.set(convertTwoDigitYear(year), month - 1, day, hour, minute, second);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  private static int convertTwoDigitYear(int year) {
    if (year >= 0 && year <= 37) {
      return 2000 + year;
    } else if (year >= 38 && year <= 99) {
      return 1900 + year;
    } else {
      // should never happen
      return year;
    }
  }

  /**
   * @return Returns the delivered.
   */
  public int getDelivered() {
    return delivered;
  }

  /**
   * @param delivered The delivered to set.
   */
  public void setDelivered(int delivered) {
    this.delivered = delivered;
  }

  /**
   * @return Returns the doneDate.
   */
  public Date getDoneDate() {
    return doneDate;
  }

  /**
   * @param doneDate The doneDate to set.
   */
  public void setDoneDate(Date doneDate) {
    this.doneDate = doneDate;
  }

  /**
   * @return Returns the error.
   */
  public String getError() {
    return error;
  }

  /**
   * @param error The error to set.
   */
  public void setError(String error) {
    this.error = error;
  }

  /**
   * @return Returns the finalStatus.
   */
  public CustomDeliveryReceiptState getFinalStatus() {
    return finalStatus;
  }

  /**
   * @param finalStatus The customFinalStatus to set.
   */
  public void setFinalStatus(CustomDeliveryReceiptState finalStatus) {
    this.finalStatus = finalStatus;
  }


  /**
   * @return Returns the id.
   */
  public String getId() {
    return id;
  }

  /**
   * @param id The id to set.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return Returns the submitDate.
   */
  public Date getSubmitDate() {
    return submitDate;
  }

  /**
   * @param submitDate The submitDate to set.
   */
  public void setSubmitDate(Date submitDate) {
    this.submitDate = submitDate;
  }

  /**
   * @return Returns the submitted.
   */
  public int getSubmitted() {
    return submitted;
  }

  /**
   * @param submitted The submitted to set.
   */
  public void setSubmitted(int submitted) {
    this.submitted = submitted;
  }

  /**
   * @return Returns the text.
   */
  public String getText() {
    return text;
  }

  /**
   * Set the text of delivery receipt. Text more than 20 characters will be trim automatically.
   *
   * @param text the text to set.
   */
  public void setText(String text) {
    if (text != null && text.length() > 20) {
      this.text = text.substring(0, 20);
    } else {
      this.text = text;
    }
  }

  @Override
  public String toString() {
        /*
         * id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done
         * date:YYMMDDhhmm stat:DDDDDDD err:E Text: . . . . . . . . .
         */
    StringBuilder stringBuilder = new StringBuilder(120);
    stringBuilder.append(DELREC_ID + ":" + id)
        .append(" ")
        .append(DELREC_SUB + ":" + intToString(submitted, 3))
        .append(" ")
        .append(DELREC_DLVRD + ":" + intToString(delivered, 3))
        .append(" ")
        .append(DELREC_SUBMIT_DATE + ":" + dateFormat.format(submitDate))
        .append(" ")
        .append(DELREC_DONE_DATE + ":" + dateFormat.format(doneDate))
        .append(" ")
        .append(DELREC_STAT + ":" + finalStatus)
        .append(" ")
        .append(DELREC_ERR + ":" + error)
        .append(" ")
        .append(DELREC_TEXT + ":" + text);
    return stringBuilder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((dateFormat == null) ? 0 : dateFormat.hashCode());
    result = prime * result + delivered;
    result = prime * result
        + ((doneDate == null) ? 0 : doneDate.hashCode());
    result = prime * result + ((error == null) ? 0 : error.hashCode());
    result = prime * result
        + ((finalStatus == null) ? 0 : finalStatus.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result
        + ((submitDate == null) ? 0 : submitDate.hashCode());
    result = prime * result + submitted;
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CustomDeliveryReceipt other = (CustomDeliveryReceipt) obj;
    if (!hasEqualId(other)) {
      return false;
    }
    if (submitted != other.submitted) {
      return false;
    }
    if (delivered != other.delivered) {
      return false;
    }
    if (!hasEqualSubmitDate(other)) {
      return false;
    }
    if (!hasEqualDoneDate(other)) {
      return false;
    }
    if (!hasEqualFinalStatus(other)) {
      return false;
    }
    if (!hasEqualError(other)) {
      return false;
    }
    if (!hasEqualText(other)) {
      return false;
    }
    return true;
  }

  private boolean hasEqualId(CustomDeliveryReceipt other) {
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    }
    return id.equals(other.id);
  }

  private boolean hasEqualDoneDate(CustomDeliveryReceipt other) {
    if (doneDate == null) {
      if (other.doneDate != null) {
        return false;
      }
    }
    return doneDate.equals(other.doneDate);
  }

  private boolean hasEqualError(CustomDeliveryReceipt other) {
    if (error == null) {
      if (other.error != null) {
        return false;
      }
    }
    return error.equals(other.error);
  }

  private boolean hasEqualFinalStatus(CustomDeliveryReceipt other) {
    if (finalStatus == null) {
      if (other.finalStatus != null) {
        return false;
      }
    }
    return finalStatus.equals(other.finalStatus);
  }

  private boolean hasEqualSubmitDate(CustomDeliveryReceipt other) {
    if (submitDate == null) {
      if (other.submitDate != null) {
        return false;
      }
    }
    return submitDate.equals(other.submitDate);
  }

  private boolean hasEqualText(CustomDeliveryReceipt other) {
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    }
    return text.equals(other.text);
  }
}
