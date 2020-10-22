/**
 * 
 */
package ui;

/**
 * @author Brayan-PC
 *
 */
public class TablaDirecciones {

	String subred,
			direccion,
			tipo,
			disponible;

	public TablaDirecciones(String subred, String direccion, String tipo, String disponible) {
		super();
		this.subred = subred;
		this.direccion = direccion;
		this.tipo = tipo;
		this.disponible = disponible;
	}

	public String getSubred() {
		return subred;
	}

	public void setSubred(String subred) {
		this.subred = subred;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDisponible() {
		return disponible;
	}

	public void setDisponible(String disponible) {
		this.disponible = disponible;
	} 
	
}
