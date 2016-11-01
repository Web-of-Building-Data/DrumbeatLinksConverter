package fi.aalto.drumbeat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

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


public class LinksGuidTypeConverter {

	private List<LinkVO> linkList = new ArrayList<LinkVO>();

	public LinksGuidTypeConverter(String csv_filename) {
		// TODO test if the file is commma separated and not ; separated

		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csv_filename), ';');
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (reader == null)
			return;
		//ColumnnPositionMappingStrategy<LinkBean> strategy = new ColumnPositionMappingStrategy<>();
		//strategy.setType(LinkBean.class);
		 HeaderColumnNameMappingStrategy<LinkBean> strategy = new HeaderColumnNameMappingStrategy<>();
         strategy.setType(LinkBean.class);
		try {
			CsvToBean<LinkBean> csvToBean = new CsvToBean<>();
			List<LinkBean> beanList = csvToBean.parse(strategy, reader);
			LinkBean head = null;

			for (LinkBean l : beanList) {
				System.out.println("Bean :"+l);
				if (l.getGuid() != null)
					head = l;
				else if (head != null)
					linkList.add(new LinkVO(head, l));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void write2CSV(String file_name) {
		CSVWriter csvWriter = null;
		try {
			@SuppressWarnings("rawtypes")
			BeanToCsv bc = new BeanToCsv();
			// Create CSVWriter for writing to Employee.csv
			csvWriter = new CSVWriter(new FileWriter(file_name), ';');
			@SuppressWarnings("rawtypes")
			ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
			mappingStrategy.setType(LinkVO.class);
			String[] columns = new String[] { "Component", "Guid", "nearest_space", "nearest_space_guid" };
			mappingStrategy.setColumnMapping(columns);
			bc.write(mappingStrategy, csvWriter, linkList);
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			try {
				// closing the writer
				csvWriter.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}

	}

	public List<LinkVO> getLinkList() {
		return linkList;
	}

	public static void main(String[] args) {
		new LinksGuidTypeConverter(args[1]);

	}

}
