/**
 * 
 */
package code;

import java.util.ArrayList;

/**
 * @author Brayan-PC
 *
 */
public class CalculadoraIP {

	public static ArrayList<Integer> obtenerBinarioOcteto(double numero) {
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

	public static ArrayList<Integer> obtenerDireccionBinaria(ArrayList<Integer> direccionDecimal) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		for (int dec : direccionDecimal) {
			bin.addAll(obtenerBinarioOcteto(dec));
		}
		return bin;
	}

	public static ArrayList<Integer> obtenerDireccionBinaria(int primero, int segundo, int tercero, int cuarto) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		bin.addAll(obtenerBinarioOcteto(primero));
		bin.addAll(obtenerBinarioOcteto(segundo));
		bin.addAll(obtenerBinarioOcteto(tercero));
		bin.addAll(obtenerBinarioOcteto(cuarto));
		return bin;
	}

	public static ArrayList<Integer> obtenerDireccionDecimal(int primero, int segundo, int tercero, int cuarto) {
		ArrayList<Integer> bin = new ArrayList<Integer>();
		bin.add(primero);
		bin.add(segundo);
		bin.add(tercero);
		bin.add(cuarto);
		return bin;
	}

	
	public static ArrayList<Integer> obtenerDireccionDecimal(ArrayList<Integer> binario) {
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
	
	public static int obtenerDecimal(ArrayList<Integer> binario) {
		int numero = 0;
		for (int i = 0; i < binario.size(); i++) {
			if (binario.get(7 - i).equals(1)) {
				numero += Math.pow(2, i);
			}
		}
		return numero;
	}


	public static ArrayList<Integer> operacionAND(ArrayList<Integer> binario1, ArrayList<Integer> binario2) {
		ArrayList<Integer> and = new ArrayList<Integer>();
		if (binario1.size() != binario2.size()) {
			return null;
		} else {
			for (int i = 0; i < binario1.size(); i++) {
				if (binario1.get(i).equals(1) && binario2.get(i).equals(1)) {
					and.add(1);
				} else {
					and.add(0);
				}
			}
		}
		return and;
	}

	public static ArrayList<Integer> generarMascara(int numero) {
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

	public static ArrayList<Integer> unirArrays(ArrayList<Integer> bin1, ArrayList<Integer> bin2,
			ArrayList<Integer> bin3, ArrayList<Integer> bin4) {
		ArrayList<Integer> newList = new ArrayList<Integer>(bin1);
		newList.addAll(bin2);
		newList.addAll(bin3);
		newList.addAll(bin4);
		return newList;
	}

	public static ArrayList<Integer> hallarRed(ArrayList<Integer> ip, ArrayList<Integer> mask) {
		return operacionAND(ip, mask);
	}

	public static ArrayList<Integer> hallarRed(ArrayList<Integer> ip, int mask) {
		return operacionAND(ip, generarMascara(mask));
	}

	public static ArrayList<Integer> hallarBroadcast(ArrayList<Integer> ip, ArrayList<Integer> mask) {
		ArrayList<Integer> broad = operacionAND(ip, mask);
		int num = mask.lastIndexOf(1);
		for (int i = num + 1; i < broad.size(); i++) {
			broad.set(i, 1);
		}
		return broad;
	}

	public static ArrayList<Integer> hallarBroadcast(ArrayList<Integer> ip, int mask) {
		ArrayList<Integer> broad = operacionAND(ip, generarMascara(mask));
		int num = mask - 1;
		for (int i = num + 1; i < broad.size(); i++) {
			broad.set(i, 1);
		}
		return broad;
	}

	public static ArrayList<Integer> obtenerDireccionBinariaSiguiente(ArrayList<Integer> binario) {
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

	public static ArrayList<ArrayList<Integer>> hallarHost(ArrayList<Integer> ip, int mask) {
		ArrayList<ArrayList<Integer>> host = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> actual = hallarRed(ip, mask);
		ArrayList<Integer> tope = hallarBroadcast(ip, mask);
		while (!actual.equals(tope)) {
			host.add(actual);
			actual = obtenerDireccionBinariaSiguiente(actual);
		}
		host.add(tope);
		return host;
	}

	public static void main(String[] args) {
		ArrayList<Integer> bin = obtenerBinarioOcteto(129);
		System.out.println(bin);
		System.out.println(obtenerDecimal(bin));
		System.out.println(obtenerDireccionDecimal(bin));

		ArrayList<Integer> bin1 = obtenerDireccionBinaria(192, 168, 0, 1);
		ArrayList<Integer> bin2 = obtenerDireccionBinaria(192, 168, 2, 3);
		System.out.println(bin1);
		System.out.println(bin2);

		int mask = 28;
		System.out.println("Ip  :" + bin2 + " " + obtenerDireccionDecimal(bin2));
		System.out.println("Mask:" + generarMascara(mask) + " " + obtenerDireccionDecimal(generarMascara(mask)));
		System.out.println("Red :" + hallarRed(bin2, mask) + " " + obtenerDireccionDecimal(hallarRed(bin2, mask)));
		System.out.println(
				"Bro :" + hallarBroadcast(bin2, mask) + " " + obtenerDireccionDecimal(hallarBroadcast(bin2, mask)));
		System.out.println("Host");
		ArrayList<Integer> red = hallarRed(bin2, mask);
//		for(int i=0;i<17;i++) {
//			red=obtenerDireccionBinariaSiguiente(red);
//			System.out.println(red+ " "+obtenerDireccionDecimal(red));
//		}
		ArrayList<ArrayList<Integer>> host1 = hallarHost(bin2, mask);
		for (ArrayList<Integer> host : host1) {
			System.out.println(host + " " + obtenerDireccionDecimal(host));
		}

	}

}
