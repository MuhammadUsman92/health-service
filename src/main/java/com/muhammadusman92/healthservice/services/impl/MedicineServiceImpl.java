package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Hospital;
import com.muhammadusman92.healthservice.entity.HospitalUser;
import com.muhammadusman92.healthservice.entity.Medicine;
import com.muhammadusman92.healthservice.entity.Prescription;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.exception.UnAuthorizedException;
import com.muhammadusman92.healthservice.payload.MedicineDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.repo.HospitalRepo;
import com.muhammadusman92.healthservice.repo.HospitalUserRepo;
import com.muhammadusman92.healthservice.repo.MedicineRepo;
import com.muhammadusman92.healthservice.repo.PrescriptionRepo;
import com.muhammadusman92.healthservice.services.MedicineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    private ConversionDtos conversionDtos;
    @Autowired
    private MedicineRepo medicineRepo;
    @Autowired
    private PrescriptionRepo prescriptionRepo;
    @Autowired
    private HospitalUserRepo hospitalUserRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Override
    public MedicineDto createMedicine(MedicineDto medicineDto) {
        Medicine medicine = conversionDtos.medicineDtoToMedicine(medicineDto);
        return conversionDtos.medicineToMedicineDto(medicineRepo.save(medicine));
    }
    @Transactional
    @Override
    public MedicineDto createMedicine(String userEmail,Integer prescriptionId, MedicineDto medicineDto) {
        Medicine medicine = conversionDtos.medicineDtoToMedicine(medicineDto);
        Prescription prescription = prescriptionRepo.findById(prescriptionId).orElseThrow(
                ()->new ResourceNotFoundException("Prescription","PrescriptionId",prescriptionId));
        HospitalUser hospitalUser = hospitalUserRepo.findByEmail(userEmail).orElseThrow(UnAuthorizedException::new);
        Hospital hospital = hospitalUser.getHospital();
        hospital.getPrescriptionSet().add(prescription);
        hospital.getPatients().add(prescription.getDisease().getPatient());
        hospitalRepo.save(hospital);
        medicine.setPrescription(prescription);
        return conversionDtos.medicineToMedicineDto(medicineRepo.save(medicine));
    }
    @Transactional
    @Override
    public MedicineDto updateMedicine(MedicineDto medicineDto, Integer medicineId) {
        Medicine medicine = conversionDtos.medicineDtoToMedicine(medicineDto);
        Medicine medicineFind = medicineRepo.findById(medicineId).orElseThrow(
                ()->new ResourceNotFoundException("Medicine","MedicineId",medicineId));
        medicine.setId(medicineFind.getId());
        return conversionDtos.medicineToMedicineDto(medicineRepo.save(medicine));
    }

    @Override
    public MedicineDto getById(Integer medicineId) {
        Medicine medicineFind = medicineRepo.findById(medicineId).orElseThrow(
                ()->new ResourceNotFoundException("Medicine","MedicineId",medicineId));
        return conversionDtos.medicineToMedicineDto(medicineFind);
    }

    @Override
    public PageResponse<MedicineDto> getAllMedicines(String authorities,String userEmail,Integer prescriptionId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Prescription prescription = prescriptionRepo.findById(prescriptionId).orElseThrow(
                ()->new ResourceNotFoundException("Prescription","PrescriptionId",prescriptionId));
        if (!authorities.contains("RESCUE_USER") && !prescription.getDisease().getPatient().getEmail().equalsIgnoreCase(userEmail)) {
            throw new UnAuthorizedException();
        }
        Page<Medicine> medicinePage = medicineRepo.findByPrescription(prescription,pageable);
        PageResponse<MedicineDto> medicineDtoPageResponse=new PageResponse<>();
        List<MedicineDto> medicineDtoList=medicinePage.getContent().stream().map(
                medicine -> conversionDtos.medicineToMedicineDto(medicine)).toList();
        medicineDtoPageResponse.setContent(medicineDtoList);
        medicineDtoPageResponse.setTotalPage(medicinePage.getTotalPages());
        medicineDtoPageResponse.setPageNumber(medicinePage.getNumber());
        medicineDtoPageResponse.setPageSize(medicinePage.getSize());
        medicineDtoPageResponse.setLast(medicinePage.isLast());
        medicineDtoPageResponse.setTotalElements(medicinePage.getTotalElements());
        return medicineDtoPageResponse;
    }

    @Override
    public void deleteMedicine(Integer medicineId) {
        Medicine medicineFind = medicineRepo.findById(medicineId).orElseThrow(
                ()->new ResourceNotFoundException("Medicine","MedicineId",medicineId));
        medicineRepo.delete(medicineFind);
    }
}
