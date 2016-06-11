package mundo;

import java.util.ArrayList;

public class Estudiante {
	
	private int codigoEstudiante;
	
	private ArrayList<EstudianteMateria> materias;
	
	private Programa programaPerteneciente;

	public Estudiante(int codigoEstudiante, Programa programaPerteneciente) {
		super();
		this.codigoEstudiante = codigoEstudiante;
		this.programaPerteneciente = programaPerteneciente;
		materias = new ArrayList<EstudianteMateria>();
	}

	public boolean existeLaMateria(String codigoMateria){
		boolean esta = false;
		for (int i = 0; i < materias.size() && !esta; i++) {
			if(materias.get(i).getMateria().getCodigoMateria().equals(codigoMateria)){
				esta = true;
			}
		}
		return esta;
	}
	
	public EstudianteMateria darMateriaPorCodigo(String codigoMateria){
		EstudianteMateria materia= null;
		boolean esta = false;
		for (int i = 0; i < materias.size() && !esta; i++) {
			if(materias.get(i).getMateria().getCodigoMateria().equals(codigoMateria)){
				materia = materias.get(i);
				esta = true;
			}
		}
		return materia;
	}
	
	public String encontrarInconsistencias(){
		String cadena = "Inconsistencias del estudiante: "+codigoEstudiante + "\n";
		for (int i = 0; i < materias.size(); i++) {
			ArrayList<Materia> prerrequisitos = materias.get(i).getMateria().getMisprerrequisitos();
			for (int j = 0; j < prerrequisitos.size(); j++) {
				EstudianteMateria materiaABuscar = darMateriaPorCodigo(prerrequisitos.get(j).getCodigoMateria());
				if(materiaABuscar ==  null || materiaABuscar.getColor()==EstudianteMateria.COLOR_ROJO){
					cadena = cadena + "Inconsistencia con el prerrequisito "+prerrequisitos.get(j).getCodigoMateria()+" de la materia "+materias.get(i).getMateria().getCodigoMateria() + "\n";
				}
			}
		}
		return cadena;
	}
	
	
	public int getCodigoEstudiante() {
		return codigoEstudiante;
	}

	public void setCodigoEstudiante(int codigoEstudiante) {
		this.codigoEstudiante = codigoEstudiante;
	}

	public ArrayList<EstudianteMateria> getMaterias() {
		return materias;
	}

	public void setMaterias(ArrayList<EstudianteMateria> materias) {
		this.materias = materias;
	}

	public Programa getProgramaPerteneciente() {
		return programaPerteneciente;
	}

	public void setProgramaPerteneciente(Programa programaPerteneciente) {
		this.programaPerteneciente = programaPerteneciente;
	}

	public boolean estaMateria(Materia materia) {
		boolean esta=false;
		
		for(int i=0; i<materias.size() && !esta;i++){
			if(materias.get(i).getMateria().getCodigoMateria()
					.equals(materia.getCodigoMateria())){
				esta=true;
			}
		}
		return esta;
	}

}
