package evacuation_Simulation;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class SimulationBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		context.setId("evacuation");
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace("space", context, new RandomCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.WrapAroundBorders(), 50, 50);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(),
						true, 50, 50));
				
		/* initialize population */
		Parameters params = RunEnvironment.getInstance().getParameters();
		int womenCount = params.getInteger ("women count");
		for (int i = 0; i < womenCount; i++){
			context.add(new Woman(space,grid));
		}
		
		int menCount = params.getInteger ("men count");
		for (int i = 0; i < menCount; i++){
			context.add(new Man(space,grid));
		}
		
		int childrenCount = params.getInteger ("children count");
		for (int i = 0; i < childrenCount; i++){
			context.add(new Child(space,grid));
		}
				
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj,  (int)pt.getX(), (int)pt.getY());
		}
		
		NetworkBuilder < Object > netBuilder = new NetworkBuilder < Object >
		("evacuation network", context , true );
		netBuilder.buildNetwork();
		
		return context;
	}
}
