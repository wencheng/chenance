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
	
	@Override
	public String toString() {
		return getName();
	}

	public boolean isRoot() {
		return getCode() % 1000000 == 0;
	}

	public String getDisplayName() {
		if ( parent != null && parent.isRoot() == false ) {
			return parent.getDisplayName() + " : " + name; 
		} else {
			return name;
		}
	}
}