package cn.sh.fang.chenance.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    private Integer id;
    
    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;
    
    @ManyToOne
    @JoinColumn(name="parent_id")
    Category parent;

    @OneToMany(mappedBy="parent")
    @JoinColumn(name="id")
    @OrderBy("id")
    List<Category> children;

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

    public List<Category> getChildren() {
        return this.children;
    }

}