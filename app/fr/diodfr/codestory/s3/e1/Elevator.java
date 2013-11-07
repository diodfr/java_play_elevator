package fr.diodfr.codestory.s3.e1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
		synchronized (users) {
			users.add(user);
		}
	}

	public void addCall(int floor, String destination) {
		Call call = new Call(floor, destination);
		synchronized (calls) {
			calls.add(call);
		}
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
		synchronized (users) {
			for (Iterator<User> usersIterator = users.iterator(); usersIterator.hasNext();) {
				User user = usersIterator.next();

				if (user.destFloor == currentFloor) {
					usersIterator.remove();
					return;
				}
			}
		}
	}

	public void userEntered() {
		synchronized (calls) {
			for (Iterator<Call> callsIterator = calls.iterator(); callsIterator.hasNext();) {
				Call call = callsIterator.next();
				if(call.floor == currentFloor) {
					callsIterator.remove();
					return;
				}
			}
		}
	}

	public String nextCommand() {
		synchronized (users) {
			synchronized (calls) {

				if (isDoorOpened()) {
					return nextCommandCloseDoor();
				} else if (!users.isEmpty()) {
					return nextCommandUser();
				} else if (!calls.isEmpty()) {
					return nextCommandCalls();
				}
				
				return CMD_NOTHING;
			}
		}
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
		int dest = optimiseGoDest(createDestCount(), users.size());

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

	Map<Integer, Integer> createDestCount() {
		Map<Integer, Integer> countMap = new TreeMap<Integer, Integer>();

		for (User user : users) {
			Integer integer = countMap.get(user.destFloor);
			countMap.put(user.destFloor, integer!= null? integer + 1 : 1);
		}

		return countMap;
	}

	/**
	 * returns the optimize destination
	 * @param destCount maps floor -> user count
	 * @param userCount number of users in the elevator
	 * @return
	 */
	int optimiseGoDest(Map<Integer,Integer> destCount, int userCount) {
		int min = Integer.MAX_VALUE;
		int dest = destCount.keySet().iterator().next();

		Map <Integer, Map<Set<Integer>, Integer>> cache = new TreeMap <Integer, Map<Set<Integer>, Integer>>();
		
		for (Entry<Integer,Integer> entry : destCount.entrySet()) {
			TreeSet<Integer> tempSet = new TreeSet<Integer>(destCount.keySet());
			tempSet.remove(entry.getKey());
			int current = optimiseGoDest(cache, destCount, entry.getKey(), entry.getValue(), tempSet, userCount);

			current += (Math.abs(entry.getKey() - currentFloor)+1) * userCount; // cout de deplacement plus ouverture
			
			if (current < min) {
				min = current;
				dest = entry.getKey();
			}
		}

		return dest;
	}

	/**
	 * returns the optimize destination
	 * @param cache
	 * @param currentPos
	 * @param destFloor
	 * @param user4ThisFloorCount
	 * @param destCount
	 * @param userCountElevator
	 * @return
	 */
	private int optimiseGoDest(Map <Integer, Map<Set<Integer>, Integer>> cache, Map<Integer, Integer> destCount, int destFloor, int user4ThisFloorCount, Set<Integer> remainingStairs, int userCountElevator) {
		int min = Integer.MAX_VALUE;

		for (Integer stair : remainingStairs) {
			TreeSet<Integer> tempSet = new TreeSet<Integer>(remainingStairs);
			tempSet.remove(stair);
			int remainingUserCount = userCountElevator - user4ThisFloorCount;
			int count4MoveToDest = (Math.abs(stair - destFloor)+1) * remainingUserCount;
			Integer optimiseGoDest = null;
			if (cache.containsKey(stair)) {
				optimiseGoDest = cache.get(stair).get(tempSet);
			} else {
				cache.put(stair, new HashMap<Set<Integer>, Integer>());
			}
			
			if (optimiseGoDest == null) {
				optimiseGoDest = optimiseGoDest(cache, destCount, stair, destCount.get(stair), tempSet, remainingUserCount);
				cache.get(stair).put(tempSet, optimiseGoDest);
			}
			
			min = Math.min(min, count4MoveToDest + optimiseGoDest + remainingUserCount); // others drops + close door
		}

		if (min == Integer.MAX_VALUE) {
			min = 0;
		}

		return min;
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
