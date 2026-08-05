# Utility Modules

## Overview
The utility modules provide a diverse set of support functions, tools, and helper classes used throughout the service-interface-java-21 application. These utilities are organized into two main packages: the general `util` package and the UI-specific `view.util` package.

## General Utilities (util package)

### State Management
- **MachineStates**: Enum defining basic machine states (INIT_CONFIG, PREAMB, MEASURE)
- **MainMachineStateEnum**: Enum for high-level application states (SETUP, CONNECT_METERS, PURGE, RUN, STOP, WAIT)
- **MeterCommStateEnum**: Enum for meter communication states (READ_METER, ENABLE_DATA_TRANSMIT, etc.)
- **StandardProcessStatesEnum**: Comprehensive enum defining states for the standard process flow

### Configuration and Properties
- **PropertiesReaderUtil**: Loads and provides access to application properties from configuration files
- **SocketConfigReaderUtil**: Specific utility for reading socket configuration settings
- **ProcessConfigReaderUtil**: Utility for reading process configuration settings
- **PreferencesHandler**: Manages user preference settings for the application

### Data Validation and Formatting
- **ValidateFields**: Provides validation for UI fields with visual error indicators
- **DateFormaterUtil**: Formatting utilities for date and time values
- **TypeConverterUtil**: Type conversion utilities for various data types
- **MaskTextField**: Text field implementation with input masking support

### File and I/O Utilities
- **MatFileHandlerUtil**: Handles MATLAB file format reading and writing
- **DeviceTagReaderUtil**: Reads device tag information
- **TestMessageReaderUtil**: Utility for reading test messages
- **ProcessLoggerUtil**: Logging utility for process events and errors

### Timing and Synchronization
- **AssyncronousTimerUtil**: Provides asynchronous timing functionality
- **TimeOutCounterUtil**: Utility for handling timeouts
- **CircularArrayList**: Fixed-size list implementation that automatically handles overflow

### Specialized Utilities
- **TemperatureSosLookUpTable**: Lookup table for temperature-related conversions
- **Utilities**: Collection of miscellaneous helper methods
- **MeterThreadListener**: Interface for listening to meter thread events
- **ChooserBrowser**: File/directory browser utility

## View Utilities (view.util package)

### UI Components
- **LedViewUtil**: Creates and manages LED-like indicators in the UI
- **ViewData**: Data structure for view-related information
- **ViewDataUtil**: Utility methods for managing view data
- **WorkIndicatorDialog**: Dialog implementation for showing work in progress

### Data Visualization
- **GraphControl**: Sophisticated control for creating and managing line charts
  - Supports real-time data visualization
  - Implements upper and lower limit lines
  - Provides circular buffer for continuous graphing
  - Thread-safe updates via Platform.runLater

## Key Design Patterns

### Singleton Pattern
- Several utilities implement the Singleton pattern for global access:
  - ProcessLoggerUtil
  - PreferencesHandler

### Observer Pattern
- LedViewUtil implements observable properties that can be monitored for changes

### Factory Pattern
- Creation of UI components and specialized objects

### Thread Safety
- GraphControl handles thread-safety concerns for UI updates
- AssyncronousTimerUtil provides thread-safe timing operations

## Usage Examples

### Configuration Access
```java
// Reading a configuration property
String dateFormat = PropertiesReaderUtil.getProperty("date.format");
```

### UI Field Validation
```java
// Validating and marking errors in a text field
if (ValidateFields.isEmpty(textField)) {
    ValidateFields.setValidateError(textField);
}
```

### Logging
```java
// Logging process information
ProcessLoggerUtil.writeInfo("Process started");
```

### LED Indicators
```java
// Creating and managing status indicators
LedViewUtil ledUtil = new LedViewUtil(imageView);
ledUtil.turnLedGreen(); // Success indicator
ledUtil.turnLedRed();   // Error indicator
```

### Graph Creation
```java
// Setting up a real-time graph
GraphControl graph = new GraphControl(lineChart, xAxis, yAxis);
graph.setValue(newValue); // Update graph with new data
```

## Integration Points

### Database Layer Integration
- Type conversion utilities for database operations
- Validation utilities for database input

### UI Integration
- Field validation for form inputs
- LED indicators for status display
- Graph controls for data visualization

### Process Control Integration
- State management enums for process control
- Timing utilities for process synchronization
- Logging for process monitoring 