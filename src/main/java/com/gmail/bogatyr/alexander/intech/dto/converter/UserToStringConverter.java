package com.gmail.bogatyr.alexander.intech.dto.converter;

import com.gmail.bogatyr.alexander.intech.domain.User;
import org.modelmapper.AbstractConverter;

import static java.util.Objects.isNull;

/**
 * Created by Alexander Bogatyrenko on 24.08.16.
 * <p>
 * This class represents...
 */
public class UserToStringConverter extends AbstractConverter<User, String> {

    @Override
    protected String convert(User user) {
        return isNull(user) ? null : user.getLogin();
    }
}
