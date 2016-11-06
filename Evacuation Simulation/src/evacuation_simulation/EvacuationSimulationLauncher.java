package evacuation_simulation;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class EvacuationSimulationLauncher extends RepastSLauncher {

	private static int N_MEN = 20;
	private static int N_WOMEN = 30;
	private static int N_CHILDREN = 10;

	protected static double FAILURE_PROBABILITY_GOOD_PROVIDER = 0.2;
	protected static double FAILURE_PROBABILITY_BAD_PROVIDER = 0.8;

	public static final boolean USE_RESULTS_COLLECTOR = true;
	public static final boolean SEPARATE_CONTAINERS = false;

	private ContainerController mainContainer;
	private ContainerController agentContainer;
	private Environment environment;

	public static Agent getAgent(Context<?> context, AID aid) {
		for(Object obj : context.getObjects(Agent.class)) {
			if(((Agent) obj).getAID().equals(aid)) {
				return (Agent) obj;
			}
		}
		return null;
	}
	
	public static int getN_MEN() {
		return N_MEN;
	}


	public static void setN_MEN(int n_MEN) {
		N_MEN = n_MEN;
	}


	public static int getN_WOMEN() {
		return N_WOMEN;
	}


	public static void setN_WOMEN(int n_WOMEN) {
		N_WOMEN = n_WOMEN;
	}


	public static int getN_CHILDREN() {
		return N_CHILDREN;
	}


	public static void setN_CHILDREN(int n_CHILDREN) {
		N_CHILDREN = n_CHILDREN;
	}

	@Override
	public String getName() {
		return "Emergency Evacuation -- SAJaS RepastS Simulation";
	}

	@Override
	protected void launchJADE() {

		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);
		
		if(SEPARATE_CONTAINERS) {
			Profile p2 = new ProfileImpl();
			agentContainer = rt.createAgentContainer(p2);
		} else {
			agentContainer = mainContainer;
		}

		createAgents();
	}

	private void createAgents() {

		try {

			AID resultsCollectorAID = null;
			if(USE_RESULTS_COLLECTOR) {
				// create results collector
				ResultsCollector resultsCollector = new ResultsCollector();
				mainContainer.acceptNewAgent("ResultsCollector", resultsCollector).start();
				resultsCollectorAID = resultsCollector.getAID();
			}

			// create population
			// children
			for (int i = 0; i < N_MEN; i++) {
				agentContainer.acceptNewAgent("Child" + i, new Man(resultsCollectorAID)).start();
			}
			
			// woman
			for (int i = 0; i < N_WOMEN; i++) {
				agentContainer.acceptNewAgent("BadProvider" + i, new Woman(resultsCollectorAID)).start();
			}

			// children
			for (int i = 0; i < N_CHILDREN; i++) {
				agentContainer.acceptNewAgent("Child" + i, new Child(resultsCollectorAID)).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Context build(Context<Object> context) {
		// http://repast.sourceforge.net/docs/RepastJavaGettingStarted.pdf
		//context.setId("evacuation"); ????
		
		environment = new Environment(context);

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("Evacuation network", context, true);
		netBuilder.buildNetwork();

		return super.build(context);
	}

}
