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

	private Environment environment;
	
	@Override
	public Context build(Context<Object> context) {
		context.setId("evacuation");
		
		environment = new Environment(context);
					
		/* initialize population */
		Parameters params = RunEnvironment.getInstance().getParameters();
		int womenCount = params.getInteger ("women count");
		for (int i = 0; i < womenCount; i++){
			context.add(new Woman(environment));
		}
		
		int menCount = params.getInteger ("men count");
		for (int i = 0; i < menCount; i++){
			context.add(new Man(environment));
		}
		
		int childrenCount = params.getInteger ("children count");
		for (int i = 0; i < childrenCount; i++){
			context.add(new Child(environment));
		}
				
		for (Object obj : context) {
			environment.place(obj);
		}
		
		NetworkBuilder < Object > netBuilder = new NetworkBuilder < Object >
		("evacuation network", context , true );
		netBuilder.buildNetwork();
		
		return context;
	}
}
