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


public class ColumnNumbersModeConversionOptions extends ConversionOptions {
	private Integer subjectGUID_columnNumber = 1;
	private Integer correspondingObjectGUID_columnNumber = 3;

	public ColumnNumbersModeConversionOptions(ConversionOptions co) {
		super(co);

	}

	public int getSubjectGUID_columnNumber() {
		return subjectGUID_columnNumber;
	}

	public void setSubjectGUID_columnNumber(int subjectGUID_columnNumber) {
		this.subjectGUID_columnNumber = subjectGUID_columnNumber;
	}

	public void setSubjectGUID_columnNumber(String num) {
		setSubjectGUID_columnNumber(toInt(num));
	}

	public int getCorrespondingObjectGUID_columnNumber() {
		return correspondingObjectGUID_columnNumber;
	}

	public void setCorrespondingObjectGUID_columnNumber(int correspondingObjectGUID_columnNumber) {
		this.correspondingObjectGUID_columnNumber = correspondingObjectGUID_columnNumber;
	}

	public void setCorrespondingObjectGUID_columnNumber(String num) {
		setCorrespondingObjectGUID_columnNumber(toInt(num));
	}

	private int toInt(String num) {
		int ret = 0;
		try {
			ret = Integer.parseInt(num.trim());

		} catch (NumberFormatException e) {
			System.err.println("Column number was not in a suitable format. Example: 1");
		}
		return ret;
	}

	// test that the column numbers does not share the same value
	@Override
	public boolean isValid() {
		boolean ret = true;
		Set<Integer> values = new HashSet<Integer>();
		if (!values.add(this.correspondingObjectGUID_columnNumber))
			ret=false;
		if (!values.add(this.subjectGUID_columnNumber))
			ret=false;
		return ret;
	}
}
