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
		return (Elevator) Cache.get(ELEVATOR_KEY);
	}

	private static void resetElevatorInstance(final int lowerFloor, final int higherFloor, final int cabinSize) {
		Cache.remove(ELEVATOR_KEY);
		Cache.set(ELEVATOR_KEY,  new Elevator(lowerFloor,higherFloor,cabinSize));
	}

	public static Result index() {
		return ok(index.render("Code Story s3 e1 : elevator on play framework"));
	}

	public static Result call(int atFloor, String to) {
		Elevator elevatorInstance = getElevatorInstance();
		if (elevatorInstance != null) {
			elevatorInstance.addCall(atFloor, to);
		} else {
			return internalServerError("Elevator not initialized");
		}
		return ok();
	}

	public static Result go(int floorToGo) {
		System.out.println("Application.go()");
		Elevator elevatorInstance = getElevatorInstance();
		if (elevatorInstance != null) {
			elevatorInstance.goTo(floorToGo);
		} else {
			return internalServerError("Elevator not initialized");
		}
		return ok();
	}

	public static Result userHasExited() {
		Elevator elevatorInstance = getElevatorInstance();
		if (elevatorInstance != null) {
			elevatorInstance.userExited();
		} else {
			return internalServerError("Elevator not initialized");
		}
		return ok();
	}

	public static Result userHasEntered() {
		Elevator elevatorInstance = getElevatorInstance();
		if (elevatorInstance != null) {
			elevatorInstance.userEntered();
		} else {
			return internalServerError("Elevator not initialized");
		}

		return ok();
	}

	public static Result reset(int lowerFloor, int higherFloor, String cause, int cabinSize) {
		// /reset?lowerFloor=0&higherFloor=19&cause=information+message
		Elevator elevatorInstance = getElevatorInstance();
		System.out.println("Application.reset()" + cause);
		if (elevatorInstance != null )
			System.out.println(elevatorInstance.toString());
		resetElevatorInstance(lowerFloor, higherFloor, cabinSize);
		return ok();
	}

	public static Result nextCommand() {
		Elevator elevatorInstance = getElevatorInstance();
		// System.out.println(elevatorInstance.toString());
		return ok(elevatorInstance.nextCommand());
	}
}
