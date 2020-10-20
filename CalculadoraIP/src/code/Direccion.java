/**
 * 
 */
package code;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Brayan-PC
 *
 */
public class Direccion {

	private ArrayList<Integer> decimal;
	private ArrayList<Integer> binario;
	private int numero;
	String cadenaBinario;
	String cadenaDecimal;

	/**
	 * @param direccion
	 * @param decimal
	 */
	public Direccion(ArrayList<Integer> direccion, boolean decimal) {
		super();
		if (decimal) {
			this.decimal = direccion;
			this.binario = obtenerDireccionBinaria(direccion);
		} else {
			this.binario = direccion;
			this.decimal = obtenerDireccionDecimal(direccion);
		}
	}

	/**
	 * @param direccion
	 * @param decimal
	 */
	public Direccion(int mascara) {
		this.numero = mascara;
		this.binario = CalculadoraIP.obtenerMascara(mascara);
		this.decimal = obtenerDireccionDecimal(binario);
	}

	/**
	 * @param direccion
	 * @param mascara
	 * @param decimal
	 */
	public Direccion(int primero, int segundo, int tercero, int cuarto) {
		super();
		this.decimal = new ArrayList<Integer>(Arrays.asList(primero, segundo, tercero, cuarto));
		this.binario = obtenerDireccionBinaria(primero, segundo, tercero, cuarto);
	}

	/**
	 * @return the decimal
	 */
	public ArrayList<Integer> getDecimal() {
		return decimal;
	}

	/**
	 * @return the binario
	 */
	public ArrayList<Integer> getBinario() {
		return binario;
	}

	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @return the cadenaBinario
	 */
	public String getCadenaBinario() {
		actualizarString();
		return cadenaBinario;
	}

	/**
	 * @return the cadenaDecimal
	 */
	public String getCadenaDecimal() {
		actualizarString();
		return cadenaDecimal;
	}

	/**
	 * Metodo para obtener la direccion siguiente a partir de la direccion binaria,
	 * es igual al metodo de obtenerDireccionDecimalSiguiente
	 * 
	 * @return
	 */
	public Direccion obtenerDireccionBinariaSiguiente() {
		return obtenerDireccionBinariaSiguiente(binario);
	}

	/**
	 * Metodo para obtener la direccion siguiente a partir de la direccion decimal,
	 * es igual al metodo de obtenerDireccionBinariaSiguiente
	 * 
	 * @return
	 */
	public Direccion obtenerDireccionDecimalSiguiente() {
		return obtenerDireccionDecimalSiguiente(decimal);
	}

	/**
	 * Metodo para obtener una direccion siguiente recortando en un punto
	 * especifico, este metodo se usa en el obtener subredes, para obtener la
	 * direccion broadcast de la siguiente subred
	 * 
	 * @param mascara
	 * @return
	 */
	public Direccion obtenerDireccionBinariaSiguiente(int mascara) {
		return obtenerDireccionBinariaSiguiente(binario, mascara);
	}

	/**
	 * Metodo para actualizar los strings de acuerdo a cada direccion
	 */
	public void actualizarString() {
		String aux1 = decimal.toString().replace("[", "");
		aux1 = aux1.replace("]", "");
		cadenaDecimal = aux1.replace(", ", ".");

		ArrayList<Integer> aux = new ArrayList<Integer>(binario);
		for (int i = 8; i < binario.size(); i += 8) {
			aux.add(i, -1);
			i++;
		}
		aux1 = aux.toString().replace("[", "");
		aux1 = aux1.replace("]", "");
		aux1 = aux1.replace(", ", "");
		cadenaBinario = aux1.replace("-1", "-");
	}

	@Override
	public String toString() {
		actualizarString();
		return cadenaDecimal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((decimal == null) ? 0 : decimal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Direccion other = (Direccion) obj;
		if (decimal == null) {
			if (other.decimal != null)
				return false;
		} else if (!decimal.equals(other.decimal))
			return false;
		return true;
	}

	private ArrayList<Integer> obtenerBinarioOcteto(double numero) {
		ArrayList<Integer> binario = new ArrayList<Integer>();
		int resto;
		do {
			resto = (int) (numero % 2);
			numero = numero / 2;
			binario.add(0, resto);
		} while (numero >= 2);
		binario.add(0, (int) numero);
		for (int i = binario.size(); i < 8; i++) {
			binario.add(0, 0);
		}
		return binario;
	}

	/**
	 * Metodo para obtener el binario de un numero cualquiera, este permite
	 * establecer un tama�o total (cantidad de ceros a la izquierda) con el
	 * parametro lenght
	 * 
	 * @param numero
	 * @param length
	 * @return
	 */
	private ArrayList<Integer> obtenerBinario(double numero, int length) {
		ArrayList<Integer> binario = new ArrayList<Integer>();
		int resto;
		do {
			resto = (int) (numero % 2);
			numero = numero / 2;
			binario.add(0, resto);
		} while (numero >= 2);
		binario.add(0, (int) numero);
		for (int i = binario.size(); i < length; i++) {
			binario.add(0, 0);
		}
		return binario;
	}

	/**
	 * Metodo para obtener la direccion binaria a partir de una direccion decimal
	 * 
	 * @param direccionDecimal
	 * @return
	 */
	private ArrayList<Integer> obtenerDireccionBinaria(ArrayList<Integer> direccionDecimal) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		for (int dec : direccionDecimal) {
			bin.addAll(obtenerBinarioOcteto(dec));
		}
		return bin;
	}

	/**
	 * Metodo para obtener una direccion binaria a partir de parametros de una
	 * direccion decimal
	 * 
	 * @param primero
	 * @param segundo
	 * @param tercero
	 * @param cuarto
	 * @return
	 */
	private ArrayList<Integer> obtenerDireccionBinaria(int primero, int segundo, int tercero, int cuarto) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		bin.addAll(obtenerBinarioOcteto(primero));
		bin.addAll(obtenerBinarioOcteto(segundo));
		bin.addAll(obtenerBinarioOcteto(tercero));
		bin.addAll(obtenerBinarioOcteto(cuarto));
		return bin;
	}

	/**
	 * Metodo para obtener la direccion decimal a partir de un array de direccion
	 * binaria
	 * 
	 * @param binario
	 * @return
	 */
	private ArrayList<Integer> obtenerDireccionDecimal(ArrayList<Integer> binario) {
		ArrayList<Integer> dec = new ArrayList<Integer>();
		int limite = binario.size() / 8;
		for (int i = 1; i <= limite; i++) {
			int tope = 8 * i - 1;
			int numero = 0;
			for (int j = 0; j < 8; j++) {
				if (binario.get(tope - j).equals(1)) {
					numero += Math.pow(2, j);
				}
			}
			dec.add(numero);
		}
		return dec;
	}

	/**
	 * Metodo para obtener el numero decimal de un numero binario
	 * 
	 * @param binario
	 * @return
	 */
	private int obtenerDecimal(ArrayList<Integer> binario) {
		int numero = 0;
		for (int i = 0; i < binario.size(); i++) {
			if (binario.get(binario.size() - 1 - i).equals(1)) {
				numero += Math.pow(2, i);
			}
		}
		return numero;
	}

	/**
	 * Metodo para obtener la direccion siguiente a partir de la direccion binaria,
	 * es igual al metodo de obtenerDireccionDecimalSiguiente
	 * 
	 * @param binario
	 * @return
	 */
	private Direccion obtenerDireccionBinariaSiguiente(ArrayList<Integer> binario) {
		ArrayList<Integer> dec = obtenerDireccionDecimal(binario);
		if (dec.size() == 4) {
			int num = dec.get(3);
			if (num < 255) {
				dec.set(3, ++num);
				return new Direccion(obtenerDireccionBinaria(dec), false);
			} else {
				num = dec.get(2);
				if (num < 255) {
					dec.set(2, ++num);
					dec.set(3, 0);
					return new Direccion(obtenerDireccionBinaria(dec), false);
				} else {
					num = dec.get(1);
					if (num < 255) {
						dec.set(1, ++num);
						dec.set(2, 0);
						dec.set(3, 0);
						return new Direccion(obtenerDireccionBinaria(dec), false);
					} else {
						num = dec.get(0);
						if (num < 255) {
							dec.set(0, ++num);
							dec.set(1, 0);
							dec.set(2, 0);
							dec.set(3, 0);
							return new Direccion(obtenerDireccionBinaria(dec), false);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Metodo para obtener la direccion siguiente a partir de la direccion decimal,
	 * es igual al metodo de obtenerDireccionBinariaSiguiente
	 * 
	 * @param decimal
	 * @return
	 */
	private Direccion obtenerDireccionDecimalSiguiente(ArrayList<Integer> decimal) {
		ArrayList<Integer> dec = decimal;
		if (dec.size() == 4) {
			int num = dec.get(3);
			if (num < 255) {
				dec.set(3, ++num);
				return new Direccion(dec, true);
			} else {
				num = dec.get(2);
				if (num < 255) {
					dec.set(2, ++num);
					dec.set(3, 0);
					return new Direccion(dec, true);
				} else {
					num = dec.get(1);
					if (num < 255) {
						dec.set(1, ++num);
						dec.set(2, 0);
						dec.set(3, 0);
						return new Direccion(dec, true);
					} else {
						num = dec.get(0);
						if (num < 255) {
							dec.set(0, ++num);
							dec.set(1, 0);
							dec.set(2, 0);
							dec.set(3, 0);
							return new Direccion(dec, true);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Metodo para obtener una direccion siguiente recortando en un punto
	 * especifico, este metodo se usa en el obtener subredes, para obtener la
	 * direccion broadcast de la siguiente subred
	 * 
	 * @param binario
	 * @param mascara
	 * @return
	 */
	private Direccion obtenerDireccionBinariaSiguiente(ArrayList<Integer> binario, int mascara) {
		ArrayList<Integer> dec = new ArrayList<Integer>(binario.subList(0, mascara));
		int aux = obtenerDecimal(dec);
		dec = obtenerBinario(++aux, dec.size());
		for (int i = dec.size(); i <= 32; i++) {
			dec.add(0);
		}
		return new Direccion(dec, false);
	}

}
