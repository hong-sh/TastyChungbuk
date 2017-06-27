package dto;

public class ReviewInfo {
	
	private int idx;
	private int store_idx;
	private int user_idx;
	private String user_name;
	private int user_age;
	private int user_sex;
	private String comment;
	private String comment_image;
	private int rate;
	private int sympathy;
	private String mod_date;
	
	public ReviewInfo()
	{
		
	}
	
	public ReviewInfo(int idx, int store_idx, int user_idx, String user_name, int user_age, int user_sex, String comment, int rate, int sympathy, String mod_date)
			
	{
		this.idx = idx;
		this.store_idx = store_idx;
		this.user_idx = user_idx;
		this.user_name = user_name;
		this.user_age = user_age;
		this.user_sex = user_sex;
		this.comment = comment;
		this.rate = rate;
		this.mod_date = mod_date;
		
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
		else if(key.equals("user_age"))
			this.user_age = Integer.parseInt(object);
		else if(key.equals("user_sex"))
			this.user_sex = Integer.parseInt(object);
		else if(key.equals("comment"))
			this.comment = object;
		else if(key.equals("rate"))
			this.rate = Integer.parseInt(object);
		else if(key.equals("sympathy"))
			this.sympathy = Integer.parseInt(object);
		else if(key.equals("mod_date"))
			this.mod_date = object;
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
	public String get_user_age()
	{
		String result = "";
		
		switch(user_age){
		case 0:
			result = "10대";
			break;
		case 1:
			result = "20대";
			break;
		case 2:
			result = "30대";
			break;
		case 3:
			result = "40대";
			break;
		case 4:
			result = "50대 이상";
			break;
		}
		
		return result;
		
	}
	public String get_user_sex()
	{
		String result = "";
		
		switch(user_sex){
		case 0:
			result = "남";
			break;
		case 1:
			result = "여";
			break;
		}
		
		return result;
		
	}
	public String get_comment()
	{
		return comment;
	}
	public float get_rate()
	{
		return (float)((float)rate/2.0);
	}
	public int get_sympathy()
	{
		return sympathy;
	}
	public String get_mod_date()
	{
		return mod_date;
	}

}
