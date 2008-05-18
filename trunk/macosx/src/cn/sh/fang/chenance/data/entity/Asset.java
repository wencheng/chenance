package cn.sh.fang.chenance.data.entity;

import javax.persistence.*;

@Table(name="t_asset")
@Entity
public class Asset extends BaseEntity {

    @Id
    private Integer id;
    
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Transaction.class)
    @JoinColumn(name="transaction_id", referencedColumnName="id")
    private Transaction transaction;

    @Column(name="item_name")
    private String itemName;

    @Column(name="item_description")
    private String itemDescription;

    @Column(name="depreciation_rate")
    private Integer depreciationRate;

    @Column(name="depreciation_period")
    private Integer depreciationPeriod;

    @Column(name="depreciation_period_unit")
    private Integer depreciationPeriodUnit;

    public String getItemName() {
        return this.itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public Transaction getTransaction() {
        return this.transaction;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public Integer getDepreciationPeriod() {
        return this.depreciationPeriod;
    }
    
    public void setDepreciationPeriod(Integer depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }
    
    public String getItemDescription() {
        return this.itemDescription;
    }
    
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    
    public Integer getDepreciationPeriodUnit() {
        return this.depreciationPeriodUnit;
    }
    
    public void setDepreciationPeriodUnit(Integer depreciationPeriodUnit) {
        this.depreciationPeriodUnit = depreciationPeriodUnit;
    }
    
    public Integer getDepreciationRate() {
        return this.depreciationRate;
    }
    
    public void setDepreciationRate(Integer depreciationRate) {
        this.depreciationRate = depreciationRate;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
}