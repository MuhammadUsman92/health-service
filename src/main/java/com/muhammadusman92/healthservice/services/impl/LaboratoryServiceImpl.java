package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Laboratory;
import com.muhammadusman92.healthservice.exception.AlreadyExistExeption;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.payload.LaboratoryDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.repo.LaboratoryRepo;
import com.muhammadusman92.healthservice.repo.LocationRepo;
import com.muhammadusman92.healthservice.services.LaboratoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LaboratoryServiceImpl implements LaboratoryService {
    @Autowired
    private ConversionDtos conversionDtos;
    @Autowired
    private LaboratoryRepo laboratoryRepo;
    @Autowired
    private LocationRepo locationRepo;
    @Override
    public LaboratoryDto createLaboratory(LaboratoryDto laboratoryDto) {
        Laboratory laboratory = conversionDtos.laboratoryDtoToLaboratory(laboratoryDto);
        if(laboratoryRepo.existsById(laboratory.getReg_no())){
            throw new AlreadyExistExeption("Reg_no", laboratory.getReg_no());
        }
        laboratory.setLocation(locationRepo.save(laboratory.getLocation()));
        return conversionDtos.laboratoryToLaboratoryDto(laboratoryRepo.save(laboratory));
    }

    @Override
    public LaboratoryDto updateLaboratory(LaboratoryDto laboratoryDto,String laboratoryId) {
        Laboratory laboratory = conversionDtos.laboratoryDtoToLaboratory(laboratoryDto);
        Laboratory laboratoryFind = laboratoryRepo.findById(laboratoryId).orElseThrow(
                ()->new ResourceNotFoundException("Laboratory","LaboratoryId",laboratoryId));
        laboratory.setReg_no(laboratoryFind.getReg_no());
        laboratory.getLocation().setId(laboratoryFind.getLocation().getId());
        laboratory.setLocation(locationRepo.save(laboratory.getLocation()));
        return conversionDtos.laboratoryToLaboratoryDto(laboratoryRepo.save(laboratory));
    }

    @Override
    public LaboratoryDto getById(String laboratoryId) {
        Laboratory laboratoryFind = laboratoryRepo.findById(laboratoryId).orElseThrow(
                ()->new ResourceNotFoundException("Laboratory","LaboratoryId",laboratoryId));
        return conversionDtos.laboratoryToLaboratoryDto(laboratoryFind);
    }

    @Override
    public PageResponse<LaboratoryDto> getAllLaboratories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Page<Laboratory> laboratoryPage = laboratoryRepo.findAll(pageable);
        PageResponse<LaboratoryDto> laboratoryDtoPageResponse=new PageResponse<>();
        List<LaboratoryDto> laboratoryDtoList=laboratoryPage.getContent().stream().map(
                laboratory -> conversionDtos.laboratoryToLaboratoryDto(laboratory)).toList();
        laboratoryDtoPageResponse.setContent(laboratoryDtoList);
        laboratoryDtoPageResponse.setPageNumber(laboratoryPage.getNumber());
        laboratoryDtoPageResponse.setTotalPage(laboratoryPage.getTotalPages());
        laboratoryDtoPageResponse.setPageSize(laboratoryPage.getSize());
        laboratoryDtoPageResponse.setLast(laboratoryPage.isLast());
        laboratoryDtoPageResponse.setTotalElements(laboratoryPage.getTotalElements());
        return laboratoryDtoPageResponse;
    }

    @Override
    public void deleteLaboratory(String laboratoryId) {
        Laboratory laboratoryFind = laboratoryRepo.findById(laboratoryId).orElseThrow(
                ()->new ResourceNotFoundException("Laboratory","LaboratoryId",laboratoryId));
        laboratoryRepo.delete(laboratoryFind);
    }

}
