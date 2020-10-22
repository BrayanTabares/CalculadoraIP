/**
 * 
 */
package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import ui.TablaDirecciones;

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
		Direccion actual = ip;
		Direccion tope = broad;
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
				actual.setTipo(Tipo.RED);
				aux.add(actual);
				actual = actual.obtenerDireccionBinariaSiguiente();
				while (!actual.equals(topeLocal)) {
					actual.setTipo(Tipo.HOST);
					aux.add(actual);
					actual = actual.obtenerDireccionBinariaSiguiente();
				}
				actual.setTipo(Tipo.BROADCAST);
				aux.add(actual);
				actual = actual.obtenerDireccionBinariaSiguiente();
				host.add(aux);
			}
		}

		return host;
	}
	/**
	 * Genera un arreglo de tipo TablaDirecciones con todas las subredes y sus host
	 * @return
	 */
	public static ArrayList<TablaDirecciones> generarDirecciones() {
		ArrayList<TablaDirecciones> tabla = new ArrayList<TablaDirecciones>();
		for (int i=0; i<subredes.size(); i++) {
			int nHost = 1;
			for(Direccion host: subredes.get(i)) {
				TablaDirecciones fila = new TablaDirecciones();
				fila.setSubred((i+1)+"");
				fila.setDireccion(host.toString());
				
				if(host.getTipo() != Tipo.HOST) {
					fila.setDisponible("No Disponible");
					fila.setTipo(host.getTipo()+"");
				}else {
					fila.setDisponible("Disponible");
					fila.setTipo(host.getTipo()+" #"+nHost);
					nHost++;
				}
					
				tabla.add(fila);				
			}
		}
		
		return tabla;
	}
	/**
	 * Se busca un host especifico en una subred indicada
	 * @param numSubred
	 * @param numHost
	 * @return
	 */
	public static ArrayList<TablaDirecciones> buscarHostEnSubred(int numSubred, int numHost){
		Direccion host = subredes.get(numSubred-1).get(numHost);
		TablaDirecciones resul = new TablaDirecciones(numSubred+"", host.toString(), host.getTipo()+" #"+numHost, "Disponible");
		return new ArrayList<TablaDirecciones>(Arrays.asList(resul));
	}
	
	public static ArrayList<TablaDirecciones> buscarHost(int numHost){
		ArrayList<TablaDirecciones> tabla = new ArrayList<TablaDirecciones>();
		for (int i=0; i<subredes.size(); i++) {
			Direccion host = subredes.get(i).get(numHost);
			TablaDirecciones resul = new TablaDirecciones((i+1)+"", host.toString(), host.getTipo()+" #"+numHost, "Disponible");
			tabla.add(resul);
		}
			
		return tabla;
	}
	
	public static ArrayList<TablaDirecciones> listarhostsEnSubred(int numSubred){
		ArrayList<TablaDirecciones> tabla = new ArrayList<TablaDirecciones>();
		int nHost = 1;
		for (Direccion host: subredes.get(numSubred-1)) {
			TablaDirecciones fila = new TablaDirecciones();
			fila.setSubred((numSubred)+"");
			fila.setDireccion(host.toString());
			
			if(host.getTipo() != Tipo.HOST) {
				fila.setDisponible("No Disponible");
				fila.setTipo(host.getTipo()+"");
			}else {
				fila.setDisponible("Disponible");
				fila.setTipo(host.getTipo()+" #"+nHost);
				nHost++;
			}
				
			tabla.add(fila);
			
		}
			
		return tabla;
	}
	
	public static void main(String[] args) {
		
		/**
		 * ip
		 */
		Direccion bin2 = new Direccion(172, 23, 96, 0);
		/**
		 * Macara
		 */
		Direccion mask = new Direccion(21);
		
		/**
		 * Cantidad de bits que usa la subred
		 */
		int bits=2;
		
		CalculadoraIP cal = new CalculadoraIP(bin2, mask, bits, false);
		ArrayList<TablaDirecciones> k = cal.generarDirecciones();
		for (TablaDirecciones fila : k) {
			System.out.println(fila.getSubred()+" - "+fila.getDireccion()+" - "+fila.getTipo()+" - "+fila.getDisponible());
		}
		
		System.out.println("Búsqueda por host y subred:");
		
		ArrayList<TablaDirecciones> resul = cal.buscarHostEnSubred(4, 478);
		System.out.println(resul.get(0).getSubred()+" - "+resul.get(0).getDireccion()+" - "+resul.get(0).getTipo()+" - "+resul.get(0).getDisponible());
		
		System.out.println("Búsqueda por host:");
		
		ArrayList<TablaDirecciones> resul2 = cal.buscarHost(510);
		for (TablaDirecciones tablaDirecciones : resul2) {
			System.out.println(tablaDirecciones.getSubred()+" - "+tablaDirecciones.getDireccion()+" - "+tablaDirecciones.getTipo()+" - "+tablaDirecciones.getDisponible());
		}
		
		System.out.println("Listar hosts en su subred:");
		
		ArrayList<TablaDirecciones> resul3 = cal.listarhostsEnSubred(4);
		for (TablaDirecciones tablaDirecciones2 : resul3) {
			System.out.println(tablaDirecciones2.getSubred()+" - "+tablaDirecciones2.getDireccion()+" - "+tablaDirecciones2.getTipo()+" - "+tablaDirecciones2.getDisponible());
		}
		
//		/**
//		 * Imprimir la subred
//		 */
//		int max = 177;
//		
//		Direccion mascara = new Direccion(mask);
//		Direccion red = hallarRed(bin2, mask);
//		Direccion broad = hallarBroadcast(bin2, mask);
//		ArrayList<Direccion> host = hallarHost(bin2, mask);
//
//		System.out.println("Ip  :" + bin2.getCadenaBinario() + " " + bin2);
//		System.out.println("\nMask:" + mascara.getCadenaBinario() + " " + mascara);
//
//		System.out.println("\nRed :" + red.getCadenaBinario() + " " + red);
//		System.out.println("\nBro :" + broad.getCadenaBinario() + " " + broad);
//		System.out.println("\nHost");

//		for(int i=0;i<17;i++) {
//			red=obtenerDireccionBinariaSiguiente(red);
//			System.out.println(red+ " "+obtenerDireccionDecimal(red));
//		}
//
//		for (Direccion aux : host) {
//			System.out
//					.println(aux.getCadenaBinario() + " " + aux.getCadenaDecimal() + " " + identificarTipo(aux, mask));
//		}

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

