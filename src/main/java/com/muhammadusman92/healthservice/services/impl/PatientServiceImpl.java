package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Gender;
import com.muhammadusman92.healthservice.entity.Patient;
import com.muhammadusman92.healthservice.exception.AlreadyExistExeption;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.payload.*;
import com.muhammadusman92.healthservice.repo.LocationRepo;
import com.muhammadusman92.healthservice.repo.PatientRepo;
import com.muhammadusman92.healthservice.services.PatientService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.annotations.Where;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private ConversionDtos conversionDtos;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private LocationRepo locationRepo;

    @Override
    public PatientDto createPatient(PatientDto patientDto) {
        Patient patient=conversionDtos.patientDtoToPatient(patientDto);
        if(patientRepo.existsCNIC(patient.getCNIC())>0){
            throw new AlreadyExistExeption("CNIC", patient.getCNIC());
        }
        patient.setLocation(locationRepo.save(patient.getLocation()));
        Patient save = patientRepo.save(patient);
        PatientDto patientDto1 = conversionDtos.patientToPatientDto(save);
        return this.patientData(save,patientDto1);
    }

    @Override
    public PatientDto updatePatient(PatientDto patientDto, String patientId) {
        Patient patient=conversionDtos.patientDtoToPatient(patientDto);
        Patient patientUpdate = patientRepo.findById(patientId).orElseThrow(
                ()->new ResourceNotFoundException("Patient","PatientId",patientId));
        patient.setCNIC(patient.getCNIC());
        Patient save = patientRepo.save(patientUpdate);
        PatientDto patientDto1 = conversionDtos.patientToPatientDto(save);
        return this.patientData(save,patientDto1);
    }

    @Override
    public PatientDto getById(String patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()->new ResourceNotFoundException("Patient","PatientId",patientId));
        PatientDto patientDto = conversionDtos.patientToPatientDto(patient);
        patientDto.setLocation(conversionDtos.locationToLocationDto(patient.getLocation()));
        patientDto.setDoctorSet(patient.getDoctorSet().stream().map(doctor -> conversionDtos.doctorToDoctorDto(doctor)).collect(Collectors.toSet()));
        patientDto.setHospitalSet(patient.getHospitalSet().stream().map(hospital->conversionDtos.hospitalToHospitalDto(hospital)).collect(Collectors.toSet()));
        patientDto.setDiseaseSet(patient.getDiseaseSet().stream().map(disease -> conversionDtos.diseaseToDiseaseDto(disease)).collect(Collectors.toSet()));
        return this.patientData(patient,patientDto);
    }

    @Override
    public PageResponse<PatientDto> getAllPatients(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Page<Patient> patientPage = patientRepo.findAll(pageable);
        PageResponse<PatientDto> patientPageResponse = new PageResponse<>();
        List<PatientDto> patientDtoList= patientPage.getContent()
                .stream().map(patient -> conversionDtos.patientToPatientDto(patient)).toList();
        patientPageResponse.setContent(patientDtoList);
        patientPageResponse.setTotalPage(patientPage.getTotalPages());
        patientPageResponse.setLast(patientPage.isLast());
        patientPageResponse.setPageNumber(patientPage.getNumber());
        patientPageResponse.setPageSize(patientPage.getSize());
        patientPageResponse.setTotalElements(patientPage.getTotalElements());
        return patientPageResponse;
    }

    @Override
    public void deletePatient(String patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()->new ResourceNotFoundException("Patient","PatientId",patientId));
        patientRepo.delete(patient);
    }
    private PatientDto patientData(Patient patient,PatientDto patientDto){
        Set<DiseaseDto> diseaseDtos = patient.getDiseaseSet().stream()
                .map(disease -> conversionDtos.diseaseToDiseaseDto(disease)).collect(Collectors.toSet());
        patientDto.setDiseaseSet(diseaseDtos);
        Set<DoctorDto> doctorDtos = patient.getDoctorSet().stream()
                .map(doctor -> conversionDtos.doctorToDoctorDto(doctor)).collect(Collectors.toSet());
        patientDto.setDoctorSet(doctorDtos);
        Set<HospitalDto> hospitalDtos = patient.getHospitalSet().stream()
                .map(hospital -> conversionDtos.hospitalToHospitalDto(hospital)).collect(Collectors.toSet());
        patientDto.setHospitalSet(hospitalDtos);
        return patientDto;
    }
}
