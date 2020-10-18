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

	public static ArrayList<String> obtenerBinarioOcteto(double numero) {
		ArrayList<String> binario = new ArrayList<String>();
		int resto;
		do {
			resto = (int) (numero % 2);
			numero = numero / 2;
			binario.add(0, Integer.toString(resto));
		} while (numero > 2); 
		binario.add(0, Integer.toString((int) numero)); 		
		for(int i=binario.size(); i<8; i++) {
			binario.add(0,"0");
		}
		return binario;
//		for (int i = 1; i <= 32; i++) {
//			if (32 - binario.size() >= i) {
//				binarioString += "0";
//			} else {
//				binarioString += binario.get(i - 1 - (32 - binario.size()));
//			}
//			if (i % 8 == 0 && i != 32 && i != 0) {
//				binarioString += ".";
//			}
//		}
	}
	
	public static int obtenerDecimal(ArrayList<String> binario){
		int numero=0;
		for(int i=0;i<binario.size();i++) {
			if(binario.get(7-i).equals("1")) {
				numero+=Math.pow(2, i);
			}
		}
		return numero;
	}
	
	public static ArrayList<String> obtenerDirecionDecimal(ArrayList<String> binario){
		ArrayList<String> dec = new ArrayList<String>();
		int limite=binario.size()/8;
		for(int i=1;i<=limite;i++) {
			int tope=8*i-1;
			int numero=0;
			for(int j=0;j<8;j++) {
				if(binario.get(tope-j).equals("1")) {
					numero+=Math.pow(2, j);
				}
			}
			dec.add(numero+"");
		}
		return dec;
	}
	
	public static ArrayList<String> operacionAND(ArrayList<String> binario1, ArrayList<String> binario2) {
		ArrayList<String> and = new ArrayList<String>();
		if (binario1.size()!=binario2.size()) {
			return null;
		}else {
			for(int i=0; i<binario1.size();i++) {
				if(binario1.get(i).equals("1")&&binario2.get(i).equals("1")) {
					and.add("1");
				}else {
					and.add("0");
				}
			}
		}
		return and;
	}
	
	public static ArrayList<String> generarMascara(int numero){
		ArrayList<String> mask = new ArrayList<String>();
		for(int i=0;i<32;i++) {
			if(i<numero) {
				mask.add("1");
			}else {
				mask.add("0");
			}
		}
		return mask;	
	}
	
	public static ArrayList<String> unirArrays(ArrayList<String> bin1,ArrayList<String> bin2,ArrayList<String> bin3,ArrayList<String> bin4){
		ArrayList<String> newList=new ArrayList<String>(bin1);
		newList.addAll(bin2);
		newList.addAll(bin3);
		newList.addAll(bin4);
		return newList;
	}
	
	public static ArrayList<String> hallarRed(ArrayList<String> ip, ArrayList<String> mask){
		return operacionAND(ip, mask);
	}
	
	public static ArrayList<String> hallarRed(ArrayList<String> ip, int mask){
		return operacionAND(ip, generarMascara(mask));
	}
	
	public static ArrayList<String> hallarBroadcast(ArrayList<String> ip, ArrayList<String> mask){
		ArrayList<String> broad=operacionAND(ip, mask);
		int num = broad.lastIndexOf("1");
		for(int i=num+1;i<broad.size();i++) {
			broad.set(i, "1");
		}
		return broad;
	}
	
	public static ArrayList<String> hallarBroadcast(ArrayList<String> ip, int mask){
		ArrayList<String> broad=operacionAND(ip, generarMascara(mask));
		int num = broad.lastIndexOf("1");
		for(int i=num+1;i<broad.size();i++) {
			broad.set(i, "1");
		}
		return broad;
	}
	
	public static ArrayList<ArrayList<String>> hallarHost(ArrayList<String> ip, int mask){
		ArrayList<ArrayList<String>> host=new ArrayList<ArrayList<String>>();
		
		
		return null;
	}

	public static void main(String[] args) {
		ArrayList<String> bin=obtenerBinarioOcteto(129);
		System.out.println(bin);
		System.out.println(obtenerDecimal(bin));
		System.out.println(obtenerDirecionDecimal(bin));
		
		ArrayList<String> bin1 =unirArrays(obtenerBinarioOcteto(192), obtenerBinarioOcteto(168), obtenerBinarioOcteto(3), obtenerBinarioOcteto(0));
		ArrayList<String> bin2 =unirArrays(obtenerBinarioOcteto(192), obtenerBinarioOcteto(168), obtenerBinarioOcteto(2), obtenerBinarioOcteto(255));
		System.out.println(bin1);
		System.out.println(bin2);
		
		int mask=24;
		System.out.println("Ip  :"+bin2+" "+obtenerDirecionDecimal(bin2));
		System.out.println("Mask:"+generarMascara(mask)+" "+obtenerDirecionDecimal(generarMascara(mask)));
		System.out.println("Red :"+hallarRed(bin2, mask)+" "+obtenerDirecionDecimal(hallarRed(bin2, mask)));
		System.out.println("Bro :"+hallarBroadcast(bin2, mask)+" "+obtenerDirecionDecimal(hallarBroadcast(bin2, mask)));
		
	}

}
