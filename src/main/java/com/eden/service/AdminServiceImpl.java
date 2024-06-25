package com.eden.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import com.eden.dao.AdminDao;
import com.eden.entity.Admin;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminDao adminDao;

	@Override
	public void register(Admin admin) {
		
		Admin adminDB = adminDao.findByAdminName(admin.getAdminname());
		
		if(!ObjectUtils.isEmpty(adminDB)) {
			throw new RuntimeException("この管理者名が既に存在している。");
		}
		
		String newpassword = DigestUtils.md5DigestAsHex(admin.getPassword().getBytes(StandardCharsets.UTF_8));
		
		admin.setPassword(newpassword);
		
		adminDao.save(admin);
	}

	@Override
	public Admin login(String adminname, String password) {
		Admin admin= adminDao.findByAdminName(adminname);
		
		if(ObjectUtils.isEmpty(admin)) {
			throw new RuntimeException("管理者の名がいないので、再び入力してください。");
		}
		
		String pwdSecret = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
		
		if(!admin.getPassword().equals(pwdSecret)) {
			throw new RuntimeException("入力したパスワードが間違っている。");
		}
		
		return admin;
	}

}
