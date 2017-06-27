package dto;

public class RateInfo {
	private int idx;
	private int store_idx;
	private int user_idx;
	private String user_name;
	private int rate;
	
	public RateInfo()
	{
		
	}
	
	public RateInfo(int idx, int store_idx, int user_idx, String user_name, int rate)
			
	{
		this.idx = idx;
		this.store_idx = store_idx;
		this.user_idx = user_idx;
		this.user_name = user_name;
		this.rate = rate;
		
	}
	
	public void put(String key, String object)
	{
		if(key.equals("idx"))
			this.idx = Integer.parseInt(object);
		else if(key.equals("store_idx"))
			this.store_idx = Integer.parseInt(object);
		else if(key.equals("user_idx"))
			this.user_idx = Integer.parseInt(object);
		else if(key.equals("user_name"))
			this.user_name = object;
		else if(key.equals("rate"))
			this.rate = Integer.parseInt(object);		
	}
	
	public int get_idx()
	{
		return idx;
	}
	public int get_store_idx()
	{
		return store_idx;
	}
	public int get_user_idx()
	{
		return user_idx;
	}
	public String get_user_name()
	{
		return user_name;
		
	}
	public int get_rate()
	{
		return rate;
	}
	

}
