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

	public static ArrayList<Integer> obtenerMascara(int numero) {
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

	public static int obtenerMascara(ArrayList<Integer> mascara) {
		return mascara.lastIndexOf(1);
	}

	public static ArrayList<Integer> unirArrays(ArrayList<Integer> bin1, ArrayList<Integer> bin2,
			ArrayList<Integer> bin3, ArrayList<Integer> bin4) {
		ArrayList<Integer> newList = new ArrayList<Integer>(bin1);
		newList.addAll(bin2);
		newList.addAll(bin3);
		newList.addAll(bin4);
		return newList;
	}

	public static Direccion hallarRed(Direccion ip, Direccion mask) {
		return new Direccion(operacionAND(ip.getBinario(), mask.getBinario()), false);
	}

	public static Direccion hallarRed(Direccion ip, int mask) {
		return new Direccion(operacionAND(ip.getBinario(), obtenerMascara(mask)), false);
	}

	public static Direccion hallarBroadcast(Direccion ip, Direccion mask) {
		ArrayList<Integer> broad = operacionAND(ip.getBinario(), mask.getBinario());
		int num = mask.getBinario().lastIndexOf(1);
		for (int i = num + 1; i < broad.size(); i++) {
			broad.set(i, 1);
		}
		return new Direccion(broad, false);
	}

	public static Direccion hallarBroadcast(Direccion ip, int mask) {
		ArrayList<Integer> broad = operacionAND(ip.getBinario(), obtenerMascara(mask));
		int num = mask - 1;
		for (int i = num + 1; i < broad.size(); i++) {
			broad.set(i, 1);
		}
		return new Direccion(broad, false);
	}

	public static ArrayList<Direccion> hallarHost(Direccion ip, int mask) {
		ArrayList<Direccion> host = new ArrayList<Direccion>();
		Direccion actual = hallarRed(ip, mask);
		Direccion tope = hallarBroadcast(ip, mask);
		while (!actual.equals(tope)) {
			host.add(actual);
			actual = actual.obtenerDireccionBinariaSiguiente();
		}
		host.add(tope);
		return host;
	}
	
	public static ArrayList<ArrayList<Direccion>> hallarSubred(Direccion ip, int mask, int num) {
		ArrayList<ArrayList<Direccion>> host = new ArrayList<ArrayList<Direccion>>();
		Direccion actual = hallarRed(ip, mask);
		Direccion tope = hallarBroadcast(ip, mask);
		int subred=(int) Math.ceil(Math.log10(num)/Math.log10(2));
		if(subred<=32-mask) {
			Direccion maskAux = new Direccion(mask);			
			for(int i=0;i<Math.pow(2,subred);i++) {
				ArrayList<Direccion> aux= new ArrayList<Direccion>();
				Direccion topeLocal = actual.obtenerDireccionBinariaSiguiente(mask+subred);
				while (!actual.equals(topeLocal)) {
					aux.add(actual);
					actual = actual.obtenerDireccionBinariaSiguiente();
				}
				host.add(aux);
			}
		}
		
		return host;
	}
	
	public static Tipo identificarTipo (Direccion ip,int mask) {
		if(hallarRed(ip, mask).equals(ip)) {
			return Tipo.RED;
		}else if(hallarBroadcast(ip, mask).equals(ip)) {
			return Tipo.BROADCAST;
		}
		return Tipo.HOST;
	}

	public static void main(String[] args) {
		Direccion bin1 = new Direccion(192, 168, 0, 1);
		Direccion bin2 = new Direccion(192, 168, 2, 3);
		System.out.println(bin1);
		System.out.println(bin2);

		int mask = 28;
		Direccion mascara = new Direccion(mask);
		Direccion red = hallarRed(bin2, mask);
		Direccion broad = hallarBroadcast(bin2, mask);
		ArrayList<Direccion> host = hallarHost(bin2, mask);
		
		System.out.println("Ip  :" + bin2.getCadenaBinario() + " " + bin2);
		System.out.println("\nMask:" + mascara.getCadenaBinario() + " " + mascara);

		System.out.println("\nRed :" + red.getCadenaBinario() + " " + red);
		System.out.println("\nBro :" + broad.getCadenaBinario() + " " + broad);
		System.out.println("\nHost");
		
//		for(int i=0;i<17;i++) {
//			red=obtenerDireccionBinariaSiguiente(red);
//			System.out.println(red+ " "+obtenerDireccionDecimal(red));
//		}
		
		for (Direccion aux : host) {
			System.out.println(aux.getCadenaBinario() + " " + aux.getCadenaDecimal()+ " "+ identificarTipo(aux, mask));
		}
		
		System.out.println("\nSubredes :");
		for (ArrayList<Direccion> aux1 : hallarSubred(bin2, 28, 4)) {
			System.out.println("Subred");
			for (Direccion aux : aux1) {
				System.out.println(aux.getCadenaBinario() + " " + aux.getCadenaDecimal()+ " "+ identificarTipo(aux, mask));
			}
		}
	}

}
