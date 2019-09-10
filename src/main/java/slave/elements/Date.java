package slave.elements;

import slave.exception.DukeException;
import slave.exception.InvalidDateException;

/**
 * Date object that represents a DD/MM/YYYY HHMM format.
 */
public class Date {

    private String dateFormat;
    private String time;
    private String timeFormat;

    private int day;
    private int month;
    private int year;

    private static String[] timeSuffixes = {"am", "am", "am", "am", "am", "am", "am", "am",
        "am", "am", "am", "am", "pm", "pm", "pm", "pm",
        "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm"};

    private static String[] timeHourSuffixes = {"12", "1", "2", "3", "4", "5", "6", "7",
        "8", "9", "10", "11", "12", "1", "2", "3",
        "4", "5", "6", "7", "8", "9", "10", "11"};

    private static String[] daySuffixes = { "th", "st", "nd", "rd", "th", "th", "th", "th",
        "th", "th", "th", "th", "th", "th", "th", "th",
        "th", "th", "th", "th", "th", "st", "nd", "rd",
        "th", "th", "th", "th", "th", "th", "th", "st" };

    private static String[] monthStrings = { "Month", "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December" };

    /**
     * Constructor to build a Date object.
     *
     * @param dateFormat Takes in date in the form DD/MM/YYYY.
     * @param time Takes in time in the form HHMM.
     * @throws DukeException If date / time given is invalid and cannot be represented properly.
     */
    public Date(String dateFormat, String time) throws DukeException {
        this.dateFormat = dateFormat;
        this.time = time;
        assert time.length() == 4 : "Invalid Time Format";
        assert dateFormat.length() > 5 : "Invalid Date Format";
        format();
    }

    /**
     * Formats the date.
     *
     * @throws DukeException Throws if the date / time given is invalid.
     */
    private void format() throws DukeException {
        try {
            formatDayMonthYear();
            formatHourMinute();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidDateException();
        }
    }

    private void formatHourMinute() {
        String hour = time.substring(0, 2);
        String minute = time.substring(2);

        int indexHour = Integer.parseInt(hour);
        assert indexHour <= 23 : "Invalid index for Hour and Time";

        this.timeFormat = timeHourSuffixes[indexHour] + "." + minute + timeSuffixes[indexHour];
    }

    private void formatDayMonthYear() {
        String[] splitDateTimeTokens = this.dateFormat.split("/");

        this.day = Integer.parseInt(splitDateTimeTokens[0]);
        this.month = Integer.parseInt(splitDateTimeTokens[1]);
        this.year = Integer.parseInt(splitDateTimeTokens[2]);

        assert this.month <= 12 : "Invalid Month";
        assert this.day <= 31 : "Invalid Day";
    }

    /**
     * Converts "DD/MM/YYYY HHMM" to the form "Day of Month Year, 12H-Time".
     *
     * @return String in the appropriate format for date.
     * @throws DukeException Throws if date is invalid.
     */
    public String convertToString() throws DukeException {
        String result;
        try {
            result = this.day + daySuffixes[this.day]
                    + " of " + monthStrings[this.month]
                    + " " + this.year + ", " + timeFormat;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidDateException();
        }
        return result;
    }
}
