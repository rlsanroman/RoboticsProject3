package project3;

public class Location {
	double x;
	double y;
	public Location(double x1, double y1){
		x = x1;
		y = y1;
	}
	
	public Location(String[] points)
	{
		if(points.length == 2)
		{
			x = Double.valueOf(points[0]);
			y = Double.valueOf(points[1]);
		}
		else
			System.out.println("SOMETHINS WRONG");
	}
	public String toString()
	{
		String values = Double.toString(x) + "," + Double.toString(y);
		return values;
	}
}


// x,y