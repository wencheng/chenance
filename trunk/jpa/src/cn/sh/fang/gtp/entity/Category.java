package cn.sh.fang.gtp.entity;

import javax.persistence.*;

/**
 * 
 */
@Table(name="t_category")
@Entity
public class Category {

    /**  */
    @Id
    private Integer id;
    
    /**  */
    @Column(name="name")
    private String name;

    /**  */
    @Column(name="description")
    private String description;

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
}