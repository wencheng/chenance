package cn.sh.fang.chenance.data.entity;

import javax.persistence.*;

@Table(name="t_repeat_payment")
@Entity
public class RepeatPayment extends BaseEntity {

    @Id
    private Integer id;
    
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Category.class)
    @JoinColumn(name="category_id", referencedColumnName="id")
    private Category category;

    @Column(name="amount")
    private Integer amount;

    @Column(name="period")
    private Integer period;

    @Column(name="period_unit")
    private Integer periodUnit;

    @Column(name="auto_approve")
    private Boolean autoApprove;

    public Boolean getAutoApprove() {
        return this.autoApprove;
    }
    
    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }
    
    public Integer getPeriodUnit() {
        return this.periodUnit;
    }
    
    public void setPeriodUnit(Integer periodUnit) {
        this.periodUnit = periodUnit;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Integer getPeriod() {
        return this.period;
    }
    
    public void setPeriod(Integer period) {
        this.period = period;
    }
    
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
}