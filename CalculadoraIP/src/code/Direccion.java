/**
 * 
 */
package code;

import java.util.ArrayList;

/**
 * @author Brayan-PC
 *
 */
public class Direccion {

	ArrayList<Integer> decimal;
	ArrayList<Integer> binario;
	ArrayList<Integer> mascara;
	int mask;
	Tipo tipo;

	/**
	 * @param direccion
	 * @param mascara
	 * @param decimal
	 */
	public Direccion(ArrayList<Integer> direccion, ArrayList<Integer> mascara, boolean decimal) {
		super();
		if (decimal) {
			this.decimal = direccion;
			this.mascara = mascara;
			this.binario = obtenerDireccionBinaria(direccion);
		} else {
			this.binario = direccion;
			this.mascara = mascara;
			this.decimal = obtenerDireccionDecimal(direccion);
		}
	}
	
	/**
	 * @param direccion
	 * @param mascara
	 * @param decimal
	 */
	public Direccion(ArrayList<Integer> direccion, int mascara, boolean decimal) {
		super();
		if (decimal) {
			this.decimal = direccion;
			this.mask = mascara;
		} else {
			this.binario = direccion;
			this.mask = mascara;
		}
	}

	/**
	 * @param direccion
	 * @param mascara
	 * @param decimal
	 */
	public Direccion(int primero, int segundo, int tercero, int cuarto, ArrayList<Integer> mascara) {
		super();
		this.decimal = obtenerDireccionDecimal(primero, segundo, tercero, cuarto);
		this.mascara = mascara;
		this.binario = obtenerDireccionBinaria(primero, segundo, tercero, cuarto);
	}
	
	/**
	 * @param direccion
	 * @param mascara
	 * @param decimal
	 */
	public Direccion(int primero, int segundo, int tercero, int cuarto, int mascara) {
		super();
		this.decimal = obtenerDireccionDecimal(primero, segundo, tercero, cuarto);
		this.mask = mascara;
		this.binario = obtenerDireccionBinaria(primero, segundo, tercero, cuarto);
	}

	/**
	 * @return the decimal
	 */
	public ArrayList<Integer> getDecimal() {
		return decimal;
	}

	/**
	 * @param decimal the decimal to set
	 */
	public void setDecimal(ArrayList<Integer> decimal) {
		this.decimal = decimal;
	}

	/**
	 * @return the binario
	 */
	public ArrayList<Integer> getBinario() {
		return binario;
	}

	/**
	 * @param binario the binario to set
	 */
	public void setBinario(ArrayList<Integer> binario) {
		this.binario = binario;
	}

	/**
	 * @return the mascara
	 */
	public ArrayList<Integer> getMascara() {
		return mascara;
	}

	/**
	 * @param mascara the mascara to set
	 */
	public void setMascara(ArrayList<Integer> mascara) {
		this.mascara = mascara;
	}

	/**
	 * @return the mask
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * @param mask the mask to set
	 */
	public void setMask(int mask) {
		this.mask = mask;
	}

	/**
	 * @return the tipo
	 */
	public Tipo getTipo() {
		return tipo;
	}
	
	public ArrayList<Integer> obtenerDireccionBinariaSiguiente() {
		return obtenerDireccionBinariaSiguiente(binario);
	}

	public ArrayList<Integer> obtenerDireccionDecimalSiguiente() {
		return obtenerDireccionDecimalSiguiente(decimal);
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

	private ArrayList<Integer> obtenerDireccionBinaria(ArrayList<Integer> direccionDecimal) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		for (int dec : direccionDecimal) {
			bin.addAll(obtenerBinarioOcteto(dec));
		}
		return bin;
	}
	
	private ArrayList<Integer> obtenerDireccionBinaria(int primero, int segundo, int tercero, int cuarto) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		bin.addAll(obtenerBinarioOcteto(primero));
		bin.addAll(obtenerBinarioOcteto(segundo));
		bin.addAll(obtenerBinarioOcteto(tercero));
		bin.addAll(obtenerBinarioOcteto(cuarto));
		return bin;
	}

	private ArrayList<Integer> obtenerDireccionDecimal(int primero, int segundo, int tercero, int cuarto) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		bin.add(primero);
		bin.add(segundo);
		bin.add(tercero);
		bin.add(cuarto);
		return bin;
	}
	
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
	
	private int obtenerDecimal(ArrayList<Integer> binario) {
		int numero = 0;
		for (int i = 0; i < binario.size(); i++) {
			if (binario.get(7 - i).equals(1)) {
				numero += Math.pow(2, i);
			}
		}
		return numero;
	}
	
	private ArrayList<Integer> generarMascara(int numero) {
		ArrayList<Integer> mask = new ArrayList<Integer>();
		for (int i = 0; i < 32; i++) {
			if (i < numero) {
				mask.add(1);
			} else {
				mask.add(0);
			}
		}
		return mask;
	}
	
	private ArrayList<Integer> obtenerDireccionBinariaSiguiente(ArrayList<Integer> binario) {
		ArrayList<Integer> dec = obtenerDireccionDecimal(binario);
		if (dec.size() == 4) {
			int num = dec.get(3);
			if (num < 255) {
				dec.set(3, ++num);
				return obtenerDireccionBinaria(dec);
			} else {
				num = dec.get(2);
				if (num < 255) {
					dec.set(2, ++num);
					dec.set(3, 0);
					return obtenerDireccionBinaria(dec);
				} else {
					num = dec.get(1);
					if (num < 255) {
						dec.set(1, ++num);
						dec.set(2, 0);
						dec.set(3, 0);
						return obtenerDireccionBinaria(dec);
					} else {
						num = dec.get(0);
						if (num < 255) {
							dec.set(0, ++num);
							dec.set(1, 0);
							dec.set(2, 0);
							dec.set(3, 0);
							return obtenerDireccionBinaria(dec);
						}
					}
				}
			}
		}
		return null;
	}
	
	private ArrayList<Integer> obtenerDireccionDecimalSiguiente(ArrayList<Integer> decimal) {
		ArrayList<Integer> dec = decimal;
		if (dec.size() == 4) {
			int num = dec.get(3);
			if (num < 255) {
				dec.set(3, ++num);
				return dec;
			} else {
				num = dec.get(2);
				if (num < 255) {
					dec.set(2, ++num);
					dec.set(3, 0);
					return dec;
				} else {
					num = dec.get(1);
					if (num < 255) {
						dec.set(1, ++num);
						dec.set(2, 0);
						dec.set(3, 0);
						return dec;
					} else {
						num = dec.get(0);
						if (num < 255) {
							dec.set(0, ++num);
							dec.set(1, 0);
							dec.set(2, 0);
							dec.set(3, 0);
							return dec;
						}
					}
				}
			}
		}
		return null;
	}

	
}
