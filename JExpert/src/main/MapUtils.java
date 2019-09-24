package main;



import java.util.HashMap;
import java.util.Map;

// Program to Increment a Key's value of a Map in Java
class MapUtils
{
	public static<K> void incrementValue(Map<K,Integer> map, K key)
	{
		// get value of the specified key
		Integer count = map.get(key);
		
		// if the map contains no mapping for the key, then
		// map the key with value of 1
		if (count == null) {
			map.put(key, 1);
		}
		// else increment the found value by 1
		else {
			map.put(key, count + 1);
		}
	}

	public static void main(String[] args)
	{
		Map<String, Integer> map = new HashMap();
		map.put("A", 1);

		

		incrementValue(map, "A");
		
		
		
		System.out.println(map);
	}
}