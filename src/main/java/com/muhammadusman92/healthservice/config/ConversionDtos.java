package com.muhammadusman92.healthservice.config;

import com.muhammadusman92.healthservice.entity.*;
import com.muhammadusman92.healthservice.payload.*;

import java.util.stream.Collectors;

public class ConversionDtos {
    public Disease diseaseDtoToDisease(DiseaseDto diseaseDto){
        Disease disease=new Disease();
        disease.setName(diseaseDto.getName());
        disease.setStage(diseaseDto.getStage());
        return disease;
    }
    public DiseaseDto diseaseToDiseaseDto(Disease disease){
        DiseaseDto diseaseDto=new DiseaseDto();
        diseaseDto.setId(disease.getId());
        diseaseDto.setName(disease.getName());
        diseaseDto.setStage(disease.getStage());
        return diseaseDto;
    }
    public Doctor doctorDtoToDoctor(DoctorDto doctorDto){
        Doctor doctor=new Doctor();
        doctor.setPmdc(doctorDto.getPmdc());
        doctor.setAbout(doctorDto.getAbout());
        doctor.setGender(Gender.valueOf(doctorDto.getGender()));
        doctor.setName(doctorDto.getName());
        doctor.setQualification(doctorDto.getQualification());
        doctor.setSpecialization(doctorDto.getSpecialization());
        return doctor;
    }
    public DoctorDto doctorToDoctorDto(Doctor doctor){
        DoctorDto doctorDto=new DoctorDto();
        doctorDto.setAbout(doctorDto.getAbout());
        doctorDto.setPmdc(doctor.getPmdc());
        doctorDto.setGender(String.valueOf(doctor.getGender()));
        doctorDto.setName(doctor.getName());
        doctorDto.setQualification(doctor.getQualification());
        doctorDto.setSpecialization(doctor.getSpecialization());
        return doctorDto;
    }
    public HospitalDto hospitalToHospitalDto(Hospital hospital){
        HospitalDto hospitalDto=new HospitalDto();
        hospitalDto.setLocation(this.locationToLocationDto(hospital.getLocation()));
        hospitalDto.setName(hospital.getName());
        hospitalDto.setEmergency_unit(hospital.isEmergency_unit());
        hospitalDto.setAbout(hospitalDto.getAbout());
        hospitalDto.setReg_no(hospital.getReg_no());
        return hospitalDto;
    }
    public Hospital hospitalDtoToHospital(HospitalDto hospitalDto){
        Hospital hospital=new Hospital();
        hospital.setAbout(hospitalDto.getAbout());
        hospital.setLocation(this.locationDtoToLocation(hospitalDto.getLocation()));
        hospital.setName(hospitalDto.getName());
        hospital.setEmergency_unit(hospitalDto.isEmergency_unit());
        hospital.setReg_no(hospitalDto.getReg_no());
        return hospital;
    }
    public Location locationDtoToLocation(LocationDto locationDto){
        Location location=new Location();
        location.setCity(locationDto.getCity());
        location.setCountry(locationDto.getCountry());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setPostal_code(locationDto.getPostal_code());
        location.setStreet(locationDto.getStreet());
        return location;
    }
    public LocationDto locationToLocationDto(Location location){
        LocationDto locationDto=new LocationDto();
        locationDto.setCity(location.getCity());
        locationDto.setCountry(location.getCountry());
        locationDto.setLatitude(location.getLatitude());
        locationDto.setLongitude(location.getLongitude());
        locationDto.setPostal_code(location.getPostal_code());
        locationDto.setStreet(location.getStreet());
        return locationDto;
    }
    public Laboratory laboratoryDtoToLaboratory(LaboratoryDto laboratoryDto){
        Laboratory laboratory=new Laboratory();
        laboratory.setName(laboratoryDto.getName());
        laboratory.setReg_no(laboratoryDto.getReg_no());
        laboratory.setLocation(locationDtoToLocation(laboratoryDto.getLocation()));
        return laboratory;
    }
    public LaboratoryDto laboratoryToLaboratoryDto(Laboratory laboratory){
        LaboratoryDto laboratoryDto=new LaboratoryDto();
        laboratoryDto.setLocation(locationToLocationDto(laboratory.getLocation()));
        laboratoryDto.setName(laboratory.getName());
        laboratoryDto.setReg_no(laboratory.getReg_no());
        return laboratoryDto;
    }
    public Medicine medicineDtoToMedicine(MedicineDto medicineDto){
        Medicine medicine=new Medicine();
        medicine.setName(medicineDto.getName());
        medicine.setDuration(medicineDto.getDuration());
        medicine.setQuantity(medicineDto.getQuantity());
        medicine.setTiming(medicineDto.getTiming());
        medicine.setType(medicineDto.getType());
        return medicine;
    }
    public MedicineDto medicineToMedicineDto(Medicine medicine){
        MedicineDto medicineDto=new MedicineDto();
        medicineDto.setId(medicine.getId());
        medicineDto.setName(medicine.getName());
        medicineDto.setDuration(medicine.getDuration());
        medicineDto.setQuantity(medicine.getQuantity());
        medicineDto.setTiming(medicine.getTiming());
        medicineDto.setType(medicine.getType());
        return medicineDto;
    }
    public Patient patientDtoToPatient(PatientDto patientDto){
        Patient patient=new Patient();
        patient.setCNIC(patientDto.getCNIC());
        patient.setEmail(patientDto.getEmail());
        patient.setBloodGroup(patientDto.getBloodGroup());
        patient.setAge(patientDto.getAge());
        patient.setGender(Gender.valueOf(patientDto.getGender()));
        patient.setHeight(patientDto.getHeight());
        patient.setName(patientDto.getName());
        patient.setWeight(patientDto.getWeight());
        patient.setLocation(locationDtoToLocation(patientDto.getLocation()));
        return patient;
    }
    public PatientDto patientToPatientDto(Patient patient){
        PatientDto patientDto=new PatientDto();
        patientDto.setCNIC(patient.getCNIC());
        patientDto.setEmail(patient.getEmail());
        patientDto.setBloodGroup(patient.getBloodGroup());
        patientDto.setAge(patient.getAge());
        patientDto.setGender(String.valueOf(patient.getGender()));
        patientDto.setHeight(patient.getHeight());
        patientDto.setName(patient.getName());
        patientDto.setWeight(patient.getWeight());
        patientDto.setLocation(locationToLocationDto(patient.getLocation()));
        return patientDto;
    }
    public PrescriptionDto prescriptionToPrescriptionDto(Prescription prescription){
        PrescriptionDto prescriptionDto=new PrescriptionDto();
        prescriptionDto.setId(prescription.getId());
        prescriptionDto.setComments(prescription.getComments());
        prescriptionDto.setDate(prescription.getDate());
        prescriptionDto.setMedicineSet(prescription.getMedicineSet().stream().map(this::medicineToMedicineDto).collect(Collectors.toSet()));
        prescriptionDto.setReportSet(prescription.getReportSet().stream().map(this::reportToReportDto).collect(Collectors.toSet()));
        prescriptionDto.setRecover(prescription.isRecover());
        return prescriptionDto;
    }
    public Prescription prescriptionDtoToPrescription(PrescriptionDto prescriptionDto){
        Prescription prescription=new Prescription();
        prescription.setRecover(prescriptionDto.isRecover());
        prescription.setDate(prescriptionDto.getDate());
        prescription.setComments(prescriptionDto.getComments());
        prescription.setMedicineSet(prescriptionDto.getMedicineSet().stream().map(this::medicineDtoToMedicine).collect(Collectors.toSet()));
        prescription.setReportSet(prescriptionDto.getReportSet().stream().map(this::reportDtoToReport).collect(Collectors.toSet()));
        return prescription;
    }
    public Report reportDtoToReport(ReportDto reportDto){
        Report report=new Report();
        report.setReport_image(reportDto.getReport_image());
        report.setCollect_date(reportDto.getCollect_date());
        report.setResult_date(reportDto.getResult_date());
        return report;
    }
    public ReportDto reportToReportDto(Report report){
        ReportDto reportDto=new ReportDto();
        reportDto.setId(report.getId());
        reportDto.setReport_image(report.getReport_image());
        reportDto.setCollect_date(report.getCollect_date());
        reportDto.setResult_date(report.getResult_date());
        return reportDto;
    }
}
