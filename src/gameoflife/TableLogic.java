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
	
private HashMap<Point, Boolean> table; //Koordin�t�k t�rol�s�ra �s hogy az adott sejt �l vagy sem;
private HashMap<Point, Boolean> livegeneration; //A k�vetkez� gener�ci� �l� tagjai
private Point actual; 
private int generation;

	public TableLogic(){		
		table = new HashMap<Point, Boolean>();
		livegeneration = new HashMap<Point, Boolean>();
		actual = new Point(0,0);
		generation=1;
		/*A table felt�lt�se m�g nem �l� sejtekkel. 
		 *A .lif fileban a (0,0) koordin�ta a j�t�kt�r k�zep�n szerepel, ez�rt indul minuszr�l a cikklus*/
		for( int i = -80; i<=80; i++ ){	
			for( int j = 80; j>=-80; j-- ){					
				table.put( new Point( i,j ), false );
				livegeneration.put( new Point( i,j ), false );	
				
			}
		}				
	}
	/* Filebeolvas�s a k�vetkez� sorrent szerint:
	 * 
	 * 1. #P Kommentig olvas�s, ezut�n a koordin�t�k elt�rol�sa majd integerr� alak�t�sa.		 *   
	 * 2. K�vetkez� sor beolvas�sa �s splitel�se karakterenk�nt
	 * 3. Megvizsg�ljuk hogy * vagy . a karakter, �s ez alapj�n megv�ltoztatjuk a t�bla 
	 *    adott sejtjeit �l�re. (�l = true)
	 * 4. Bez�rjuk a filet �s kirajzoljuk az �l� sejteket.
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
	/*Megvizsg�lja minden sejt �llapot�t �s kirajzolja a koordin�ta alapj�n. 
	 *Mivel a lif fileok koordin�t�i m�sok mint ahogy a fillRect() met�dus m�k�dik,
	 *ez�rt �t kell konvert�lni hogy a helyes koordin�t�n jelenjenek meg a sejtek */
	
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
				applyRule( i,j );//	k�vetkez� gener�ci� kisz�mol�sa		
			}
		}		
		for ( Map.Entry<Point, Boolean> entry : table.entrySet() ) { // A table alaphelyzetbe �ll�t�sa
			table.computeIfPresent( entry.getKey(), ( k, v ) -> v=false );			
	    }
		for (Map.Entry<Point, Boolean> entry : livegeneration.entrySet() ) {
			if(livegeneration.get( entry.getKey() ) ){
				table.computeIfPresent( entry.getKey(), ( k, v ) -> v=true ); //A table felt�lt�se a k�vetkez� gener�ci�val
				livegeneration.computeIfPresent( entry.getKey(), ( k, v ) -> v=false ); //A k�v. gen. alaphelyzetbe �ll�t�sa
			}			
	    }		
	}
	//A 8 szomsz�dos sejt megvizsg�l�sa �s szumm�z�sa
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
        if( neighbours == 3 || ( neighbours==2 && table.get( actual ) ) ){ //k�vetkez� gener�ci� elt�rol�sa egy seg�dt�bl�ban
        	livegeneration.computeIfPresent( actual, ( k, v ) -> v=true );
        }            
    }
	int getGeneration(){
		return generation;
	}
}