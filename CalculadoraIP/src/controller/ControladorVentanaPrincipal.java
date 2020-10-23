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
			numBitsHost, numDireccionesHost, numBuscarSubred, numBuscarHost, numSubredUtil, numHostUtil, numBuscarNHost;

	@FXML
	private CheckBox checkSubredes, checkNumBits, checkNumSubredes, checkBuscarDireccion, checkBuscarSubred,
			checkBuscarNumSubred, checkBuscarNumHost, checkBuscarNHost;

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

			int cantidadSubredes = 1;
			boolean enBits = false;

			if (checkSubredes.isSelected()) {
				if (checkNumSubredes.isSelected()) {
					cantidadSubredes = Integer.parseInt(numSubredes.getText());
					cantidadSubredes=(int)Math.pow(2,(int) Math.ceil(Math.log10(cantidadSubredes) / Math.log10(2)));
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

			if(checkSubredes.isSelected()) {
				try {
					int aux1=Integer.parseInt(numBits.getText());
					int aux=(int)Math.pow(2,aux1);
					if(aux1+mask>=32) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Error");
						alert.setHeaderText("error");
						alert.setContentText("La configuración de subredes es inválida");
						alert.show();
					}
					numSubredes.setText(aux+"");
					if(aux>=2) {
						numSubredUtil.setText(aux-2+"");
						numHostUtil.setText(((int)Math.pow(2, 32-(mask+aux1)))*(aux-2)-(2*(aux-2)) + "");
					}else {
						numSubredUtil.setText(1+"");
						numHostUtil.setText(((int)Math.pow(2, 32-(mask+aux1))-2)+ "");
					}
					numDireccionesHost.setText(((int)Math.pow(2, 32-(mask+aux1))-2)*aux + "");
					numBitsHost.setText(32 - (mask+aux1) + "");
				}catch(Exception ex) {
					
				}
			}else {
				numSubredUtil.setText(1+"");
				numHostUtil.setText(((int)Math.pow(2, 32-(mask))-2)+ "");
				numDireccionesHost.setText(((int)Math.pow(2, 32-(mask))-2)+ "");
				numBitsHost.setText(32 - mask + "");
			}
			
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
			try {
				int primero = Integer.parseInt(buscarPrimer.getText());
				int segundo = Integer.parseInt(buscarSegundo.getText());
				int tercero = Integer.parseInt(buscarTercero.getText());
				int cuarto = Integer.parseInt(buscarCuarto.getText());

				tablaDirecciones = CalculadoraIP.buscarHost(new Direccion(primero, segundo, tercero, cuarto).getDecimal());

				tablaBusqueda.getItems().clear();
				tablaBusqueda.getItems().addAll(tablaDirecciones);
				tablaBusqueda.refresh();
				
			}catch(Exception ex) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setHeaderText("error");
				alert.setContentText("Ocurrió al obtener la ip de búsqueda");
				alert.show();
			}
		}else {
			if(checkBuscarNumHost.isSelected()&&checkBuscarNumSubred.isSelected()) {
				try {
					int subred=Integer.parseInt(numBuscarSubred.getText());
					int host=Integer.parseInt(numBuscarHost.getText());
					tablaDirecciones=CalculadoraIP.buscarHostEnSubred(subred, host);
					
					tablaBusqueda.getItems().clear();
					tablaBusqueda.getItems().addAll(tablaDirecciones);
					tablaBusqueda.refresh();
					
				}catch(Exception ex) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Error");
					alert.setHeaderText("error");
					alert.setContentText("Ocurrió un error al obtener el host en la subred específica");
					alert.show();
				}
				
			}else if(checkBuscarNumSubred.isSelected()) {	
				try {
					int subred=Integer.parseInt(numBuscarSubred.getText());
					
					if(checkBuscarNHost.isSelected()) {
						int cant=Integer.parseInt(numBuscarNHost.getText());
						tablaDirecciones=new ArrayList<TablaDirecciones>(CalculadoraIP.listarhostsEnSubred(subred).subList(0, cant));
						
					}else {
						tablaDirecciones=CalculadoraIP.listarhostsEnSubred(subred);
					}
					
					tablaBusqueda.getItems().clear();
					tablaBusqueda.getItems().addAll(tablaDirecciones);
					tablaBusqueda.refresh();
					
				}catch(Exception ex) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Error");
					alert.setHeaderText("error");
					alert.setContentText("Ocurrió un error al obtener el numero de subred");
					alert.show();
				}
				
			}else if(checkBuscarNumHost.isSelected()) {
				try {
					int host=Integer.parseInt(numBuscarHost.getText());
					tablaDirecciones=CalculadoraIP.buscarHost(host);
					
					tablaBusqueda.getItems().clear();
					tablaBusqueda.getItems().addAll(tablaDirecciones);
					tablaBusqueda.refresh();
					
				}catch(Exception ex) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Error");
					alert.setHeaderText("error");
					alert.setContentText("Ocurrió un error al obtener el número de host");
					alert.show();
				}
			}else {
				limpiar(null);
			}
		}
	}

	@FXML
	void limpiar(ActionEvent event) {
		tablaDirecciones = CalculadoraIP.generarDirecciones();
		
		tablaBusqueda.getItems().clear();
		tablaBusqueda.getItems().addAll(tablaDirecciones);
		tablaBusqueda.refresh();
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
		numSubredes.addEventFilter(KeyEvent.ANY, event -> {
			// eventoTextField(numSubredes,255, event);
			if (numSubredes.getText().length() > 0) {
				try {
					int aux = Integer.parseInt(numSubredes.getText());
					if(aux!=0) {
						numBits.setText((int) Math.ceil(Math.log10(aux) / Math.log10(2)) + "");
					}			
				} catch (Exception exp) {

				}
			}
		});

		numBits.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		numBits.addEventFilter(KeyEvent.ANY, event -> {
			eventoTextField(numBits,32,0, event);
			if (numBits.getText().length() > 0) {
				try {
					int aux = Integer.parseInt(numBits.getText());
					numSubredes.setText((int) Math.pow(2, aux) + "");
				} catch (Exception exp) {

				}
			}
		});

		numBuscarSubred.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		// ipPrimer.addEventFilter(KeyEvent.ANY, event -> { eventoTextField(numSubredes,
		// 255, event);});

		numBuscarHost.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		// ipPrimer.addEventFilter(KeyEvent.ANY, event -> { eventoTextField(ipPrimer,
		// 255, event);});

		numBuscarNHost.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		
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

		// tablaBusqueda.getColumns().addAll(subred, direccion, tipo, usable);

		// TablaDirecciones aux= new TablaDirecciones("sumadre", "lasuya", "si", "me la
		// pela");

		tablaBusqueda.setItems(arrayTabla);
		// tablaBusqueda.getItems().add(aux);

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
		numBuscarNHost.setDisable(true);
		buscarPrimer.setDisable(false);
		buscarSegundo.setDisable(false);
		buscarTercero.setDisable(false);
		buscarCuarto.setDisable(false);
		checkBuscarSubred.setSelected(false);
		checkBuscarDireccion.setSelected(true);
		checkBuscarNumHost.setDisable(true);
		checkBuscarNumSubred.setDisable(true);
		checkBuscarNHost.setDisable(true);

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
		switchBuscar5(null);
		switchBuscar3(null);
		switchBuscar4(null);
		

	}

	@FXML
	void switchBuscar3(ActionEvent event) {
		if (!checkBuscarNumSubred.isSelected()) {
			numBuscarSubred.setDisable(true);
			checkBuscarNHost.setDisable(true);
			numBuscarNHost.setDisable(true);
		} else {
			numBuscarSubred.setDisable(false);
			checkBuscarNHost.setDisable(false);
			switchBuscar5(null);
		}
	}

	@FXML
	void switchBuscar4(ActionEvent event) {
		if (!checkBuscarNumHost.isSelected()) {
			numBuscarHost.setDisable(true);
		} else {
			numBuscarHost.setDisable(false);
			if(checkBuscarNumSubred.isSelected()) {
				checkBuscarNHost.setSelected(false);
				switchBuscar5(null);
			}else {
				
			}
		}
	}
	
	@FXML
	void switchBuscar5(ActionEvent event) {
		if (!checkBuscarNHost.isSelected()) {
			numBuscarNHost.setDisable(true);
		} else {
			numBuscarNHost.setDisable(false);
			if(checkBuscarNumHost.isSelected()) {
				checkBuscarNumHost.setSelected(false);
				switchBuscar4(null);
			}
		}
	}

}
