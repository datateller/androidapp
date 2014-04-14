package cn.com.datateller.model;

public class Weather {

	private String city;
	private String temperature;
	private String detailinfo;
	private String windstrong;
	private String wind;

	@Override
	public String toString() {
		return "Weather [city=" + city + ", temperature=" + temperature
				+ ", detailinfo=" + detailinfo + ", windstrong=" + windstrong
				+ ", wind=" + wind + "]";
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getDetailinfo() {
		return detailinfo;
	}

	public void setDetailinfo(String detailinfo) {
		this.detailinfo = detailinfo;
	}

	public String getWindstrong() {
		return windstrong;
	}

	public void setWindstrong(String windstrong) {
		this.windstrong = windstrong;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

}
