package com.cinema.backendcinemaappify.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cinema")
public class Cinema {

    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    private String nombre;

    @NotBlank
    private String foto;

    @NotBlank
    @Size(max = 50)
    @Email
    private String correo;

    @NotBlank
    @Size(max = 120)
    private String contrasenia;

    private String estado = "pending";

    public Cinema() {
    }

    public Cinema(String nombre, String correo, String contrasenia) {
        this.nombre = nombre;
        //this.foto = foto;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.estado = "pending";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
