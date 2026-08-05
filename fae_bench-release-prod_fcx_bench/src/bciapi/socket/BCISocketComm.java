//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BCISocketComm.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.socket;


import org.slf4j.Logger;

import util.ProcessLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Marcos Oliveira
 *
 */
public final class BCISocketComm {

	/** Logger SLF4J desta classe (infraestrutura de logs da bancada). */
	private static final Logger LOG = ProcessLog.get(BCISocketComm.class);

	// private int PORT = 2514;
	// private int PORT = 8888;
	private int PORT;

	// private String IP = "127.0.0.1";
	// private String IP = "128.30.1.231";
	private String IP;

	private BufferedReader in = null;

	InputStream input = null;

	private PrintWriter out = null;

	private Socket clientSocket;

	private boolean connected = false;

	private boolean readingPack = false;

	// private static BCISocketComm INSTANCE = new BCISocketComm();
	private static BCISocketComm INSTANCE;

	private Semaphore mutex = new Semaphore(1);

	// private BenchBean benchBeanView = BenchBean.getInstance();

	/**
	 * Retrieves the unique intance of the socket connetion to BCI
	 * 
	 * @return
	 */
	public static BCISocketComm getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BCISocketComm();
		}
		return INSTANCE;
	}

	/**
	 * 
	 * Creates a object for class BCISocketComm.java
	 */
	public BCISocketComm() {
		if (!connected) {
			readConfigXML();
			if (connect()) {
				connected = true;
			} else {
				connected = false;
			}
		}
	}

	public static void BCISocketCommStart() {
		INSTANCE = new BCISocketComm();
	}

	/**
	 * Connects clientSocket socket to configured ip and port on BCI. Also creates writer and
	 * 
	 * @return true in case connection is successful
	 */
	public boolean connect() {
		boolean retorno = false;
		LOG.debug("ENTRADA connect(): abrindo socket com a BCI em {}:{} (timeout 500 ms)", IP, PORT);
		long inicioConnectBci = ProcessLog.iniciarCronometro();
		try {
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(IP, PORT), 500);
			if (clientSocket.isConnected()) {
				clientSocket.setSoTimeout(10000);
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				input = clientSocket.getInputStream();
				retorno = true;
				System.out.println("Conected to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
				LOG.info("BCI conectada em {}:{} em {} ms", clientSocket.getInetAddress(), clientSocket.getPort(), ProcessLog.duracaoMs(inicioConnectBci));
			}
		} catch (SocketTimeoutException ex) {
			ProcessLog.erro(LOG, "BCISocketComm.connect(): TIMEOUT ao conectar na BCI " + IP + ":" + PORT, ex);
			System.err.println("MESSAGE: No Connection. SocketTimeoutException at BCISocketComm.connect() for: " + IP + ":" + PORT + " Message:" + ex.getMessage());
			retorno = false;
		} catch (ConnectException ex) {
			ProcessLog.erro(LOG, "BCISocketComm.connect(): conexao RECUSADA pela BCI " + IP + ":" + PORT, ex);
			System.err.println("MESSAGE: No Connection. ConnectException at BCISocketComm.connect() for: " + IP + ":" + PORT + " Message:" + ex.getMessage());
			retorno = false;
		} catch (UnknownHostException ex1) {
			ProcessLog.erro(LOG, "BCISocketComm.connect(): host da BCI DESCONHECIDO " + IP + ":" + PORT, ex1);
			System.err.println("MESSAGE: No Connection. UnknownHostException at BCISocketComm.connect() for: " + IP + ":" + PORT + " Message:" + ex1.getMessage());
			retorno = false;
		} catch (SocketException ex3) {
			ProcessLog.erro(LOG, "BCISocketComm.connect(): erro de socket com a BCI " + IP + ":" + PORT, ex3);
			System.err.println("MESSAGE: No Connection. SocketException at BCISocketComm.connect() for: " + IP + ":" + PORT + " Message:" + ex3.getMessage());
			retorno = false;
		} catch (IOException ex2) {
			ProcessLog.erro(LOG, "BCISocketComm.connect(): erro de E/S com a BCI " + IP + ":" + PORT, ex2);
			System.err.println("MESSAGE: No Connection. IOException at BCISocketComm.connect() for: " + IP + ":" + PORT + ". Message:" + ex2.getMessage());
			retorno = false;
		}
		LOG.debug("SAIDA connect(): {} em {} ms", (retorno ? "CONECTADA" : "FALHOU"), ProcessLog.duracaoMs(inicioConnectBci));
		return retorno;
	}

	/**
	 * Method disconects the socket and closes the streams used for this socket
	 * 
	 * @return
	 */
	public boolean disconnect() {
		boolean retorno = false;
		try {
			// Allow the socket to close ONLY if it is not currently reading a
			// package
			if (!readingPack) {
				if (out != null && in != null && clientSocket != null) {
					out.close();
					in.close();
					clientSocket.close();
				}
				retorno = true; // Indicates if disconnection has been
								// succesfull
				this.connected = false;
			} else {
				System.err.println("MESSAGE: Could not disconnect at BCISocketComm.disconnect(): reading package");
				retorno = false;
			}
		} catch (IOException e) {
			ProcessLog.erro(LOG, "BCISocketComm.disconnect(): erro de E/S ao fechar o socket da BCI", e);
			System.err.println("ERROR: IOException Could not disconnect at BCISocketComm.disconnect(). Message: " + e.getMessage());
		}
		LOG.info("FINALIZACAO: desconexao da BCI. Resultado: {}", retorno);
		return retorno;
	}

	/**
	 * Sends data to server
	 * 
	 * @param data
	 *            - representing the Json of the object
	 */
	public synchronized void sendData(String data) {
		if (clientSocket.isConnected()) {
			try {
				mutex.acquire();
				out.println(data);
				out.flush();
				// System.out.println(data);//TODO remove
				// mutex.release();
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException Could not send data at BCISocketComm.sendData(). Message: " + e.getMessage());
			}
		}
	}

	/**
	 * 
	 */
	public synchronized String readData() {
		String retorno = null;
		try {
			retorno = in.readLine();
			mutex.release();
		} catch (SocketTimeoutException e) {
			connected = false;
//			BenchBean.getInstance().getBciConnectLed().turnLedRed();
			System.err.println("MESSAGE: SocketTimeoutException on BCISocketComm.readData() for: " + IP + ". Message:" + e.getMessage());
		} catch (IOException e) {
			connected = false;
//			BenchBean.getInstance().getBciConnectLed().turnLedRed();
			System.err.println("MESSAGE: IOException on BCISocketComm.readData() for: " + IP + ". Message:" + e.getMessage());
		}
		return retorno;
	}

	/**
	 * 
	 */
	public synchronized byte[] readDataByte() {
		byte[] retorno = new byte[0];
		int available = 0;
		try {
			while ((available = input.available()) > 0) {
				retorno = new byte[available];
				input.read(retorno, 0, available);
			}
		} catch (SocketTimeoutException e) {
			connected = false;
			System.err.println("MESSAGE: SocketTimeoutException on BCISocketComm.readDataByte() for: " + IP + ". Message:" + e.getMessage());
		} catch (IOException e) {
			System.err.println("MESSAGE: IOException on BCISocketComm.readDataByte() for: " + IP + ". Message:" + e.getMessage());
		}
		return retorno;
	}

	/**
	 * Reads the socketConfig.xml to acquire the Ip and Port for connection
	 */
	public void readConfigXML() {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("./socketConfig.xml"));
			doc.getDocumentElement().normalize();

			NodeList socketsList = doc.getElementsByTagName("socket");

			for (int temp = 0; temp < socketsList.getLength(); temp++) {
				Node nNode = socketsList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String attr = eElement.getAttribute("socketId");
					if (attr.equals("si")) {
						IP = eElement.getElementsByTagName("ip").item(0).getTextContent();
						PORT = Integer.parseInt(eElement.getElementsByTagName("port").item(0).getTextContent());
					}
				}
			}

		} catch (ParserConfigurationException e) {
			ProcessLog.erro(LOG, "BCISocketComm.readConfigXML()", e);
			e.printStackTrace();
		} catch (SAXException e) {
			ProcessLog.erro(LOG, "BCISocketComm.readConfigXML()", e);
			e.printStackTrace();
		} catch (IOException e) {
			ProcessLog.erro(LOG, "BCISocketComm.readConfigXML()", e);
			e.printStackTrace();
		}

	}

	/**
	 * Function returns the value of attribute connected
	 * 
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Function sets the value for attribute connected
	 * 
	 * @param connected
	 *            the connected to set
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
