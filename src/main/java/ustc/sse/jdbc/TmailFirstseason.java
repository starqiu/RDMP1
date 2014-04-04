package ustc.sse.jdbc;

import java.util.Date;

/**
 * TmailFirstseason entity. @author MyEclipse Persistence Tools
 */

public class TmailFirstseason implements java.io.Serializable {

	// Fields

	/** */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long userId;
	private Long brandId;
	private Integer type;
	private Date visitDatetime;

	// Constructors

	/** default constructor */
	public TmailFirstseason() {
	}

	/** full constructor */
	public TmailFirstseason(Long userId, Long brandId, Integer type,
			Date visitDatetime) {
		this.userId = userId;
		this.brandId = brandId;
		this.type = type;
		this.visitDatetime = visitDatetime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getVisitDatetime() {
		return this.visitDatetime;
	}

	public void setVisitDatetime(Date visitDatetime) {
		this.visitDatetime = visitDatetime;
	}

}