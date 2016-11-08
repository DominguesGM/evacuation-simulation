package evacuation_simulation;

import jade.core.AID;
import repast.simphony.random.RandomHelper;

public class IndependentKnowledgeable extends Person{

	public IndependentKnowledgeable(AID resultsCollector){
		super(resultsCollector);
		
		independence = RandomHelper.nextIntFromTo(MAX_SCALE / 2, MAX_SCALE);
		areaKnowledge = RandomHelper.nextIntFromTo(MAX_SCALE / 2, MAX_SCALE);
	}
	
	
}
