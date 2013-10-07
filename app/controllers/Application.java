package controllers;

import java.util.concurrent.Callable;

import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import fr.diodfr.codestory.s3.e1.Elevator;

public class Application extends Controller {
	
	private static final String ELEVATOR_KEY = "Elevator";

	private static Elevator getElevatorInstance() {
		try {
			return Cache.getOrElse(ELEVATOR_KEY, new Callable<Elevator>() {
				@Override
				public Elevator call() throws Exception {
					return new Elevator();
				}
			}, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return new Elevator();
		}
	}
	
	private static void resetElevatorInstance() {
		Cache.remove(ELEVATOR_KEY);
	}
	
    public static Result index() {
        return ok(index.render("Code Story s3 e1 : elevator on play framework"));
    }

    public static Result call(int atFloor, String to) {
    	getElevatorInstance().addCall(atFloor, to);
    	return ok();
    }
    
    public static Result go(int floorToGo) {
        getElevatorInstance().goTo(floorToGo);
    	return ok();
    }

    public static Result userHasExited() {
    	getElevatorInstance().userExited();
        return ok();
    }

    public static Result userHasEntered() {
        getElevatorInstance().userEntered();
    	return ok();
    }
    
    public static Result reset(String cause) {
    	resetElevatorInstance();
        return ok();
    }
    
	public static Result nextCommand() {
        Elevator elevatorInstance = getElevatorInstance();
		// System.out.println(elevatorInstance.toString());
        return ok(elevatorInstance.nextCommand());
    }
}
