package cn.sh.fang.chenance.data.entity;

import javax.persistence.*;

@Table(name="t_account")
@Entity
public class Account extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="bank_name")
    private String bankName;

    @Column(name="bank_branch")
    private String bankBranch;

    @Column(name="bank_account_no")
    private String bankAccountNo;

    @Column(name="bank_url")
    private String bankUrl;

    @Column(name="interest_rate")
    private Integer interestRate;

    @Column(name="start_balance")
    private Integer startBalance;

    @Column(name="current_balance")
    private Integer currentBalance;

    @Column(name="currency")
    private Integer currency;

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getCurrentBalance() {
        return this.currentBalance;
    }
    
    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }
    
    public String getBankAccountNo() {
        return this.bankAccountNo;
    }
    
    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }
    
    public String getBankName() {
        return this.bankName;
    }
    
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
    public String getBankUrl() {
        return this.bankUrl;
    }
    
    public void setBankUrl(String bankUrl) {
        this.bankUrl = bankUrl;
    }
    
    public String getBankBranch() {
        return this.bankBranch;
    }
    
    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getInterestRate() {
        return this.interestRate;
    }
    
    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }
    
    public Integer getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(Integer currency) {
        this.currency = currency;
    }
    
    public Integer getStartBalance() {
        return this.startBalance;
    }
    
    public void setStartBalance(Integer startBalance) {
        this.startBalance = startBalance;
    }
    
}