package com.app.raptor.Models;

public class UserDetails {
    private String nama, nim, judulkp, dospem, pembimbing, email;

    public UserDetails(){}

    public UserDetails(String nama, String nim, String judulkp, String dospem, String pembimbing, String email) {
        this.nama = nama;
        this.nim = nim;
        this.judulkp = judulkp;
        this.dospem = dospem;
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

    public String getDospem() {
        return dospem;
    }

    public void setDospem(String dospem) {
        this.dospem = dospem;
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
