package com.example.payexample;

public class Amount {
    Integer total;
    Integer tax_free;
    Integer vat;
    Integer point;
    Integer discount;

    public Amount(Integer total, Integer tax_free, Integer vat, Integer point, Integer discount) {
        this.total = total;
        this.tax_free = tax_free;
        this.vat = vat;
        this.point = point;
        this.discount = discount;
    }

    public Amount() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTax_free() {
        return tax_free;
    }

    public void setTax_free(Integer tax_free) {
        this.tax_free = tax_free;
    }

    public Integer getVat() {
        return vat;
    }

    public void setVat(Integer vat) {
        this.vat = vat;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
