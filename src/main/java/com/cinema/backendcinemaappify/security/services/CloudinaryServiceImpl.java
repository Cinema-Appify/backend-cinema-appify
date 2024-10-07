package com.cinema.backendcinemaappify.security.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl {

    private Cloudinary cloudinary;

    public CloudinaryServiceImpl() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "duhypoi5d");
        valuesMap.put("api_key", "599989977423184");
        valuesMap.put("api_secret", "6DPY-3WF3NNy4R1IDu7fcZRL2UM");
        cloudinary = new Cloudinary(valuesMap);
    }

    public Map uploadImage(Object file) throws IOException {
        return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    }

    public Map deleteImage(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

}
