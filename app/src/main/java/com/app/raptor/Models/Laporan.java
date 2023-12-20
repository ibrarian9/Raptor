package com.app.raptor.Models;

public class Laporan {
    private String tanggal, agenda, waktu, lokasi, narasumber, kegiatan, foto;

    public Laporan(){}

    public Laporan(String tanggal, String agenda, String waktu, String lokasi, String narasumber, String kegiatan, String foto) {
        this.tanggal = tanggal;
        this.agenda = agenda;
        this.waktu = waktu;
        this.lokasi = lokasi;
        this.narasumber = narasumber;
        this.kegiatan = kegiatan;
        this.foto = foto;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getNarasumber() {
        return narasumber;
    }

    public void setNarasumber(String narasumber) {
        this.narasumber = narasumber;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
