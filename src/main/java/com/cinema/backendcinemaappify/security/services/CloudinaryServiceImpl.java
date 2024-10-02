package com.cinema.backendcinemaappify.security.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl {

    @Autowired
    private Cloudinary cloudinary;

    public Map uploadImage(Object file) throws IOException {
        return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    }

    public Map deleteImage(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

}
