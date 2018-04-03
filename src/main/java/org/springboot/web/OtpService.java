package org.springboot.web;

import java.util.List;

import org.springboot.dao.OtpDao;
import org.springboot.entity.Otp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("otpService")
public class OtpService {

	@Autowired
    private OtpDao otpDao;
	
	public List<Otp> findAll() {
        return otpDao.findCode();
    }
	
	public void insert(Otp otp) {
		otpDao.insert(otp);
    }
}
