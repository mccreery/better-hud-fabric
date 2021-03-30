package mccreery.betterhud.internal.element.text;

import java.util.Date;

public class SystemClock extends Clock {
    @Override
    protected Date getDate() {
        return new Date();
    }
}
