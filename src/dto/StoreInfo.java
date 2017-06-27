package dto;

public class StoreInfo {
	
	private int ipx;
	private String name;
	private String ceo;
	private String type;
	private String tel;
	private String tel1;
	private String tel2;
	private String tel3;
	private String detail;
	private String address;
	private int review_count;
	private float rate;
	private String lat;
	private String lng;
	private double distance;
	private int rate_count;
	private int favorite_count;
	
	public StoreInfo()
	{
		
	}
	
	public StoreInfo(int ipx, String name, String ceo, String type, String tel , String tel1, String tel2, String tel3,
			String detail, String address, int review_count, float rate, String lat, String lng, double distance,
			int rate_count, int favorite_count)
			
	{
		this.ipx = ipx;
		this.name = name;
		this.ceo = ceo;
		this.type = type;
		this.tel = tel;
		this.tel1 = tel1;
		this.tel2 = tel2;
		this.tel3 = tel3;
		this.detail = detail;
		this.address = address;
		this.review_count = review_count;
		this.rate = rate;
		this.lat = lat;
		this.lng = lng;
		this.distance = distance;
		this.rate_count = rate_count;
		this.favorite_count = favorite_count;
		
	}
	
	public void put(String key, String object)
	{
		if(key.equals("ipx"))
			this.ipx = Integer.parseInt(object);
		else if(key.equals("name"))
			this.name = object;
		else if(key.equals("ceo"))
			this.ceo = object;
		else if(key.equals("type"))
			this.type = object;
		else if(key.equals("tel"))
			this.tel = object;
		else if(key.equals("detail"))
			this.detail = object;
		else if(key.equals("address"))
			this.address = object;
		else if(key.equals("review_count"))
			this.review_count = Integer.parseInt(object);
		else if(key.equals("rate"))
			this.rate = Float.parseFloat(object);
		else if(key.equals("lat"))
			this.lat = object;
		else if(key.equals("lng"))
			this.lng = object;
		else if (key.equals("distance"))
			this.distance = Double.parseDouble(object);
		else if(key.equals("rate_count"))
			this.rate_count = Integer.parseInt(object);
		else if(key.equals("favorite_count"))
			this.favorite_count = Integer.parseInt(object);
		
	}
	
	public int get_ipx()
	{
		return ipx;
	}
	public String get_name()
	{
		return name;
		
	}
	public String get_ceo()
	{
		return ceo;
	}
	public String get_type()
	{
		return type;
	}
	public String get_tel()
	{
		return tel;
	}
	public String get_detail()
	{
		return detail;
	}
	public String get_address()
	{
		return address;
	}
	public int get_review_count()
	{
		return review_count;
	}
	public float get_rate()
	{
		return rate;
	}
	public String get_lat()
	{
		return lat;
	}
	public String get_lng()
	{
		return lng;
	}
	public String get_distance()
	{
		return String.format("%.2f" , distance)+"km";
	}
	public String get_real_distance()
	{
		return String.valueOf(distance);
	}
	public int get_rate_count()
	{
		return rate_count;
	}
	public int get_favorite_count()
	{
		return favorite_count;
	}
}
