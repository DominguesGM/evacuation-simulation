package evacuation_Simulation;

import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Person {

	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	
	protected Gender gender; 		 	/* MALE / FEMALE */
	protected int age;				 	/* [5, 65] */
	protected float areaKnowledge;	 	/* [0, 1] */
	protected boolean leader;	 	 	/* true/false */
	protected boolean independent;	 	/* true/false */
	protected float physicalIntegrity;	/* [0, 1] */
	protected float fatigue;	 	 	/* [0, 1] */
	protected float panic;		 	 	/* [0, 1] */
	
	protected float initialSpeed;		 	 	
	protected float currentSpeed;
	
	public Person( ContinuousSpace < Object > space , Grid < Object > grid){
			this.space = space ;
			this.grid = grid ;			
			
			fatigue = 0;
			physicalIntegrity = 1;
			panic = 0;
	}
	
	public void moveTowards(GridPoint pt){
		
		// TODO check if there first if there is an obstacle
		
		if(!pt.equals(grid.getLocation(this))){
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
			
			fatigue++; // TODO review this increase 
		}
	}
}
