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

@Table(name="t_investment")
@Entity
public class Investment extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Transaction.class)
    @JoinColumn(name="transaction_id", referencedColumnName="id")
    private Transaction transaction;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Investment.class)
    @JoinColumn(name="related_id", referencedColumnName="id")
    private Investment related;

    @Column(name="is_buy_or_sell")
    private Boolean isBuyOrSell;

    @Column(name="price")
    private Integer price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="amount")
    private Integer amount;

    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Boolean getIsBuyOrSell() {
        return this.isBuyOrSell;
    }
    
    public void setIsBuyOrSell(Boolean isBuyOrSell) {
        this.isBuyOrSell = isBuyOrSell;
    }
    
    public Transaction getTransaction() {
        return this.transaction;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public Investment getRelated() {
        return this.related;
    }
    
    public void setRelated(Investment related) {
        this.related = related;
    }
    
    public Integer getPrice() {
        return this.price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
}