package evacuation_Simulation;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.random.RandomHelper;

public class Child extends Person{

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
	
	public Child( ContinuousSpace < Object > space , Grid < Object > grid){
		super(space, grid);
		gender = (RandomHelper.nextIntFromTo(0, 1) == 1)? Gender.MALE : Gender.FEMALE;
	}
	
}
