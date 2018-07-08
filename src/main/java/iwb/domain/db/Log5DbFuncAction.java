package iwb.domain.db;

// Generated Feb 4, 2007 3:49:13 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import iwb.domain.result.W5DbFuncResult;

/**
 * LogUserAction generated by hbm2java
 */
@Entity
@Table(name="log5_db_func_action")
public class Log5DbFuncAction implements java.io.Serializable {

	private int logId;

	private int userId;
	
	private int dbFuncId;

	private String dsc;
	
	private int processTime;
	private long startTime;

	@Column(name="dsc")
	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}



	public Log5DbFuncAction() {
	}

	public Log5DbFuncAction(W5DbFuncResult r) {
		this.dbFuncId = r.getDbFuncId();
		if(r.getScd()!=null && r.getScd().get("userId")!=null)this.userId = (Integer)r.getScd().get("userId");
		this.startTime=System.currentTimeMillis();
		this.processTime = -1;
	}

    @SequenceGenerator(name="sex_log_db_func_action",sequenceName="iwb.seq_log_db_func_action",allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sex_log_db_func_action")
	@Column(name="log_id")
	public int getLogId() {
		return this.logId;
	}

	@Column(name="db_func_id")
	public int getDbFuncId() {
		return dbFuncId;
	}

	public void setDbFuncId(int dbFuncId) {
		this.dbFuncId = dbFuncId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	@Column(name="user_id")
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name="process_time")
	public int getProcessTime() {
		return this.processTime;
	}

	public void setProcessTime(int processTime) {
		this.processTime = processTime;
	}

	public void calcProcessTime() {
		this.processTime = (int)(System.currentTimeMillis() - this.startTime);
	}

	@Transient
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}