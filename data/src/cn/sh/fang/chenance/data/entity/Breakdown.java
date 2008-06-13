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

@Table(name="t_receipt_item")
@Entity
public class Breakdown extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Transaction.class)
    @JoinColumn(name="transaction_id", referencedColumnName="id")
    private Transaction transaction;

    @Column(name="item_name")
    private String itemName;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Category.class)
    @JoinColumn(name="category_id", referencedColumnName="id")
    private Category category;

    @Column(name="price")
    private Integer price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="amount")
    private Integer amount;

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getItemName() {
        return this.itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Integer getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Integer getPrice() {
        return this.price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }
    
    public Transaction getTransaction() {
        return this.transaction;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
    
}