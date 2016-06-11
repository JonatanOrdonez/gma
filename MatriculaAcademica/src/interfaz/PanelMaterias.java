package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.text.BreakIterator;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mundo.Estudiante;
import mundo.EstudianteMateria;
import mundo.Materia;
import mundo.Programa;

public class PanelMaterias extends JPanel implements MouseListener{

	//Atributos
	private InterfazPrincipal principal;
	private String nombrePrograma;
	private String nombreEstudiante;
	
	//Constructor
	public PanelMaterias(InterfazPrincipal principal) {
		this.principal = principal;
		setBorder(BorderFactory.createLineBorder(Color.BLUE));
		JLabel lab = new JLabel();
		lab.setPreferredSize(new Dimension(1270, 1820));
		nombrePrograma = "";
		nombreEstudiante = "";
		add(lab);
		addMouseListener(this);
	}
	
	

	public String getNombrePrograma() {
		return nombrePrograma;
	}



	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}



	public String getNombreEstudiante() {
		return nombreEstudiante;
	}



	public void setNombreEstudiante(String nombreEstudiante) {
		this.nombreEstudiante = nombreEstudiante;
	}



	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(!nombrePrograma.equals("")){
			Programa programa = principal.getUniversidad().buscarProgramaNombre(nombrePrograma);
			if(nombreEstudiante.equals("")){
				int y = 20;
				for (int i = 1; i <= programa.obtenerSemestreMaximo(); i++) {
					g.setColor(Color.WHITE);
					g.fillRect(20, y, 100, 170);
					g.setColor(Color.BLACK);
					g.drawString("Semestre 0"+i, 30, y+90);
					
					ArrayList<Materia> materias = programa.obtenerMateriasPorSemestre(i);
					int x = 130;
					for (int j = 0; j < materias.size(); j++) {
						Materia materiaActual = materias.get(j);
						String nombreMateria = materiaActual.getNombreMateria();
						if(nombreMateria.length() > 25){
							nombreMateria = nombreMateria.substring(0,25)+"...";
						}
						g.setColor(Color.WHITE);
						g.fillRect(x, y, 180, 40);
						String prerrequisitos = programa.obtenerPrerrequisitos(materiaActual.getCodigoMateria());
						g.setColor(Color.BLACK);
						g.drawString(prerrequisitos, x+10, y+25);
						g.setColor(Color.WHITE);
						g.fillRect(x, y+50, 180, 70);
						g.setColor(Color.BLACK);
						g.drawString(materiaActual.getCodigoMateria(), x+10, y+70);
						g.drawString(nombreMateria, x+10, y+90);
						g.drawString(materiaActual.getCreditos()+"C - "+materiaActual.getIntesidadHoraria()+"H", x+10, y+110);
						g.setColor(Color.WHITE);
						g.fillRect(x, y+130, 180, 40);
						g.setColor(Color.BLACK);
						String postRrequisitos = programa.obtenerPostRrequisitos(materiaActual.getCodigoMateria());
						g.drawString(postRrequisitos, x+10, y+155);
						x+=190;
					}
					g.setColor(Color.LIGHT_GRAY);
					g.drawLine(20, y+175, x-10, y+175);
					
					y += 180;
					g.drawLine(125, 20, 125, y-10);
				}
			}
			else{
				Estudiante estudiante = programa.buscarEstudiante(nombreEstudiante);
				int y = 20;
				for (int i = 1; i <= programa.obtenerSemestreMaximo(); i++) {
					g.setColor(Color.WHITE);
					g.fillRect(20, y, 100, 170);
					g.setColor(Color.BLACK);
					g.drawString("Semestre 0"+i, 30, y+90);
					
					ArrayList<Materia> materias = programa.obtenerMateriasPorSemestre(i);
					int x = 130;
					for (int j = 0; j < materias.size(); j++) {
						Materia materiaActual = materias.get(j);
						EstudianteMateria materiaDelEstudiante = estudiante.darMateriaPorCodigo(materiaActual.getCodigoMateria());
						String nombreMateria = materiaActual.getNombreMateria();
						if(nombreMateria.length() > 25){
							nombreMateria = nombreMateria.substring(0,25)+"...";
						}
						g.setColor(Color.WHITE);
						g.fillRect(x, y, 180, 40);
						String prerrequisitos = programa.obtenerPrerrequisitos(materiaActual.getCodigoMateria());
						g.setColor(Color.BLACK);
						g.drawString(prerrequisitos, x+10, y+25);
						if(materiaDelEstudiante == null){
							g.setColor(Color.WHITE);
							g.fillRect(x, y+50, 180, 70);
							g.setColor(Color.BLACK);
							g.drawString(materiaActual.getCodigoMateria(), x+10, y+70);
							g.drawString(nombreMateria, x+10, y+90);
							g.drawString(materiaActual.getCreditos()+"C - "+materiaActual.getIntesidadHoraria()+"H", x+10, y+110);
						}
						else{
							Color miColor = obtenerColor(materiaDelEstudiante.getColor());
							g.setColor(miColor);
							g.fillRect(x, y+50, 180, 70);
							g.setColor(Color.WHITE);
							g.drawString(materiaActual.getCodigoMateria(), x+10, y+70);
							g.drawString(nombreMateria, x+10, y+90);
							g.drawString(materiaActual.getCreditos()+"C - "+materiaActual.getIntesidadHoraria()+"H", x+10, y+110);
						}
						g.setColor(Color.WHITE);
						g.fillRect(x, y+130, 180, 40);
						g.setColor(Color.BLACK);
						String postRrequisitos = programa.obtenerPostRrequisitos(materiaActual.getCodigoMateria());
						g.drawString(postRrequisitos, x+10, y+155);
						x+=190;
					}
					g.setColor(Color.LIGHT_GRAY);
					g.drawLine(20, y+175, x-10, y+175);
					
					y += 180;
					g.drawLine(125, 20, 125, y-10);
				}
			}
		}
	}
	private Color obtenerColor(char color) {
		if(color == 'r'){
			return Color.RED;
		}
		if(color == 'v'){
			return Color.GREEN;
		}
		if(color == 'n'){
			return Color.ORANGE;
		}
		if(color == 'a'){
			return Color.CYAN;
		}
		return null;
	}



	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
