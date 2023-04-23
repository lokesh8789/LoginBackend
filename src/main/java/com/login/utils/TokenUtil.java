package com.login.utils;

import com.login.entities.SaveToken;
import com.login.repo.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenUtil {
    @Autowired
    TokenRepo tokenRepo;
    public String generateToken(Integer id){
        String randomToken = UUID.randomUUID().toString();
        randomToken=randomToken.replaceAll("$","-");
        randomToken=randomToken.replaceAll("%","+");
        StringBuilder token=new StringBuilder(randomToken);
        token.insert((int) (Math.random()*token.length()),"$"+id+"%");
        return token.toString();
    }
    public Integer getUserIdFromToken(String token){
        try{
            String temp=token.substring(token.indexOf("$")+1,token.indexOf("%"));
            return Integer.parseInt(temp);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token){
        Integer id = getUserIdFromToken(token);
        if (id == null) {
            return false;
        }
        SaveToken saveToken = tokenRepo.findById(id).orElse(null);
        if (saveToken == null) {
            return false;
        } else if (!saveToken.getToken().equals(token)) {
            return false;
        }
        return true;
    }
}
