package mccreery.betterhud.internal.element.text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jobicade.betterhud.element.settings.Legend;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.EnumProperty;

public abstract class Clock extends TextElement {
    private BooleanProperty twentyFour, showSeconds, fullYear;
    private EnumProperty dateType;

    public Clock(String name) {
        super(name);

        addSetting(new Legend("misc"));
        twentyFour = new BooleanProperty("24hr");
        addSetting(twentyFour);
        showSeconds = new BooleanProperty("showSeconds");
        addSetting(showSeconds);
        showSeconds.setValuePrefix(BooleanProperty.VISIBLE);

        dateType = new EnumProperty("dateType", "dmy", "mdy", "ymd");
        addSetting(dateType);
        fullYear = new BooleanProperty("fullYear");
        addSetting(fullYear);
        fullYear.setValuePrefix(BooleanProperty.VISIBLE);

        border = true;
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
}
