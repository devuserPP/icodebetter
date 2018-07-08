package iwb.domain.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// Generated Feb 4, 2007 2:55:52 PM by Hibernate Tools 3.2.0.b9

/**
 * WQueryField generated by hbm2java
 */


@Entity
@Table(name="w5_query_field",schema="iwb")
public class W5QueryFieldCreation implements java.io.Serializable {

	private int queryFieldId;
		
	private int queryId;
	private int mainTableFieldId;

	private String dsc;

	private short fieldTip;

	private short tabOrder;
	
	private short versionNo;
	private	int insertUserId;
	private	int versionUserId;
	private	java.sql.Timestamp versionDttm;
	
	private short postProcessTip;
	private String projectUuid;
	private int customizationId;
	
	@Column(name="version_no")
	public short getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(short versionNo) {
		this.versionNo = versionNo;
	}

	@Column(name="insert_user_id")
	public int getInsertUserId() {
		return insertUserId;
	}

	public void setInsertUserId(int insertUserId) {
		this.insertUserId = insertUserId;
	}

	@Column(name="version_user_id")
	public int getVersionUserId() {
		return versionUserId;
	}

	public void setVersionUserId(int versionUserId) {
		this.versionUserId = versionUserId;
	}

	@Column(name="version_dttm")
	public java.sql.Timestamp getVersionDttm() {
		return versionDttm;
	}

	public void setVersionDttm(java.sql.Timestamp versionDttm) {
		this.versionDttm = versionDttm;
	}

	public W5QueryFieldCreation() {
	}
	@Id
	@Column(name="query_field_id")
	public int getQueryFieldId() {
		return queryFieldId;
	}

	@Column(name="query_id")
	public int getQueryId() {
		return queryId;
	}

	@Column(name="dsc")
	public String getDsc() {
		return dsc;
	}

	@Column(name="field_tip")
	public short getFieldTip() {
		return fieldTip;
	}

	@Column(name="tab_order")
	public short getTabOrder() {
		return tabOrder;
	}


	public void setQueryFieldId(int queryFieldId) {
		this.queryFieldId = queryFieldId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}

	public void setFieldTip(short fieldTip) {
		this.fieldTip = fieldTip;
	}

	public void setTabOrder(short tabOrder) {
		this.tabOrder = tabOrder;
	}
	@Column(name="post_process_tip")
	public short getPostProcessTip() {
		return postProcessTip;
	}

	public void setPostProcessTip(short postProcessTip) {
		this.postProcessTip = postProcessTip;
	}

	@Column(name="main_table_field_id")
	public int getMainTableFieldId() {
		return mainTableFieldId;
	}

	public void setMainTableFieldId(int mainTableFieldId) {
		this.mainTableFieldId = mainTableFieldId;
	}
	@Column(name="project_uuid")
	public String getProjectUuid() {
		return projectUuid;
		}

	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;

	}

	@Column(name="customization_id")
	public int getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(int customizationId) {
		this.customizationId = customizationId;
	}
	
}