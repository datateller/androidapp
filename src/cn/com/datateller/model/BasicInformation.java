package cn.com.datateller.model;

import java.io.Serializable;

public class BasicInformation implements Serializable {

	private static final long serialVersionUID = -3932291561013966095L;
	private int id;
	private String link;
	private String title;
	private String icon;
	private String pic;
	private String Abstract;
	private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getAbstract() {
		return Abstract;
	}

	public void setAbstract(String abstract1) {
		Abstract = abstract1;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		BasicInformation other = (BasicInformation) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServerBasicInfor [id=" + id + ", link=" + link + ", title="
				+ title + ", icon=" + icon + ", pic=" + pic + ", Abstract="
				+ Abstract + ", address=" + address + "]";
	}
	
	
}
