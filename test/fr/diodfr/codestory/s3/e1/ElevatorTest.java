package fr.diodfr.codestory.s3.e1;

import org.junit.Assert;
import org.junit.Test;

public class ElevatorTest {

	@Test
	public void testToString() {
		Elevator elevator = new Elevator();
		
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[]\n", elevator.toString());
	}

	@Test
	public void addCall() {
		Elevator elevator = new Elevator();
		elevator.addCall(1, "UP");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
	}

	@Test
	public void addCall1() {
		Elevator elevator = new Elevator();
		elevator.addCall(1, "DOWN");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=DOWN]\n", elevator.toString());
	}

	@Test
	public void openDoors() {
		Elevator elevator = new Elevator();
		elevator.openDoor();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void userEntered() {
		Elevator elevator = new Elevator();
		elevator.addCall(0, "DOWN");
		elevator.openDoor();
		elevator.userEntered();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void addUser() {
		Elevator elevator = new Elevator();
		elevator.goTo(3);
		elevator.goTo(1);
		elevator.goTo(5);
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[dest=3, dest=1, dest=5]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void userExited() {
		Elevator elevator = new Elevator();
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
		Elevator elevator = new Elevator();
		elevator.openDoor();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_CLOSE, elevator.nextCommand());
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void replyToUserCall() {
		Elevator elevator = new Elevator();
		elevator.addCall(1, "UP");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_UP, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
		elevator.userEntered();
		Assert.assertEquals("currentFloor=1\ndoor=true\nusers=[]\ncalls=[]\n", elevator.toString());
	}
	
	@Test
	public void replyToUser2Call() {
		Elevator elevator = new Elevator();
		elevator.addCall(1, "UP");
		elevator.addCall(0, "UP");
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[]\ncalls=[f=1;dir=UP, f=0;dir=UP]\n", elevator.toString());
		Assert.assertEquals(Elevator.CMD_OPEN, elevator.nextCommand());
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[f=1;dir=UP, f=0;dir=UP]\n", elevator.toString());
		elevator.userEntered();
		Assert.assertEquals("currentFloor=0\ndoor=true\nusers=[]\ncalls=[f=1;dir=UP]\n", elevator.toString());
	}
	
	@Test
	public void replyToGo() {
		Elevator elevator = new Elevator();
		elevator.goTo(1);
		elevator.goTo(1);
		elevator.goTo(1);
		elevator.goTo(5);
		Assert.assertEquals("currentFloor=0\ndoor=false\nusers=[dest=1, dest=1, dest=1, dest=5]\ncalls=[]\n", elevator.toString());

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
}
