package fi.aalto.drumbeat;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import guidcompressor.GuidCompressor;
/*
* 
Jyrki Oraskari, Aalto University, 2016 

This research has partly been carried out at Aalto University in DRUMBEAT 
“Web-Enabled Construction Lifecycle” (2014-2017) —funded by Tekes, 
Aalto University, and the participating companies.

The MIT License (MIT)
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
public class LinkBean {
	@CsvBindByName(column = "Component",required=false)
	private String component;
	@CsvBindByName(column = "GUID",required=false)
	private String guid;
	@CsvBindByName(column = "Nearest Spaces",required=false)
	private String nearest_space;
	@CsvBindByName(column = "GUID (Nearest Spaces)",required=false)
	
	private String nearest_space_guid;

	public String getComponent() {
		if(component==null)
			return "";
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		if(guid==null || guid.length()==0)
			this.guid=null;
		else
		    this.guid = GuidCompressor.uncompressGuidString(guid).toUpperCase();
	}

	public String getNearest_space() {
		return nearest_space;
	}

	public void setNearest_space(String nearest_space) {
		this.nearest_space = nearest_space;
	}

	public String getNearest_space_guid() {
		return nearest_space_guid;
	}

	public void setNearest_space_guid(String nearest_space_guid) {
		if(nearest_space_guid==null || nearest_space_guid.length()==0)
			this.nearest_space_guid=null;
		else
		    this.nearest_space_guid = GuidCompressor.uncompressGuidString(nearest_space_guid).toUpperCase();
	}

	@Override
	public String toString() {
		return "LinkBean [component=" + component + ", guid=" + guid + ", nearest_space=" + nearest_space
				+ ", nearest_space_guid=" + nearest_space_guid + "]";
	}

}
