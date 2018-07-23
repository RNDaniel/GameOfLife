package gameoflife;
import javax.swing.*;

public class GolMain {
	
	public static void main(String[] args) {		
		GolGui frame = new GolGui();		
		frame.pack();		
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );		
		frame.setVisible( true ); 		
	}
}
