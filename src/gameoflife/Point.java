package gameoflife;
//Pont osztály a koordináták tárolásához
public class Point {
	int x;
	int y;
	Point(int x, int y){
		this.x=x;
		this.y=y;
	}

	@Override 
	public int hashCode( ) {
		   int result = 11;
		   result = 17 * result + x;
		   result = 17 * result + y;
		   return result;
	}
	@Override
	public boolean equals( Object o) {
		if(o instanceof Point){
			Point other = (Point) o;
			if( this.x==other.x && this.y==other.y){
				return true;
			}
		}
		return false;
	}	 
		
}
