package com.app.raptor.Models;

public class UserDetails {
    private String nama, nim, judulkp, semester, dospem, pembimbing, email, tglMulai, uid;

    public UserDetails(){}

    public UserDetails(String nama, String nim, String judulkp, String semester, String dospem, String pembimbing, String email, String tglMulai, String uid) {
        this.nama = nama;
        this.nim = nim;
        this.judulkp = judulkp;
        this.semester = semester;
        this.dospem = dospem;
        this.pembimbing = pembimbing;
        this.email = email;
        this.tglMulai = tglMulai;
        this.uid = uid;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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

    public String getTglMulai() {
        return tglMulai;
    }

    public void setTglMulai(String tglMulai) {
        this.tglMulai = tglMulai;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
