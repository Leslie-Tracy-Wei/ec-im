package ec.im.webServer.base.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 基础实体类
 */
public abstract class BaseModel<PK> implements Serializable {
    private static final long serialVersionUID = 2539516018935036745L;

    /**
     * 主键id
     */
    protected PK id;

    /**
     * 链路跟踪id
     */
    protected String traceId;

    /**
     * 创建人
     */
    protected String createdBy;

    /**
     * 创建时间
     */
    protected Timestamp creationDate;

    /**
     * 更新人
     */
    protected String updatedBy;

    /**
     * 更新时间
     */
    protected Timestamp updationDate;

    /**
     * 是否可用 0-逻辑删除 1-有效
     */
    protected Long enabledFlag = 1L;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(Timestamp updationDate) {
        this.updationDate = updationDate;
    }

    public Long getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Long enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (getId() instanceof Comparable) {
            result = getId().hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof BaseModel)) {
            return false;
        }

        BaseModel other = (BaseModel) o;
        if (getId() != null && other.getId() != null) {
            if (getId() instanceof Comparable) {
                return ((Comparable) getId()).compareTo(other.getId()) == 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
