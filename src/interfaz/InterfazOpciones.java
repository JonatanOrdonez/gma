package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import mundo.Materia;
import mundo.Programa;

public class InterfazOpciones extends JFrame implements ActionListener{

	//Atributos
	private JButton butAceptar;
	private InterfazPrincipal principal;
	private JComboBox<String> boxProgramas;
	private JRadioButton lab1;
	private JRadioButton lab2;
	private JRadioButton lab3;
	private JRadioButton lab4;
	private JRadioButton lab5;
	private JRadioButton lab6;
	private JRadioButton lab7;
	
	//Constructor
	public InterfazOpciones(InterfazPrincipal principal) {
		this.principal = principal;
		setSize(490,370);
		setTitle("Opciones");
		setResizable(false);
		setLocationRelativeTo(principal);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		ButtonGroup group = new ButtonGroup();
		lab1 = new JRadioButton("(1)  Generar reporte estudiante.");
		lab1.setPreferredSize(new Dimension(470, 25));
		lab2 = new JRadioButton("(2)  Generar reporte por programa.");
		lab7 = new JRadioButton("(3)  Linea de prerrequisitos más larga.");
		lab7.setPreferredSize(new Dimension(470, 25));
		lab3 = new JRadioButton("(4)  Curso que es prerrequisito de la mayor cantidad de cursos (Directamente).");
		lab4 = new JRadioButton("(5)  Curso que es prerrequisito de la mayor cantidad de cursos (Indirectamente).");
		lab5 = new JRadioButton("(6)  Relación entre todos y cada uno de los cursos.");
		lab6 = new JRadioButton("(7)  Relación de un curso con los demás cursos relacionados con el.");
		group.add(lab1);
		group.add(lab2);
		group.add(lab7);
		group.add(lab3);
		group.add(lab4);
		group.add(lab5);
		group.add(lab6);
		butAceptar = new JButton("Aceptar");
		butAceptar.setActionCommand("aceptar");
		butAceptar.addActionListener(this);
		JLabel labProg = new JLabel("Seleccione un programa académico:");
		labProg.setPreferredSize(new Dimension(470, 30));
		boxProgramas = new JComboBox<String>();
		boxProgramas.setPreferredSize(new Dimension(200, 30));
		JLabel labOp = new JLabel("-----------------------------------------------------Opciones----------------------------------------------------");
		labOp.setForeground(Color.GREEN);
		JLabel labAux = new JLabel();
		labAux.setPreferredSize(new Dimension(190, 30));
		add(labProg);
		add(boxProgramas);
		add(labOp);
		add(lab1);
		add(lab2);
		add(lab7);
		add(lab3);
		add(lab4);
		add(lab5);
		add(lab6);
		add(labAux);
		add(butAceptar);
		lab1.setSelected(true);
	}
	
	public InterfazPrincipal getPrincipal() {
		return principal;
	}

	public void setPrincipal(InterfazPrincipal principal) {
		this.principal = principal;
	}
	
	public void cargarProgramas(){
		ArrayList<Programa> progs = principal.getUniversidad().getProgramas();
		boxProgramas.removeAllItems();
		for(Programa porg : progs){
			boxProgramas.addItem(porg.getNombrePrograma());
		}
	}
	
	private void lab1Action(){
		String programa = boxProgramas.getSelectedItem().toString();
		String texto = JOptionPane.showInputDialog("Digite el código del estudiante:");
		if(!texto.equals("")){
			principal.getUniversidad().reporteEstudiante(programa, texto);
		}
	}
	
	private void lab2Action(){
		principal.getUniversidad().reportePrograma(boxProgramas.getSelectedItem().toString());
	}
	
	private void lab3Action(){
		Programa programa = principal.getUniversidad().buscarProgramaNombre(boxProgramas.getSelectedItem().toString());
		String texto = principal.getUniversidad().prerrequisitoMayorCantidadCursosDirectamente(programa);
		JOptionPane.showMessageDialog(null, texto, "Linea de prerrequisitos", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void lab7Action(){
		Programa programa = principal.getUniversidad().buscarProgramaNombre(boxProgramas.getSelectedItem().toString());
		String texto = principal.getUniversidad().lineaDePrerrequisitosMasLarga(programa);
		JOptionPane.showMessageDialog(null, texto, "Linea de prerrequisitos", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void lab4Action(){
		Programa programa = principal.getUniversidad().buscarProgramaNombre(boxProgramas.getSelectedItem().toString());
		String texto = principal.getUniversidad().prerrequisitoMayorCantidadCursosIndirectamente(programa);
		JOptionPane.showMessageDialog(null, texto, "Linea de prerrequisitos", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void lab6Action(){
		Programa programa = principal.getUniversidad().buscarProgramaNombre(boxProgramas.getSelectedItem().toString());
		Materia materia = mostrarMateria(programa);
		InterfazArbol arbol = new InterfazArbol(this, materia);
		arbol.setVisible(true);
	}
	
	private Materia mostrarMateria(Programa programa){
		
		JLabel labEstilo2 = new JLabel("Seleccione la materia del programa "+programa.getNombrePrograma());
		JComboBox<String> comboProgramas = new JComboBox<String>();
		ArrayList<Materia> materias = new ArrayList<>(programa.getMaterias()
				.values());

		String[] tipoString2 = new String[materias.size()];
		for (int i = 0; i < tipoString2.length; i++) {
			tipoString2[i] = materias.get(i).getCodigoMateria() + " - "+materias.get(i).getNombreMateria();
		}
		comboProgramas = new JComboBox<String>(tipoString2);
		comboProgramas.setSelectedIndex(0);

		final JComponent[] inputs2 = new JComponent[] { labEstilo2,
				comboProgramas };
		JOptionPane.showMessageDialog(null, inputs2, "Materias",
				JOptionPane.PLAIN_MESSAGE);

		Materia materiaSeleccionada = materias.get(comboProgramas.getSelectedIndex());
		
		return materiaSeleccionada;
		
	}
	
	private void lab5Action(){
		String programa = boxProgramas.getSelectedItem().toString();
		principal.getUniversidad().materiaVSMaterias(programa);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if(comando.equals("aceptar")){
			if(lab1.isSelected()){
				lab1Action();
			}
			else if(lab2.isSelected()){
				lab2Action();
			}
			else if(lab3.isSelected()){
				lab3Action();
			}
			else if(lab4.isSelected()){
				lab4Action();
			}
			else if(lab5.isSelected()){
				lab5Action();
			}
			else if(lab6.isSelected()){
				lab6Action();
			}
			else if(lab7.isSelected()){
				lab7Action();
			}
		}
	}

}
