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
package metercomm.socket;


import org.slf4j.Logger;

import util.ProcessLog;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

import enumerations.MeterCmdEnum;
import si.dbcomm.model.MeterDataModel;
import util.MachineStates;

/**
 * @author Marcos Oliveira
 *
 */
public final class MeterSocketComm {

	/** Logger SLF4J desta classe (infraestrutura de logs da bancada). */
	private static final Logger LOG = ProcessLog.get(MeterSocketComm.class);

	private int port;

	private String ip;

	private byte[] ufoId;

	// private byte[] fwVersion = new byte[6];
	private byte[] fwVersion = new byte[8];

	private BufferedReader in = null;

	private PrintWriter out = null;

	private OutputStream outStream;

	private BufferedInputStream inStreamBuffer;

	private Socket clientSocket;

	private InputStream input = null;

	private WritableByteChannel channelOutput;

	private ReadableByteChannel channelInput;

	private boolean connected = false;

	private boolean readingPack = false;

	private boolean isValidPack = false;

	/**
	 * 
	 * Creates a object for class BCISocketComm.java
	 */
	public MeterSocketComm() {
		if (!connected) {
			if (connect()) {
				connected = true;
			} else {
				System.err.println("Could not connect");
			}
		}
	}

	/**
	 * 
	 * Creates a object for class BCISocketComm.java
	 */
	public MeterSocketComm(String ip, int port) {
		if (!connected) {
			this.ip = ip;
			this.port = port;
			if (connect()) {
				connected = true;
			} else {
				connected = false;
			}
		}
	}

	/**
	 * Connects clientSocket socket to configured ip and port on BCI. Also creates writer and
	 * 
	 * @return true in case connection is successful
	 */
	public boolean connect() {
		boolean retorno = false;
		ProcessLog.setMedidorTag(ip != null ? ip : "SOCKET-?");
		LOG.debug("ENTRADA connect(): abrindo socket TCP para {}:{} (timeout de conexao 1000 ms)", ip, port);
		long inicioConnect = ProcessLog.iniciarCronometro();
		try {
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(ip, port), 1000);
			if (clientSocket.isConnected()) {
				clientSocket.setSoTimeout(2000);
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				input = clientSocket.getInputStream();
				outStream = clientSocket.getOutputStream();
				channelOutput = Channels.newChannel(outStream);
				channelInput = Channels.newChannel(input);
				in = new BufferedReader(new InputStreamReader(input));
				retorno = true;
			}
		} catch (SocketTimeoutException ex) {
			ProcessLog.erro(LOG, "MeterSocketComm.connect(): TIMEOUT ao conectar em " + ip + ":" + port, ex);
			System.err.println("MESSAGE: No Connection. SocketTimeoutException at MeterSocketComm.connect() for: " + ip + ":" + port + " Message:" + ex.getMessage());
			retorno = false;
		} catch (ConnectException ex) {
			ProcessLog.erro(LOG, "MeterSocketComm.connect(): conexao RECUSADA em " + ip + ":" + port, ex);
			System.err.println("MESSAGE: No Connection. ConnectException at MeterSocketComm.connect() for: " + ip + ":" + port + " Message:" + ex.getMessage());
			retorno = false;
		} catch (UnknownHostException ex1) {
			ProcessLog.erro(LOG, "MeterSocketComm.connect(): host DESCONHECIDO " + ip + ":" + port, ex1);
			System.err.println("MESSAGE: No Connection. UnknownHostException at MeterSocketComm.connect() for: " + ip + ":" + port + " Message:" + ex1.getMessage());
			retorno = false;
		} catch (SocketException ex3) {
			ProcessLog.erro(LOG, "MeterSocketComm.connect(): erro de socket em " + ip + ":" + port, ex3);
			System.err.println("MESSAGE: No Connection. SocketException at MeterSocketComm.connect() for: " + ip + ":" + port + " Message:" + ex3.getMessage());
			retorno = false;
		} catch (IOException ex2) {
			ProcessLog.erro(LOG, "MeterSocketComm.connect(): erro de E/S em " + ip + ":" + port, ex2);
			System.err.println("MESSAGE: No Connection. IOException at MeterSocketComm.connect() for: " + ip + ":" + port + ". Message:" + ex2.getMessage());
			retorno = false;
		}
		LOG.debug("SAIDA connect(): {} para {}:{} em {} ms", (retorno ? "CONECTADO" : "FALHOU"), ip, port, ProcessLog.duracaoMs(inicioConnect));
		return retorno;
	}

	public boolean disconnect() {
		boolean retorno = false;
		try {
			// Allow the socket to close ONLY if it is not currently read a packgae
			if (!readingPack) {
				if (out != null && in != null && clientSocket != null) {
					out.close();
					in.close();
					input.close();
					outStream.close();
					channelOutput.close();
					channelInput.close();
					clientSocket.close();
				}
				retorno = true; // Indicates if disconnection has been succesfull
				this.connected = false;
				LOG.info("FINALIZACAO: socket do medidor em {}:{} desconectado", ip, port);
			} else {
				System.err.println("MESSAGE: Could not disconnect at MeterSocketComm.disconnect(): reading package");
				LOG.warn("Nao foi possivel desconectar o socket de {}:{}: leitura de pacote em andamento", ip, port);
				retorno = false;
			}
		} catch (IOException e) {
			System.err.println("ERROR: IOException Could not disconnect at MeterSocketComm.disconnect(). Message: " + e.getMessage());
		}
		return retorno;
	}

	/**
	 * @param data
	 */
	// public boolean sendDataByte(byte data) throws MeterSocketDisconectedException {
	public boolean sendDataByte(byte data) {
		boolean retorno = false;
		try {
			outStream.write(data);
			outStream.flush();
			retorno = true;
		} catch (IOException e) {
			System.err.println("ERROR: IOException Could not send data at MeterSocketComm.sendDataByte(). Message: " + e.getMessage());
			retorno = false;
		}

		return retorno;
	}

	/**
	 * @param data
	 */
	public boolean sendData(ByteBuffer data) {
		boolean retorno = false;
		try {
			// outStream.flush();
			// out.flush();
			if (channelOutput.isOpen()) {
				if (data.position() == data.limit()) {
					data.flip();
				}
				if (channelOutput.write(data) == data.limit()) {
					retorno = true;
				} else {
					retorno = false;
				}
			}
		} catch (IOException e) {
			System.err.println("ERROR: IOException Could not send data at MeterSocketComm.sendData(). Message: " + e.getMessage());
		}

		return retorno;
	}

	/**
	 * Method checks if a package is received to assure meter is connected. It checks R1 parameter from the meter. In case it is null or NaN the connection is NOT verified.
	 * 
	 * @return true in case the received meter package is valid
	 * 
	 */
	public boolean verifiConnection() {
		inStreamBuffer = new BufferedInputStream(input);
		byte receiveCommand = 0;
		byte receiveConf = 0;
		byte receivedId[] = new byte[4];
		boolean checked = false;

		if (isConnected()) {
			sendDataByte(MeterCmdEnum.CMD_LOAD_PRE_CALIB_OPMODE.getValue());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.verifiConnection()", e);
				e.printStackTrace();
			}
			sendDataByte(MeterCmdEnum.CMD_LOAD_PRE_CALIB_OPMODE.getValue());

			receivedId = readID();

			if (receivedId != null) {
				try {
					receiveCommand = readByte();
					receiveConf = readByte();
					if (receiveCommand == MeterCmdEnum.CMD_LOAD_PRE_CALIB_OPMODE.getValue()) {
						if (receiveConf == MeterCmdEnum.CMD_ACK.getValue() || receiveConf == MeterCmdEnum.CMD_NACK.getValue()) {
							checked = true;
						}
					}
				} catch (SocketTimeoutException so) {
					System.err.println("ERROR: SocketTimeoutException MeterSocketComm.verifiConnection(). Message: " + so.getMessage());
				}

			} else {
				sendDataByte(MeterCmdEnum.CMD_LOAD_PRE_CALIB_OPMODE.getValue());
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					ProcessLog.erro(LOG, "MeterSocketComm.verifiConnection()", e);
					e.printStackTrace();
				}
				sendDataByte(MeterCmdEnum.CMD_LOAD_PRE_CALIB_OPMODE.getValue());

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					ProcessLog.erro(LOG, "MeterSocketComm.verifiConnection()", e);
					e.printStackTrace();
				}

				receivedId = readID();

				if (receivedId != null) {
					try {
						receiveCommand = readByte();
						receiveConf = readByte();
						if (receiveCommand == MeterCmdEnum.CMD_LOAD_PRE_CALIB_OPMODE.getValue()) {
							if (receiveConf == MeterCmdEnum.CMD_ACK.getValue() || receiveConf == MeterCmdEnum.CMD_NACK.getValue()) {
								checked = true;
							}
						}
					} catch (SocketTimeoutException so) {
						System.err.println("ERROR: SocketTimeoutException MeterSocketComm.verifiConnection(). Message: " + so.getMessage());
					}
				} else {
					checked = false;
				}
			}
		}
		return checked;
	}

	public boolean verifiConnection2() {
		inStreamBuffer = new BufferedInputStream(input);
		byte receiveCommand = 0;
		byte receiveConf = 0;
		byte receivedId[] = new byte[4];
		boolean checked = false;

		if (isConnected()) {
			sendDataByte(MeterCmdEnum.CMD_LOAD_CALIB_OPMODE.getValue());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.verifiConnection2()", e);
				e.printStackTrace();
			}
			sendDataByte(MeterCmdEnum.CMD_LOAD_CALIB_OPMODE.getValue());

			receivedId = readID();
			if (receivedId != null) {
				if (receivedId[0] != 0x00) {
					try {
						receiveCommand = readByte();
						receiveConf = readByte();
						if (receiveCommand == MeterCmdEnum.CMD_LOAD_CALIB_OPMODE.getValue()) {
							if (receiveConf == MeterCmdEnum.CMD_ACK.getValue() || receiveConf == MeterCmdEnum.CMD_NACK.getValue()) {
								checked = true;
							}
						}
					} catch (SocketTimeoutException so) {
						System.err.println("ERROR: SocketTimeoutException MeterSocketComm.verifiConnection(). Message: " + so.getMessage());
					}

				} else {
					sendDataByte(MeterCmdEnum.CMD_LOAD_CALIB_OPMODE.getValue());
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						ProcessLog.erro(LOG, "MeterSocketComm.verifiConnection2()", e);
						e.printStackTrace();
					}
					sendDataByte(MeterCmdEnum.CMD_LOAD_CALIB_OPMODE.getValue());

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						ProcessLog.erro(LOG, "MeterSocketComm.verifiConnection2()", e);
						e.printStackTrace();
					}

					receivedId = readID();

					if (receivedId[0] != 0x00) {
						try {
							receiveCommand = readByte();
							receiveConf = readByte();
							if (receiveCommand == MeterCmdEnum.CMD_LOAD_CALIB_OPMODE.getValue()) {
								if (receiveConf == MeterCmdEnum.CMD_ACK.getValue() || receiveConf == MeterCmdEnum.CMD_NACK.getValue()) {
									checked = true;
								}
							}
						} catch (SocketTimeoutException so) {
							System.err.println("ERROR: SocketTimeoutException MeterSocketComm.verifiConnection(). Message: " + so.getMessage());
						}
					} else {
						checked = false;
					}
				}
			}
		}
		return checked;
	}
	
//	public boolean sendMeterCmd(MeterCmdEnum cmd) {
//		byte receivedId[] = new byte[4];
//		boolean checked = false;
//		byte receiveConf = 0;
//		byte receiveCommand = 0;
//
//		if (isConnected()) {
//			// Read firmware version
//			checked = false;
//			// Send cmd
//			sendDataByte(cmd.getValue());
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			sendDataByte(cmd.getValue());
//
//			// try {
//			// Thread.sleep(500);
//			// } catch (InterruptedException e) {
//			// e.printStackTrace();
//			// }
//
//			receivedId = readID();
//
//			if (receivedId != null) {
//				try {
//					receiveCommand = readByte();
//					receiveConf = readByte();
//					if (receiveCommand == cmd.getValue()) {
//						if (receiveConf == MeterCmdEnum.CMD_ACK.getValue() || receiveConf == MeterCmdEnum.CMD_NACK.getValue()) {
//							checked = true;
//						}
//					}
//				} catch (SocketTimeoutException so) {
//					System.err.println("ERROR: SocketTimeoutException MeterSocketComm.sendMeterCmd(). Message: " + so.getMessage());
//				}
//			} else {
//				checked = false;
//			}
//		}
//		return checked;
//	}

	public boolean readFwVersion() {
		byte receivedId[] = new byte[4];
		boolean checked = false;
		byte receiveConf = 0;
		byte receiveCommand = 0;

		if (isConnected()) {
			// Read firmware version
			checked = false;
			// Send cmd
			sendDataByte(MeterCmdEnum.CMD_READ_FIRMWARE_VERSION.getValue());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.readFwVersion()", e);
				e.printStackTrace();
			}
			sendDataByte(MeterCmdEnum.CMD_READ_FIRMWARE_VERSION.getValue());

			receivedId = readID();

			if (receivedId != null) {
				try {
					receiveCommand = readByte();
					receiveConf = readByte();
					if (receiveCommand == MeterCmdEnum.CMD_READ_FIRMWARE_VERSION.getValue()) {
						if (receiveConf == MeterCmdEnum.CMD_ACK.getValue() || receiveConf == MeterCmdEnum.CMD_NACK.getValue()) {
							checked = true;
						}
					}
				} catch (SocketTimeoutException so) {
					System.err.println("ERROR: SocketTimeoutException MeterSocketComm.verifiConnection(). Message: " + so.getMessage());
				}
			} else {
				checked = false;
			}
			if (checked) {
				checked = false;
				try {
					// byte[] version = new byte[6];
					fwVersion[0] = readByte();
					fwVersion[1] = readByte();
					fwVersion[2] = readByte();
					fwVersion[3] = readByte();
					fwVersion[4] = readByte();
					fwVersion[5] = readByte();
					// If fw is greater then 2 build version is last 2 bytes
					if (fwVersion[0] == 1 && fwVersion[4] < 8) {
						fwVersion[6] = 0;
						fwVersion[7] = 0;
					} else {
						fwVersion[6] = readByte();
						fwVersion[7] = readByte();
					}
					//
					checked = true;

				} catch (SocketTimeoutException so) {
					System.err.println("ERROR: SocketTimeoutException MeterSocketComm.verifiConnection(). Message: " + so.getMessage());
					checked = false;
				}
			}
		}
		return checked;
	}

	/**
	 * 
	 */
	public byte[] readMeterConfigData() {
		byte[] data = null;
		byte receivedId[] = new byte[4];
		boolean checked = false;
		byte receiveConf = 0;
		byte receiveCommand = 0;
		
		if (isConnected()) {
			// Read firmware version
			checked = false;
			
			
			// Send cmd
			
			sendDataByte(MeterCmdEnum.CMD_READ_CONFIG_PARAMETERS.getValue());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.readMeterConfigData()", e);
				e.printStackTrace();
			}
			sendDataByte(MeterCmdEnum.CMD_READ_CONFIG_PARAMETERS.getValue());
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.readMeterConfigData()", e);
				e.printStackTrace();
			}
			
			//
			receivedId = readID();
			
			if (receivedId != null) {
				try {
					receiveCommand = readByte();
					receiveConf = readByte();
					if (receiveCommand == MeterCmdEnum.CMD_READ_CONFIG_PARAMETERS.getValue()) {
						if (receiveConf == MeterCmdEnum.CMD_ACK.getValue() || receiveConf == MeterCmdEnum.CMD_NACK.getValue()) {
							checked = true;
						}
					}
				} catch (SocketTimeoutException so) {
					System.err.println("ERROR: SocketTimeoutException MeterSocketComm.readMeterConfigData(). Message: " + so.getMessage());
					}
			} else {
				checked = false;
			}
				
				
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.readMeterConfigData()", e);
				e.printStackTrace();
			}
		
			if (checked) {
				checked = false;
		
				try {
					data = new byte[654];
					int count = 0;
					byte value;
					while (count < 654) {
						value = readByte();
						data[count] = value;
						count++;
					}
				} catch (SocketTimeoutException e) {
					data = null;
					ProcessLog.erro(LOG, "MeterSocketComm.readMeterConfigData()", e);
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	public boolean sendMeterSystemDate(Calendar currentDate) {
		// inStreamBuffer = new BufferedInputStream(input);
		byte receiveCommand = 0;
		byte receiveConf = 0;
		byte receivedId[] = new byte[4];
		boolean checked = false;

		if (isConnected()) {
			sendDataByte(MeterCmdEnum.CMD_UPDATE_DATE.getValue());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.sendMeterSystemDate()", e);
				e.printStackTrace();
			}
			sendDataByte(MeterCmdEnum.CMD_UPDATE_DATE.getValue());

			receivedId = readID();

			if (receivedId != null) {
				try {
					receiveCommand = readByte();
					receiveConf = readByte();
					if (receiveCommand == MeterCmdEnum.CMD_UPDATE_DATE.getValue()) {
						if (receiveConf == MeterCmdEnum.CMD_ACK.getValue()) {
							checked = true;
						}
					}
				} catch (SocketTimeoutException so) {
					System.err.println("ERROR: SocketTimeoutException MeterSocketComm.sendMeterSystemDate() meter: " + this.getIp() + ". Message: " + so.getMessage());
				}

			} else {
				checked = false;
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.sendMeterSystemDate()", e);
				e.printStackTrace();
			}

			// send date
			if (checked) {
				ByteBuffer data = ByteBuffer.allocate(6);
				data.put((byte) currentDate.get(Calendar.SECOND));
				data.put((byte) currentDate.get(Calendar.MINUTE));
				data.put((byte) currentDate.get(Calendar.HOUR));
				data.put((byte) currentDate.get(Calendar.DAY_OF_MONTH));
				data.put((byte) (currentDate.get(Calendar.MONTH) + 1)); // +1 = The first month of the year is 0
				data.put((byte) (currentDate.get(Calendar.YEAR) - 1900));

				sendData(data);
				receivedId = readID();

				if (receivedId != null) {
					try {
						receiveCommand = readByte();
						receiveConf = readByte();
						if (receiveCommand == MeterCmdEnum.CMD_UPDATE_DATE.getValue()) {
							if (receiveConf == MeterCmdEnum.CMD_ACK.getValue()) {
								checked = true;
							}
						}
					} catch (SocketTimeoutException so) {
						System.err.println("ERROR: SocketTimeoutException MeterSocketComm.sendMeterSystemDate() meter: " + this.getIp() + ". Message: " + so.getMessage());
					}

				} else {
					checked = false;
				}
			}
		}
		return checked;
	}

	public boolean sendMeterReplaceDate(Calendar currentDate) {
		// inStreamBuffer = new BufferedInputStream(input);
		byte receiveCommand = 0;
		byte receiveConf = 0;
		byte receivedId[] = new byte[4];
		boolean checked = false;

		if (isConnected()) {
			sendDataByte(MeterCmdEnum.CMD_WRITE_REPLACEMENT_DATE.getValue());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.sendMeterReplaceDate()", e);
				e.printStackTrace();
			}
			sendDataByte(MeterCmdEnum.CMD_WRITE_REPLACEMENT_DATE.getValue());

			receivedId = readID();

			if (receivedId != null) {
				try {
					receiveCommand = readByte();
					receiveConf = readByte();
					if (receiveCommand == MeterCmdEnum.CMD_WRITE_REPLACEMENT_DATE.getValue()) {
						if (receiveConf == MeterCmdEnum.CMD_ACK.getValue()) {
							checked = true;
						}
					}
				} catch (SocketTimeoutException so) {
					System.err.println("ERROR: SocketTimeoutException MeterSocketComm.sendMeterReplaceDate() meter: " + this.getIp() + ". Message: " + so.getMessage());
				}

			} else {
				checked = false;
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterSocketComm.sendMeterReplaceDate()", e);
				e.printStackTrace();
			}

			// send date
			if (checked) {
				ByteBuffer data = ByteBuffer.allocate(6);
				data.put((byte) currentDate.get(Calendar.SECOND));
				data.put((byte) currentDate.get(Calendar.MINUTE));
				data.put((byte) currentDate.get(Calendar.HOUR));
				data.put((byte) currentDate.get(Calendar.DAY_OF_MONTH));
				data.put((byte) (currentDate.get(Calendar.MONTH) + 1)); // +1 = The first month of the year is 0
				data.put((byte) (currentDate.get(Calendar.YEAR) - 1900));

				sendData(data);
				receivedId = readID();

				if (receivedId != null) {
					try {
						receiveCommand = readByte();
						receiveConf = readByte();
						if (receiveCommand == MeterCmdEnum.CMD_UPDATE_DATE.getValue()) {
							if (receiveConf == MeterCmdEnum.CMD_ACK.getValue()) {
								checked = true;
							}
						}
					} catch (SocketTimeoutException so) {
						System.err.println("ERROR: SocketTimeoutException MeterSocketComm.sendMeterReplaceDate() meter: " + this.getIp() + ". Message: " + so.getMessage());
					}

				} else {
					checked = false;
				}
			}
		}
		return checked;
	}

	boolean timeouCheckTimeFlag = false;

	public boolean sendWmBusConfig(ByteBuffer sn, ByteBuffer key, ByteBuffer interval) {
		boolean checked = false;
		boolean notFoundAckorNack = false;
		int timeouCheckTime = 515;
		byte receiveCommand = 0;
		byte receiveConf = 0;
		byte receivedId[] = new byte[4];

		try {
			if (isConnected()) {
				sendDataByte(MeterCmdEnum.CMD_WRITE_WMBUS_CONFIG.getValue());
				Thread.sleep(50);
				sendDataByte(MeterCmdEnum.CMD_WRITE_WMBUS_CONFIG.getValue());
				receivedId = readID();

				notFoundAckorNack = true;
				CompletableFuture cpf = CompletableFuture.runAsync(() -> {
					try {
						Thread.sleep(timeouCheckTime);
					} catch (InterruptedException e) {
						System.err.println("ERROR: InterruptedException of assync thread MeterSocketThread.sendDataToMeter()");
						// e.printStackTrace();
					}
				}).thenAccept((timer) -> timeouCheckTimeFlag = true);

				if (checked) {
					ByteBuffer data = sn;

					// Sending WmBus Serial number
					sendData(data);
					sendData(key);
					sendData(interval);
					receivedId = readID();

					if (receivedId != null) {
						try {
							receiveCommand = readByte();
							receiveConf = readByte();
							if (receiveCommand == MeterCmdEnum.CMD_WRITE_WMBUS_CONFIG.getValue()) {
								if (receiveConf == MeterCmdEnum.CMD_ACK.getValue()) {
									checked = true;
								}
							}
						} catch (SocketTimeoutException so) {
							System.err.println("ERROR: SocketTimeoutException MeterSocketComm.verifiConnection(). Message: " + so.getMessage());
						}

					} else {
						checked = false;
					}
				}
			}
		} catch (InterruptedException e) {
			ProcessLog.erro(LOG, "MeterSocketComm.sendWmBusConfig()", e);
			e.printStackTrace();
		}
		return checked;
	}

	public boolean checkForNaN() {
		MeterDataModel pack;
		PackageHandler pckHdl = new PackageHandler();
		boolean retorno = false;
		if (isConnected()) {
			pack = pckHdl.unpackData(this.readDataLine());
			if (pack != null) {
				if (!Double.isNaN(pack.getR1())) {
					if (!Double.isNaN(pack.getTtrev()) && !Double.isNaN(pack.getTtstd())) {
						retorno = true;
					}
				}
			} else {
				retorno = false;
			}
		}
		return retorno;
	}

	/**
	 * This reads a data line from the socket. This method also cheks the preambul of the pakcage that arrives from meter
	 * 
	 * @return A ByteBuffer with the data read from meter
	 */
	public ByteBuffer readDataLine() {
		inStreamBuffer = new BufferedInputStream(input);
		ByteBuffer byteBuffer = ByteBuffer.allocate(78);
		int b = 0;
		int pkgCount = 0;
		int dataCount = 0;
		int ambCounter = 0;
		MachineStates state = MachineStates.PREAMB;
		isValidPack = false;
		if (clientSocket.isConnected()) {
			// byteBuffer.clear();
			while (pkgCount < 85) {
				try {
					switch (state) {
						case PREAMB:
							while (ambCounter <= 7) {
								if (!clientSocket.isClosed()) {
									if ((b = inStreamBuffer.read()) != -1) {
										readingPack = true;
										if (b == 255) {
											ambCounter++;
											pkgCount++;
											isValidPack = true;
										} else {
											ambCounter = 0;
											pkgCount = 0;
											isValidPack = false;
										}
									} else {
										System.err.println("MESSAGE: clientSocket read was -1 MeterSocketComm.readDataLine() for the PREAMB");
									}
								} else {
									System.err.println("ERROR: clientSocket is closed on PREAMB read for MeterSocketComm.readDataLine() ");
									ambCounter = 0;
									pkgCount = 0;
									isValidPack = false;
									break;
								}
							}
							ambCounter = 0;
							if (isValidPack) {
								state = MachineStates.MEASURE;
							}
						break;
						case MEASURE:
							byteBuffer.clear();
							while (dataCount < 78) {
								if (!clientSocket.isClosed()) {
									if ((b = inStreamBuffer.read()) != -1) {
										byteBuffer.put((byte) b);
										dataCount++;
										pkgCount++;
									} else {
										System.err.println("MESSAGE: clientSocket read was -1 MeterSocketComm.readDataLine() for the MEASUREMENT");
									}
								} else {
									System.err.println("ERROR: clientSocket is closed on MEASURE read for MeterSocketComm.readDataLine() Thread: " + Thread.currentThread().getName() + ".");
									dataCount = 0;
									pkgCount = 85;
									state = MachineStates.PREAMB;
									break;
								}
							}
							readingPack = false;
							state = MachineStates.PREAMB;
						break;
						case INIT_CONFIG:
						break;
						default:
						break;
					}

				} catch (BufferOverflowException e1) {
					System.err.println("MESSAGE: BufferOverflowException on MeterSocketComm.readDataLine() for meter in position: " + clientSocket.getInetAddress() + ". Message:" + e1.getCause()
							+ " datacount=" + dataCount + " " + "pkgCount=" + pkgCount + " byteBuffer.limit=" + byteBuffer.limit() + " byteBuffer.position=" + byteBuffer.position());
				} catch (SocketTimeoutException e2) {
					connected = false;
					// if socket has been disconnected, this is used to indicate.
					byteBuffer.clear();
					byteBuffer = null;
					System.err
							.println("MESSAGE: SocketTimeoutException on MeterSocketComm.readDataLine() for: " + ip + " Thread: " + Thread.currentThread().getName() + ". Message:" + e2.getMessage());
					break;
				} catch (IOException e3) {
					connected = false;
					// if socket has been disconnected, this is used to indicate.
					byteBuffer.clear();
					byteBuffer = null;
					System.err.println("MESSAGE: IOException on MeterSocketComm.readDataLine() for: " + ip + " Thread: " + Thread.currentThread().getName() + ".. Message:" + e3.getMessage());
					break;
				}
			}
			if (byteBuffer != null) {
				byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			}
		}
		return byteBuffer;
	}

	public ByteBuffer readDataFromMeter() {
		inStreamBuffer = new BufferedInputStream(input);
		ByteBuffer byteBuffer = ByteBuffer.allocate(78);
		ByteBuffer byteAck = ByteBuffer.allocate(2);
		int b = 0;
		int pkgCount = 0;
		int dataCount = 0;
		int ambCounter = 0;
		int pos = 0;
		MachineStates state = MachineStates.PREAMB;
		isValidPack = false;
		if (clientSocket.isConnected()) {
			// byteBuffer.clear();
			while (pkgCount < 85) {
				try {
					switch (state) {
						case PREAMB:
							while (ambCounter <= 7) {
								if (!clientSocket.isClosed()) {
									if ((b = inStreamBuffer.read()) != -1) {
										readingPack = true;
										if (b == 255) {
											ambCounter++;
											pkgCount++;
											isValidPack = true;
										} else {
											ambCounter = 0;
											pkgCount = 0;
											isValidPack = false;
											// new to identify an possible ack
											if (b == this.ufoId[pos]) {
												pos++;
												if (pos == 3) {
													byteAck.clear();
													// placing the Command byte
													b = inStreamBuffer.read();
													byteAck.put((byte) b);

													// placing the ack or nack
													b = inStreamBuffer.read();

													// if(b == ){ need to check is is ack or nack
													//
													// }
													byteAck.put((byte) b);
													return byteAck;
													// break mainloop;
												}
											} else {
												pos = 0;
											}
										}
									} else {
										System.err.println("MESSAGE: clientSocket read was -1 MeterSocketComm.readDataLine() for the PREAMB");
									}
								} else {
									System.err.println("ERROR: clientSocket is closed on PREAMB read for MeterSocketComm.readDataLine() ");
									ambCounter = 0;
									pkgCount = 0;
									isValidPack = false;
									break;
								}
							}
							ambCounter = 0;
							if (isValidPack) {
								state = MachineStates.MEASURE;
							}
						break;
						case MEASURE:
							byteBuffer.clear();
							while (dataCount < 78) {
								if (!clientSocket.isClosed()) {
									if ((b = inStreamBuffer.read()) != -1) {
										byteBuffer.put((byte) b);
										dataCount++;
										pkgCount++;
									} else {
										System.err.println("MESSAGE: clientSocket read was -1 MeterSocketComm.readDataLine() for the MEASUREMENT");
									}
								} else {
									System.err.println("ERROR: clientSocket is closed on MEASURE read for MeterSocketComm.readDataLine() Thread: " + Thread.currentThread().getName() + ".");
									dataCount = 0;
									pkgCount = 85;
									state = MachineStates.PREAMB;
									break;
								}
							}
							readingPack = false;
							state = MachineStates.PREAMB;
						break;
						case INIT_CONFIG:
						break;
						default:
						break;
					}

				} catch (BufferOverflowException e1) {
					System.err.println("MESSAGE: BufferOverflowException on MeterSocketComm.readDataLine() for meter in position: " + clientSocket.getInetAddress() + ". Message:" + e1.getCause()
							+ " datacount=" + dataCount + " " + "pkgCount=" + pkgCount + " byteBuffer.limit=" + byteBuffer.limit() + " byteBuffer.position=" + byteBuffer.position());
				} catch (SocketTimeoutException e2) {
					connected = false;
					// if socket has been disconnected, this is used to indicate.
					byteBuffer.clear();
					byteBuffer = null;
					System.err
							.println("MESSAGE: SocketTimeoutException on MeterSocketComm.readDataLine() for: " + ip + " Thread: " + Thread.currentThread().getName() + ". Message:" + e2.getMessage());
					break;
				} catch (IOException e3) {
					connected = false;
					// if socket has been disconnected, this is used to indicate.
					byteBuffer.clear();
					byteBuffer = null;
					System.err.println("MESSAGE: IOException on MeterSocketComm.readDataLine() for: " + ip + " Thread: " + Thread.currentThread().getName() + ".. Message:" + e3.getMessage());
					break;
				}
			}
			if (byteBuffer != null) {
				byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			}
		}
		return byteBuffer;
	}

	/**
	 * 
	 */
	public byte[] readData() {
		String retorno = null;
		try {
			retorno = in.readLine();
		} catch (IOException e) {
			ProcessLog.erro(LOG, "MeterSocketComm.readData()", e);
			e.printStackTrace();
		}
		return retorno.getBytes();
	}

	public boolean readData(byte[] array, int size) throws IOException {
			input.read(array, 0, size);
			return true;
	}
	
	public byte readByte() throws SocketTimeoutException {
		byte ret = -1;
		int data = 0;
		try {
			if ((data = input.read()) != -1) {
				ret = (byte) data;
			} else {
				System.err.println("MESSAGE: MeterSocketComm.readByte() No data available. For " + Thread.currentThread().getName());
			}
		} catch (SocketTimeoutException to) {
			throw to;
		} catch (IOException e) {
			System.err.println("ERROR: IOException on MeterSocketComm.readByte() for " + Thread.currentThread().getName());
		}
		return ret;
	}

	public int availableBytes(){
		int ret = 0;
		try {
			ret = this.input.available();
		} catch (IOException e) {
			ProcessLog.erro(LOG, "MeterSocketComm.availableBytes()", e);
			e.printStackTrace();
		}
		return ret;
	}
	
	public byte[] readID() {
		byte ret[] = new byte[4];
		int available = 0;
		int i = 0;
		while (i < 4) {
			try {
				available = this.readByte();
				if (available == -1) {
					break;
				}
				ret[i++] = (byte) available;
			} catch (SocketTimeoutException so) {
				System.err.println("ERROR: SocketTimeoutException on MeterSocketComm.readID() for " + Thread.currentThread().getName());
				ret = null;
				break;
			}
		}
		if (ufoId == null) {
			ufoId = new byte[4];
			ufoId = ret;
		}
		return ret;
	}

	public ByteBuffer readByteChannel() {
		ByteBuffer ret = ByteBuffer.allocate(1);
		try {
			if (channelInput.isOpen()) {
				if (channelInput.read(ret) != -1) {

				}
			}
		} catch (SocketTimeoutException to) {
			ProcessLog.erro(LOG, "MeterSocketComm.readByteChannel()", to);
			to.printStackTrace();
		} catch (IOException e) {
			ProcessLog.erro(LOG, "MeterSocketComm.readByteChannel()", e);
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 */
	public byte[] readDataByte() {
		byte[] retorno = new byte[0];
		int available = 0;
		try {
			while ((available = input.available()) > 0) {
				retorno = new byte[available];
				input.read(retorno, 0, available);
			}

		} catch (IOException e) {
			ProcessLog.erro(LOG, "MeterSocketComm.readDataByte()", e);
			e.printStackTrace();
		}
		return retorno;
	}

	/**
	 * Function indicates if socket object is connected as client.
	 * 
	 * @return
	 */
	public boolean isConnected() {
		boolean returno = false;
		if (clientSocket != null) {
			returno = clientSocket.isConnected();
		}

		return returno;
	}

	/**
	 * Function returns the value of attribute in
	 * 
	 * @return the in
	 */
	public BufferedReader getIn() {
		return in;
	}

	/**
	 * Function sets the value for attribute in
	 * 
	 * @param in
	 *            the in to set
	 */
	public void setIn(BufferedReader in) {
		this.in = in;
	}

	/**
	 * Function returns the value of attribute out
	 * 
	 * @return the out
	 */
	public PrintWriter getOut() {
		return out;
	}

	/**
	 * Function sets the value for attribute out
	 * 
	 * @param out
	 *            the out to set
	 */
	public void setOut(PrintWriter out) {
		this.out = out;
	}

	/**
	 * Function returns the value of attribute input
	 * 
	 * @return the input
	 */
	public InputStream getInput() {
		return input;
	}

	/**
	 * Function sets the value for attribute input
	 * 
	 * @param input
	 *            the input to set
	 */
	public void setInput(InputStream input) {
		this.input = input;
	}

	/**
	 * Function returns the value of attribute isValidPack
	 * 
	 * @return the isValidPack
	 */
	public boolean isValidPack() {
		return isValidPack;
	}

	/**
	 * Function sets the value for attribute isValidPack
	 * 
	 * @param isValidPack
	 *            the isValidPack to set
	 */
	public void setValidPack(boolean isValidPack) {
		this.isValidPack = isValidPack;
	}

	/**
	 * Function returns the value of attribute pORT
	 * 
	 * @return the pORT
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Function sets the value for attribute pORT
	 * 
	 * @param pORT
	 *            the pORT to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Function returns the value of attribute iP
	 * 
	 * @return the iP
	 */
	public String getIp() {
		return this.ip;
	}

	/**
	 * Function sets the value for attribute iP
	 * 
	 * @param iP
	 *            the iP to set
	 */
	public void setIp(String ipAdress) {
		this.ip = ipAdress;
	}

	/**
	 * Function returns the value of attribute uFO_ID
	 * 
	 * @return the uFO_ID
	 */
	public byte[] getUfoId() {
		return this.ufoId;
	}

	/**
	 * Function sets the value for attribute uFO_ID
	 * 
	 * @param uFO_ID
	 *            the uFO_ID to set
	 */
	public void setUfoId(byte[] id) {
		this.ufoId = id;
	}

	/**
	 * Function returns the value of attribute fwVersion
	 * 
	 * @return the fwVersion
	 */
	public byte[] getFwVersion() {
		return fwVersion;
	}

	/**
	 * Function sets the value for attribute fwVersion
	 * 
	 * @param fwVersion
	 *            the fwVersion to set
	 */
	public void setFwVersion(byte[] fwVersion) {
		this.fwVersion = fwVersion;
	}

}
