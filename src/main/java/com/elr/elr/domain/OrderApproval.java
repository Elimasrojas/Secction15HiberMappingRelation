package com.elr.elr.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;


@Entity
public class OrderApproval extends BaseEntity {
    /*
    * Creamon relacion bi direcconal uno a uno*/
    @OneToOne
    @JoinColumn(name = "order_header_id") //
    private OrderHeader orderHeader;
    private String approvedBy;

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public OrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(OrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderApproval that)) return false;
        if (!super.equals(o)) return false;

        if (getOrderHeader() != null ? !getOrderHeader().equals(that.getOrderHeader()) : that.getOrderHeader() != null)
            return false;
        return getApprovedBy() != null ? getApprovedBy().equals(that.getApprovedBy()) : that.getApprovedBy() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getOrderHeader() != null ? getOrderHeader().hashCode() : 0);
        result = 31 * result + (getApprovedBy() != null ? getApprovedBy().hashCode() : 0);
        return result;
    }
}