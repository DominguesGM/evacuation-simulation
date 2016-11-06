package evacuation_simulation;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import repast.simphony.context.Context;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;
import sajas.domain.DFService;
import serviceConsumerProviderVis.onto.ServiceOntology;
import serviceConsumerProviderVis.onto.ServiceProposalRequest;

public class Person extends Agent{
	public static int PANIC_VARIATION = 10;
	public static int FATIGUE_VARIATION = 10;
	public static int PHYSICAL_INTEGRITY_VARIATION = 10;
	
	protected AID resultsCollector;

	//////////////////////////////////////////////////////////////////////////
	protected Context<?> context;
	protected Network<Object> net;
	
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	
	///////////////////////////////////////////////////////////////////////
	protected Gender gender; 		 	/* MALE / FEMALE */
	protected int age;				 	/* [5, 65] */
	protected float areaKnowledge;	 	/* [0, 100] */
	protected boolean leader;	 	 	/* true/false */
	protected boolean independent;	 	/* true/false */
	protected float physicalIntegrity;	/* [0, 100] */
	protected float fatigue;	 	 	/* [0, 100] */
	protected float panic;		 	 	/* [0, 100] */
	
	protected float initialSpeed;		 	 	
	protected float currentSpeed;
	
	///////////////////////////////////////////////////////////////////////////
	private Codec codec;
	private Ontology serviceOntology;	
	protected ACLMessage myCfp;

	
	public Person(AID resultsCollector, Gender gender){
			this.resultsCollector = resultsCollector;
			this.gender = gender;
//			this.space = environment.space ;
//			this.grid = environment.grid ;			
			
			fatigue = 0;
			physicalIntegrity = 100;
			panic = 0;
			
			// TODO initialize other parameters
	}
	
	@Override
	public void setup() {
		
		// TODO understand what this does!!!!!!!!!

		// register language and ontology
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);
		
		// subscribe DF
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("service-provider");
		template.addServices(sd);
//		addBehaviour(new DFSubscInit(this, template));

		// prepare cfp message
		myCfp = new ACLMessage(ACLMessage.CFP);
		myCfp.setLanguage(codec.getName());
		myCfp.setOntology(serviceOntology.getName());
		myCfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
//		//
//		ServiceProposalRequest serviceProposalRequest = new ServiceProposalRequest(requiredService);
//		try {
//			getContentManager().fillContent(myCfp, serviceProposalRequest);
//		} catch (CodecException | OntologyException e) {
//			e.printStackTrace();
//		}
		
		// waker behaviour for starting CNets
		//addBehaviour(new StartCNets(this, 2000));
	}
	
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	public void moveTowards(GridPoint pt){
		
		// TODO check if there first if there is an obstacle or person
		
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
