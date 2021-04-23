package com.ntnu.gidd.config;

import com.ntnu.gidd.dto.Activity.ActivityDto;
import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.model.Registration;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.createTypeMap(Registration.class, ActivityListDto.class)
                .addMapping(src ->src.getActivity().getId(), ActivityListDto::setId)
                .addMapping(src -> src.getActivity().getTrainingLevel().getLevel(), ActivityListDto::setLevel);
        modelMapper.createTypeMap(Registration.class, ActivityDto.class)
                .addMapping(src ->src.getActivity().getId(), ActivityDto::setId)
                .addMapping(src -> src.getActivity().getTrainingLevel().getLevel(), ActivityDto::setLevel);

        return modelMapper;
    }
}