package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.EnumProperty;
import net.minecraft.text.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// TODO background shading
public abstract class Clock extends TextElement {
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

    protected DateFormat getDateFormat() {
        if (fullYear.get()) {
            return dateType.get().getLongFormat();
        } else {
            return dateType.get().getShortFormat();
        }
    }

    @Override
    protected List<Text> getText(HudRenderContext context) {
        Date date = getDate();

        return Arrays.asList(
            Text.of(getTimeFormat().format(date)),
            Text.of(getDateFormat().format(date))
        );
    }

    protected abstract Date getDate();

    public enum DateType {
        DAY_MONTH_YEAR("dd/MM/yy", "dd/MM/yyyy"),
        MONTH_DAY_YEAR("MM/dd/yy", "MM/dd/yyyy"),
        YEAR_MONTH_DAY("yy/MM/dd", "yyyy/MM/dd");

        private final DateFormat shortFormat;
        private final DateFormat longFormat;

        DateType(String shortFormat, String longFormat) {
            this.shortFormat = new SimpleDateFormat(shortFormat);
            this.longFormat = new SimpleDateFormat(longFormat);
        }

        public DateFormat getShortFormat() {
            return shortFormat;
        }

        public DateFormat getLongFormat() {
            return longFormat;
        }
    }
}
