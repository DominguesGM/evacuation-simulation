package evacuation_simulation;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;
import sajas.domain.DFService;
import serviceConsumerProviderVis.onto.ServiceOntology;

public class Person extends Agent{
	public static final int PANIC_VARIATION = 10;
	public static final int FATIGUE_VARIATION = 5;
	public static final int MOBILITY_VARIATION = 15;
	
	public static final int MAX_AGE = 65;
	public static final int MIN_AGE = 5;
	
	public static final int MAX_SCALE= 100;
	public static final int MIN_SCALE= 0;
	
	protected AID resultsCollector;

	//////////////////////////////////////////////////////////////////////////
	protected Context<?> context;
	protected Network<Object> net;
	
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	
	/*
	 * Human attributes
	 */
	protected int areaKnowledge;	 /* [0, 100] */
	protected int altruism;			 /* [0, 100] */
	protected int independence;	 	 /* [0, 100] */
	
	protected int fatigue;	 	 	 /* [0, 100] */
	protected int mobility;			 /* [0, 100] */
	protected int panic;		 	 /* [0, 100] */
	
	protected Gender gender; 		 /* MALE / FEMALE */
	protected int age;				 /* [5, 65] */
	
	protected float maxSpeed;		 	 	
	protected float currentSpeed;
	
	///////////////////////////////////////////////////////////////////////////
	private Codec codec;
	private Ontology serviceOntology;	
	protected ACLMessage myCfp;

	
	public Person(AID resultsCollector){
			this.resultsCollector = resultsCollector;		
			
			areaKnowledge = MAX_SCALE / 2;
			independence = MAX_SCALE / 2;
			// TODO check the use Normal distribution
			//altruistic = RandomHelper.getBinomial().nextIntFromTo(MIN_SCALE, MAX_SCALE);
			altruism = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE);
			
			fatigue = MIN_SCALE;
			mobility = MAX_SCALE;
			panic = MIN_SCALE;			
			
			gender = (RandomHelper.nextIntFromTo(0, 1) == 1) ? Gender.MALE : Gender.FEMALE;
			
			// TODO check the use Normal distribution
			//age = RandomHelper.getBinomial().nextInt(MIN_AGE, MAX_AGE);
			age = RandomHelper.nextIntFromTo(MIN_AGE, MAX_AGE);
			
			maxSpeed = gender.getMaxSpeed();
			if(age < 18 || age >35)
				maxSpeed -= 10;
			
			if(age>45)
				maxSpeed -= 10;

			currentSpeed = maxSpeed  / 2;
	}
	
	
	
	/**
	 * @return the areaKnowledge
	 */
	public int getAreaKnowledge() {
		return areaKnowledge;
	}

	/**
	 * @param areaKnowledge the areaKnowledge to set
	 */
	public void setAreaKnowledge(int areaKnowledge) {
		this.areaKnowledge = enforceBounds(areaKnowledge);
	}

	/**
	 * @return the altruism
	 */
	public int getAltruism() {
		return altruism;
	}

	/**
	 * @param altruism the altruism to set
	 */
	public void setAltruism(int altruism) {
		this.altruism = enforceBounds(altruism);
	}

	/**
	 * @return the independence
	 */
	public int getIndependence() {
		return independence;
	}

	/**
	 * @param independence the independence to set
	 */
	public void setIndependence(int independence) {
		this.independence = enforceBounds(independence);
	}

	/**
	 * @return the fatigue
	 */
	public int getFatigue() {
		return fatigue;
	}

	/**
	 * @param fatigue the fatigue to set
	 */
	public void setFatigue(int fatigue) {
		this.fatigue = enforceBounds(fatigue);
	}

	/**
	 * @return the mobility
	 */
	public int getMobility() {
		return mobility;
	}

	/**
	 * @param mobility the mobility to set
	 */
	public void setMobility(int mobility) {
		this.mobility = enforceBounds(mobility);
	}

	/**
	 * @return the panic
	 */
	public int getPanic() {
		return panic;
	}

	/**
	 * @param panic the panic to set
	 */
	public void setPanic(int panic) {
		this.panic = enforceBounds(panic);
	}

	/**
	 * @return the currentSpeed
	 */
	public float getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * @param currentSpeed the currentSpeed to set
	 */
	public void setCurrentSpeed(float currentSpeed) {
		if(currentSpeed > maxSpeed){
			this.currentSpeed = currentSpeed;
		}else{
			this.currentSpeed = maxSpeed;
		}
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return the maxSpeed
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}
	
	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/*
	 * generatePanicVariation.
	 * Updates the panic level.
	 * Younger and older people are more prone to panic variations.
	 * The panic increases faster than it decreases.
	 * @param isIncrease  
	 */
	public void generatePanicVariation(boolean isIncrease){
		int variation = PANIC_VARIATION * (isIncrease ? 1 : -1);
		
		// younger and older people are more prone to panic variations 
		if(age < MAX_AGE / 3 || age > 2 * MAX_AGE / 3){
			if(isIncrease){
				variation *= 1.2;	
			}else{
				variation *= 0.7;
			}
		}else{
			if(!isIncrease){
				variation *= 0.8;
			}
		}
		
		setPanic(panic + variation);		
	}
	
	/*
	 * generateFatigueVariation.
	 * Updates the fatigue level.
	 * Younger and older people are more prone to fatigue variations.
	 * The fatigue increases faster than it decreases when recovering.
	 * @param isIncrease  
	 */
	public void generateFatigueVariation(boolean isIncrease){
		int variation = FATIGUE_VARIATION * (isIncrease ? 1 : -1);
		
		// younger and older people are more prone to fatigue variations 
		if(age < MAX_AGE / 3 || age > 2 * MAX_AGE / 3){
			if(isIncrease){
				variation *= 1.2;	
			}else{
				variation *= 0.7;
			}
		}else{
			if(!isIncrease){
				variation *= 0.8;
			}
		}
		
		setFatigue(fatigue + variation);		
	}
	
	/*
	 * generateMobilityVariation.
	 * Updates the mobility.
	 * Younger and older people are more prone to mobility variations.
	 * The mobility can only be decreased, except when one is helped by another.
	 */
	public void generateMobilityVariation(){
		int variation = -MOBILITY_VARIATION;
		
		// younger and older people are more prone to fatigue variations 
		if(age < MAX_AGE / 3 || age > 2 * MAX_AGE / 3){
				variation *= 1.2;	
		}
		
		setMobility(mobility + variation);		
	}
	
	/*
	 * shareMobility.
	 * Updates the mobility of two people, moving together.
	 */
	public void shareMobility(Person altruisticPerson){
		int sharedMobility = (altruisticPerson.getAltruism() + this.getAltruism()) / 2;
		
		altruisticPerson.setMobility(sharedMobility);
		this.setMobility(sharedMobility);		
	}
	
	/*
	 * enforceBounds.
	 * Ensures the given attribute is within MIN_SCALE and MAX_SCALE.
	 * @param attribute
	 * @return attribute within bounds
	 */
	private int enforceBounds(int attribute){
		if(attribute > MAX_SCALE){
			return MAX_SCALE;
		}else if(attribute < MIN_SCALE){
			return MIN_SCALE;
		}else{
			return attribute;
		}
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
