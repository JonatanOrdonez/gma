package interfaz;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import mundo.Materia;
import mundo.Programa;

public class InterfazArbol extends JFrame{

	//Atributos
	private InterfazOpciones principal;
	private JTree arbol;
	
	public InterfazArbol(InterfazOpciones principal, Materia codigoMateria){
		this.principal = principal;
		setSize(480, 520);
		setResizable(false);
		setLocationRelativeTo(principal);
		pintar(codigoMateria);
	}
	
	private void pintar(Materia materia){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(materia.getCodigoMateria() + " - "+materia.getNombreMateria());
		cargarNodos(root, materia);	
		arbol = new JTree(root);		
		
		JScrollPane treeView = new JScrollPane(arbol);
		add(treeView);
	}
	
	private void cargarNodos(DefaultMutableTreeNode nodo, Materia materia) {
		ArrayList<Materia> prerrequisitoDe = materia.getPrerrequistoDe();
		for (int i = 0; i < prerrequisitoDe.size(); i++) {
			Materia materiaHija = prerrequisitoDe.get(i);
			DefaultMutableTreeNode nodoHijo = new DefaultMutableTreeNode(materiaHija.getCodigoMateria() + " - "+materiaHija.getNombreMateria());
			nodo.add(nodoHijo);
			cargarNodos(nodoHijo, materiaHija);	
		}
	}
}
