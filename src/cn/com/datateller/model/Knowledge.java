package cn.com.datateller.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Knowledge implements Serializable {

	private static final long serialVersionUID = -2557747238919380284L;
	private ArrayList<String> tags; //
	private Integer knowledgeId; // `
	private String knowledgeContent; //
	private String knowledgeTitle; //
	private String knowledgePicLink; //
	private ArrayList<Commerical> commericals;
	private String icon;
	private String Abstract;

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public Integer getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(Integer knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getKnowledgeContent() {
		return knowledgeContent;
	}

	public void setKnowledgeContent(String knowledgeContent) {
		this.knowledgeContent = knowledgeContent;
	}

	public String getKnowledgeTitle() {
		return knowledgeTitle;
	}

	public void setKnowledgeTitle(String knowledgeTitle) {
		this.knowledgeTitle = knowledgeTitle;
	}

	public String getKnowledgePicLink() {
		return knowledgePicLink;
	}

	public void setKnowledgePicLink(String knowledgePicLink) {
		this.knowledgePicLink = knowledgePicLink;
	}

	public ArrayList<Commerical> getCommericals() {
		return commericals;
	}

	public void setCommericals(ArrayList<Commerical> commericals) {
		this.commericals = commericals;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAbstract() {
		return Abstract;
	}

	public void setAbstract(String abstract1) {
		Abstract = abstract1;
	}

	@Override
	public String toString() {
		return "Knowledge [tags=" + tags + ", knowledgeId=" + knowledgeId
				+ ", knowledgeContent=" + knowledgeContent
				+ ", knowledgeTitle=" + knowledgeTitle + ", knowledgePicLink="
				+ knowledgePicLink + ", commericals=" + commericals + ", icon="
				+ icon + ", Abstract=" + Abstract + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((knowledgeId == null) ? 0 : knowledgeId.hashCode());
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
		Knowledge other = (Knowledge) obj;
		if (knowledgeId == null) {
			if (other.knowledgeId != null)
				return false;
		} else if (!knowledgeId.equals(other.knowledgeId))
			return false;
		return true;
	}

}
