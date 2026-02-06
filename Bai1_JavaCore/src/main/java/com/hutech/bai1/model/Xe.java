package com.hutech.bai1.model;

import java.time.LocalDate;

/**
 * Bài 1.2.3: Lớp Xe (Abstract)
 */
public abstract class Xe {
    protected LocalDate ngaySx;
    protected String bienSo;

    public Xe() {
    }

    public Xe(LocalDate ngaySx, String bienSo) {
        this.ngaySx = ngaySx;
        this.bienSo = bienSo;
    }

    public LocalDate getNgaySx() {
        return ngaySx;
    }

    public void setNgaySx(LocalDate ngaySx) {
        this.ngaySx = ngaySx;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    // Phương thức abstract tính phí đăng kiểm
    public abstract double tinhPhiDangKiem();

    // Phương thức abstract tính thời hạn đăng kiểm (tháng)
    public abstract int getThoiHanDangKiem();

    @Override
    public String toString() {
        return String.format("Biển số: %s, Ngày SX: %s", bienSo, ngaySx);
    }
}
