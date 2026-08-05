# BCIAPI Module Documentation

## Purpose
The BCIAPI (Bench Control Interface API) module provides an interface for controlling bench equipment used in testing and calibration operations. It enables communication with hardware devices through socket communication and provides a structured way to send commands and process responses.

## Responsibilities
- Provide a standardized interface for bench control operations
- Implement command models for various hardware operations
- Handle socket communication with bench equipment
- Process responses from bench equipment
- Support data persistence for bench operations

## Module Components

### Command Models
Located in `src/bciapi/command/model/`, these classes represent different operations that can be performed on bench equipment:

- **Valve Commands**: `OpenValve.java`, `CloseValve.java`, `GetValveState.java`
- **Pump Commands**: `StartPump.java`, `StopPump.java`, `SetPumpLoad.java`, `GetPumpState.java`
- **Line Commands**: `OpenLine.java`, `CloseLine.java`, `GetLineState.java`
- **Sensor Commands**: `GetSensorTemp.java`, `GetSensorPressure.java`, `GetSensorHumidity.java`, `GetSensorLevel.java`
- **Flow Control Commands**: `SetFlowRate.java`, `SetAutoFlowRate.java`
- **Counter Commands**: `StartCounter.java`, `StopCounter.java`, `GetCounter.java`
- **Meter Commands**: `ReadRefMeter.java`
- **Scale Commands**: `GetScaleWeight.java`
- **System Commands**: `Ack.java`, `Nack.java`, `Reset.java`, `Stop.java`, `GetAlarms.java`

All command models inherit from `CommandParent.java` which provides common functionality and properties.

### Interface
Located in `src/bciapi/interfaces/`:

- **ApiInterface.java**: Defines all methods required for bench control operations, including:
  - Equipment control methods (valve, pump, line operations)
  - Sensor reading methods (temperature, pressure, humidity, level)
  - Flow rate control methods
  - Counter operations
  - Scale and meter reading operations

### Implementation
Located in `src/bciapi/impl/`:

- **BenchControlImpl.java**: Implements the ApiInterface and contains the actual logic for communicating with bench hardware. This is a singleton class that serves as the main implementation of the API.

### Service Layer
Located in `src/bciapi/service/`:

- **BenchControlService.java**: Serves as a service layer between the application and the BCI implementation, handling additional functionality like data persistence.

### Socket Communication
Located in `src/bciapi/socket/`:

- **BCISocketComm.java**: Handles socket communication with bench equipment, including:
  - Connection management (connect, disconnect)
  - Data transmission (sendData, readData)
  - Configuration loading (readConfigXML)

### Models
Located in `src/bciapi/model/`:

- **CommandLogModel.java**: Represents logging information for commands.

## Communication Flow

1. The application interacts with the `BenchControlService`
2. The service uses `BenchControlImpl` to execute commands
3. `BenchControlImpl` creates appropriate command objects from the `command.model` package
4. Commands are transmitted via `BCISocketComm` to the bench equipment
5. Responses are received through socket communication and processed back to the application

## Key Features

- Structured command pattern for bench equipment control
- Socket-based communication protocol
- Support for various hardware components (valves, pumps, lines, sensors)
- Flow rate and counter control capabilities
- Reference meter reading capabilities
- Scale reading capabilities
- Alarm management

## Dependencies

- `si.dbcomm.model` package for data models (PumpModel, ValveModel, etc.)
- `util.PropertiesReaderUtil` for configuration loading

## Extension Points

The API can be extended by:
- Adding new command models in the `command.model` package
- Extending the `ApiInterface` with new methods
- Implementing additional functionality in `BenchControlImpl`

## Threading Considerations

- The socket communication is designed to handle concurrent requests
- Semaphores are used to control access to shared resources
- Care should be taken when extending the API to maintain thread safety

## Error Handling

- Socket communication errors are handled through exception catching
- Command responses include acknowledgment (Ack) and negative acknowledgment (Nack) mechanisms
- The API provides methods to check for and reset alarms

## Design Patterns

- **Singleton Pattern**: Used in BenchControlImpl to ensure a single instance of the controller
- **Command Pattern**: Each operation is encapsulated in its own command class
- **Facade Pattern**: The service layer provides a simplified interface to the complex subsystem

## Performance Considerations

- Socket timeout settings should be configured appropriately for the network environment
- Buffer sizes for socket communication should be optimized for the expected data volume
- Consider the impact of frequent socket connections/disconnections on performance

## Security Considerations

- The API assumes operation in a secure network environment
- No authentication mechanism is built into the socket communication
- Consider implementing additional security measures for production environments

## Future Improvements

- Enhanced error reporting and logging
- Support for additional sensor types
- Improved configuration management
- Performance optimizations for high-throughput scenarios 