package com.cinema.backendcinemaappify.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "cinema")
public class Cinema {

    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private String photo;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    private String state = "pending";

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public Cinema() {
    }

    public Cinema(String name, String email, String password) {
        this.name = name;
        //this.photo = photo;
        this.email = email;
        this.password = password;
        this.state = "pending";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return name;
    }

    public void setNombre(String nombre) {
        this.name = nombre;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String foto) {
        this.photo = foto;
    }

    public String getCorreo() {
        return email;
    }

    public void setCorreo(String correo) {
        this.email = correo;
    }

    public String getContrasenia() {
        return password;
    }

    public void setContrasenia(String contrasenia) {
        this.password = contrasenia;
    }

    public String getEstado() {
        return state;
    }

    public void setEstado(String estado) {
        this.state = estado;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
