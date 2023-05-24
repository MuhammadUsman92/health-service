package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Hospital;
import com.muhammadusman92.healthservice.entity.Location;
import com.muhammadusman92.healthservice.exception.AlreadyExistExeption;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.payload.HospitalDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.repo.HospitalRepo;
import com.muhammadusman92.healthservice.repo.LocationRepo;
import com.muhammadusman92.healthservice.services.HospitalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private ConversionDtos conversionDtos;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private LocationRepo locationRepo;
    @Override
    public HospitalDto createHospital(HospitalDto hospitalDto) {
        Hospital hospital = conversionDtos.hospitalDtoToHospital(hospitalDto);
        if(hospitalRepo.existsById(hospital.getReg_no())){
            throw new AlreadyExistExeption("Reg_no", hospital.getReg_no());
        }
        hospital.setLocation(locationRepo.save(hospital.getLocation()));
        return conversionDtos.hospitalToHospitalDto(hospitalRepo.save(hospital));
    }

    @Override
    public HospitalDto updateHospital(HospitalDto hospitalDto, String hospitalId) {
        Hospital hospital = conversionDtos.hospitalDtoToHospital(hospitalDto);
        Hospital hospitalFind = hospitalRepo.findById(hospitalId).orElseThrow(
                ()->new ResourceNotFoundException("Hospital","HospitalId",hospitalId));
        hospital.getLocation().setId(hospitalFind.getLocation().getId());
        hospital.setLocation(locationRepo.save(hospital.getLocation()));
        return conversionDtos.hospitalToHospitalDto(hospitalRepo.save(hospital));
    }

    @Override
    public HospitalDto getById(String hospitalId) {
        Hospital hospitalFind = hospitalRepo.findById(hospitalId).orElseThrow(
                ()->new ResourceNotFoundException("Hospital","HospitalId",hospitalId));
        return conversionDtos.hospitalToHospitalDto(hospitalFind);
    }

    @Override
    public PageResponse<HospitalDto> getAllHospitals(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Hospital> hospitalPage = hospitalRepo.findAll(pageable);
        PageResponse<HospitalDto> hospitalDtoPageResponse=new PageResponse<>();
        List<HospitalDto> hospitalDtoList = hospitalPage.getContent().stream().map(
                hospital -> conversionDtos.hospitalToHospitalDto(hospital)).toList();
        hospitalDtoPageResponse.setContent(hospitalDtoList);
        hospitalDtoPageResponse.setPageSize(hospitalPage.getSize());
        hospitalDtoPageResponse.setTotalPage(hospitalPage.getTotalPages());
        hospitalDtoPageResponse.setPageNumber(hospitalPage.getNumber());
        hospitalDtoPageResponse.setLast(hospitalPage.isLast());
        hospitalDtoPageResponse.setTotalElements(hospitalPage.getTotalElements());
        return hospitalDtoPageResponse;
    }

    @Override
    public void deleteHospital(String hospitalId) {
        Hospital hospitalFind = hospitalRepo.findById(hospitalId).orElseThrow(
                ()->new ResourceNotFoundException("Hospital","HospitalId",hospitalId));
        hospitalRepo.delete(hospitalFind);
    }

}
