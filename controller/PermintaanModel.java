package com.mycompany.peminjamanbarang.controller;



public class PermintaanModel {
    private int id;
    private String namaPengaju;
    private String namaBarang;
    private String kodeBarang;
    private String status;

    public PermintaanModel(int id, String namaPengaju, String namaBarang, String kodeBarang, String status) {
        this.id = id;
        this.namaPengaju = namaPengaju;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.status = status;
    }

    public int getId() { return id; }
    public String getNamaPengaju() { return namaPengaju; }
    public String getNamaBarang() { return namaBarang; }
    public String getKodeBarang() { return kodeBarang; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
