package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.EnumProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class Clock extends HudElement {
    private final BooleanProperty twentyFour;
    private final BooleanProperty showSeconds;
    private final BooleanProperty fullYear;
    private final EnumProperty<DateType> dateType;

    public Clock() {
        twentyFour = new BooleanProperty("24hr", true);
        addProperty(twentyFour);
        showSeconds = new BooleanProperty("showSeconds", false);
        addProperty(showSeconds);

        dateType = new EnumProperty<>("dateType", DateType.MONTH_DAY_YEAR);
        addProperty(dateType);
        fullYear = new BooleanProperty("fullYear", true);
        addProperty(fullYear);
    }

    protected DateFormat getTimeFormat() {
        StringBuilder format = new StringBuilder();
        format.append("HH:mm");

        if(showSeconds.get()) {
            format.append(":ss");
        }
        if(!twentyFour.get()) {
            format.append(" a");
            format.replace(0, 2, "hh");
        }
        return new SimpleDateFormat(format.toString());
    }

    private static final String[] dateFormats = {"dd/MM/yy", "MM/dd/yy", "yy/MM/dd"};
    private static final String[] dateFormatsFull = {"dd/MM/yyyy", "MM/dd/yyyy", "yyyy/MM/dd"};

    protected DateFormat getDateFormat() {
        return new SimpleDateFormat((fullYear.get() ? dateFormatsFull : dateFormats)[dateType.getIndex()]);
    }

    @Override
    protected List<String> getText() {
        Date date = getDate();

        return Arrays.asList(
            getTimeFormat().format(date),
            getDateFormat().format(date)
        );
    }

    protected abstract Date getDate();

    public enum DateType {
        DAY_MONTH_YEAR,
        MONTH_DAY_YEAR,
        YEAR_MONTH_DAY
    }
}
