package cn.com.datateller.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.datateller.model.BasicInformation;

public class ListViewHelper {

	public List<Map<String, Object>> getData(List<BasicInformation> basicKnowledgeList) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		for (int i = 0; i < basicKnowledgeList.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("img", basicKnowledgeList.get(i).getIcon());
			map.put("title", basicKnowledgeList.get(i).getTitle());
			map.put("id", basicKnowledgeList.get(i).getId());
			map.put("abstract", basicKnowledgeList.get(i).getAbstract());
			list.add(map);
		}
		System.out.println(list.size());
		return list;
	}

}
