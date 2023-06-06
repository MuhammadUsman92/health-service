package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Doctor;
import com.muhammadusman92.healthservice.entity.Gender;
import com.muhammadusman92.healthservice.entity.Hospital;
import com.muhammadusman92.healthservice.entity.HospitalUser;
import com.muhammadusman92.healthservice.exception.AlreadyExistExeption;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.exception.UnAuthorizedException;
import com.muhammadusman92.healthservice.payload.DoctorDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.repo.DoctorRepo;
import com.muhammadusman92.healthservice.repo.HospitalRepo;
import com.muhammadusman92.healthservice.repo.HospitalUserRepo;
import com.muhammadusman92.healthservice.services.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private ConversionDtos conversionDtos;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private HospitalUserRepo hospitalUserRepo;

    @Override
    public DoctorDto createDoctor(String userEmail,DoctorDto doctorDto) {
        Doctor doctor = conversionDtos.doctorDtoToDoctor(doctorDto);
        if(doctorRepo.existsById(doctor.getPmdc())){
            HospitalUser hospitalUser = hospitalUserRepo.findByEmail(userEmail).orElseThrow(UnAuthorizedException::new);
            Hospital hospital = hospitalUser.getHospital();
            Doctor findDoctor = doctorRepo.findById(doctor.getPmdc()).orElseThrow(()->new ResourceNotFoundException("Doctor","doctorId",doctorDto.getPmdc()));
            findDoctor.getHospitalSet().add(hospital);
            hospital.getDoctorSet().add(findDoctor);
            hospitalRepo.save(hospital);
            doctorRepo.save(findDoctor);
            throw new AlreadyExistExeption("PMDC", doctor.getPmdc(),"Doctor is added to hospital");
        }
        HospitalUser hospitalUser = hospitalUserRepo.findByEmail(userEmail).orElseThrow(UnAuthorizedException::new);
        Hospital hospital = hospitalUser.getHospital();
        doctor.getHospitalSet().add(hospital);
        hospital.getDoctorSet().add(doctor);
        hospitalRepo.save(hospital);
        Doctor save = doctorRepo.save(doctor);
        return conversionDtos.doctorToDoctorDto(save);
    }
    @Transactional
    @Override
    public DoctorDto updateDoctor(DoctorDto doctorDto, String doctorId) {
        Doctor doctor = conversionDtos.doctorDtoToDoctor(doctorDto);
        Doctor doctorFind = doctorRepo.findById(doctorId).orElseThrow(
                ()->new ResourceNotFoundException("Doctor","DoctorId",doctorId));
        doctor.setPmdc(doctorFind.getPmdc());
        return conversionDtos.doctorToDoctorDto(doctorRepo.save(doctor));
    }

    @Override
    public DoctorDto getById(String doctorId) {
        Doctor doctorFind = doctorRepo.findById(doctorId).orElseThrow(
                ()->new ResourceNotFoundException("Doctor","DoctorId",doctorId));
        return conversionDtos.doctorToDoctorDto(doctorFind);
    }

    @Override
    public PageResponse<DoctorDto> getAllDoctors(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Page<Doctor> doctorPage=doctorRepo.findAll(pageable);
        PageResponse<DoctorDto> doctorDtoPageResponse=new PageResponse<>();
        List<DoctorDto> doctorDtoList = doctorPage.getContent().stream().map(
                doctor -> conversionDtos.doctorToDoctorDto(doctor)).toList();
        doctorDtoPageResponse.setContent(doctorDtoList);
        doctorDtoPageResponse.setPageSize(doctorPage.getSize());
        doctorDtoPageResponse.setPageNumber(doctorPage.getNumber());
        doctorDtoPageResponse.setTotalPage(doctorPage.getTotalPages());
        doctorDtoPageResponse.setLast(doctorPage.isLast());
        doctorDtoPageResponse.setTotalElements(doctorPage.getTotalElements());
        return doctorDtoPageResponse;
    }

    @Override
    public List<DoctorDto> getAllDoctors(String userEmail) {
        HospitalUser hospitalUser = hospitalUserRepo.findByEmail(userEmail).orElseThrow(UnAuthorizedException::new);
        Hospital hospital = hospitalUser.getHospital();
        List<DoctorDto> doctorDtos = hospital.getDoctorSet().stream().map(doctor -> conversionDtos.doctorToDoctorDto(doctor)).collect(Collectors.toList());
        return doctorDtos;
    }

    @Override
    public void deleteDoctor(String doctorId) {
        Doctor doctorFind = doctorRepo.findById(doctorId).orElseThrow(
                ()->new ResourceNotFoundException("Doctor","DoctorId",doctorId));
        doctorRepo.delete(doctorFind);
    }

}
