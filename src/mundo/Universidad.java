package mundo;

import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

public class Universidad {

	private ArrayList<Programa> programas;
	private ArrayList<Estudiante> estudiantes;

	public Universidad() {
		super();
		programas = new ArrayList<Programa>();
		estudiantes = new ArrayList<Estudiante>();

	}

	public void cargarMallaCurricular(String ruta) throws IOException {
		File file = new File(ruta);
		BufferedReader in = new BufferedReader(new FileReader(file));

		String linea = in.readLine();
		boolean seguir = false;
		String[] cargarPrograma = null;
		if (linea != null) {
			cargarPrograma = linea.split(";");
			seguir = true;
		}
		if (seguir) {

			Programa programa = new Programa(cargarPrograma[0],
					cargarPrograma[1]);
			programas.add(programa);

			linea = in.readLine();
			while (linea != null) {
				String[] materias = linea.split(";");
				if (materias.length == 5) {
					Materia materia = new Materia(
							Integer.parseInt(materias[0]), materias[1],
							materias[2], Integer.parseInt(materias[3]),
							Integer.parseInt(materias[4]));
					programa.getMaterias().put(materias[1], materia);
				} else {
					Materia materia = new Materia(
							Integer.parseInt(materias[0]), materias[1],
							materias[2], Integer.parseInt(materias[3]),
							Integer.parseInt(materias[4]));
					programa.getMaterias().put(materias[1], materia);
					String[] prerrequisitos = materias[materias.length - 1]
							.split("-");
					for (int i = 0; i < prerrequisitos.length; i++) {
						Materia prerrequisitoMateria = programa.getMaterias()
								.get(prerrequisitos[i]);
						prerrequisitoMateria.getPrerrequistoDe().add(materia);
						materia.getMisprerrequisitos()
								.add(prerrequisitoMateria);
					}
				}
				linea = in.readLine();
			}

		}
		in.close();

		JOptionPane.showMessageDialog(null,
				"El archivo se cargó correctamente", "Carga exitosa",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void cargarInformacionEstudiante(String ruta) throws IOException{
		File archivo = new File(ruta);
		FileReader reader = new FileReader(archivo);
		BufferedReader lector = new BufferedReader(reader);
		String linea = lector.readLine();
		while (linea != null && !linea.equals("")) {
			String[] caracteres = linea.split(";");
			String codigoPrograma = caracteres[0];
			String codigoEstudiante = caracteres[1];
			Programa prog = buscarPrograma(codigoPrograma);
			Estudiante estudiante = new Estudiante(Integer.parseInt(codigoEstudiante), prog);
			prog.getEstudiantes().add(estudiante);

			for (int i = 2; i < caracteres.length; i++) {
				String[] caracteres2 = caracteres[i].split("-");
				String codigoMateria = caracteres2[0];
				double nota = Double.parseDouble(caracteres2[1]);

				Materia materia = prog.buscarMateria(codigoMateria);
				EstudianteMateria materiaEst = new EstudianteMateria(materia, nota);

//				if (!prerrequisitosAprobados(estudiante, codigoPrograma, materia)) {
//					materiaEst.setColor(MateriaEstudiante.NARANJA);
				
				if (nota >= 3.0) {
					if (estudiante.estaMateria(materia)) {
						eliminarMateriaEstudiante(estudiante, materiaEst.getMateria().getCodigoMateria());
//						estudiante.getMateriasDelEstudiante().remove(materia);
						materiaEst.setColor(materiaEst.COLOR_AZUL);
					}else if (!prerrequisitosAprobados(estudiante, codigoPrograma, materia)) {
						materiaEst.setColor(EstudianteMateria.COLOR_NARANJA); 
						} else {
						materiaEst.setColor(EstudianteMateria.COLOR_VERDE);
					}
				} else if (!prerrequisitosAprobados(estudiante, codigoPrograma, materia)) {
					materiaEst.setColor(EstudianteMateria.COLOR_NARANJA); 
				} else {
					materiaEst.setColor(EstudianteMateria.COLOR_ROJO);
				}
				estudiante.getMaterias().add(materiaEst);
			}
			linea = lector.readLine();
		}
		lector.close();
		reader.close();
		JOptionPane.showMessageDialog(null,
				"El archivo se cargó correctamente", "Carga exitosa",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	private boolean prerrequisitosAprobados(Estudiante estudiante, String codigoPrograma, Materia materia) {
		boolean aprobados = true;
		Programa programaA = buscarPrograma(codigoPrograma);
		ArrayList<Materia> prerrequisitos = materia.getMisprerrequisitos();
		for (int i = 0; i < prerrequisitos.size() && aprobados; i++) {
			EstudianteMateria mat = buscarMateriaDeUnEstudiante(estudiante, prerrequisitos.get(i).getCodigoMateria());
			if(mat!=null){
				if (mat.getColor() != EstudianteMateria.COLOR_VERDE) {
					aprobados = false;
				}
			}
		}
		return aprobados;
	}

	private EstudianteMateria buscarMateriaDeUnEstudiante(Estudiante estudiante, String codigoMateria) {
		EstudianteMateria mat = null;
		boolean encontro = false;

		for (int i = 0; i < estudiante.getMaterias().size() && !encontro; i++) {
			if (estudiante.getMaterias().get(i).getMateria().getCodigoMateria().equals(codigoMateria)) {
				mat = estudiante.getMaterias().get(i);
				encontro = true;
			}
		}
		return mat;
	}

	private void eliminarMateriaEstudiante(Estudiante estudiante, String codigoMateria) {
		boolean eliminado=false;
		for (int i = 0; i < estudiante.getMaterias().size() && !eliminado; i++) {
			if(estudiante.getMaterias().get(i).getMateria().getCodigoMateria().equals(codigoMateria)){
				estudiante.getMaterias().remove(i);
				eliminado=true;
			}
		}
	}

	public void cargarInforamcionEstudiante2(String ruta) throws IOException {
		File file = new File(ruta);
		BufferedReader in = new BufferedReader(new FileReader(file));

		String linea = in.readLine();

		while (linea != null) {
			String[] cadena = linea.split(";");

			Programa nuevoPrograma = buscarPrograma(cadena[0]);

			Estudiante student = new Estudiante(Integer.parseInt(cadena[1]),
					nuevoPrograma);

			for (int i = 2; i < cadena.length; i++) {

				String[] datosCurso = cadena[i].split("-");

				if (student.darMateriaPorCodigo(datosCurso[0]) != null) {
					double notaAReemplazar = Double.parseDouble(datosCurso[1]);
					EstudianteMateria materia = student
							.darMateriaPorCodigo(datosCurso[0]);

					if (notaAReemplazar < 3 && materia.getNota() >= 3
							|| notaAReemplazar >= 3 && materia.getNota() < 3) {
						materia.setColor(materia.COLOR_AZUL);
					}
					if (notaAReemplazar > materia.getNota()) {
						materia.setNota(notaAReemplazar);
					}

				} else {
					EstudianteMateria curso = new EstudianteMateria(
							nuevoPrograma.getMaterias().get(datosCurso[0]),
							Double.parseDouble(datosCurso[1]));

					if (curso.getNota() >= 3) {
						curso.setColor(curso.COLOR_VERDE);
					} else {
						curso.setColor(curso.COLOR_ROJO);
					}
					student.getMaterias().add(curso);
				}

			}

			/*
			 * PONER ESTO CUANDO SE ESTAN AGREGANDO LOS PANELES EN LA INTERFAZ
			 * GRAFICA
			 * 
			 * for (int i = 0; i < student.getMaterias().size(); i++) {
			 * if(student
			 * .getMaterias().get(i).getMateria().getMisprerrequisitos() !=
			 * null){ for (int j = 0; j <
			 * student.getMaterias().get(i).getMateria
			 * ().getMisprerrequisitos().size(); j++) { String codigoMateria =
			 * student
			 * .getMaterias().get(i).getMateria().getMisprerrequisitos().get
			 * (j).getCodigoMateria(); EstudianteMateria curso =
			 * student.darMateriaPorCodigo(codigoMateria); if(curso.getColor()
			 * == curso.COLOR_ROJO || curso.getColor() == curso.COLOR_BLANCO ){
			 * student.getMaterias().get(i); } } } }
			 */
			nuevoPrograma.getEstudiantes().add(student);
			estudiantes.add(student);

			linea = in.readLine();
		}
		in.close();
		JOptionPane.showMessageDialog(null,
				"El archivo se cargó correctamente", "Carga exitosa",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public String lineaDePrerrequisitosMasLarga(Programa programa) {
		ArrayList<Materia> materias = new ArrayList<>(programa.getMaterias()
				.values());
		HashMap<String, Integer> longitudes = new HashMap<>();
		int caminoMasLargo = 0;
		Materia materiaCaminoMasLargo = null;

		for (int i = 0; i < materias.size(); i++) {
			int camino = ObtenerCaminoMasLargo(materias.get(i), longitudes);
			if (camino > caminoMasLargo) {
				caminoMasLargo = camino;
				materiaCaminoMasLargo = materias.get(i);
			}
		}

		if (materiaCaminoMasLargo != null) {
			return "La materia con la linea de prerrequistos mas larga es: \n"
					+ "Nombre de la materia: "+materiaCaminoMasLargo.getNombreMateria()+"\n"
					+ "Código de la materia: "+materiaCaminoMasLargo.getCodigoMateria()+"\n"
					+ " con "+caminoMasLargo + "cursos como requisito de esta materia.";
		} else {
			return "No se encontro la materia con la linea de prerrequisitos mas larga";
		}
	}

	public int ObtenerCaminoMasLargo(Materia materia,
			HashMap<String, Integer> longitudes) {
		Integer mayorCamino = longitudes.get(materia.getCodigoMateria());
		if (mayorCamino == null) {
			mayorCamino = 0;
			for (int i = 0; i < materia.getMisprerrequisitos().size(); i++) {
				Materia actual = materia.getMisprerrequisitos().get(i);
				int valorCamino = ObtenerCaminoMasLargo(actual, longitudes) + 1;

				if (valorCamino > mayorCamino) {
					mayorCamino = valorCamino;
				}
			}

			longitudes.put(materia.getCodigoMateria(), mayorCamino);
		}

		return mayorCamino;

	}

	public String prerrequisitoMayorCantidadCursosDirectamente(Programa programa) {
		ArrayList<Materia> materias = new ArrayList<>(programa.getMaterias()
				.values());
		int mayor = 0;
		Materia materiaMayor = null;
		for (int i = 0; i < materias.size(); i++) {
			if (materias.get(i).getPrerrequistoDe().size() > mayor) {
				mayor = materias.get(i).getPrerrequistoDe().size();
				materiaMayor = materias.get(i);
			}
		}

		if (materiaMayor != null) {
			return "La materia con la linea de prerrequistos mas larga es: \n"
					+ "Nombre de la materia: "+materiaMayor.getNombreMateria()+"\n"
					+ "Código de la materia: "+materiaMayor.getCodigoMateria()+"\n"
					+ " siendo prerrequisito "+mayor + " cursos.";
		} else {
			return "No se encontró el curso que es prerrequisto de la mayor cantidad de otros cursos directamente";
		}

	}

	public String prerrequisitoMayorCantidadCursosIndirectamente(
			Programa programa) {
		ArrayList<Materia> materias = new ArrayList<>(programa.getMaterias()
				.values());
		HashMap<String, Integer> longitudes = new HashMap<>();
		int caminoMasLargo = 0;
		Materia materiaMayorPrerrequisito = null;

		for (int i = 0; i < materias.size(); i++) {
			int camino = obtenerNumeroCursosSiendoPrerrequisito(
					materias.get(i), longitudes);
			if (camino > caminoMasLargo) {
				caminoMasLargo = camino;
				materiaMayorPrerrequisito = materias.get(i);
			}
		}
		if (materiaMayorPrerrequisito != null) {
			return "La materia con la linea de prerrequistos mas larga es: \n"
					+ "Nombre de la materia: "+materiaMayorPrerrequisito.getNombreMateria()+"\n"
					+ "Código de la materia: "+materiaMayorPrerrequisito.getCodigoMateria()+"\n"
					+ " siendo prerrequisito "+caminoMasLargo + " cursos.";
		} else {
			return "No se encontró el curso que es prerrequisto de la mayor cantidad de otros cursos indirectamente";
		}

	}

	public int obtenerNumeroCursosSiendoPrerrequisito(Materia materia,
			HashMap<String, Integer> longitudes) {
		Integer numMateriasQueEsPre = longitudes
				.get(materia.getCodigoMateria());
		if (numMateriasQueEsPre == null) {
			numMateriasQueEsPre = 0;
			for (int i = 0; i < materia.getPrerrequistoDe().size(); i++) {
				Materia actual = materia.getPrerrequistoDe().get(i);
				numMateriasQueEsPre += obtenerNumeroCursosSiendoPrerrequisito(
						actual, longitudes) + 1;
			}

			longitudes.put(materia.getCodigoMateria(), numMateriasQueEsPre);
		}

		return numMateriasQueEsPre;
	}

	public Programa buscarPrograma(String codigoPrograma) {
		Programa programa = null;
		boolean salir = false;
		for (int i = 0; i < programas.size() && !salir; i++) {
			if (programas.get(i).getCodigoPrograma().equals(codigoPrograma)) {
				programa = programas.get(i);
				salir = true;
			}
		}
		return programa;
	}

	public Programa buscarProgramaNombre(String nombrePrograma){
		Programa programa = null;
		boolean salir = false;
		for (int i = 0; i < programas.size() && !salir; i++) {
			if (programas.get(i).getNombrePrograma().equals(nombrePrograma)) {
				programa = programas.get(i);
				salir = true;
			}
		}
		return programa;
	}
	
	public ArrayList<Materia> materiasPorSemetre(Programa programa, int semestre) {
		ArrayList<Materia> materias = new ArrayList<>();

		ArrayList<Materia> todasMaterias = new ArrayList<>(programa
				.getMaterias().values());

		for (int i = 0; i < todasMaterias.size(); i++) {
			if (todasMaterias.get(i).getSemestre() == semestre) {
				materias.add(todasMaterias.get(i));
			}
		}

		return materias;
	}

	public int mayorSemestre(Programa programa) {
		ArrayList<Materia> todasMaterias = new ArrayList<>(programa
				.getMaterias().values());

		int maximo = 0;
		for (int i = 0; i < todasMaterias.size(); i++) {
			if (todasMaterias.get(i).getSemestre() > maximo) {
				maximo = todasMaterias.get(i).getSemestre();
			}
		}

		return maximo;
	}

	public int[][] floydWarshall(Programa programa) {
		ArrayList<Materia> materias = new ArrayList<>(programa.getMaterias()
				.values());
		HashMap<String, Integer> conversionCodigo = new HashMap<>();
		for (int i = 0; i < materias.size(); i++) {
			conversionCodigo.put(materias.get(i).getCodigoMateria(), i);
		}

		int[][] grafoMatrix = new int[materias.size()][materias.size()];
		int[][] dist = new int[materias.size()][materias.size()];

		for (int i = 0; i < materias.size(); i++) {
			Materia prerequisito = materias.get(i);
			ArrayList<Materia> vecinos = prerequisito.getPrerrequistoDe();
			for (int j = 0; j < vecinos.size(); j++) {
				grafoMatrix[conversionCodigo.get(prerequisito
						.getCodigoMateria())][conversionCodigo.get(vecinos.get(
						j).getCodigoMateria())] = 1;
			}
		}

		for (int i = 0; i < grafoMatrix.length; i++) {
			for (int j = 0; j < grafoMatrix.length; j++) {
				dist[i][j] = grafoMatrix[i][j];
				if (dist[i][j] == 0 && i != j)
					dist[i][j] = Integer.MAX_VALUE;
			}
		}

		int n = grafoMatrix.length;

		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (dist[i][k] != Integer.MAX_VALUE
							&& dist[k][j] != Integer.MAX_VALUE
							&& (dist[i][k] + dist[k][j] < dist[i][j]))
						dist[i][j] = dist[i][k] + dist[k][j];
				}
			}
		}
		
		return dist;

	}

	public ArrayList<Programa> getProgramas() {
		return programas;
	}

	public void setProgramas(ArrayList<Programa> programas) {
		this.programas = programas;
	}

	public ArrayList<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(ArrayList<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public String fechaActual(){
		String fecha = "";
		Date dat = new Date();
		fecha = dat.toString();
		return fecha;
	}
	public void reporteEstudiante(String nomProg, String codEst){
		try {
			Programa programa = buscarProgramaNombre(nomProg);
			Estudiante estudiante = programa.buscarEstudiante(codEst);
			if(estudiante == null){
				JOptionPane.showMessageDialog(null,
						"No se encontró el estudiante, pruebe nuevamente.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			else{
				FileOutputStream archivo = new FileOutputStream("pdf/reporteEstudiante.pdf");
				Font fuente1 = new Font();
				fuente1.setSize(15);
				fuente1.setStyle(Font.BOLD);
				Document documento = new Document();
				Font fuente2 = new Font();
				fuente2.setSize(13);
					PdfWriter.getInstance(documento, archivo);
					documento.open();
					documento.add(new Paragraph("Universidad icesi "+fechaActual(), fuente1));
					documento.add(new Paragraph("Nombre del programa: "+programa.getNombrePrograma(), fuente1));
					documento.add(new Paragraph("Código del programa: "+programa.getCodigoPrograma(), fuente1));
					documento.add(new Paragraph(" "));
					documento.add(new Paragraph("Código del estudiante: "+estudiante.getCodigoEstudiante()+"", fuente1));
					documento.add(new Paragraph(" "));
					documento.add(new Paragraph("Materias aprobadas:", fuente1));
					ArrayList<EstudianteMateria> matAprobadas = programa.materiasAprobadas(estudiante);
					if(matAprobadas.isEmpty()){
						documento.add(new Paragraph("No se econtraron materias aprobadas.", fuente2));
						documento.add(new Paragraph(" "));
					}
					else{
						for(EstudianteMateria matA : matAprobadas){
							documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
							documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
							documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
							documento.add(new Paragraph(" "));
						}
					}
					documento.add(new Paragraph("Materias perdidas:", fuente1));
					ArrayList<EstudianteMateria> matPerdidas = programa.materiasPerdidas(estudiante);
					if(matPerdidas.isEmpty()){
						documento.add(new Paragraph("No se econtraron materias perdidas.", fuente2));
						documento.add(new Paragraph(" "));
					}
					else{
						for(EstudianteMateria matA : matPerdidas){
							documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
							documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
							documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
							documento.add(new Paragraph(" "));
						}
					}
					documento.add(new Paragraph("Materias con inconsitencias:", fuente1));
					ArrayList<EstudianteMateria> matInconsistencia = programa.materiasInconsistencia(estudiante);
					if(matInconsistencia.isEmpty()){
						documento.add(new Paragraph("No se econtraron materias con inconsistencias.", fuente2));
						documento.add(new Paragraph(" "));
					}
					else{
						for(EstudianteMateria matA : matInconsistencia){
							documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
							documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
							documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
							documento.add(new Paragraph(" "));
						}
					}
					documento.add(new Paragraph("Materias que fueron repetidas:", fuente1));
					ArrayList<EstudianteMateria> matRepetidas = programa.materiasRepetidas(estudiante);
					if(matRepetidas.isEmpty()){
						documento.add(new Paragraph("No se econtraron materias que fueron repetidas.", fuente2));
					}
					else{
						for(EstudianteMateria matA : matRepetidas){
							documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
							documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
							documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
							documento.add(new Paragraph(" "));
						}
					}
					documento.close();
					JOptionPane.showMessageDialog(null,
							"Reporte del estudiante ("+estudiante.getCodigoEstudiante()+") generado correctamente.", "Exito",
							JOptionPane.INFORMATION_MESSAGE);
					Desktop.getDesktop().open(new File("pdf/reporteEstudiante.pdf"));
			}
		} catch (DocumentException | IOException e) {
			JOptionPane.showMessageDialog(null, "Error al generar el reporte: "+e.getMessage());
		}
	}
	
	public void reportePrograma(String nomProg){
		try {
			Programa programa = buscarProgramaNombre(nomProg);
			if(programa.getEstudiantes().isEmpty()){
				JOptionPane.showMessageDialog(null,
						"No se encontraron estudiantes para generar el reporte.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			else{
				FileOutputStream archivo = new FileOutputStream("pdf/reportePrograma.pdf");
				Font fuente1 = new Font();
				fuente1.setSize(15);
				fuente1.setStyle(Font.BOLD);
				Document documento = new Document();
				Font fuente2 = new Font();
				fuente2.setSize(13);
				PdfWriter.getInstance(documento, archivo);
				documento.open();
				documento.add(new Paragraph("Universidad icesi "+fechaActual(), fuente1));
				documento.add(new Paragraph("Nombre del programa: "+programa.getNombrePrograma(), fuente1));
				documento.add(new Paragraph("Código del programa: "+programa.getCodigoPrograma(), fuente1));
				for(Estudiante estudiante : programa.getEstudiantes()){
						documento.add(new Paragraph(" "));
						documento.add(new Paragraph("Código del estudiante: "+estudiante.getCodigoEstudiante()+"", fuente1));
						documento.add(new Paragraph("Materias aprobadas:", fuente1));
						ArrayList<EstudianteMateria> matAprobadas = programa.materiasAprobadas(estudiante);
						if(matAprobadas.isEmpty()){
							documento.add(new Paragraph("No se econtraron materias aprobadas.", fuente2));
							documento.add(new Paragraph(" "));
						}
						else{
							for(EstudianteMateria matA : matAprobadas){
								documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
								documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
								documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
								documento.add(new Paragraph(" "));
							}
						}
						documento.add(new Paragraph("Materias perdidas:", fuente1));
						ArrayList<EstudianteMateria> matPerdidas = programa.materiasPerdidas(estudiante);
						if(matPerdidas.isEmpty()){
							documento.add(new Paragraph("No se econtraron materias perdidas.", fuente2));
							documento.add(new Paragraph(" "));
						}
						else{
							for(EstudianteMateria matA : matPerdidas){
								documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
								documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
								documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
								documento.add(new Paragraph(" "));
							}
						}
						documento.add(new Paragraph("Materias con inconsitencias:", fuente1));
						ArrayList<EstudianteMateria> matInconsistencia = programa.materiasInconsistencia(estudiante);
						if(matInconsistencia.isEmpty()){
							documento.add(new Paragraph("No se econtraron materias con inconsistencias.", fuente2));
							documento.add(new Paragraph(" "));
						}
						else{
							for(EstudianteMateria matA : matInconsistencia){
								documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
								documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
								documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
								documento.add(new Paragraph(" "));
							}
						}
						documento.add(new Paragraph("Materias que fueron repetidas:", fuente1));
						ArrayList<EstudianteMateria> matRepetidas = programa.materiasRepetidas(estudiante);
						if(matRepetidas.isEmpty()){
							documento.add(new Paragraph("No se econtraron materias que fueron repetidas.", fuente2));
						}
						else{
							for(EstudianteMateria matA : matRepetidas){
								documento.add(new Paragraph("Nombre materia: "+matA.getMateria().getNombreMateria(), fuente2));
								documento.add(new Paragraph("Código materia: "+matA.getMateria().getCodigoMateria(), fuente2));
								documento.add(new Paragraph("Nota obtenida: "+matA.getNota(), fuente2));
								documento.add(new Paragraph(" "));
							}
						}
				}
				JOptionPane.showMessageDialog(null,
						"Reporte del programa de ("+programa.getNombrePrograma()+") generado correctamente.", "Exito",
						JOptionPane.INFORMATION_MESSAGE);
					documento.close();
					Desktop.getDesktop().open(new File("pdf/reportePrograma.pdf"));
			}
		} catch (DocumentException | IOException e) {
			JOptionPane.showMessageDialog(null, "Error al generar el reporte: "+e.getMessage());
		}
	}
	
	public void materiaVSMaterias(String nombre){
		try {
			Programa programa1 = buscarProgramaNombre(nombre);
			int[][] dist = floydWarshall(programa1);
			FileOutputStream archivo = new FileOutputStream("pdf/floyd.pdf");
			Font fuente1 = new Font();
			fuente1.setSize(15);
			fuente1.setStyle(Font.BOLD);
			Document documento = new Document();
			Font fuente2 = new Font();
			fuente2.setSize(13);
			PdfWriter.getInstance(documento, archivo);
			documento.open();
			documento.add(new Paragraph("Universidad icesi "+fechaActual(), fuente1));
			documento.add(new Paragraph("Nombre del programa: "+programa1.getNombrePrograma(), fuente1));
			documento.add(new Paragraph("Código del programa: "+programa1.getCodigoPrograma(), fuente1));
			documento.add(new Paragraph(" "));
			ArrayList<Materia> materias = new ArrayList<>(programa1.getMaterias()
					.values());
			StringBuilder cadena = new StringBuilder();
			for (int i = 0; i < dist.length; i++) {
				for (int j = 0; j < dist.length; j++) {
					if(dist[i][j] != Integer.MAX_VALUE){
						documento.add(new Paragraph("La relacion entre la materia "+materias.get(i).getCodigoMateria() + " - "+ materias.get(i).getNombreMateria() +" y la materia "+materias.get(j).getCodigoMateria()+  " - "+ materias.get(j).getNombreMateria()+ " es "+dist[i][j]+" \n", fuente2));
					}
				}
			}
			documento.close();
			Desktop.getDesktop().open(new File("pdf/floyd.pdf"));
		} catch (DocumentException | IOException e) {
			JOptionPane.showMessageDialog(null, "Error al generar el reporte: "+e.getMessage());
		}
	}
}
