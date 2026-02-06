package com.hutech.bai1.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Bài 1.2.3: Lớp XeOto kế thừa Xe
 */
public class XeOto extends Xe {
    private int soGhe;
    private boolean kinhDoanh;

    public XeOto() {
        super();
    }

    public XeOto(LocalDate ngaySx, String bienSo, int soGhe, boolean kinhDoanh) {
        super(ngaySx, bienSo);
        this.soGhe = soGhe;
        this.kinhDoanh = kinhDoanh;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public boolean isKinhDoanh() {
        return kinhDoanh;
    }

    public void setKinhDoanh(boolean kinhDoanh) {
        this.kinhDoanh = kinhDoanh;
    }

    @Override
    public double tinhPhiDangKiem() {
        // Phí cơ bản
        double phiCoBan = 340000;
        
        // Xe kinh doanh có phí cao hơn
        if (kinhDoanh) {
            phiCoBan += 100000;
        }
        
        // Xe trên 9 chỗ có phí cao hơn
        if (soGhe > 9) {
            phiCoBan += 50000 * (soGhe - 9);
        }
        
        return phiCoBan;
    }

    @Override
    public int getThoiHanDangKiem() {
        int tuoiXe = Period.between(ngaySx, LocalDate.now()).getYears();
        
        if (kinhDoanh) {
            // Xe kinh doanh
            if (tuoiXe < 5) return 12;      // 12 tháng
            if (tuoiXe < 12) return 6;      // 6 tháng
            return 3;                        // 3 tháng
        } else {
            // Xe cá nhân
            if (tuoiXe < 7) return 24;      // 24 tháng
            if (tuoiXe < 12) return 12;     // 12 tháng
            return 6;                        // 6 tháng
        }
    }

    @Override
    public String toString() {
        return String.format("XeOto{%s, Số ghế: %d, Kinh doanh: %s, Phí ĐK: %.0f VNĐ, Thời hạn: %d tháng}",
                super.toString(), soGhe, kinhDoanh ? "Có" : "Không", 
                tinhPhiDangKiem(), getThoiHanDangKiem());
    }
}
