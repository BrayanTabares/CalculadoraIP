package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import code.CalculadoraIP;
import code.Direccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.IntegerStringConverter;
import ui.TablaDirecciones;

public class ControladorVentanaPrincipal implements Initializable {

	@FXML
	private Button btnCalcular;

	@FXML
	private TextField ipPrimer, ipSegundo, ipTercero, ipCuarto, maskPrimer, maskSegundo, maskTercero, maskCuarto,
			numMascara, numBits, numSubredes, redPrimer, redSegundo, redTercero, redCuarto, broadPrimer, broadSegundo,
			broadTercero, broadCuarto, buscarPrimer, buscarSegundo, buscarTercero, buscarCuarto, numBitsRed,
			numBitsHost, numDireccionesHost, numBuscarSubred, numBuscarHost;

	@FXML
	private CheckBox checkSubredes, checkNumBits, checkNumSubredes, checkBuscarDireccion, checkBuscarSubred,
			checkBuscarNumSubred, checkBuscarNumHost;

	@FXML
	private TableColumn<TablaDirecciones, String> subred, direccion, tipo, usable;

	@FXML
	private TableView<TablaDirecciones> tablaBusqueda;

	private ObservableList<TablaDirecciones> arrayTabla = FXCollections.observableArrayList();
	private ObservableList<TablaDirecciones> arrayTablaBusqueda = FXCollections.observableArrayList();
	private ArrayList<TablaDirecciones> tablaDirecciones = new ArrayList<TablaDirecciones>();

	private Direccion ip;
	private Direccion mascara;
	private CalculadoraIP calculadora;

	@FXML
	void calcularIP(ActionEvent event) {
		try {
			int primero = Integer.parseInt(ipPrimer.getText());
			int segundo = Integer.parseInt(ipSegundo.getText());
			int tercero = Integer.parseInt(ipTercero.getText());
			int cuarto = Integer.parseInt(ipCuarto.getText());

			int mask = Integer.parseInt(numMascara.getText());

			int cantidadSubredes = 0;
			boolean enBits = false;

			if (checkSubredes.isSelected()) {
				if (checkNumSubredes.isSelected()) {
					cantidadSubredes = Integer.parseInt(numSubredes.getText());
				} else {
					cantidadSubredes = Integer.parseInt(numBits.getText());
					enBits = true;
				}
			}

			ip = new Direccion(primero, segundo, tercero, cuarto);
			mascara = new Direccion(mask);

			Direccion red = CalculadoraIP.hallarRed(ip, mascara);
			ArrayList<Integer> redList = red.getDecimal();

			redPrimer.setText(redList.get(0) + "");
			redSegundo.setText(redList.get(1) + "");
			redTercero.setText(redList.get(2) + "");
			redCuarto.setText(redList.get(3) + "");

			Direccion broad = CalculadoraIP.hallarBroadcast(ip, mascara);
			ArrayList<Integer> broadList = broad.getDecimal();

			broadPrimer.setText(broadList.get(0) + "");
			broadSegundo.setText(broadList.get(1) + "");
			broadTercero.setText(broadList.get(2) + "");
			broadCuarto.setText(broadList.get(3) + "");

			numBitsRed.setText(mask + "");

			numBitsHost.setText(32 - mask + "");

			new CalculadoraIP(ip, mascara, cantidadSubredes, enBits);

			tablaDirecciones = CalculadoraIP.generarDirecciones();

			tablaBusqueda.getItems().clear();
			tablaBusqueda.getItems().addAll(tablaDirecciones);
			tablaBusqueda.refresh();

		} catch (Exception ex) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("error");
			alert.setContentText("Ocurrió un error al calcular la ip");
			alert.show();
		}
	}

	@FXML
	void buscar(ActionEvent event) {
		if (checkBuscarDireccion.isSelected()) {
			String direccionTextField = buscarPrimer.getText() + buscarSegundo + buscarTercero + buscarCuarto;
			String compare = "";
			for (int i = 0; i < arrayTabla.size(); i++) {

				compare = arrayTabla.get(i).getDireccion();

				if (direccionTextField.equals(compare)) {
					TablaDirecciones tablax = arrayTabla.get(i);
					arrayTablaBusqueda.add(tablax);
				}

			}
			arrayTabla = FXCollections.observableArrayList(arrayTablaBusqueda);
		}
	}

	@FXML
	void limpiar(ActionEvent event) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		/**
		 * Pestaña Principal
		 */
		checkSubredes.setSelected(false);
		numMascara.setText("1");
		onSubred(null);
		maskPrimer.setDisable(true);
		maskSegundo.setDisable(true);
		maskTercero.setDisable(true);
		maskCuarto.setDisable(true);

		// Campos pestaña Principal

		UnaryOperator<Change> integerFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("-?([0-9]*)?")) {
				return change;
			}
			return null;
		};

		ipPrimer.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipPrimer.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipPrimer, 255, 0, event);
		});
		ipSegundo.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipSegundo.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipSegundo, 255, 0, event);
		});
		ipTercero.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipTercero.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipTercero, 255, 0, event);
		});
		ipCuarto.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		ipCuarto.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(ipCuarto, 255, 0, event);
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
			eventoTextField(numMascara, 32, 1, event);
			if (numMascara.getText().length() > 0) {
				try {
					int aux = Integer.parseInt(numMascara.getText());
					mascara = new Direccion(aux);
					ArrayList<Integer> array = mascara.getDecimal();
					maskPrimer.setText(array.get(0) + "");
					maskSegundo.setText(array.get(1) + "");
					maskTercero.setText(array.get(2) + "");
					maskCuarto.setText(array.get(3) + "");
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

		numBuscarSubred.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		// ipPrimer.addEventFilter(KeyEvent.ANY, event -> { eventoTextField(numSubredes,
		// 255, event);});

		numBuscarHost.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		// ipPrimer.addEventFilter(KeyEvent.ANY, event -> { eventoTextField(ipPrimer,
		// 255, event);});

		/**
		 * Pestaña Tablas
		 */

		checkBuscarDireccion.setSelected(true);
		checkBuscarNumSubred.setSelected(true);
		switchBuscar1(null);

		buscarPrimer.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		buscarPrimer.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(buscarPrimer, 255, 0, event);
		});
		buscarSegundo.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		buscarSegundo.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(buscarSegundo, 255, 0, event);
		});
		buscarTercero.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		buscarTercero.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(buscarTercero, 255, 0, event);
		});
		buscarCuarto.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		buscarCuarto.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(buscarCuarto, 255, 0, event);
		});

//		subred = new TableColumn<>("Subred");
//		direccion = new TableColumn<>("Direccion");
//		tipo = new TableColumn<>("Tipo");
//		usable = new TableColumn<>("Usable");
		subred.setCellValueFactory(new PropertyValueFactory<>("subred"));
		direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
		tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		usable.setCellValueFactory(new PropertyValueFactory<>("disponible"));

		//tablaBusqueda.getColumns().addAll(subred, direccion, tipo, usable);
		
		//TablaDirecciones aux= new TablaDirecciones("sumadre", "lasuya", "si", "me la pela");
		
		tablaBusqueda.setItems(arrayTabla);
	//	tablaBusqueda.getItems().add(aux);

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

	@FXML
	void switchBuscar1(ActionEvent event) {
		numBuscarHost.setDisable(true);
		numBuscarSubred.setDisable(true);
		buscarPrimer.setDisable(false);
		buscarSegundo.setDisable(false);
		buscarTercero.setDisable(false);
		buscarCuarto.setDisable(false);
		checkBuscarSubred.setSelected(false);
		checkBuscarDireccion.setSelected(true);
		checkBuscarNumHost.setDisable(true);
		checkBuscarNumSubred.setDisable(true);

	}

	@FXML
	void switchBuscar2(ActionEvent event) {
		numBuscarHost.setDisable(true);
		numBuscarSubred.setDisable(false);
		buscarPrimer.setDisable(true);
		buscarSegundo.setDisable(true);
		buscarTercero.setDisable(true);
		buscarCuarto.setDisable(true);
		checkBuscarSubred.setSelected(true);
		checkBuscarDireccion.setSelected(false);
		checkBuscarNumHost.setDisable(false);
		checkBuscarNumSubred.setDisable(false);
		switchBuscar3(null);
		switchBuscar4(null);

	}

	@FXML
	void switchBuscar3(ActionEvent event) {
		if (!checkBuscarNumSubred.isSelected()) {
			numBuscarSubred.setDisable(true);
		} else {
			numBuscarSubred.setDisable(false);
		}
	}

	@FXML
	void switchBuscar4(ActionEvent event) {
		if (!checkBuscarNumHost.isSelected()) {
			numBuscarHost.setDisable(true);
		} else {
			numBuscarHost.setDisable(false);
		}
	}

}
