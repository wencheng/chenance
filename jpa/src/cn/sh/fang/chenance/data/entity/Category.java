package cn.sh.fang.chenance.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Table(name="t_category")
@Entity
public class Category extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(name="code")
	private Integer code;

	@Column(name="name")
    private String name;

    @Column(name="description")
    private String description;
    
    @ManyToOne
    @JoinColumn(name="parent_id")
    Category parent;

    @OneToMany(mappedBy="parent")
    @OrderBy("id")
    List<Category> children = new ArrayList<Category>();;

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChildren() {
        return this.children;
    }

	public void setChildren(List<Category> cats) {
		this.children = cats;
	}

	public void appendChild(Category c) {
		this.children.add(c);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}