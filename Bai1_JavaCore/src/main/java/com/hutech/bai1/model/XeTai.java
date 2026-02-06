package com.hutech.bai1.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Bài 1.2.3: Lớp XeTai kế thừa Xe
 */
public class XeTai extends Xe {
    private int trongTai; // Đơn vị: kg

    public XeTai() {
        super();
    }

    public XeTai(LocalDate ngaySx, String bienSo, int trongTai) {
        super(ngaySx, bienSo);
        this.trongTai = trongTai;
    }

    public int getTrongTai() {
        return trongTai;
    }

    public void setTrongTai(int trongTai) {
        this.trongTai = trongTai;
    }

    @Override
    public double tinhPhiDangKiem() {
        // Phí cơ bản theo trọng tải
        if (trongTai <= 2000) {
            return 340000;
        } else if (trongTai <= 7000) {
            return 450000;
        } else if (trongTai <= 15000) {
            return 560000;
        } else {
            return 700000;
        }
    }

    @Override
    public int getThoiHanDangKiem() {
        int tuoiXe = Period.between(ngaySx, LocalDate.now()).getYears();
        
        // Xe tải kinh doanh
        if (tuoiXe < 7) return 12;      // 12 tháng
        if (tuoiXe < 12) return 6;      // 6 tháng
        return 3;                        // 3 tháng
    }

    @Override
    public String toString() {
        return String.format("XeTai{%s, Trọng tải: %d kg, Phí ĐK: %.0f VNĐ, Thời hạn: %d tháng}",
                super.toString(), trongTai, tinhPhiDangKiem(), getThoiHanDangKiem());
    }
}
