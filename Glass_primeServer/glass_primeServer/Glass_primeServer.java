//Author Name: M. Corey Glass
//Date: 6.26.22
//Program Name: Glass_primeServer
//Purpose: learn the basics of client/server architecture

package glass_primeServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Glass_primeServer extends Application {
	
	static final int PORT = 8000;
	
	@Override
	public void start(Stage primaryStage) {
		
		// create server output for messages
		TextArea output = new TextArea();
		
		// create GUI
		Scene scene = new Scene(new ScrollPane(output), 450, 200);
		primaryStage.setTitle("Prime Server"); // set window title
		primaryStage.setScene(scene);
		primaryStage.show(); // start the GUI
		
		// kill the JVM/server when JavaFX window is closed
		primaryStage.setOnCloseRequest(x -> System.exit(0));
		
		// TODO use following logic and a client server handshake to find free port
			// ie, server finds first free port after 8000, if it finds 8001 it waits there
			// client starts looking for server at port 8000, if it finds server there it sends a message
			// can be anything, some string, and then waits for proper response
			// if reposnse is not received, try next port in range
		
//		for (int port : FREE_PORT_RANGE) {
//		    try (ServerSocket serverSocket = new ServerSocket(port)) {
//		        assertThat(serverSocket).isNotNull();
//		        assertThat(serverSocket.getLocalPort()).isEqualTo(port);
//		        return;
//		    } catch (IOException e) {
//		        assertThat(e).hasMessageContaining("Address already in use");
//		    }
//		}
//		fail("No free port in the range found");

		
		new Thread( () -> {
			try (ServerSocket serverSocket = new ServerSocket(PORT)) {
				
				Platform.runLater(() ->	output.appendText("Prime Server started - " + new Date() + '\n'));

				// listen for client
				Socket socket = serverSocket.accept();

				// create input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

				while (true) {
					int input = inputFromClient.readInt();

					// check input for prime
					boolean result = isPrime(input);

					// send results to client
					outputToClient.writeBoolean(result);

					Platform.runLater(() -> {
						output.appendText("Input from client - " + input + '\n');
						output.appendText("Input is prime -  " + result + '\n');		// TODO set calculated output
					});
				}
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	private static boolean isPrime(int n)
	{
		// check for simple primes
		if (n <= 1)
			return false;
		if (n <= 3)
			return true;

		// check for other simple primes
		if (n % 2 == 0 || n % 3 == 0)
			return false;

		for (int i = 5; i * i <= n; i = i + 6)
			if (n % i == 0 || n % (i + 2) == 0)
				return false;

		return true;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
