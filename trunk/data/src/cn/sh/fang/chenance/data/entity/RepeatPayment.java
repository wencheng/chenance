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

@Table(name="t_repeat_payment")
@Entity
public class RepeatPayment extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Category.class)
    @JoinColumn(name="category_id", referencedColumnName="id")
    private Category category;

    @Column(name="amount")
    private Integer amount;

    @Column(name="period")
    private Integer period;

    @Column(name="period_unit")
    private Integer periodUnit;

    @Column(name="auto_confirm")
    private Boolean autoConfirm;

    public Boolean getAutoConfirm() {
        return this.autoConfirm;
    }
    
    public void setAutoConfirm(Boolean autoApprove) {
        this.autoConfirm = autoApprove;
    }
    
    public Integer getPeriodUnit() {
        return this.periodUnit;
    }
    
    public void setPeriodUnit(Integer periodUnit) {
        this.periodUnit = periodUnit;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Integer getPeriod() {
        return this.period;
    }
    
    public void setPeriod(Integer period) {
        this.period = period;
    }
    
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
}