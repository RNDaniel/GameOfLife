package gameoflife;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
// A grafikus megjelenítéshez
public class GolGui extends JFrame {
	
	private ActionListener timeaction;	
	private TableLogic gamePanel;
	private JPanel buttonPanel;
	private JMenuItem loadfile;
	private JTextArea area;
	private Button start, next;
	private Timer timer;	
	private int speed;	
	
	public GolGui(){		
		//A frame
		setTitle( "Game Of Life" );
	    setBackground( Color.gray );
	    setPreferredSize( new Dimension( 821,928 ) ); 
	    
	    //Panelek a frame-en
	    JPanel mainPanel = new JPanel();
	    getContentPane().add( mainPanel );	    
	    mainPanel.setLayout( new BorderLayout() );
	    gamePanel = new TableLogic();
	    mainPanel.add( gamePanel, BorderLayout.CENTER );
	    createButtonPanel();
	    mainPanel.add( buttonPanel, BorderLayout.SOUTH );  
	    
	    //A menü
	    JMenuBar menubar = new JMenuBar();
	    JMenu file = new JMenu( "File" );
	    loadfile = new JMenuItem( "Open File" );
	    menubar.add(file);
	    file.add( loadfile );
	    ( this ).setJMenuBar( menubar );
	    
	    //A generáció számláláshoz	    
	    area = new JTextArea();
	    area.setText( gamePanel.getGeneration() + "  Generation" );
	    gamePanel.add( area );  
	    
	    //A kirajzolás sebességét egy timer biztosítja	      
	    timeaction = new ActionListener(){	       	
	    	public void actionPerformed( ActionEvent e ) {
	    	area.setText( gamePanel.getGeneration() + "  Generation" );
	    	gamePanel.play();
	    	gamePanel.drawing();	    	
	    	}	    	
	    };
	    speed = 100;
	    timer = new Timer( speed, timeaction );	
	    //Dialógusablak a menühöz
	    fileOpener();  
	}
	
	//Akciógombok. A slower és faster gombokkal lehet a kirajzolás sebességét megváltoztatni.
	private void createButtonPanel(){		
		buttonPanel= new JPanel();
		buttonPanel.setLayout( new FlowLayout() );
		buttonPanel.setPreferredSize(new Dimension( 900,60 ) );
		
		Dimension buttonsize = new Dimension( 105,60 );
		
		Button slower  = new Button( "<< Slower" );		
		slower.setPreferredSize( buttonsize );
		slower.setFont(new Font( "Times New Roman", Font.ITALIC, 20 ) );
		slower.setEnabled( false );
		buttonPanel.add( slower );		
		
		Button faster  = new Button("Faster >>");		
		faster.setPreferredSize( buttonsize );
		faster.setFont(new Font( "Times New Roman", Font.ITALIC, 20 ) );
		faster.setEnabled( false );
		buttonPanel.add( faster );		
		
		start  = new Button( "Start" );
		start.setPreferredSize( buttonsize );
		start.setFont(new Font( "Times New Roman", Font.ITALIC, 20 ) );	
		start.setEnabled( false );
		buttonPanel.add( start );
		
		Button stop  = new Button( "Stop" );
		stop.setPreferredSize( buttonsize );
		stop.setFont( new Font( "Times New Roman", Font.ITALIC, 20 ) );
		stop.setEnabled( false );
		buttonPanel.add( stop );		
		
		next  = new Button( "Next" );
		next.setPreferredSize( buttonsize );
		next.setFont( new Font( "Times New Roman", Font.ITALIC, 20 ) );
		next.setEnabled( false );
		buttonPanel.add( next );
		
		slower.addActionListener( e -> {
			timer.stop();
			slower();
			timer.start();
		} );
		
		faster.addActionListener( e -> {
			if(speed>0){
				timer.stop();			
				faster();			
				timer.start();
			}
		} );
		
		start.addActionListener( e -> {
			timer.start();
			start.setEnabled( false );
			next.setEnabled( false );
	        stop.setEnabled( true );
	        slower.setEnabled( true );
			faster.setEnabled( true );
		} );
		
		stop.addActionListener( e -> {
			timer.stop();
			start.setEnabled( true );
			next.setEnabled( true );
			slower.setEnabled( false );
			faster.setEnabled( false );
	        stop.setEnabled( false );
		} );
		
		next.addActionListener( e -> {
			area.setText(gamePanel.getGeneration() + "  Generation");
			gamePanel.play();
	    	gamePanel.drawing();
	    	
		} );		
		
	}	
	
	//Dialógusablak csakis lif kiterjesztésû fileokhoz.
	private void fileOpener(){
		
		loadfile.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed( ActionEvent event ) {		    	
		    JFileChooser chooser = new JFileChooser();
		    FileFilter filter = new FileNameExtensionFilter( "LIF file", "lif" );
		    chooser.setAcceptAllFileFilterUsed( false );
		    chooser.addChoosableFileFilter( filter );  
		    
		    int returnVal = chooser.showOpenDialog( GolGui.this );
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		File file = chooser.getSelectedFile();
		    		String filePath = file.getAbsolutePath();
		    		gamePanel.LoadFile( filePath );
		    		start.setEnabled( true );		    		
		    		next.setEnabled( true );
		    	}
		    }
		});
	}
	private void slower(){
		speed+=25;		
		timer = new Timer( speed, timeaction );		
	}
	private void faster(){		
		speed-=25;		
		timer = new Timer( speed, timeaction );		
	}	
}
