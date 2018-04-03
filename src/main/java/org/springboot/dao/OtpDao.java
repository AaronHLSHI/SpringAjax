package org.springboot.dao;

import java.util.List;

import org.springboot.entity.Otp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository("otpDao")
public class OtpDao {

	@Autowired
    private MongoTemplate mongoTemplate;

	public void insert(Otp otp) {
        mongoTemplate.insert(otp);
    }
    
    public List<Otp> findCode() {
    	return mongoTemplate.findAll(Otp.class);
    }
}
