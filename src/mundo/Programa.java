package mundo;

import java.util.ArrayList;
import java.util.HashMap;

public class Programa {
	
	private String codigoPrograma;
	private String nombrePrograma;
	
	private ArrayList<Estudiante> estudiantes;
	private HashMap<String,Materia> materias;
	
	public Programa(String codigoPrograma, String nombrePrograma) {
		super();
		this.codigoPrograma = codigoPrograma;
		this.nombrePrograma = nombrePrograma;
		estudiantes = new ArrayList<Estudiante>();
		materias = new HashMap<>();
	}

	public String encontrarInconsistencias(){
		String cadena = "Inconsistencias del programa "+nombrePrograma  + "\n";
		for (int i = 0; i < estudiantes.size(); i++) {
			cadena = cadena + "------------------\n";
			cadena = cadena + estudiantes.get(i).encontrarInconsistencias();
		}
		return cadena;
	}
	
	public Estudiante buscarEstudiante(String codigo){
		for(int i = 0; i < estudiantes.size(); i++){
			String codEst = estudiantes.get(i).getCodigoEstudiante()+"";
			if(codEst.equals(codigo)){
				return estudiantes.get(i);
			}
		}
		return null;
	}
	
	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	public ArrayList<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(ArrayList<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public HashMap<String, Materia> getMaterias() {
		return materias;
	}

	public void setMaterias(HashMap<String, Materia> materias) {
		this.materias = materias;
	}

	public int obtenerSemestreMaximo() {
		int i = 0;
		for(String key : materias.keySet()){
			Materia materia = materias.get(key);
			if(materia.getSemestre() > i){
				i = materia.getSemestre();
			}
		}
		return i;
	}

	public Materia buscarMateria(String codigo){
		for(String key : materias.keySet()){
			Materia materia = materias.get(key);
			if(materia.getCodigoMateria().equals(codigo)){
				return materia;
			}
		}
		return null;
	}
	
	public ArrayList<Materia> obtenerMateriasPorSemestre(int i) {
		ArrayList<Materia> materiasSemestre = new ArrayList<Materia>();
		for(String key : materias.keySet()){
			Materia materia = materias.get(key);
			if(materia.getSemestre() == i){
				materiasSemestre.add(materia);
			}
		}
		return materiasSemestre;
	}

	public String obtenerPrerrequisitos(String codigoMateria) {
		Materia materia = buscarMateria(codigoMateria);
		ArrayList<Materia> pre = materia.getMisprerrequisitos();
		String texto = "";
		for(int i = 0; i < pre.size(); i++){
			Materia act = pre.get(i);
			if(i == pre.size()-1){
				texto+=act.getCodigoMateria();
			}
			else{
				texto+= act.getCodigoMateria() + " - ";
			}
		}
		return texto;
	}

	public String obtenerPostRrequisitos(String codigoMateria) {
		Materia materia = buscarMateria(codigoMateria);
		ArrayList<Materia> pre = materia.getPrerrequistoDe();
		String texto = "";
		for(int i = 0; i < pre.size(); i++){
			Materia act = pre.get(i);
			if(i == pre.size()-1){
				texto+=act.getCodigoMateria();
			}
			else{
				texto+= act.getCodigoMateria() + " - ";
			}
		}
		return texto;
	}
	
	public ArrayList<EstudianteMateria> materiasAprobadas(Estudiante est){
		ArrayList<EstudianteMateria> materias = new ArrayList<EstudianteMateria>();
		for(EstudianteMateria mat : est.getMaterias()){
			if(mat.getColor() == EstudianteMateria.COLOR_VERDE){
				materias.add(mat);
			}
		}
		return materias;
	}
	
	public ArrayList<EstudianteMateria> materiasPerdidas(Estudiante est){
		ArrayList<EstudianteMateria> materias = new ArrayList<EstudianteMateria>();
		for(EstudianteMateria mat : est.getMaterias()){
			if(mat.getColor() == EstudianteMateria.COLOR_ROJO){
				materias.add(mat);
			}
		}
		return materias;
	}

	public ArrayList<EstudianteMateria> materiasInconsistencia(Estudiante est){
		ArrayList<EstudianteMateria> materias = new ArrayList<EstudianteMateria>();
		for(EstudianteMateria mat : est.getMaterias()){
			if(mat.getColor() == EstudianteMateria.COLOR_NARANJA){
				materias.add(mat);
			}
		}
		return materias;
	}
	
	public ArrayList<EstudianteMateria> materiasRepetidas(Estudiante est){
		ArrayList<EstudianteMateria> materias = new ArrayList<EstudianteMateria>();
		for(EstudianteMateria mat : est.getMaterias()){
			if(mat.getColor() == EstudianteMateria.COLOR_AZUL){
				materias.add(mat);
			}
		}
		return materias;
	}
}
