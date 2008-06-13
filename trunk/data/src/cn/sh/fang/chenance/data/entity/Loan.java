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

@Table(name="t_account")
@Entity
public class Loan extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(name="interest")
    private Integer interest;

    @Column(name="interest_rate")
    private Integer interestRate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInterest() {
		return interest;
	}

	public void setInterest(Integer interest) {
		this.interest = interest;
	}
   
    public Integer getInterestRate() {
        return this.interestRate;
    }
    
    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

}