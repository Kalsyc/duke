package slave.elements;

import slave.exception.DukeException;
import slave.exception.InvalidDateException;

import java.time.LocalDateTime;

public class DateTime {

    private LocalDateTime localDateTime;

    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    private static String[] daySuffixes = { "th", "st", "nd", "rd", "th", "th", "th", "th",
            "th", "th", "th", "th", "th", "th", "th", "th",
            "th", "th", "th", "th", "th", "st", "nd", "rd",
            "th", "th", "th", "th", "th", "th", "th", "st" };

    private static String[] hourSuffixes = {"12", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "1", "2", "3",
            "4", "5", "6", "7", "8", "9", "10", "11"};

    private static String[] monthStrings = { "Month", "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };

    private static String[] timeSuffixes = {"am", "am", "am", "am", "am", "am", "am", "am",
            "am", "am", "am", "am", "pm", "pm", "pm", "pm",
            "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm"};

    public DateTime(String dateFormat, String time) throws InvalidDateException {
        assert time.length() == 4 : "Invalid Time Format";
        assert dateFormat.length() > 5 : "Invalid Date Format";
        try {
            this.formatHourMinute(time);
            this.formatDayMonthYear(dateFormat);
        } catch (AssertionError error) {
            throw new InvalidDateException();
        }
        this.localDateTime = LocalDateTime.of(this.year, this.month, this.day, this.hour, this.minute);
    }

    private void formatHourMinute(String time) {
        String hour = time.substring(0, 2);
        String minute = time.substring(2);
        this.hour = Integer.parseInt(hour);
        this.minute = Integer.parseInt(minute);
        assert this.hour <= 24 : "Invalid hour";
        assert this.minute <= 59 : "Invalid minute";
    }

    private void formatDayMonthYear(String dateFormat) {
        String[] splitDateTimeTokens = dateFormat.split("/");

        this.day = Integer.parseInt(splitDateTimeTokens[0]);
        this.month = Integer.parseInt(splitDateTimeTokens[1]);
        this.year = Integer.parseInt(splitDateTimeTokens[2]);

        assert this.month <= 12 : "Invalid Month";
        assert this.day <= 31 : "Invalid Day";
    }

    public String convertToString() throws DukeException {
        String result;
        try {
            result = this.localDateTime.getDayOfMonth() + daySuffixes[this.localDateTime.getDayOfMonth()]
                    + " of " + monthStrings[this.localDateTime.getMonthValue()]
                    + " " + this.localDateTime.getYear() + ", "
                    + hourSuffixes[this.localDateTime.getHour()] + "."
                    + this.localDateTime.getMinute() + timeSuffixes[this.localDateTime.getHour()];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidDateException();
        }
        return result;
    }
}
