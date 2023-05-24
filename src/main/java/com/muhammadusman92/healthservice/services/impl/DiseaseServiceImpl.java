package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Disease;
import com.muhammadusman92.healthservice.entity.Patient;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.payload.DiseaseDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.PrescriptionDto;
import com.muhammadusman92.healthservice.repo.DiseaseRepo;
import com.muhammadusman92.healthservice.repo.PatientRepo;
import com.muhammadusman92.healthservice.services.DiseaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class DiseaseServiceImpl implements DiseaseService {
    @Autowired
    private DiseaseRepo diseaseRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private ConversionDtos conversionDtos;
    @Override
    public DiseaseDto createDisease(String patientId,DiseaseDto diseaseDto) {
        Disease disease = conversionDtos.diseaseDtoToDisease(diseaseDto);
        disease.setPatient(patientRepo.findById(patientId).orElseThrow(
                ()->new ResourceNotFoundException("Patient","PatientId",patientId)));
        Disease save = diseaseRepo.save(disease);
        DiseaseDto diseaseDto1 = conversionDtos.diseaseToDiseaseDto(save);
        Set<PrescriptionDto> prescriptionDtos = save.getPrescriptionSet().stream()
                .map(prescription -> conversionDtos.prescriptionToPrescriptionDto(prescription)).collect(Collectors.toSet());
        diseaseDto1.setPrescriptionDtoSet(prescriptionDtos);
        return diseaseDto1;
    }

    @Override
    public DiseaseDto updateDisease(DiseaseDto diseaseDto, Integer diseaseId) {
        Disease disease = conversionDtos.diseaseDtoToDisease(diseaseDto);
        Disease diseaseUpdate = diseaseRepo.findById(diseaseId).orElseThrow(
                ()->new ResourceNotFoundException("Disease","DiseaseId",diseaseId));
        disease.setId(diseaseUpdate.getId());
        Disease save = diseaseRepo.save(disease);
        DiseaseDto diseaseDto1 = conversionDtos.diseaseToDiseaseDto(save);
        Set<PrescriptionDto> prescriptionDtos = save.getPrescriptionSet().stream()
                .map(prescription -> conversionDtos.prescriptionToPrescriptionDto(prescription)).collect(Collectors.toSet());
        diseaseDto1.setPrescriptionDtoSet(prescriptionDtos);
        return diseaseDto1;
    }

    @Override
    public DiseaseDto getById(Integer diseaseId) {
        Disease diseaseFind = diseaseRepo.findById(diseaseId).orElseThrow(
                ()->new ResourceNotFoundException("Disease","DiseaseId",diseaseId));
        DiseaseDto diseaseDto = conversionDtos.diseaseToDiseaseDto(diseaseFind);
        Set<PrescriptionDto> prescriptionDtos = diseaseFind.getPrescriptionSet().stream()
                .map(prescription -> conversionDtos.prescriptionToPrescriptionDto(prescription)).collect(Collectors.toSet());
        diseaseDto.setPrescriptionDtoSet(prescriptionDtos);
        return diseaseDto;
    }

    @Override
    public PageResponse<DiseaseDto> getAllDiseases(String patientId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient", "PatientId", patientId));
        Page<Disease> postPage = diseaseRepo.findByPatient(patient,pageable);
        List<Disease> diseaseListByPatient = postPage.getContent();
        List<DiseaseDto> diseaseDtoList=diseaseListByPatient.stream().map(
                (disease -> conversionDtos.diseaseToDiseaseDto(disease))).collect(Collectors.toList());
        PageResponse<DiseaseDto> pageResponse=new PageResponse<>();
        pageResponse.setPageNumber(postPage.getNumber());
        pageResponse.setPageSize(postPage.getSize());
        pageResponse.setTotalPage(postPage.getTotalPages());
        pageResponse.setLast(postPage.isLast());
        pageResponse.setTotalElements(postPage.getTotalElements());
        pageResponse.setContent(diseaseDtoList);
        return pageResponse;

    }

    @Override
    public void deleteDisease(Integer diseaseId) {
        Disease disease = diseaseRepo.findById(diseaseId).orElseThrow(
                () -> new ResourceNotFoundException("Disease", "DiseaseId", diseaseId));
        diseaseRepo.delete(disease);
    }

}
