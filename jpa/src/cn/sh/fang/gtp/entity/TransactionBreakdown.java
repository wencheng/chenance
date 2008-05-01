package cn.sh.fang.gtp.entity;

import javax.persistence.*;

/**
 * 
 */
@Table(name="t_transaction_breakdown")
@Entity
public class TransactionBreakdown {

    /**  */
    @Id
    private Integer id;
    
    /**  */
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Transaction.class)
    @JoinColumn(name="transaction_id", referencedColumnName="id")
    private Transaction transaction;

    public Transaction getTransaction() {
        return this.transaction;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
}