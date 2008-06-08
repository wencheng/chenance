package cn.sh.fang.chenance.data.entity;

import javax.persistence.*;

@Table(name="t_transaction")
@Entity
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Category.class)
    @JoinColumn(name="category_id", referencedColumnName="id")
    private Category category;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Account.class)
    @JoinColumn(name="account_id", referencedColumnName="id")
    private Account account;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=RepeatPayment.class)
    @JoinColumn(name="repeat_payment_id", referencedColumnName="id")
    private RepeatPayment repeatPayment;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Transaction.class)
    @JoinColumn(name="from_or_to", referencedColumnName="id")
    private Transaction fromOrTo;

    @Column(name="_date")
    private java.util.Date Date;

    @Column(name="debit")
    private Integer debit;

    @Column(name="credit")
    private Integer credit;

    @Column(name="is_approved")
    private Boolean isApproved;

    public java.util.Date getDate() {
        return this.Date;
    }
    
    public void setDate(java.util.Date Date) {
        this.Date = Date;
    }
    
    public Integer getDebit() {
        return this.debit;
    }
    
    public void setDebit(Integer debit) {
        this.debit = debit;
    }
    
    public RepeatPayment getRepeatPayment() {
        return this.repeatPayment;
    }
    
    public void setRepeatPayment(RepeatPayment repeatPayment) {
        this.repeatPayment = repeatPayment;
    }
    
    public Integer getCredit() {
        return this.credit;
    }
    
    public void setCredit(Integer credit) {
        this.credit = credit;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Transaction getFromOrTo() {
        return this.fromOrTo;
    }
    
    public void setFromOrTo(Transaction fromOrTo) {
        this.fromOrTo = fromOrTo;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    
    public Boolean getIsApproved() {
        return this.isApproved;
    }
    
    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
    
}