package mundo;

import java.util.ArrayList;

public class EstudianteMateria {
	
	public static final char COLOR_ROJO = 'r';
	public static final char COLOR_VERDE = 'v';
	public static final char COLOR_AZUL = 'a';
	public static final char COLOR_BLANCO = 'b';
	public static final char COLOR_NARANJA = 'n';
	
	private Materia materia;
	
	private int semestre;
	private double nota;
	private char color;
	
	public EstudianteMateria(Materia materia, double nota) {
		
		this.nota = nota;
		this.materia = materia;
		color = COLOR_BLANCO;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public char getColor() {
		return color;
	}

	public void setColor(char color) {
		this.color = color;
	}
	
}
