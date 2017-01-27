package fi.aalto.drumbeat;

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


public class ConversionOptions {
	String separator = ";";
	String inputFile;
	String resultFile;

	String subject_prefix = "http://drumbeat.cs.hut.fi/drumbeat/objects/";
	String predicate_uri =  "http://drumbeat.cs.hut.fi/link";
	String object_prefix = " http://drumbeat.cs.hut.fi/drumbeat/objects/";

	
	public ConversionOptions() {
		
	}
	
	public ConversionOptions(ConversionOptions co) {
		this.separator = co.separator;
		this.inputFile = co.inputFile;
		this.resultFile = co.resultFile;
		this.subject_prefix = co.subject_prefix;
		this.predicate_uri = co.predicate_uri;
		this.object_prefix = co.object_prefix;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getResultFile() {
		return resultFile;
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}

	public String getSubject_prefix() {
		return subject_prefix;
	}

	public void setSubject_prefix(String subject_prefix) {
		this.subject_prefix = subject_prefix;
	}

	public String getPredicate_uri() {
		return predicate_uri;
	}

	public void setPredicate_uri(String predicate_uri) {
		this.predicate_uri = predicate_uri;
	}

	public String getObject_prefix() {
		return object_prefix;
	}

	public void setObject_prefix(String object_prefix) {
		this.object_prefix = object_prefix;
	}
	
	public boolean isValid() {
		return true;
	}


}
