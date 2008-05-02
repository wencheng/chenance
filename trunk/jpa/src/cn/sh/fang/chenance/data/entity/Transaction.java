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
    @Column(name="_date")
    private java.util.Date Date = new java.util.Date();

    /**  */
    @Column(name="debit")
    private Integer debit = 0;

    /**  */
    @Column(name="credit")
    private Integer credit = 0;

    /**  */
    @Column(name="is_repeat")
    private Boolean isRepeat;

    public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getDebit() {
        return this.debit;
    }
    
    public void setDebit(Integer debit) {
        this.debit = debit;
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