# Socket Communication Modules

## 1. MeterComm Module Socket Implementation

### MeterSocketComm Class
- **Purpose**: Provides low-level socket communication with meter devices
- **Responsibilities**:
  - Establishing and managing TCP socket connections to meter devices
  - Sending and receiving binary data to/from meters
  - Handling connection timeouts and errors
  - Supporting reading of firmware version and configuration data
  - Managing byte buffers for data transmission

### MeterSocketThread Class
- **Purpose**: Implements a threaded communication model for meter interactions
- **Responsibilities**:
  - Running socket communications in separate threads
  - Implementing acknowledgment (ACK/NACK) protocol
  - Handling timeouts and retries
  - Managing state transitions for meter communication
  - Buffering and processing data packages
  - Supporting observable pattern for communication events

### CommPackage Class
- **Purpose**: Represents a data package used in meter communications
- **Responsibilities**:
  - Storing meter data values (status, gains, flow rates, volumes, etc.)
  - Providing accessor methods for meter data attributes

### PackageHandler Class
- **Purpose**: Processes binary data packages from meters
- **Responsibilities**:
  - Unpacking binary data into structured meter data models
  - Converting raw binary data into meaningful measurements
  - Handling byte ordering (Little Endian)

## 2. BCIAPI Module Socket Implementation

### BCISocketComm Class
- **Purpose**: Provides socket communication with the Bench Control Interface (BCI)
- **Responsibilities**:
  - Implementing a Singleton pattern for BCI communication
  - Managing TCP socket connection to the BCI
  - Reading configuration from XML files
  - Sending and receiving data to/from the BCI
  - Using a mutex (Semaphore) to synchronize communication
  - Handling connection timeouts and errors

## Communication Architecture

### Socket Communication Flow
1. Applications create instances of socket communication classes
2. Connection is established to the target device (meter or BCI)
3. Commands are sent through the socket using byte-level communication
4. Responses are read and processed into structured data models
5. Acknowledgment protocol ensures reliable communication
6. Threaded model allows non-blocking communication

### Threading Model
- Socket communication runs in dedicated threads
- MeterSocketThread implements Runnable interface
- Asynchronous operations use CompletableFuture for timeout handling
- Observable pattern signals state changes to observers

### Error Handling
- Comprehensive exception handling for socket errors
- Timeout management with retries
- Logging of communication errors
- Connection state monitoring

### Protocol Details
- Binary protocol for meter communication
- Command-based interaction model
- ACK/NACK responses for command verification
- Little-endian byte ordering for binary data 