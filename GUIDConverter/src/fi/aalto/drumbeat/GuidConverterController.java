package fi.aalto.drumbeat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
 * The GNU Affero General Public License
 * 
 * Copyright (c) 2016 Jyrki Oraskari (Jyrki.Oraskari@aalto.fi / jyrki.oraskari@aalto.fi)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

public class GuidConverterController implements Initializable {
	
	@FXML
	MenuBar myMenuBar;
	
	@FXML
	Label statusTxt;
	
	@FXML
	TableView<LinkVO> linksTable;
	@SuppressWarnings("rawtypes")
	@FXML
	TableColumn componentCol;
	@SuppressWarnings("rawtypes")
	@FXML
	TableColumn guidCol;
	@SuppressWarnings("rawtypes")
	@FXML
	TableColumn nearest_spaceCol;
	@SuppressWarnings("rawtypes")
	@FXML
	TableColumn nearest_space_guidCol;

	@FXML
	TitledPane export_panel;

	@FXML
	TextField http_head_for_guid;

	@FXML
	TextField http_head_for_nearest;

	@FXML
	TextField http_for_property;

	
	@FXML
	TextArea example_triple;
	
	@FXML	
	Button  writeCSVFileButton;
	
	ObservableList<LinkVO> links_list=FXCollections.observableArrayList();
	
	FileChooser fc;
	
	LinksGuidTypeConverter read_csv_file=null;
	
	@SuppressWarnings("unchecked")
	@FXML
	private void readCSVFile() {
		componentCol.setCellValueFactory(
			    new PropertyValueFactory<LinkVO,String>("component")
			);
	    guidCol.setCellValueFactory(
			    new PropertyValueFactory<LinkVO,String>("guid")
			);
	    nearest_spaceCol.setCellValueFactory(
			    new PropertyValueFactory<LinkVO,String>("nearest_space")
			);
	    nearest_space_guidCol.setCellValueFactory(
			    new PropertyValueFactory<LinkVO,String>("nearest_space_guid")
			);

		links_list.clear();	
		linksTable.setDisable(true);
		
		Stage stage = (Stage) myMenuBar.getScene().getWindow();
		File file = null;

		if (fc == null) {
			fc = new FileChooser();
			fc.setInitialDirectory(new File("."));
		}
		FileChooser.ExtensionFilter ef1;
		ef1 = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter ef2;
		ef2 = new FileChooser.ExtensionFilter("All Files", "*.*");
		fc.getExtensionFilters().clear();
		fc.getExtensionFilters().addAll(ef1, ef2);

		if (file == null)
			file = fc.showOpenDialog(stage);
		if (file == null)
			return;
		fc.setInitialDirectory(file.getParentFile());
        read_csv_file=new LinksGuidTypeConverter(file.getAbsolutePath());
		links_list.addAll(read_csv_file.getLinkList());
		linksTable.setDisable(false);
		linksTable.setItems(links_list);
		export_panel.setDisable(false);
		export_panel.setExpanded(true);
		writeCSVFileButton.setDisable(false);
	}

	@FXML
	private void writeTurtleFile() {
		if(read_csv_file==null)
			return;
		Stage stage = (Stage) myMenuBar.getScene().getWindow();
		File file = null;

		if (fc == null) {
			fc = new FileChooser();
			fc.setInitialDirectory(new File("."));
		}
		FileChooser.ExtensionFilter ef1;
		ef1 = new FileChooser.ExtensionFilter("Turtle files (*.ttl)", "*.ttl");
		FileChooser.ExtensionFilter ef2;
		ef2 = new FileChooser.ExtensionFilter("All Files", "*.*");
		fc.getExtensionFilters().clear();
		fc.getExtensionFilters().addAll(ef1, ef2);

		if (file == null)
			file = fc.showSaveDialog(stage);
		if (file == null)
			return;
		fc.setInitialDirectory(file.getParentFile());
		
		
		Model model = ModelFactory.createDefaultModel();
		Property link_property=model.createProperty(http_for_property.getText());

		String subject_guid_base =http_head_for_guid.getText();
		String object_guid_base =http_head_for_nearest.getText();
		
		for (LinkVO l : links_list)
		{
			Resource subject_guid = model.createResource(subject_guid_base+l.getGuid());
			Resource object_guid = model.createResource(object_guid_base+l.getNearest_space_guid());
			subject_guid.addProperty(link_property, object_guid);
		}
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(file);
			model.write(fop,"TURTLE");
			fop.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void writeCSVFile() {
		if(read_csv_file==null)
			return;

		Stage stage = (Stage) myMenuBar.getScene().getWindow();
		File file = null;

		if (fc == null) {
			fc = new FileChooser();
			fc.setInitialDirectory(new File("."));
		}
		FileChooser.ExtensionFilter ef1;
		ef1 = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter ef2;
		ef2 = new FileChooser.ExtensionFilter("All Files", "*.*");
		fc.getExtensionFilters().clear();
		fc.getExtensionFilters().addAll(ef1, ef2);

		if (file == null)
			file = fc.showSaveDialog(stage);
		if (file == null)
			return;
		fc.setInitialDirectory(file.getParentFile());
		System.out.println(file.getAbsolutePath());
		read_csv_file.write2CSV(file.getAbsolutePath());

		
	}
	
	@FXML 
	public void field_change()
	{
		Model model = ModelFactory.createDefaultModel();
		Property link_property=model.createProperty(http_for_property.getText());

		String subject_guid_base =http_head_for_guid.getText();
		String object_guid_base =http_head_for_nearest.getText();
		
		Resource subject_guid = model.createResource(subject_guid_base+"bc860405-54c0-4384-adc1-4702b8ff100d".toUpperCase());
		Resource object_guid = model.createResource(object_guid_base+"e7edfebf-7aee-46fe-8c11-18ccd4885a1e".toUpperCase());
		subject_guid.addProperty(link_property, object_guid);
		
		StmtIterator iter = model.listStatements();

		if (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Triple t=stmt.asTriple();
		    example_triple.setText( enclose(t.getSubject().getURI())+" "+ enclose(t.getPredicate().getURI())+" "+ enclose(t.getObject().getURI())+" .");
		}
	}
	
	private String enclose(String uri)
	{
		return '<'+uri+'>';
	}
	
	
	@FXML
	private void tableClick() {
	}
	
	@FXML
	private void aboutAction() {
		Stage stage = (Stage) myMenuBar.getScene().getWindow();
		new About(stage).show();
	}

	@FXML
	private void closeApplicationAction() {
		Stage stage = (Stage) myMenuBar.getScene().getWindow();
		stage.close();
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		http_head_for_guid.setText("http://drumbeat.cs.hut.fi/");
		http_head_for_nearest.setText("http://drumbeat.cs.hut.fi/");
		http_for_property.setText("http://drumbeat.cs.hut.fi/link");
		field_change();
	}
}
