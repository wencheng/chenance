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


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 5582998883084529012L;
	
	@Transient
	PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	@Column(name="INSERT_DATETIME", insertable=false, updatable=false)
	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertDatetime;

	@Column(name="UPDATE_DATETIME", insertable=false, updatable=false)
	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Version
	private Date updateDatetime;

	@Column(name="UPDATER")
	private String updater;

	@Column(name="IS_DELETED")
	private boolean isDeleted;

	public boolean isDeleted() {
		return this.isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getInsertDatetime() {
		return insertDatetime;
	}

	public void setInsertDatetime(Date insertDatetime) {
		this.insertDatetime = insertDatetime;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public String getUpdater() {
		return this.updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
 
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }
 
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
 
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }    

    public void firePropertyChange(String propertyName, Object old, Object newv) {
        changeSupport.firePropertyChange(propertyName, old, newv);
    }    

}
