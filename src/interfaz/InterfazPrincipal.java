package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import mundo.Estudiante;
import mundo.Programa;
import mundo.Universidad;

public class InterfazPrincipal extends JFrame implements ActionListener{

	//Atributos
	private PanelMaterias pMaterias;
	private JComboBox<String> boxProgramas;
	private JButton butCargarEstudiantes;
	private JButton butCargarPrograma;
	private JLabel labProgramaSeleccionado;
	private JTextField txtCodigoEstudiante;
	private JLabel labCodigo;
	private JButton butBuscar;
	private JButton butOpciones;
	private InterfazOpciones iOpciones;
	
	private Universidad universidad;

	//Constructor
	public InterfazPrincipal() {
		setSize(950,650);
		setResizable(false);
		setTitle("Matrícula Académica Icesi 1.0");
		setLocationRelativeTo(this);
		setLayout(new FlowLayout());
		pMaterias = new PanelMaterias(this);
		JScrollPane sP = new JScrollPane(pMaterias);
		sP.setPreferredSize(new Dimension(930, 450));
		sP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		universidad = new Universidad();
		iOpciones = new InterfazOpciones(this);
		
		JPanel panelGeneral = new JPanel();
		panelGeneral.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelGeneral.setPreferredSize(new Dimension(930, 150));
		panelGeneral.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		panelGeneral.setBackground(Color.WHITE);

			JPanel panelCarga = new JPanel();
			panelCarga.setPreferredSize(new Dimension(250, 140));
			panelCarga.setBackground(Color.WHITE);
			panelCarga.setBorder(BorderFactory.createTitledBorder("Cargar"));
			butCargarPrograma = new JButton("Cargar programa académico");
			butCargarPrograma.setActionCommand("cargarPrograma");
			butCargarPrograma.addActionListener(this);
			butCargarEstudiantes = new JButton("         Cargar estudiantes         ");
			butCargarEstudiantes.setActionCommand("cargarEstudiantes");
			butCargarEstudiantes.addActionListener(this);
			butCargarEstudiantes.setEnabled(false);
			butOpciones = new JButton("Opciones");
			butOpciones.setActionCommand("opciones");
			butOpciones.addActionListener(this);
//			butOpciones.setEnabled(false);
			JLabel labAux = new JLabel("------------------------Opciones------------------------");
			panelCarga.add(butCargarPrograma);
			panelCarga.add(butCargarEstudiantes);
			panelCarga.add(labAux);
			panelCarga.add(butOpciones);
			panelGeneral.add(panelCarga);
			
			JPanel panelPrograma = new JPanel();
			panelPrograma.setPreferredSize(new Dimension(250, 140));
			panelPrograma.setBackground(Color.WHITE);
			panelPrograma.setBorder(BorderFactory.createTitledBorder("Seleccione un programa"));
			labProgramaSeleccionado = new JLabel("Ninguno",SwingConstants.CENTER);
			labProgramaSeleccionado.setForeground(Color.BLUE);
			labProgramaSeleccionado.setPreferredSize(new Dimension(200, 30));
			boxProgramas = new JComboBox<String>();
			boxProgramas.setPreferredSize(new Dimension(200, 30));
			panelPrograma.add(new JLabel("Programa seleccionado:"));
			panelPrograma.add(labProgramaSeleccionado);
			panelPrograma.add(boxProgramas);
			panelGeneral.add(panelPrograma);
			
			JPanel panelBuscar = new JPanel();
			panelBuscar.setPreferredSize(new Dimension(250, 140));
			panelBuscar.setBackground(Color.WHITE);
			panelBuscar.setBorder(BorderFactory.createTitledBorder("Buscar estudiante"));
			panelBuscar.add(new JLabel("     Código:"));
			labCodigo = new JLabel("...");
			labCodigo.setPreferredSize(new Dimension(150, 20));
			panelBuscar.add(labCodigo);
			txtCodigoEstudiante = new JTextField();
			txtCodigoEstudiante.setPreferredSize(new Dimension(180, 25));
			panelBuscar.add(txtCodigoEstudiante);
			butBuscar = new JButton("Buscar");
			butBuscar.setActionCommand("buscar");
			butBuscar.addActionListener(this);
			panelBuscar.add(butBuscar);
			
			
			panelGeneral.add(panelBuscar);
		
		add(sP);
		add(panelGeneral);
	}
	
	public Universidad getUniversidad() {
		return universidad;
	}

	public void setUniversidad(Universidad universidad) {
		this.universidad = universidad;
	}
	
	private String cargarArchivo() throws Exception{
		  String texto="";   
		   JFileChooser file=new JFileChooser("docs");
		   file.showOpenDialog(null);
		   if(file.getSelectedFile() != null){
			   texto = file.getSelectedFile().getPath();
			   if(!texto.endsWith("txt")){
				   throw new Exception("Formato de archivo inválido");
			   }			   
		   }
		  return texto;
	}
	
	private void cargarProgramas(){
		try {
			String ruta = cargarArchivo();
			if(!ruta.equals("")){
				butCargarEstudiantes.setEnabled(true);
				universidad.cargarMallaCurricular(ruta);
				boxProgramas.removeAllItems();
				for (Programa prog : universidad.getProgramas()) {
					boxProgramas.addItem(prog.getNombrePrograma());
				}
				String texto = boxProgramas.getItemAt(0);
				pMaterias.setNombrePrograma(texto);
				pMaterias.repaint();
				labProgramaSeleccionado.setText(texto);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void cargarEstudiantes(){
		try {
			String ruta = cargarArchivo();
			if(!ruta.equals("")){
				universidad.cargarInformacionEstudiante(ruta);
				butOpciones.setEnabled(true);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public static void main(String[] args) {
		InterfazPrincipal ip = new InterfazPrincipal();
		ip.setVisible(true);
	}

	private void buscarEstudiante(){
		Object obj = boxProgramas.getSelectedItem();
		if(obj == null){
			txtCodigoEstudiante.setText("");
			JOptionPane.showMessageDialog(null, "Por favor cargue un programa académico", "Error", JOptionPane.WARNING_MESSAGE);			
		}
		else{
			String nombrePrograma = obj.toString();
			String codEstudiante = txtCodigoEstudiante.getText();
			Programa prog = universidad.buscarProgramaNombre(nombrePrograma);
			Estudiante est = prog.buscarEstudiante(codEstudiante);
			if(est == null){
				txtCodigoEstudiante.setText("");
				JOptionPane.showMessageDialog(null, "No se encontraron resultados, pruebe nuevamente", "Error", JOptionPane.WARNING_MESSAGE);
			}
			else{
				txtCodigoEstudiante.setText("");
				pMaterias.setNombrePrograma(nombrePrograma);
				pMaterias.setNombreEstudiante(est.getCodigoEstudiante()+"");
				pMaterias.repaint();
				labCodigo.setText(codEstudiante);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if(comando.equals("cargarPrograma")){
			cargarProgramas();
		}
		else if(comando.equals("cargarEstudiantes")){
			cargarEstudiantes();
		}
		else if(comando.equals("buscar")){
			buscarEstudiante();
		}
		else if(comando.equals("opciones")){
			iOpciones.setVisible(true);
			iOpciones.cargarProgramas();
		}
	}
}
