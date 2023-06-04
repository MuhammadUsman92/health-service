package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.entity.Hospital;
import com.muhammadusman92.healthservice.entity.HospitalUser;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.repo.HospitalRepo;
import com.muhammadusman92.healthservice.repo.HospitalUserRepo;
import com.muhammadusman92.healthservice.services.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalUserServiceImpl implements HospitalUserService {
    @Autowired
    private HospitalUserRepo hospitalUserRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Override
    public String addHospitalUser(String hospitalId, String email) {
        Hospital hospital = hospitalRepo.findById(hospitalId).orElseThrow(()->
                new ResourceNotFoundException("hospital","hospitalId",hospitalId));
        HospitalUser hospitalUser = new HospitalUser();
        hospitalUser.setHospital(hospital);
        hospitalUser.setEmail(email);
        HospitalUser hospitalUser1 = hospitalUserRepo.save(hospitalUser);
        hospital.getHospitalUserSet().add(hospitalUser1);
        hospitalRepo.save(hospital);
        return "OK";
    }
}
