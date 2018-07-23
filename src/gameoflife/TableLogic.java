package gameoflife;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.*;

//Minden ami "Back-End"
public class TableLogic extends JPanel {
	
private HashMap<Point, Boolean> table; //Koordináták tárolására és hogy az adott sejt él vagy sem;
private HashMap<Point, Boolean> livegeneration; //A következõ generáció élõ tagjai
private Point actual; 
private int generation;

	public TableLogic(){		
		table = new HashMap<Point, Boolean>();
		livegeneration = new HashMap<Point, Boolean>();
		actual = new Point(0,0);
		generation=1;
		/*A table feltöltése még nem élõ sejtekkel. 
		 *A .lif fileban a (0,0) koordináta a játéktér közepén szerepel, ezért indul minuszról a cikklus*/
		for( int i = -80; i<=80; i++ ){	
			for( int j = 80; j>=-80; j-- ){					
				table.put( new Point( i,j ), false );
				livegeneration.put( new Point( i,j ), false );	
				
			}
		}				
	}
	/* Filebeolvasás a következõ sorrent szerint:
	 * 
	 * 1. #P Kommentig olvasás, ezután a koordináták eltárolása majd integerré alakítása.		 *   
	 * 2. Következõ sor beolvasása és splitelése karakterenként
	 * 3. Megvizsgáljuk hogy * vagy . a karakter, és ez alapján megváltoztatjuk a tábla 
	 *    adott sejtjeit élõre. (él = true)
	 * 4. Bezárjuk a filet és kirajzoljuk az élõ sejteket.
	 *    
	 * */
	public void LoadFile( String filePath ){		
		try {			
			BufferedReader br = new BufferedReader( new FileReader( filePath ) );		    
		    String line = br.readLine();
		    String[] splitline;		    
		    int x =0;
		    int n=0;
		    int y=0;
		    while ( line != null ) {		    	
		        splitline = line.split( " ", 3 );		       
		        if(splitline[0].equals( "#P" )){		        	
		        	x = Integer.parseInt( splitline[1] );		        	
		        	y = Integer.parseInt( splitline[2] ); 
		        	n=x;
		        }
		        splitline = splitline[0].split( "" );
		        
		        if( splitline[0].equals( "." )|| splitline[0].equals( "*" ) ){
		        	for( String s:splitline ){		        		
		        		if(s.equals( "*" )){     			
		        			
		        			actual.x=x;
		        			actual.y=y;		        			
		        			table.computeIfPresent( actual, ( k, v ) -> v=true );		        			
		        		}
		        		x++;	        	
		        	}
		        	y--;
		        	x=n;
		        }		     
		        line = br.readLine();
		    }
		drawing();
		br.close();
		}catch ( FileNotFoundException e ) {
		      e.printStackTrace();
	    } catch ( IOException e ) {
	      e.printStackTrace();
	    } 
	}
	/*Megvizsgálja minden sejt állapotát és kirajzolja a koordináta alapján. 
	 *Mivel a lif fileok koordinátái mások mint ahogy a fillRect() metódus mûködik,
	 *ezért át kell konvertálni hogy a helyes koordinátán jelenjenek meg a sejtek */
	
	public void paintComponent( Graphics g ){		
		super.paintComponent( g );		
		for( int i =-80; i<=80; i++ ){
			for( int j = 80; j>=-80; j-- ){				
				actual.x=i;
				actual.y=j;									
				if( table.get( actual ) ){					
					g.setColor( Color.BLUE );
					g.fillRect( 400+i*5, 400-j*5, 5, 5 );				
				}
			}
		}		
	}	  
	
	void drawing(){
		repaint();
	}
	
	void play(){	
		generation++;
		
		for( int i =-80; i<=80; i++ ){
			for( int j = 80; j>=-80; j-- ){				
				applyRule( i,j );//	következõ generáció kiszámolása		
			}
		}		
		for ( Map.Entry<Point, Boolean> entry : table.entrySet() ) { // A table alaphelyzetbe állítása
			table.computeIfPresent( entry.getKey(), ( k, v ) -> v=false );			
	    }
		for (Map.Entry<Point, Boolean> entry : livegeneration.entrySet() ) {
			if(livegeneration.get( entry.getKey() ) ){
				table.computeIfPresent( entry.getKey(), ( k, v ) -> v=true ); //A table feltöltése a következõ generációval
				livegeneration.computeIfPresent( entry.getKey(), ( k, v ) -> v=false ); //A köv. gen. alaphelyzetbe állítása
			}			
	    }		
	}
	//A 8 szomszédos sejt megvizsgálása és szummázása
	private void applyRule( int x, int y ) {
		
        int neighbours = 0;
        actual.x=x;
        actual.y=y+1;
        
        if( y<80 && table.get( actual ) ){
        	neighbours++;        	
        }
        actual.x=x+1;
        if( y<80 && x<80 && table.get( actual ) ){
        	neighbours++;        	
        }
        actual.y=y;
        if( x<80 && table.get( actual ) ){
        	neighbours++;
        }
        actual.y=y-1;
        if( y>-80 && x<80 && table.get( actual ) ){
        	neighbours++;        	
        }
        actual.x=x;
        if( y>-80 && table.get( actual ) ){
        	neighbours++;        	
        }
        actual.x=x-1;
        if( y>-80 && x>-80 && table.get( actual ) ){
        	neighbours++;        	
        }
        actual.y=y;
        if( x>-80 && table.get( actual ) ){
        	neighbours++;        	
        }
        actual.y=y+1;
        if( y<80 && x>-80 && table.get( actual ) ){
        	neighbours++;        	
        } 
        actual.x=x;
        actual.y=y;        
        if( neighbours == 3 || ( neighbours==2 && table.get( actual ) ) ){ //következõ generáció eltárolása egy segédtáblában
        	livegeneration.computeIfPresent( actual, ( k, v ) -> v=true );
        }            
    }
	int getGeneration(){
		return generation;
	}
}