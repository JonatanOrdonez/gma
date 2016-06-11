package mundo;

import java.util.ArrayList;

public class Materia {

	private ArrayList<Materia> prerrequistoDe;
	private ArrayList<Materia> Misprerrequisitos;
	
	private int semestre;
	private String codigoMateria;
	private String nombreMateria;
	private int intesidadHoraria;
	private int creditos;
	
	

	public Materia(int semestre, String codigoMateria, String nombreMateria, int intesidadHoraria,
			int creditos) {
		super();
		this.semestre = semestre;
		this.codigoMateria = codigoMateria;
		this.intesidadHoraria = intesidadHoraria;
		this.creditos = creditos;
		this.nombreMateria = nombreMateria;
		prerrequistoDe = new ArrayList<Materia>();
		Misprerrequisitos = new ArrayList<Materia>();
	}

	public ArrayList<Materia> getPrerrequistoDe() {
		return prerrequistoDe;
	}

	public void setPrerrequistoDe(ArrayList<Materia> prerrequistoDe) {
		this.prerrequistoDe = prerrequistoDe;
	}

	public ArrayList<Materia> getMisprerrequisitos() {
		return Misprerrequisitos;
	}

	public void setMisprerrequisitos(ArrayList<Materia> misprerrequisitos) {
		Misprerrequisitos = misprerrequisitos;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public String getCodigoMateria() {
		return codigoMateria;
	}

	public void setCodigoMateria(String codigoMateria) {
		this.codigoMateria = codigoMateria;
	}

	public int getIntesidadHoraria() {
		return intesidadHoraria;
	}

	public void setIntesidadHoraria(int intesidadHoraria) {
		this.intesidadHoraria = intesidadHoraria;
	}

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}

	public String getNombreMateria() {
		return nombreMateria;
	}

	public void setNombreMateria(String nombreMateria) {
		this.nombreMateria = nombreMateria;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return codigoMateria + " " + nombreMateria + " " + creditos + "C - " + intesidadHoraria + "H";
	}
	
}
