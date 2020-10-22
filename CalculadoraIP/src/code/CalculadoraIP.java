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

	
	private static Direccion red;
	private static Direccion broad;
	private static Direccion ip;
	private static Direccion mask;
	private static ArrayList<Direccion> host;
	private static ArrayList<ArrayList<Direccion>> subredes;
	
	public CalculadoraIP(Direccion ip, Direccion mask) {
		this.ip = ip;
		this.mask = mask;
		red = hallarRed(ip, mask);
		broad = hallarBroadcast(ip, mask.getNumero());
		host = hallarHost(ip, mask.getNumero());
		
	}
	public CalculadoraIP(Direccion ip, Direccion mask, int cantidadSubredes, boolean enBits) {
		this.ip = ip;
		this.mask = mask;
		red = hallarRed(ip, mask);
		broad = hallarBroadcast(ip, mask.getNumero());
		host = hallarHost(ip, mask.getNumero());
		subredes = hallarSubred(cantidadSubredes, enBits);
			
	}
	
	/**
	 * Operacion and entre dos direcciones binarias instanciadas en dos ArrayList
	 * Integer
	 * 
	 * @param binario1
	 * @param binario2
	 * @return
	 */
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

	/**
	 * Generar la mascara de red en un ArrayList, este metodo perdi� su
	 * funcionalidad ya que la clase Direccion tambien lo tiene
	 * 
	 * @param numero
	 * @return
	 */
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

	/**
	 * Obtener el valor numerico de una mascara de red, este metodo perdi�
	 * funcionalidad ya que la clase Direccion tambi�n lo tiene
	 * 
	 * @param mascara
	 * @return
	 */
	public static int obtenerMascara(ArrayList<Integer> mascara) {
		return mascara.lastIndexOf(1);
	}

	/**
	 * Metodo para hallar la direccion de red de cualquier ip teniendo en cuenta su
	 * mascara de red, recibe direcciones de la clase Direccion
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static Direccion hallarRed(Direccion ip, Direccion mask) {
		return new Direccion(operacionAND(ip.getBinario(), mask.getBinario()), false);
	}

	/**
	 * Metodo para hallar la direccion de red de cualquier ip teniendo en cuenta su
	 * mascara de red, recibe una direccion ip de la clase direccion y genera
	 * automaticamente la mascara de red con un valor numerico
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static Direccion hallarRed(Direccion ip, int mask) {
		return new Direccion(operacionAND(ip.getBinario(), obtenerMascara(mask)), false);
	}

	/**
	 * Metodo para hallar la direccion de broadcast de cualquier ip teniendo en
	 * cuenta su mascara de red, recibe direcciones de la clase Direccion
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static Direccion hallarBroadcast(Direccion ip, Direccion mask) {
		ArrayList<Integer> broad = operacionAND(ip.getBinario(), mask.getBinario());
		int num = mask.getBinario().lastIndexOf(1);
		for (int i = num + 1; i < broad.size(); i++) {
			broad.set(i, 1);
		}
		return new Direccion(broad, false);
	}

	/**
	 * Metodo para hallar la direccion de broadcast de cualquier ip teniendo en
	 * cuenta su mascara de red, recibe una direccion ip de la clase direccion y
	 * genera automaticamente la mascara de red con un valor numerico
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static Direccion hallarBroadcast(Direccion ip, int mask) {
		ArrayList<Integer> broad = operacionAND(ip.getBinario(), obtenerMascara(mask));
		int num = mask - 1;
		for (int i = num + 1; i < broad.size(); i++) {
			broad.set(i, 1);
		}
		return new Direccion(broad, false);
	}

	/**
	 * Metodo para hallar los host de una red, tomando cualquier direccion ip y una
	 * mascara de red generada automaticamente de un valor num�rico
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
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

	/**
	 * Metodo para hallar las subredes dando una direccion ip cualquiera, una
	 * mascara de red en valor numerico y una cantidad de subredes especifica
	 * 
	 * @param ip
	 * @param mask
	 * @param num
	 * @return
	 */
	public static ArrayList<ArrayList<Direccion>> hallarSubred(int num, boolean enBits) {
		int subred = 1;
		if(enBits)
			subred = (int) Math.ceil(Math.log10(num) / Math.log10(2));
		else
			subred = num;
		ArrayList<ArrayList<Direccion>> host = new ArrayList<ArrayList<Direccion>>();
		Direccion actual = red;
		Direccion tope = broad;
		if (subred <= 32 - mask.getNumero()) {
			Direccion maskAux = new Direccion(mask.getNumero());
			for (int i = 0; i < Math.pow(2, subred); i++) {
				ArrayList<Direccion> aux = new ArrayList<Direccion>();
				Direccion topeLocal = actual.obtenerDireccionBinariaSiguiente(mask.getNumero() + subred);
				while (!actual.equals(topeLocal)) {
					aux.add(actual);
					actual = actual.obtenerDireccionBinariaSiguiente();
				}
				host.add(aux);
			}
		}

		return host;
	}

	/**
	 * Metodo para identificar si una direccion ip corresponde a Red, Broadcast o
	 * host, tener en cuenta que solo se identifica de acuerdo a la mascara de red,
	 * asi que no es funcional para las subredes a menos que se tengan las mascaras
	 * que se generan en las subredes (no implementado)
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static Tipo identificarTipo(Direccion ip, int mask) {
		if (hallarRed(ip, mask).equals(ip)) {
			return Tipo.RED;
		} else if (hallarBroadcast(ip, mask).equals(ip)) {
			return Tipo.BROADCAST;
		}
		return Tipo.HOST;
	}


	
	
public static void main(String[] args) {
		
		/**
		 * ip
		 */
		Direccion bin2 = new Direccion(172, 23, 96, 0);
		/**
		 * Macara
		 */
		int mask = 21;
		
		/**
		 * Cantidad de bits que usa la subred
		 */
		int bits=8;
		
		/**
		 * Imprimir la subred
		 */
		int max = 177;
		
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
			System.out
					.println(aux.getCadenaBinario() + " " + aux.getCadenaDecimal() + " " + identificarTipo(aux, mask));
		}

//		System.out.println("\nSubredes :");
//		int i=1;
//		for (ArrayList<Direccion> aux1 : hallarSubred(bin2, mask, (int)Math.pow(2, bits))) {
//			System.out.println("Subred "+i);
//			for (Direccion aux : aux1) {
//				if(i==max) {
//					System.out.println(
//							aux.getCadenaBinario() + " " + aux.getCadenaDecimal() + " " + identificarTipo(aux, mask));
//				}
//				
//			}
//			i++;
//		}
	}

}

