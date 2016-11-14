package serviceConsumerProviderVis.onto;

import evacuation_simulation.Gender;
import jade.content.Predicate;

public class EvacueeStats implements Predicate {

	private static final long serialVersionUID = 1L;
	
	private Gender gender;
	private int age;
	private int areaKnowledge;
	private int altruism;
	private int independence;
	private int fatigue;
	private int physicalCondition;
	private int panic;
	
	
	private long evacuatedAt; 
	
	public EvacueeStats(Gender gender, int age, int areaKnowledge, int altruism, int independence, int fatigue, int mobility, int panic) {
		this.gender = gender;
		this.age = age;
		this.areaKnowledge = areaKnowledge;
		this.altruism = altruism;
		this.independence = independence;
		this.fatigue = fatigue;
		this.physicalCondition = mobility;
		this.panic = panic;
		this.evacuatedAt = System.currentTimeMillis();
	}

	/**
	 * @return the physicalCondition
	 */
	public int getPhysicalCondition() {
		return physicalCondition;
	}

	/**
	 * @return the evacuatedAt
	 */
	public long getEvacuatedAt() {
		return evacuatedAt;
	}
}