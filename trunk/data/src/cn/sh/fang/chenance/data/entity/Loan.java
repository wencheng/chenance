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