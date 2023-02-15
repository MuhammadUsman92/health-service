package com.muhammadusman92.healthservice;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Disease;
import com.muhammadusman92.healthservice.payload.DiseaseDto;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HealthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthServiceApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.typeMap(Disease.class, DiseaseDto.class).addMappings(mapper->{
//			mapper.skip(DiseaseDto::setPrescriptionSet);
//		});
		return modelMapper;
	}
	@Bean
	public ConversionDtos conversionDtos(){
		return new ConversionDtos();
	}

}
