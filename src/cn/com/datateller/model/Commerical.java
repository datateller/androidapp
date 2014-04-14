package cn.com.datateller.model;

import java.io.Serializable;

public class Commerical implements Serializable {

	private static final long serialVersionUID = 4288682310842345905L;
	private Integer commericalId;
	private Integer knowledgeId;
	private String commericalTitle;
	private String commericalLink;

	public Integer getCommericalId() {
		return commericalId;
	}

	public void setCommericalId(Integer commericalId) {
		this.commericalId = commericalId;
	}

	public Integer getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(Integer knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getCommericalTitle() {
		return commericalTitle;
	}

	public void setCommericalTitle(String commericalTitle) {
		this.commericalTitle = commericalTitle;
	}

	public String getCommericalLink() {
		return commericalLink;
	}

	public void setCommericalLink(String commericalLink) {
		this.commericalLink = commericalLink;
	}

	@Override
	public String toString() {
		return "Commerical [commericalId=" + commericalId + ", knowledgeId="
				+ knowledgeId + ", commericalTitle=" + commericalTitle
				+ ", commericalLink=" + commericalLink + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commericalId == null) ? 0 : commericalId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commerical other = (Commerical) obj;
		if (commericalId == null) {
			if (other.commericalId != null)
				return false;
		} else if (!commericalId.equals(other.commericalId))
			return false;
		return true;
	}

}
