package fr.diodfr.codestory.s3.e1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Elevator {
	static final String CMD_DOWN = "DOWN";
	static final String CMD_UP = "UP";
	static final String CMD_CLOSE = "CLOSE";
	static final String CMD_OPEN = "OPEN";
	static final String CMD_NOTHING = "NOTHING";

	public static class Call {
		public boolean up;
		public int floor;

		public Call(int floor, String up) {
			this.floor = floor;
			this.up = CMD_UP.equals(up);
		}
		
		@Override
		public String toString() {
			return "f=" + floor + ";dir=" + (up?CMD_UP:CMD_DOWN);
		}
	}
	
	
	public static class User {
		private int destFloor;
		
		public User(int floor) {
			this.destFloor = floor;
		}
		
		@Override
		public String toString() {
			return "dest=" + destFloor;
		}
	}
	
	private boolean doorsOpened = false;
	private int currentFloor = 0;
	
	private List<Call> calls = new ArrayList<Call>();
	private List<User> users = new ArrayList<User>();

	public void goTo(int floor) {
		User user = new User(floor);
		users.add(user );
	}
	
	public void addCall(int floor, String destination) {
		Call call = new Call(floor, destination);
		calls.add(call);
	}
	
	private boolean isDoorOpened() {
		return doorsOpened;
	}
	
	protected String openDoor() {
		doorsOpened = true;
		
		return CMD_OPEN;
	}
	
	private String closeDoor() {
		doorsOpened = false;
		
		return CMD_CLOSE;
	}
	
	private String up() {
		currentFloor ++;
		
		return CMD_UP;
	}
	
	private String down() {
		currentFloor--;
		
		return CMD_DOWN;
	}
	
	public void userExited() {
		for (User user : users) {
			if (user.destFloor == currentFloor) {
				users.remove(user);
				return;
			}
		}
	}
	
	public void userEntered() {
		for (Call call : calls) {
			if(call.floor == currentFloor) {
				calls.remove(call);
				return;
			}
		}
	}
	
	public String nextCommand() {
		if (isDoorOpened()) {
			return nextCommandCloseDoor();
		} else if (!users.isEmpty()) {
			return nextCommandUser();
		} else if (!calls.isEmpty()) {
			return nextCommandCalls();
		}
		
		return CMD_NOTHING;
	}
	
	private String nextCommandCalls() {
		int[] minMax = new int[2];
		minCallFloor(minMax);
		
		int dest = Math.abs(currentFloor-minMax[0]) < Math.abs(currentFloor-minMax[1]) ? minMax[0]: minMax[1] ;
		
		if (dest == currentFloor) {
			return openDoor();
		} else if (dest > currentFloor) {
			return up();
		}
		
		return down();
	}

	private void minCallFloor(int[] minMax) {
		minMax[0] = Integer.MAX_VALUE;
		minMax[1] = Integer.MIN_VALUE;
		
		for (Call call : calls) {
			if (call.floor < minMax[0] && call.up) {
				minMax[0] = call.floor;
			}
			if (call.floor > minMax[1] && !call.up) {
				minMax[1] = call.floor;
			}
		}
		
		if (minMax[0] == Integer.MAX_VALUE) {
			minMax[0] = minMax[1];
		} else if (minMax[1] == Integer.MIN_VALUE) {
			minMax[1] = minMax[0];
		}
	}

	private String nextCommandUser() {
		int dest = currentFloor;
		int diffDest = Integer.MAX_VALUE;
		
		for (User user : users) {
			int diffCurrent = Math.abs(currentFloor - user.destFloor);
		
			if (diffCurrent < diffDest) {
				diffDest = diffCurrent;
				dest = user.destFloor;
			}
		}
		
		if (currentFloor == dest) {
			return openDoor();
		} else if (currentFloor < dest) {
			if (findCall(currentFloor, true)) {
				return openDoor();
			}
			
			return up();
		}
		
		if (findCall(currentFloor, false)) {
			return openDoor();
		}
		
		return down();
	}

	private boolean findCall(int floor, boolean up) {
		for (Call call : calls) {
			if (call.floor == floor && call.up == up) {
				return true;
			}
		}
		return false;
	}

	private String nextCommandCloseDoor() {
		return (isDoorOpened()? closeDoor(): CMD_NOTHING);
	}

	@Override
	public String toString() {
		StringBuilder status = new StringBuilder();
		status.append("currentFloor=");status.append(currentFloor);status.append("\n");
		status.append("door=");status.append(doorsOpened);status.append("\n");
		status.append("users=");status.append(Arrays.toString(users.toArray()));status.append("\n");
		status.append("calls=");status.append(Arrays.toString(calls.toArray()));status.append("\n");
		return status.toString();
	}
}
 