package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import code.Direccion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.IntegerStringConverter;

public class ControladorVentanaPrincipal implements Initializable {

	@FXML
	private Button btnCalcular;

	@FXML
	private TextField ipPrimer, ipSegundo, ipTercero, ipCuarto, maskPrimer, maskSegundo, maskTercero, maskCuarto,
			numMascara, numBits, numSubredes;

	@FXML
	private CheckBox checkSubredes, checkNumBits, checkNumSubredes;
	
	private Direccion ip;
	private Direccion mascara;

	@FXML
	void calcularIP(ActionEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		checkSubredes.setSelected(false);
		numMascara.setText("1");
		onSubred(null);
		maskPrimer.setDisable(true);
		maskSegundo.setDisable(true);
		maskTercero.setDisable(true);
		maskCuarto.setDisable(true);

		UnaryOperator<Change> integerFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("-?([0-9]*)?")) {
				return change;
			}
			return null;
		};

		ipPrimer.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipPrimer.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipPrimer, 255,0, event);
		});
		ipSegundo.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipSegundo.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipSegundo, 255,0, event);
		});
		ipTercero.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipTercero.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipTercero, 255,0, event);
		});
		ipCuarto.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipCuarto.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipCuarto, 255,0, event);
		});

		
		UnaryOperator<Change> integerFilterMask = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("-?([1-9][0-9]*)?")) {
				return change;
			}
			return null;
		};
		
		numMascara.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilterMask));
		numMascara.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(numMascara, 32,1, event);
			if (numMascara.getText().length() > 0) {
				try {
					int aux = Integer.parseInt(numMascara.getText());
					mascara=new Direccion(aux);
					ArrayList<Integer> array= mascara.getDecimal();
					maskPrimer.setText(array.get(0)+"");
					maskSegundo.setText(array.get(1)+"");
					maskTercero.setText(array.get(2)+"");
					maskCuarto.setText(array.get(3)+"");
				} catch (Exception exp) {
					
				}
			}
		});

		numSubredes.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		// ipPrimer.addEventFilter(KeyEvent.ANY, event -> { eventoTextField(numSubredes,
		// 255, event);});

		numBits.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		// ipPrimer.addEventFilter(KeyEvent.ANY, event -> { eventoTextField(ipPrimer,
		// 255, event);});
	}

	@FXML
	void prueba(ActionEvent event) {
		System.out.println("evento");
	}

	void eventoTextField(TextField text, int max, int min, KeyEvent event) {
		if (text.getText().length() > 0) {
			try {
				int aux = Integer.parseInt(text.getText());
				if (aux > max) {
					text.setText(max + "");
					text.positionCaret(text.getLength());
				} else if (aux < min) {
					text.setText(min + "");
					text.positionCaret(text.getLength());
				} else if (aux == 0) {
					text.setText(0 + "");
					text.positionCaret(text.getLength());
				}
			} catch (Exception exp) {
				text.setText("");
			}
		}
	}

	@FXML
	void switchSubred1(ActionEvent event) {
		numBits.setDisable(true);
		numSubredes.setDisable(false);
		checkNumBits.setSelected(false);
		checkNumSubredes.setSelected(true);

	}

	@FXML
	void switchSubred2(ActionEvent event) {
		numBits.setDisable(false);
		numSubredes.setDisable(true);
		checkNumSubredes.setSelected(false);
		checkNumBits.setSelected(true);
	}

	@FXML
	void onSubred(ActionEvent event) {
		if (checkSubredes.isSelected()) {
			checkNumBits.setDisable(false);
			checkNumSubredes.setDisable(false);
			switchSubred1(null);
		} else {
			checkNumBits.setDisable(true);
			checkNumSubredes.setDisable(true);
			numBits.setDisable(true);
			numSubredes.setDisable(true);
		}
	}
}
