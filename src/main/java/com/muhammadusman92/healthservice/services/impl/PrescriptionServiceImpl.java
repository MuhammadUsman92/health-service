package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.*;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.exception.UnAuthorizedException;
import com.muhammadusman92.healthservice.payload.MedicineDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.PrescriptionDto;
import com.muhammadusman92.healthservice.payload.ReportDto;
import com.muhammadusman92.healthservice.repo.*;
import com.muhammadusman92.healthservice.services.PrescriptionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired
    private ConversionDtos conversionDtos;
    @Autowired
    private PrescriptionRepo prescriptionRepo;
    @Autowired
    private DiseaseRepo diseaseRepo;
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private HospitalUserRepo hospitalUserRepo;
    @Autowired
    private MedicineRepo medicineRepo;
    @Override
    public PrescriptionDto createPrescription(PrescriptionDto prescriptionDto) {
        Prescription prescription = conversionDtos.prescriptionDtoToPrescription(prescriptionDto);
        return conversionDtos.prescriptionToPrescriptionDto(prescriptionRepo.save(prescription));
    }

    @Override
    public PrescriptionDto createPrescription(PrescriptionDto prescriptionDto, Integer diseaseId, String doctorId, String hospitalUserEmail) {
        Prescription prescription = conversionDtos.prescriptionDtoToPrescription(prescriptionDto);
        Disease disease = diseaseRepo.findById(diseaseId).orElseThrow(
                ()->new ResourceNotFoundException("Disease","DiseaseId",diseaseId));
        prescription.setDisease(disease);
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(
                ()->new ResourceNotFoundException("Doctor","DoctorId",doctorId));
        prescription.setDoctor(doctor);
        HospitalUser hospitalUser =  hospitalUserRepo.findByEmail(hospitalUserEmail).orElseThrow(
                ()->new ResourceNotFoundException("hospitalUser","hospitalUser Email",hospitalUserEmail));
        prescription.setHospital(hospitalUser.getHospital());
        disease.getPatient().getHospitalSet().add(hospitalUser.getHospital());
        disease.getPatient().getDoctorSet().add(doctor);
        patientRepo.save(disease.getPatient());
        hospitalUser.getHospital().getPatients().add(disease.getPatient());
        hospitalRepo.save(hospitalUser.getHospital());
        doctor.getPatientSet().add(disease.getPatient());
        doctorRepo.save(doctor);
        Prescription save = prescriptionRepo.save(prescription);
        Set<Medicine> medicineSet = prescription.getMedicineSet();
        medicineSet.stream().map(medicine -> {
            medicine.setPrescription(save);
            return medicine;
        }).collect(Collectors.toSet());
        List<Medicine> medicineList = medicineRepo.saveAll(medicineSet);
        medicineSet = new HashSet<>(medicineList);
        save.setMedicineSet(medicineSet);
        PrescriptionDto prescriptionDto1 = conversionDtos.prescriptionToPrescriptionDto(save);
        return prescriptionData(save,prescriptionDto1);
    }

    @Override
    public PrescriptionDto updatePrescription(PrescriptionDto prescriptionDto, Integer prescriptionId) {
        Prescription prescription = conversionDtos.prescriptionDtoToPrescription(prescriptionDto);
        Prescription prescriptionUpdate = prescriptionRepo.findById(prescriptionId).orElseThrow(
                ()->new ResourceNotFoundException("Prescription","PrescriptionId",prescriptionId));
        prescription.setId(prescriptionUpdate.getId());
        prescription.setDisease(prescriptionUpdate.getDisease());
        prescription.setHospital(prescriptionUpdate.getHospital());
        prescription.setDoctor(prescriptionUpdate.getDoctor());
        Prescription save = prescriptionRepo.save(prescription);
        PrescriptionDto prescriptionDto1 = conversionDtos.prescriptionToPrescriptionDto(save);
        return prescriptionData(save,prescriptionDto1);
    }

    @Override
    public PrescriptionDto getById(String authorities,String userEmail,Integer prescriptionId) {
        Prescription prescriptionFind = prescriptionRepo.findById(prescriptionId).orElseThrow(
                ()->new ResourceNotFoundException("Prescription","PrescriptionId",prescriptionId));
        PrescriptionDto prescriptionDto = conversionDtos.prescriptionToPrescriptionDto(prescriptionFind);
        prescriptionDto.setHospital(conversionDtos.hospitalToHospitalDto(prescriptionFind.getHospital()));
        prescriptionDto.setDoctor(conversionDtos.doctorToDoctorDto(prescriptionFind.getDoctor()));
        return prescriptionData(prescriptionFind,prescriptionDto);
    }

    @Override
    public PageResponse<PrescriptionDto> getAllPrescriptions(String userEmail,String authorities,Integer diseaseId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Disease disease = diseaseRepo.findById(diseaseId).orElseThrow(
                ()->new ResourceNotFoundException("Disease","DiseaseId",diseaseId));
        if (!authorities.contains("RESCUE_USER") && !disease.getPatient().getEmail().equalsIgnoreCase(userEmail)) {
           throw new UnAuthorizedException();
        }
        Page<Prescription> prescriptionPage=prescriptionRepo.findByDisease(disease,pageable);
        PageResponse<PrescriptionDto> prescriptionPageResponse=new PageResponse<>();
        List<PrescriptionDto> prescriptionDtoList = prescriptionPage.getContent().stream().map(
                prescription -> conversionDtos.prescriptionToPrescriptionDto(prescription)).toList();
        prescriptionPageResponse.setContent(prescriptionDtoList);
        prescriptionPageResponse.setPageSize(prescriptionPage.getSize());
        prescriptionPageResponse.setPageNumber(prescriptionPage.getNumber());
        prescriptionPageResponse.setTotalPage(prescriptionPage.getTotalPages());
        prescriptionPageResponse.setLast(prescriptionPage.isLast());
        prescriptionPageResponse.setTotalElements(prescriptionPage.getTotalElements());
        return prescriptionPageResponse;
    }
    @Override
    public void deletePrescription(Integer prescriptionId) {
        Prescription prescriptionFind = prescriptionRepo.findById(prescriptionId).orElseThrow(
                ()->new ResourceNotFoundException("Prescription","PrescriptionId",prescriptionId));
        prescriptionRepo.delete(prescriptionFind);
    }
    private PrescriptionDto prescriptionData(Prescription prescription,PrescriptionDto prescriptionDto){
        prescriptionDto.setDisease(conversionDtos.diseaseToDiseaseDto(prescription.getDisease()));
        prescriptionDto.setDoctor(conversionDtos.doctorToDoctorDto(prescription.getDoctor()));
        prescriptionDto.setHospital(conversionDtos.hospitalToHospitalDto(prescription.getHospital()));
        Set<MedicineDto> medicineDtos = prescription.getMedicineSet().stream()
                .map(medicine -> conversionDtos.medicineToMedicineDto(medicine)).collect(Collectors.toSet());
        prescriptionDto.setMedicineSet(medicineDtos);
        Set<ReportDto> reportDtos = prescription.getReportSet().stream()
                .map(report -> conversionDtos.reportToReportDto(report)).collect(Collectors.toSet());
        prescriptionDto.setReportSet(reportDtos);
        prescriptionDto.setMedicineSet(prescription.getMedicineSet().stream().map(medicine -> conversionDtos.medicineToMedicineDto(medicine)).collect(Collectors.toSet()));
        return prescriptionDto;
    }
}
