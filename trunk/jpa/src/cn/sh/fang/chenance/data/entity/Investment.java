package cn.sh.fang.chenance.data.entity;

import javax.persistence.*;

/**
 * 
 */
@Table(name="t_investment")
@Entity
public class Investment extends BaseEntity {

    /**  */
    @Id
    private Integer id;
    
    /**  */
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Account.class)
    @JoinColumn(name="account_id", referencedColumnName="id")
    private Account account;

    /**  */
    @Column(name="is_buy_or_sell")
    private Boolean isBuyOrSell;

    /**  */
    @Column(name="price")
    private Integer price;

    /**  */
    @Column(name="quantity")
    private Integer quantity;

    /**  */
    @Column(name="amount")
    private Integer amount;

    public Integer getPrice() {
        return this.price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }
    
    public Boolean getIsBuyOrSell() {
        return this.isBuyOrSell;
    }
    
    public void setIsBuyOrSell(Boolean isBuyOrSell) {
        this.isBuyOrSell = isBuyOrSell;
    }
    
    public Integer getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
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
    
}