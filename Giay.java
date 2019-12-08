package com.example.shoesshop;

import java.io.Serializable;

public class Giay implements Serializable {
    private int id;
    private String tenSP;
    private String gioiThieu;
    private int giaGiay;
    private String nhaSanXuat;
    private int soLuong;
    private int hinhAnh;
    private String diaChiMua;
    private String emailDangNhap;

    public Giay() {
    }

    public Giay(int id, String tenSP, String gioiThieu, int giaGiay, String nhaSanXuat, int soLuong, int hinhAnh, String diaChiMua, String emailDangNhap) {
        this.id = id;
        this.tenSP = tenSP;
        this.gioiThieu = gioiThieu;
        this.giaGiay = giaGiay;
        this.nhaSanXuat = nhaSanXuat;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
        this.diaChiMua = diaChiMua;
        this.emailDangNhap = emailDangNhap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getGioiThieu() {
        return gioiThieu;
    }

    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }

    public int getGiaGiay() {
        return giaGiay;
    }

    public void setGiaGiay(int giaGiay) {
        this.giaGiay = giaGiay;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getDiaChiMua() {
        return diaChiMua;
    }

    public void setDiaChiMua(String diaChiMua) {
        this.diaChiMua = diaChiMua;
    }

    public String getEmailDangNhap() {
        return emailDangNhap;
    }

    public void setEmailDangNhap(String emailDangNhap) {
        this.emailDangNhap = emailDangNhap;
    }
}
