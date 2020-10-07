package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialsMapper credentialsMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credentials> getAllCredentials(int userId){
        List <Credentials> credentials = credentialsMapper.getCredentials(userId);
        return credentials;
    }

    public int createCredentials(Credentials credentials){

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credentials.setPassword(encryptionService.encryptValue(credentials.getPassword(), encodedKey));
        credentials.setKey(encodedKey);
        return credentialsMapper.insertCredential(credentials);
    }

    public Credentials getById(Integer credentialId){return credentialsMapper.getCredentialsById(credentialId);}

    public int deleteCredential(Integer credentialId){ return credentialsMapper.deleteCredential(credentialId);}

    public int updateCredential(Credentials credentials){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credentials.setPassword(encryptionService.encryptValue(credentials.getPassword(), encodedKey));
        credentials.setKey(encodedKey);
        return credentialsMapper.updateCredential(credentials);
    }
}
