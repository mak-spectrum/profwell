package org.profwell.vacancy.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Access(AccessType.FIELD)
public class SalaryRange {

    @Column(name="SALARY_FROM", nullable = true)
    private Integer from;

    @Column(name="SALARY_TILL", nullable = true)
    private Integer till;

    @Enumerated(EnumType.STRING)
    @Column(name="SALARY_CURRENCY", nullable = true, length = 3)
    private Currency currency;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Integer getTill() {
        return till;
    }

    public void setTill(Integer till) {
        this.till = till;
    }

}
