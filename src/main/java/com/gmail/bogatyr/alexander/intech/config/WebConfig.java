package com.gmail.bogatyr.alexander.intech.config;

import com.gmail.bogatyr.alexander.intech.dto.converter.UserToStringConverter;
import com.gmail.bogatyr.alexander.intech.dto.converter.ZonedDateTimeToStringConverter;
import com.gmail.bogatyr.alexander.intech.dto.mapper.UserDtoToUserPropertyMap;
import com.gmail.bogatyr.alexander.intech.dto.mapper.UserToUserDtoPropertyMap;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

/**
 * Created by Alexander Bogatyrenko on 16.08.16.
 * <p>
 * This class represents...
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(new UserToStringConverter());
        mapper.addConverter(new ZonedDateTimeToStringConverter());
        mapper.addMappings(new UserDtoToUserPropertyMap());
        mapper.addMappings(new UserToUserDtoPropertyMap());
        return mapper;
    }
}