package evacuation_simulation.onto;

import jade.content.AgentAction;

public class HelpConfirmation implements AgentAction {
	
	private static final long serialVersionUID = 1L;
	
	private int mobility;

	public HelpConfirmation() {
	}
	
	public HelpConfirmation(int mobility) {
		this.mobility = mobility;
	}

	/**
	 * @return the knowledge
	 */
	public int getMobility() {
		return mobility;
	}

	/**
	 * @param knowkledge the knowledge to set
	 */
	public void setMobility(int mobility) {
		this.mobility = mobility;
	}
}