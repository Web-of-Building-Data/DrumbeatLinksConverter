package fi.aalto.drumbeat;

import java.util.HashSet;
import java.util.Set;

/*
* 
Jyrki Oraskari, Aalto University, 2017 

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


public class ColumnNamesModeConversionOptions extends ConversionOptions{
	
	String subjectElementGUID_columnName = "GUID";
	String correspondingObjectGUID_columnName = "";

	public ColumnNamesModeConversionOptions(ConversionOptions co) {
		super(co);
	}

	public String getSubjectElementGUID_columnName() {
		return subjectElementGUID_columnName;
	}

	public void setSubjectElementGUID_columnName(String subjectElementGUID_columnName) {
		this.subjectElementGUID_columnName = subjectElementGUID_columnName;
	}


	public String getCorrespondingObjectGUID_columnName() {
		return correspondingObjectGUID_columnName;
	}

	public void setCorrespondingObjectGUID_columnName(String correspondingObjectGUID_columnName) {
		this.correspondingObjectGUID_columnName = correspondingObjectGUID_columnName;
	}
	
	
	// test that the column numbers does not share the same value
	@Override
	public boolean isValid() {
		boolean ret = true;
		Set<String> values = new HashSet<String>();
		if (!values.add(this.correspondingObjectGUID_columnName))
			ret=false;
		if (!values.add(this.subjectElementGUID_columnName))
			ret=false;
		return ret;
	}
	
}
