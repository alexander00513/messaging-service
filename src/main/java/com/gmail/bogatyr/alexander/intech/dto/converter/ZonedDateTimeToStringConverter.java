package com.gmail.bogatyr.alexander.intech.dto.converter;

import org.modelmapper.AbstractConverter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Alexander Bogatyrenko on 25.08.16.
 * <p>
 * This class represents...
 */
public class ZonedDateTimeToStringConverter extends AbstractConverter<ZonedDateTime, String> {

    @Override
    protected String convert(ZonedDateTime source) {
        return source.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm:ss"));
    }
}
