import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.routeAndCall;
import static play.test.Helpers.status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import play.api.Application;
import play.api.DefaultApplication;
import play.api.Mode;
import play.api.Play;
import play.mvc.Content;
import play.mvc.Result;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

	@Test
	public void simpleCheck() {
		int a = 1 + 1;
		assertThat(a).isEqualTo(2);
	}

	@Test
	public void renderTemplate() {
		Content html = views.html.index.render("Your new application is ready.");
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Your new application is ready.");
	}

	//     You can also retrieve an action reference from the reverse router, such as controllers.routes.ref.Application.index. You can then invoke it:
	//     
	//
	//    	@Test
	//    	public void callIndex() {
	//    	    Result result = callAction(
	//    	      controllers.routes.ref.Application.index("Kiki")
	//    	    );
	//    	    assertThat(status(result)).isEqualTo(OK);
	//    	    assertThat(contentType(result)).isEqualTo("text/html");
	//    	    assertThat(charset(result)).isEqualTo("utf-8");
	//    	    assertThat(contentAsString(result)).contains("Hello Kiki");
	//    	}
	//    	Testing the router
	//    	Instead of calling the Action yourself, you can let the Router do it:
	//
	//    	@Test
	//    	public void badRoute() {
	//    	  Result result = routeAndCall(fakeRequest(GET, "/xx/Kiki"));
	//    	  assertThat(result).isNull();
	//    	}

	@Test
	public void testReset() {
		File file = new File(".");
		//		Assert.fail( file.getAbsolutePath() );
		Application appl = new DefaultApplication(file,this.getClass().getClassLoader(), null, Mode.Test());
		Play.start(appl);

		Result result = callAction(
				controllers.routes.ref.Application.reset(0, 20, "TEST", 30)
				);
		assertThat(status(result)).isEqualTo(OK);
		result = callAction(
				controllers.routes.ref.Application.call(0, "UP")
				);
		assertThat(status(result)).isEqualTo(OK);

		nextCommand("OPEN");

		result = callAction(
				controllers.routes.ref.Application.go(10)
				);
		assertThat(status(result)).isEqualTo(OK);

		nextCommand("CLOSE");
		nextCommand("UP");
		nextCommand("UP");

		assertThat(status(result)).isEqualTo(OK);
		result = callAction(
				controllers.routes.ref.Application.call(3, "UP")
				);
		assertThat(status(result)).isEqualTo(OK);
		nextCommand("UP");

		nextCommand("OPEN");

		result = callAction(
				controllers.routes.ref.Application.go(10)
				);
		assertThat(status(result)).isEqualTo(OK);

		nextCommand("CLOSE");

		nextCommand("UP"); // L4
		nextCommand("UP"); // L5
		nextCommand("UP"); // L6
		nextCommand("UP"); // L7
		nextCommand("UP"); // L8
		nextCommand("UP"); // L9
		nextCommand("UP"); // L10
		nextCommand("OPEN");
		result = callAction(
				controllers.routes.ref.Application.userHasExited()
				);
		assertThat(status(result)).isEqualTo(OK);
		result = callAction(
				controllers.routes.ref.Application.userHasExited()
				);
		assertThat(status(result)).isEqualTo(OK);

		nextCommand("CLOSE");
	}

	private void nextCommand(String expectedCommand) {
		Result result;
		result = callAction(
				controllers.routes.ref.Application.nextCommand()
				);
		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentAsString(result)).isEqualTo(expectedCommand);
	}

//	@Test
	public void tesUseCase() {
		File file = new File(".");
		//		Assert.fail( file.getAbsolutePath() );
		Application appl = new DefaultApplication(file,this.getClass().getClassLoader(), null, Mode.Test());
		Play.start(appl);

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(getClass().getResource("IntegrationTestRequest.txt").toURI())));

			while(fileReader.ready()) {
				String getCommand = fileReader.readLine();
				
				Result result = routeAndCall(fakeRequest(GET, getCommand));
				
				assertThat(status(result)).isEqualTo(OK);
				System.err.println(getCommand);
				System.out.println(contentAsString(result));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
