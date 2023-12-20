package com.app.raptor.Models;

public class UserDetails {
    private String nama, nim, judulkp, dospen, pembimbing, email;

    public UserDetails(){}

    public UserDetails(String nama, String nim, String judulkp, String dospen, String pembimbing, String email) {
        this.nama = nama;
        this.nim = nim;
        this.judulkp = judulkp;
        this.dospen = dospen;
        this.pembimbing = pembimbing;
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getJudulkp() {
        return judulkp;
    }

    public void setJudulkp(String judulkp) {
        this.judulkp = judulkp;
    }

    public String getDospen() {
        return dospen;
    }

    public void setDospen(String dospen) {
        this.dospen = dospen;
    }

    public String getPembimbing() {
        return pembimbing;
    }

    public void setPembimbing(String pembimbing) {
        this.pembimbing = pembimbing;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
