package it.polito.tdp.formulaone;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller del turno A --> switchare al branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;
	private boolean flag = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Integer> boxAnno;

    @FXML
    private TextField textInputK;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	Integer anno = this.boxAnno.getValue();
    	
    	if(anno==null) {
    		this.txtResult.setText("Devi selezionare un anno\n");
    		return;
    	}
    	
    	this.model.creaGrafo(anno);
    	
    	this.txtResult.setText("Grafo creato!\n#Vertici: "+this.model.getNumeroVertici()+"\n#Archi: "+this.model.getNumeroArchi()+"\n");
    	
    	this.txtResult.appendText("Il pilota migliore è: "+this.model.getMigliorPilota()+"\n");
    	
    	this.flag = true;

    }

    @FXML
    void doTrovaDreamTeam(ActionEvent event) {
    	
    	if(this.flag==false) {
    		this.txtResult.setText("Devi creare prima il grafo\n");
    		return;
    	}
    	
    	int numero;
    	
    	try {
    		
    		numero = Integer.parseInt(this.textInputK.getText());
    	    		
    	}catch(NumberFormatException ne) {
    		this.txtResult.setText("Formato dimensione errato");
    		return;
    	}
    	
    	this.txtResult.clear();
    	
    	this.model.run(numero);
    	
    	for(Driver d: this.model.getBest()) {
    		this.txtResult.appendText(d+"\n");
    	}
    	
    	this.txtResult.appendText("Tasso: "+this.model.getTasso()+"\n");
    	
    
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FormulaOne.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.getStagioni());
	}
}
