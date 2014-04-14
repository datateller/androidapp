package cn.com.datateller.util;

public enum SexEnum {

	BOY("ÄÐ", 1), GIRL("Å®", 2);

	private String name;
	private int index;

	private SexEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static String getNameByIndex(int index){
		for (SexEnum s : SexEnum.values()) {
			if(s.getIndex()==index)
				return s.name;
		}
		return null;
	}
	
    public static int getIndexByName(String name){
    	for (SexEnum s : SexEnum.values()) {
			if(s.getName()==name)
				return s.index;
		}
    	return 0;
    }
	
}
