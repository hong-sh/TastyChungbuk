package dto;

public class FavoriteInfo {

	private int ipx;
	private String name;
	private String type;
	private String address;
	private int review_count;
	private int rate;

	public FavoriteInfo() {

	}

	public FavoriteInfo(int ipx, String name, String type, String address,
			int review_count, int rate)

	{
		this.ipx = ipx;
		this.name = name;
		this.type = type;
		this.address = address;
		this.review_count = review_count;
		this.rate = rate;

	}

	public void put(String key, String object) {
		if (key.equals("ipx"))
			this.ipx = Integer.parseInt(object);
		else if (key.equals("name"))
			this.name = object;
		else if (key.equals("type"))
			this.type = object;
		else if (key.equals("address"))
			this.address = object;
		else if (key.equals("review_count"))
			this.review_count = Integer.parseInt(object);
		else if (key.equals("rate"))
			this.rate = Integer.parseInt(object);

	}

	public int get_ipx() {
		return ipx;
	}

	public String get_name() {
		return name;

	}

	public String get_type() {
		return type;
	}

	public String get_address() {
		return address;
	}

	public int get_review_count() {
		return review_count;
	}

	public int get_rate() {
		return rate;
	}

	

}
