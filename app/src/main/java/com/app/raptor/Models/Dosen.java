package com.app.raptor.Models;

public class Dosen {

    private String email, nama, nip, noHp, role;

    public Dosen(){

    }

    public Dosen(String email, String nama, String nip, String noHp, String role) {
        this.email = email;
        this.nama = nama;
        this.nip = nip;
        this.noHp = noHp;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
