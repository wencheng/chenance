package cn.sh.fang.chenance.data.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 5582998883084529012L;

	@Column(name="INSERT_DATETIME", updatable=false)
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
}
