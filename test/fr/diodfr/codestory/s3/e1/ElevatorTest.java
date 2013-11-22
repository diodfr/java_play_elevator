package fr.diodfr.codestory.s3.e1;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

public class ElevatorTest {

	@Test
	public void testToString() {
		Elevator elevator = new Elevator(0,5,5);
		
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[]\n", elevator.toString());
	}

	@Test
	public void addCall() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.addCall(1, "UP");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
	}

	@Test
	public void addCall1() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.addCall(1, "DOWN");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=DOWN]\n", elevator.toString());
	}

	@Test
	public void openDoors() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.openDoor();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void userEntered() {
//		Elevator elevator = new Elevator(0,5,5);
//		elevator.addCall(0, "DOWN");
//		elevator.openDoor();
//		elevator.userEntered();
//		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void addUser() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.goTo(3);
		elevator.goTo(1);
		elevator.goTo(5);
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[dest=3, dest=1, dest=5]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void userExited() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.goTo(0);
		elevator.goTo(0);
		elevator.goTo(1);
		elevator.goTo(5);
		elevator.openDoor();
		elevator.userExited();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[dest=0, dest=1, dest=5]\ncalls=[]\n", elevator.toString());
		
		elevator.userExited();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[dest=1, dest=5]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void closeDoor() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.openDoor();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_CLOSE, elevator.nextCommand());
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void replyToUserCall() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.addCall(1, "UP");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
//		elevator.userEntered();
//		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void replyToUser2Call() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.addCall(1, "UP");
		elevator.addCall(0, "UP");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP, f=0;dir=UP]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[f=1;dir=UP, f=0;dir=UP]\n", elevator.toString());
//		elevator.userEntered();
//		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
	}
	
	@Test
	public void replyToGo() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.goTo(1);
		elevator.goTo(1);
		elevator.goTo(1);
		elevator.goTo(5);
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[dest=1, dest=1, dest=1, dest=5]\ncalls=[]\n", elevator.toString());
		
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
		map.put(1, 3);
		map.put(5, 1);
		Assert.assertEquals(map , elevator.createDestCount());

		Assert.assertArrayEquals(new int[]{1, 14, 8}, elevator.optimiseGoDest(elevator.createDestCount(), 4));
		
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=false\nusers=[dest=1, dest=1, dest=1, dest=5]\ncalls=[]\n", elevator.toString());
		
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[dest=1, dest=1, dest=1, dest=5]\ncalls=[]\n", elevator.toString());

		elevator.userExited();
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[dest=1, dest=1, dest=5]\ncalls=[]\n", elevator.toString());

		elevator.userExited();
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[dest=1, dest=5]\ncalls=[]\n", elevator.toString());

		elevator.userExited();
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[dest=5]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_CLOSE, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=false\nusers=[dest=5]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=2\ndoor=false\nusers=[dest=5]\ncalls=[]\n", elevator.toString());
		
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=3\ndoor=false\nusers=[dest=5]\ncalls=[]\n", elevator.toString());
		
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=4\ndoor=false\nusers=[dest=5]\ncalls=[]\n", elevator.toString());
		
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=5\ndoor=false\nusers=[dest=5]\ncalls=[]\n", elevator.toString());
	
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=5\ndoor=true\nusers=[dest=5]\ncalls=[]\n", elevator.toString());
	
		elevator.userExited();
		Assert.assertEquals("currentFloor=5\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
		
		Assert.assertEquals(Elevator.CMD_CLOSE, elevator.nextCommand());
		Assert.assertEquals("currentFloor=5\ndoor=false\nusers=[]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_NOTHING, elevator.nextCommand());
		Assert.assertEquals("currentFloor=5\ndoor=false\nusers=[]\ncalls=[]\n", elevator.toString());

	}
	
	@Test
	public void testOptimisationPoints1() {
		Elevator elevator = new Elevator(0,5,5);
		elevator.goTo(2);
		elevator.goTo(2);
		elevator.goTo(2);
		elevator.goTo(1);
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[dest=2, dest=2, dest=2, dest=1]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=false\nusers=[dest=2, dest=2, dest=2, dest=1]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=2\ndoor=false\nusers=[dest=2, dest=2, dest=2, dest=1]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=2\ndoor=true\nusers=[dest=2, dest=2, dest=2, dest=1]\ncalls=[]\n", elevator.toString());
	
		elevator.userExited();
		elevator.userExited();
		elevator.userExited();
		Assert.assertEquals("currentFloor=2\ndoor=true\nusers=[dest=1]\ncalls=[]\n", elevator.toString());
	
		Assert.assertEquals(Elevator.CMD_CLOSE, elevator.nextCommand());
		Assert.assertEquals("currentFloor=2\ndoor=false\nusers=[dest=1]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_DOWN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=false\nusers=[dest=1]\ncalls=[]\n", elevator.toString());

		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[dest=1]\ncalls=[]\n", elevator.toString());

		elevator.userExited();
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());

	}
	
	@Test
	public void test2User10Floor() {
		Elevator elevator = new Elevator(0, 20, 20);
		
		elevator.addCall(0, "UP");
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());

		elevator.goTo(10);
		Assert.assertEquals(Elevator.CMD_CLOSE, elevator.nextCommand());
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L1
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L2
		
		elevator.addCall(3, "UP");
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L3
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		elevator.goTo(10);
		Assert.assertEquals(Elevator.CMD_CLOSE, elevator.nextCommand());
	
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L4
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L5
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L6
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L7
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L8
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L9
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand()); // L10
		
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		
		elevator.userExited();
		elevator.userExited();
	}
	/*
	 * 

		assertThat(status(result)).isEqualTo(OK);
		result = callAction(
				controllers.routes.ref.Application.call(3, "UP")
				);
		assertThat(status(result)).isEqualTo(OK);
		nextCommand("OPEN");
		
		result = callAction(
				controllers.routes.ref.Application.go(10)
				);
		assertThat(status(result)).isEqualTo(OK);

		nextCommand("CLOSE");

	 */
	
	@Test
	public void testGo() {
		Elevator elevator = new Elevator(0,100,100);
		
		for (int i = 0; i < 100; i++) {
			elevator.goTo((int) Math.rint(Math.random() * 100));
		}
		
//		elevator.nextCommand();
		for (int i = 0; i < 1000; i++) {
			elevator.nextCommand();
		}
	}
}
