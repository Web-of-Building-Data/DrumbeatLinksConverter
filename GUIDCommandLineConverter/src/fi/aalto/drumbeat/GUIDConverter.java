package fi.aalto.drumbeat;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

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

public class GUIDConverter {

	private Options options = new Options();
	private String defaultAnswer = null;

	public GUIDConverter(final String[] args) {
		final CommandLineParser parser = new DefaultParser();
		initializeOptions();

		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
			if (commandLine.hasOption("y"))
				defaultAnswer = "y";

			if (commandLine.hasOption("n"))
				defaultAnswer = "n";

			if (commandLine.hasOption("h"))
				help();
			else
				executeCommandLine(commandLine);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void initializeOptions() {
		options.addOption("y", "yes", false, "\"Yes\" as a default answer for user inputs.");
		options.addOption("n", "no", false, "\"no\" as a default answer for user inputs.");
		options.addOption("h", "help", false, "Instructions");
		options.addOption("i", "inputFile", true, "The CSV file that is to be converted.");
		options.addOption("r", "resultFile", false, "The result of the conversation in Turtle format.");
		options.addOption("c", "CSVSeparator", false, "The separator characted used in the CSV file.");
		options.addOption("s", "subject_prefix", false, "The subject prefix for the RDF triples.");
		options.addOption("p", "predicate_uri", false, "The predicate uri of the links.");
		options.addOption("o", "object_prefix", false, "The object prefix for the RDF triples.");
		options.addOption("cns", "subjectElementGUID_columnName", false, "The subject element GUID column name");
		options.addOption("cno", "correspondingObjectGUID_columnName", false,
				"The corresponding object GUID column name");

		options.addOption("cis", "subjectGUID_columnNumber", false, "The subject element GUID column number");
		options.addOption("cio", "correspondingObjectGUID_columnNumber", false,
				"The corresponding object GUID column number");
	}

	final static int STATE_START = 0;
	final static int STATE_INPUTFILE = 1;
	final static int STATE_OUTPUTFILE = 2;
	final static int STATE_FILESOK = 3;
	final static int STATE_COLNAMES = 4;
	final static int STATE_COLNUMBERS = 5;
	ConversionOptions conversion_options = new ConversionOptions();

	private int state = STATE_START;

	File fi = null;

	private boolean stateMachine(CommandLine commandLine) {

		switch (state) {
		case STATE_START:
			System.out.println("GUIDConverter:");

			final String separator = getOption('c', commandLine);
			if (separator != StringUtils.EMPTY) {
				if (separator.length() > 0)
					conversion_options.setSeparator(separator.charAt(0));
			}
			System.out.println(" CSV column separator: " + conversion_options.getSeparator());

			final String subject_prefix = getOption('s', commandLine);
			if (separator != StringUtils.EMPTY)
				conversion_options.setSubject_prefix(subject_prefix);
			System.out.println(" Subject prefix: " + conversion_options.getSubject_prefix());

			final String predicate_uri = getOption('p', commandLine);
			if (separator != StringUtils.EMPTY)
				conversion_options.setPredicate_uri(predicate_uri);
			System.out.println(" Predicate URI: " + conversion_options.getPredicate_uri());

			final String object_prefix = getOption('o', commandLine);
			if (separator != StringUtils.EMPTY)
				conversion_options.setObject_prefix(object_prefix);
			System.out.println(" Object prefix: " + conversion_options.getObject_prefix());
			state = STATE_INPUTFILE;
			break;
		case STATE_INPUTFILE:
			final String inputFile = getOption('i', commandLine);
			if (inputFile == StringUtils.EMPTY) {
				System.err.println(" -i inputfile is missing.");
				System.err.println(" Use -h for instructions.");
				return true;
			}
			conversion_options.setInputFile(inputFile);
			fi = new File(inputFile);
			if (!fi.exists() || fi.isDirectory()) {
				System.err.println(" The inputfile is missing or there is no suitable input file");
				System.err.println(" Use -h for instructions.");
				return true;
			} else
				state = STATE_OUTPUTFILE;
			break;
		case STATE_OUTPUTFILE:
			System.out.println(" Input file is: " + conversion_options.getInputFile());
			String resultFile = getOption('r', commandLine);
			if (resultFile == StringUtils.EMPTY) {
				int index = fi.getAbsolutePath().lastIndexOf("."); // Oletus:
																	// voi olla
																	// monta
																	// pistettä.
																	// Viimeinen
				String fi_start = fi.getAbsolutePath().substring(0, index);
				resultFile = fi_start + ".ttl";
				System.out.println(" Result file is: " + resultFile);
			}
			conversion_options.setResultFile(resultFile);
			File fr = new File(resultFile);
			if (fr.exists()) {
				System.out.println("The named result file exists!");
				System.out.println("Shall we overwrite it?  Y/n");
				String input = "";
				if (defaultAnswer != null)
					input = defaultAnswer;
				else {
					input = (new Scanner(System.in)).nextLine();
				}

				input = input.trim().toLowerCase();
				if (input.length() == 0)
					input = "y";
				System.out.println("You entered :" + input);
				if (input.trim().toLowerCase().charAt(0) != 'y') {
					return true;
				}
			}
			if (fr.isDirectory()) {
				System.err.println(" The named result file is an existing directory");
				System.err.println(" Use -h for instructions.");
				return true;
			}

			state = STATE_FILESOK;
			break;
		case STATE_FILESOK:
			state = STATE_COLNUMBERS;
			if (commandLine.hasOption("cns")) {
				state = STATE_COLNAMES;
			}
			if (commandLine.hasOption("cno")) {
				state = STATE_COLNAMES;
			}
			break;

		case STATE_COLNAMES:
			conversion_options = new ColumnNamesModeConversionOptions(conversion_options);
			System.out.println("Column names are used.");
			ColumnNamesModeConversionOptions cona = (ColumnNamesModeConversionOptions) conversion_options;
			final String subjectElementGUID_columnName = getOption("cns", commandLine);
			final String correspondingObjectGUID_columnName = getOption("cno", commandLine);
			System.out.println("subjectElementGUID_columnName:"+subjectElementGUID_columnName);
			//TODO test this
			if (subjectElementGUID_columnName != StringUtils.EMPTY)
				cona.setSubjectElementGUID_columnName(subjectElementGUID_columnName);
			if (correspondingObjectGUID_columnName != StringUtils.EMPTY)
				cona.setCorrespondingObjectGUID_columnName(correspondingObjectGUID_columnName);
			if (!cona.isValid()) {
				System.out.println("Invalid column selection: Each value should be unique,");
				return true;
			}

			convert(cona);
			return true;

		case STATE_COLNUMBERS:
			conversion_options = new ColumnNumbersModeConversionOptions(conversion_options);
			System.out.println("Column numbers are used.");
			ColumnNumbersModeConversionOptions conu = (ColumnNumbersModeConversionOptions) conversion_options;

			final String subjectGUID_columnNumber = getOption("cis", commandLine);
			final String correspondingObjectGUID_columnNumber = getOption("cio", commandLine);
			if (subjectGUID_columnNumber != StringUtils.EMPTY)
				conu.setSubjectGUID_columnNumber(subjectGUID_columnNumber);
			if (correspondingObjectGUID_columnNumber != StringUtils.EMPTY)
				conu.setCorrespondingObjectGUID_columnNumber(correspondingObjectGUID_columnNumber);

			if (!conu.isValid()) {
				System.out.println("Invalid column selection: Each value should be unique,");
				return true;
			}
			convert(conu);
			return true;

		default:
			System.err.println("Unknown state. Should not happen.");
		}
		return false;
	}

	private void convert(ColumnNamesModeConversionOptions options) {
		CsvToBean<GenericLinkBean> csvToBean = new CsvToBean<GenericLinkBean>();

		Map<String, String> columnMapping = new HashMap<String, String>();
		columnMapping.put("guid", options.getSubjectElementGUID_columnName());
		columnMapping.put("connected_guid", options.getCorrespondingObjectGUID_columnName());

		HeaderColumnNameTranslateMappingStrategy<GenericLinkBean> strategy = new HeaderColumnNameTranslateMappingStrategy<GenericLinkBean>();
		strategy.setType(GenericLinkBean.class);
		strategy.setColumnMapping(columnMapping);

		List<GenericLinkBean> list = null;
		System.out.println("input file name: "+options.getInputFile());
		CSVReader reader = new CSVReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(options.getInputFile())));
		list = csvToBean.parse(strategy, reader);

		
		for(GenericLinkBean gb:list)
	       System.out.println(gb);
		
		
		//TODO varmista, että column namet ovat olemassa!
	}

	private void convert(ColumnNumbersModeConversionOptions options) {
		String subject_guid = null;
		try {
			CSVReader reader = new CSVReader(new FileReader(options.getInputFile()), options.getSeparator(), '"', 0);
			try {
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						try {
							String line_guid = nextLine[options.getSubjectGUID_columnNumber() - 1].trim();
							if (line_guid != null && line_guid.length() > 0)
								subject_guid = line_guid;
							String object_guid = nextLine[options.getCorrespondingObjectGUID_columnNumber() - 1].trim();
							if (object_guid != null && object_guid.length() > 0) {
								GenericLinkBean gb = new GenericLinkBean(subject_guid, object_guid);
								System.out.println(gb);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} finally {
				reader.close();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		//TODO conversion
	}

	private void executeCommandLine(CommandLine commandLine) {
		int count = 0;
		while (stateMachine(commandLine) == false) {
			count++;
			if (count > 100) {
				System.err.println("Should not happen");
				help();
				break;
			}
		}

	}

	public static String getOption(final char option, final CommandLine commandLine) {
		if (commandLine.hasOption(option))
			return commandLine.getOptionValue(option);
		return StringUtils.EMPTY;
	}

	public static String getOption(final String option, final CommandLine commandLine) {
		if (commandLine.hasOption(option))
			return commandLine.getOptionValue(option);
		return StringUtils.EMPTY;
	}

	private void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("GUIDConverter", options);
		System.exit(0);
	}

	public static void main(String[] args) {
		new GUIDConverter(args);
	}

}
