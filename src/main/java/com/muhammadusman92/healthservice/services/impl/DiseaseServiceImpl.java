package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.*;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.exception.UnAuthorizedException;
import com.muhammadusman92.healthservice.payload.DiseaseDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.PrescriptionDto;
import com.muhammadusman92.healthservice.repo.DiseaseRepo;
import com.muhammadusman92.healthservice.repo.HospitalRepo;
import com.muhammadusman92.healthservice.repo.HospitalUserRepo;
import com.muhammadusman92.healthservice.repo.PatientRepo;
import com.muhammadusman92.healthservice.services.DiseaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
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
    @Autowired
    private HospitalUserRepo hospitalUserRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Transactional
    @Override
    public DiseaseDto createDisease(String userEmail,String patientId,DiseaseDto diseaseDto) {
        Disease disease = conversionDtos.diseaseDtoToDisease(diseaseDto);
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient", "PatientId", patientId));
        if(!patient.getEmail().equalsIgnoreCase(userEmail)){
            HospitalUser hospitalUser = hospitalUserRepo.findByEmail(userEmail).orElseThrow(UnAuthorizedException::new);
            Hospital hospital = hospitalUser.getHospital();
            patient.getHospitalSet().add(hospital);
            hospital.getPatients().add(patient);
            hospitalRepo.save(hospital);
        }
        disease.setPatient(patient);
        Disease save = diseaseRepo.save(disease);
        patientRepo.save(patient);
        return conversionDtos.diseaseToDiseaseDto(save);
    }

    @Transactional
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
    public DiseaseDto getById(String authorities,String email,Integer diseaseId) {
        Disease diseaseFind = diseaseRepo.findById(diseaseId).orElseThrow(
                ()->new ResourceNotFoundException("Disease","DiseaseId",diseaseId));
        if (!authorities.contains("RESCUE_USER") && !diseaseFind.getPatient().getEmail().equalsIgnoreCase(email)) {
            throw new UnAuthorizedException();
        }
        DiseaseDto diseaseDto = conversionDtos.diseaseToDiseaseDto(diseaseFind);
        Set<PrescriptionDto> prescriptionDtos = new HashSet<>();
        for(Prescription prescription:diseaseFind.getPrescriptionSet()){
            PrescriptionDto prescriptionDto = conversionDtos.prescriptionToPrescriptionDto(prescription);
            prescriptionDto.setDoctor(conversionDtos.doctorToDoctorDto(prescription.getDoctor()));
            prescriptionDto.setHospital(conversionDtos.hospitalToHospitalDto(prescription.getHospital()));
            prescriptionDto.setMedicineSet(prescription.getMedicineSet().stream().map(medicine ->
                    conversionDtos.medicineToMedicineDto(medicine)).collect(Collectors.toSet()));
            prescriptionDto.setReportSet(prescription.getReportSet().stream().map(report ->
                    conversionDtos.reportToReportDto(report)).collect(Collectors.toSet()));
            prescriptionDtos.add(prescriptionDto);
        }
        diseaseDto.setPrescriptionDtoSet(prescriptionDtos);
        return diseaseDto;
    }
    @Override
    public PageResponse<DiseaseDto> getAllDiseases(String authorities,String userEmail,String patientId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient", "PatientId", patientId));
        if (!authorities.contains("RESCUE_USER") && !patient.getEmail().equalsIgnoreCase(userEmail)) {
            throw new UnAuthorizedException();
        }
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
    @Transactional
    @Override
    public void deleteDisease(Integer diseaseId) {
        Disease disease = diseaseRepo.findById(diseaseId).orElseThrow(
                () -> new ResourceNotFoundException("Disease", "DiseaseId", diseaseId));
        diseaseRepo.delete(disease);
    }

}
