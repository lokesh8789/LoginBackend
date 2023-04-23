package com.login.service;

import com.login.dto.LoginDto;
import com.login.entities.SaveToken;
import com.login.repo.TokenRepo;
import com.login.repo.UserRepo;
import com.login.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    UserRepo userRepo;
    @Autowired
    TokenRepo tokenRepo;
    @Override
    public String validateLogin(LoginDto loginDto) {
        Integer id=userRepo.findIdByEmailAndPassword(loginDto.getUserName(), loginDto.getPassword());
        if (id == null) {
            return null;
        }
        String token = tokenUtil.generateToken(id);
        SaveToken saveToken=new SaveToken(id,token);
        tokenRepo.save(saveToken);
        Integer userIdFromToken = tokenUtil.getUserIdFromToken(token);
        System.out.println(userIdFromToken+"------------------");
        return token;
    }

    @Override
    public Boolean logout(String token) {
        Integer id = tokenUtil.getUserIdFromToken(token);
        SaveToken saveToken = tokenRepo.findById(id).orElse(null);
        if(saveToken==null){
            return false;
        }
        tokenRepo.delete(saveToken);
        return true;
    }
}
