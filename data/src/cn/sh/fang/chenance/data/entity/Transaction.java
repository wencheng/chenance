/*
 * Copyright 2008 Wencheng FANG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @Column(name="budget")
    private Integer budget;

    @Column(name="debit")
    private Integer debit = 0;

    @Column(name="credit")
    private Integer credit = 0;

    @Column(name="balance")
    private Integer balance = 0;

    @Column(name="is_confirmed")
    private Boolean isConfirmed;

    public java.util.Date getDate() {
        return this.Date;
    }
    
    public void setDate(java.util.Date Date) {
    	Date.setHours(0);
    	Date.setMinutes(0);
    	Date.setSeconds(0);
    	Date.setTime(Date.getTime()/1000*1000);
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
    
    public Boolean getIsConfirmed() {
        return this.isConfirmed;
    }
    
    public void setIsConfirmed(Boolean isApproved) {
        this.isConfirmed = isApproved;
    }

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

}