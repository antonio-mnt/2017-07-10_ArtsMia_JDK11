package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private boolean flag = false;
	private int numero = 0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Integer> boxLUN;

    @FXML
    private Button btnCalcolaComponenteConnessa;

    @FXML
    private Button btnCercaOggetti;

    @FXML
    private Button btnAnalizzaOggetti;

    @FXML
    private TextField txtObjectId;

    @FXML
    private TextArea txtResult;

    @FXML
    void doAnalizzaOggetti(ActionEvent event) {
    	
    	this.model.creaGrafo();
    	
    	this.txtResult.setText("Grafo creato!\nVertici: "+this.model.getNumeroVertici()+" Archi: "+this.model.getNumeroArchi()+"\n");
    	
    	this.flag = true;

    }

    @FXML
    void doCalcolaComponenteConnessa(ActionEvent event) {
    	
    	int numero;
    	
    	try {
    		numero = Integer.parseInt(this.txtObjectId.getText());
    	}catch(NumberFormatException ne) {
    		this.txtResult.setText("Valore di id errato.");
    		return;
    	}
    	
    	if(this.flag==false) {
    		this.txtResult.setText("Premere prima il bottone Analizza oggetti per creare il grafo");
    		return;
    	}
    	
    	if(!this.model.getIdMap().containsKey(numero)){
    		this.txtResult.setText("Valore di id inesistente");
    		return;
    	}
    	
    	this.numero = numero;
    	
    	int n = this.model.determinaComponenteConnessa(this.model.getIdMap().get(numero));
    	
    	this.txtResult.setText("La componente connessa contiene "+n+" vertici");
    	this.boxLUN.getItems().clear();
    	if(n<2) {
    		this.txtResult.appendText("\nNon è possibile riempire la combox di LUN");
    		return;
    	}
    	
    	
    	List<Integer> dimensione = new ArrayList<>();
    	for(int i = 2;i<=n; i++) {
    		if(i>=200) {
    			break;
    		}
    		dimensione.add(i);
    	}
    	
    	this.boxLUN.getItems().addAll(dimensione);
    	

    }

    @FXML
    void doCercaOggetti(ActionEvent event) {
    	
    	if(this.boxLUN.getValue()==null) {
    		this.txtResult.setText("Selezionare LUN");
    		return;
    	}
    	
    	this.model.trovaPercoso(this.numero, this.boxLUN.getValue());
    	
    	if(this.model.getBest()==null) {
    		this.txtResult.setText("Nessun cammino trovato");
    		return;
    	}
    	
    	
    	
    	this.txtResult.setText(this.model.getBest().toString()+"\n");
    	this.txtResult.appendText(this.model.getPesoTotale()+"\n");
    	

    }

    @FXML
    void initialize() {
        assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.flag = false;
	}
}
