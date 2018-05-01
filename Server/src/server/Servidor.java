package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.management.OperatingSystemMXBean;

public class Servidor {
	private static final int TIME_OUT = 10000;
	public static final int N_THREADS = 8;
	private static ServerSocket elSocket;
	private static Servidor elServidor;

	public Servidor() {
	}

	private ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);

	public static void main(String[] args) throws IOException {
		elServidor = new Servidor();
		elServidor.runServidor();
	}

	private void runServidor() {
		int num = 0;
		try {
			System.out.print("Puerto: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			int puerto = Integer.parseInt(br.readLine());
			int puerto = 8081;
			elSocket = new ServerSocket(puerto);
			System.out.println("Servidor escuchando en puerto: " + puerto);
			for (;;) {
				Socket sThread = null;

				sThread = elSocket.accept();
				sThread.setSoTimeout(TIME_OUT);
				System.out.println("Thread " + num + " recibe a un cliente.");
				executor.submit(new Worker(num, sThread));
				num++;
               // System.err.println(bean.getSystemCpuLoad());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
