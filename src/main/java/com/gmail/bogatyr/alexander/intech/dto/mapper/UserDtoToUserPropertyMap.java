package com.gmail.bogatyr.alexander.intech.dto.mapper;

import com.gmail.bogatyr.alexander.intech.domain.User;
import com.gmail.bogatyr.alexander.intech.dto.UserDTO;
import org.modelmapper.PropertyMap;

/**
 * Created by Alexander Bogatyrenko on 24.08.16.
 * <p>
 * This class represents...
 */
public class UserDtoToUserPropertyMap extends PropertyMap<UserDTO, User> {

    @Override
    protected void configure() {
        skip(destination.getPassword());
        skip(destination.getAuthorities());
    }
}
