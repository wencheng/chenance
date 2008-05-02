package cn.sh.fang.chenance.data.entity;

import javax.persistence.*;

/**
 * 
 */
@Table(name="t_transaction")
@Entity
public class Transaction extends BaseEntity {

    /**  */
    @Id
    private Integer id;
    
    /**  */
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Category.class)
    @JoinColumn(name="category_id", referencedColumnName="id")
    private Category category;

    /**  */
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Account.class)
    @JoinColumn(name="account_id", referencedColumnName="id")
    private Account account;

    /**  */
    @Column(name="account_id")
    private Integer accountId;

    /**  */
    @Column(name="_date")
    private java.util.Date Date;

    /**  */
    @Column(name="category_id")
    private Integer categoryId;

    /**  */
    @Column(name="debit")
    private Integer debit;

    /**  */
    @Column(name="credit")
    private Integer credit;

    /**  */
    @Column(name="is_repeat")
    private Boolean isRepeat;

    public Integer getDebit() {
        return this.debit;
    }
    
    public void setDebit(Integer debit) {
        this.debit = debit;
    }
    
    public Integer getAccountId() {
        return this.accountId;
    }
    
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    
    public Integer getCategoryId() {
        return this.categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public Integer getCredit() {
        return this.credit;
    }
    
    public void setCredit(Integer credit) {
        this.credit = credit;
    }
    
    public Boolean getIsRepeat() {
        return this.isRepeat;
    }
    
    public void setIsRepeat(Boolean isRepeat) {
        this.isRepeat = isRepeat;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public java.util.Date getDate() {
        return this.Date;
    }
    
    public void setDate(java.util.Date Date) {
        this.Date = Date;
    }
    
}