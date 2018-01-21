package my.apps.vagoster.rssfeaderv;

import java.util.ArrayList;

public class RSSItem 
{
	private String _title = "";
	private String _description = "";
	private String _link = null;
	private String _category = null;
	private String _pubdate = null;
	private String _image=null;

	
	RSSItem()
	{
	}
	void setTitle(String title)
	{
		_title = title;
	}
	void setDescription(String description)
	{
		_description = description;
	}
	void setLink(String link)
	{
		_link = link;
	}
	void setCategory(String category)
	{
		_category = category;
	}
	void setPubDate(String pubdate)
	{
		_pubdate = pubdate;
	}
	String getTitle()
	{
		return _title;
	}
	String getDescription()
	{
		return _description;
	}
	String getLink()
	{
		return _link;
	}
	String getCategory()
	{
		return _category;
	}
	String getPubDate()
	{
		return _pubdate;
	}
	
	public String toString()
	{
		// limit how much text we display
		/*if (_title.length() > 200)
		{
			return _title.substring(0, 200) + "...";
		}*/
		return _title;
	}
	void addToTheTitle(String fraction)
	{
		_title += fraction;
	}
	public void addToTheDesc(String fraction) 
	{
		_description += fraction;
	}
	public String getImage() {
		return _image;
	}
	public void setImage(String _image) {
		this._image = _image;
	}
	
}
