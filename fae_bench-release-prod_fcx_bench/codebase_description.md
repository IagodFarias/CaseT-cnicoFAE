# Service Interface Java 21 Codebase

## Overview
This document provides a comprehensive description of the service-interface-java-21 codebase, a JavaFX application designed for meter calibration, testing, and management in industrial flow measurement systems. The documentation is incrementally built as more parts of the codebase are analyzed.

## System Architecture

### High-Level Architecture
*This section will contain information about the overall system architecture, including component diagrams and system dependencies.*

### Technology Stack
- Java 21
- JavaFX (UI Framework)
- Hibernate/JPA (Database Persistence)
- Socket Communication (Device Interaction)
- PostgreSQL (Database - inferred from dependencies)
- Apache Commons Math (Statistical Calculations)

## Core Modules

### bciapi (Bench Control Interface API)
*This module appears to provide an API for controlling bench equipment.*

#### Command Models
The command models represent different operations that can be performed:
- Commands for valve operations (open/close)
- Commands for pump operations (start/stop)
- Commands for line operations (open/close)
- Commands for sensor readings (temperature, humidity, pressure, level)
- Commands for meter operations

#### Implementation
*Details about the implementation of the API will be added here.*

#### Socket Communication
The BCISocketComm class implements a Singleton pattern for socket communication with the Bench Control Interface. It manages TCP socket connections, handles data transmission, and implements error handling for connection issues. See [socket_description.md](socket_description.md) for detailed information about the socket communication implementation.

### controller
*This module contains controller classes for various components of the system.*

### metercomm
*This module appears to handle communication with meter devices.*

#### Socket Implementation
The metercomm socket package contains classes for establishing and managing TCP socket connections with meter devices:
- MeterSocketComm: Handles low-level socket connections and data transfer
- MeterSocketThread: Implements a threaded communication model
- CommPackage: Data structure for meter communication
- PackageHandler: Processes binary data packages from meters

Refer to [socket_description.md](socket_description.md) for a comprehensive documentation of the socket communication architecture, protocol details, and threading model.

### model
The model components represent the data structures used throughout the application, divided into two main categories:

#### Non-Persistent Models
Located in the `model` package, these classes represent transient data structures:
- **EstimatedDistanceModel**: Used for acoustic path length estimations in ultrasonic meter calibration
- Other utility models for calculations and temporary data storage

#### Database Entity Models
Located primarily in the `si.dbcomm.model` package, these JPA-annotated entities form the core data model:
- Rich domain entities with relationships, validation, and business logic
- Extensive use of JPA annotations for object-relational mapping
- Generated metamodel classes for type-safe queries
- Complex relationship network representing the domain model

The model architecture follows object-oriented design principles with:
- Clear separation between persistent and transient attributes
- Consistent naming conventions and access patterns
- Complex entity relationship management
- Extensive use of JPA features for data integrity

For detailed information about the model components, see [model_description.md](model_description.md).

### services
The services module implements specialized business logic for complex domain operations, particularly focused on calibration and testing processes:

#### CalibrationService
The core service implementing meter calibration algorithms:
- Zero flow parameter calculation (calculateZeroFlow, calculateStdDeviationZeroFlow)
- Path length estimation for ultrasonic meters (estimatePathLength)
- Calibration constant computation (calculateConstants)
- Error analysis and uncertainty calculation (calculateVerifError)
- Statistical processing of measurement data (meanRefFlowRate, calcVelStdDeviation)
- Temperature compensation for speed of sound (sosWater)
- Fluid dynamics calculations (flowToReynolds, viscWater)

The service uses sophisticated data structures including:
- ArrayLists for measurement data storage (benchData, benchDataZeroFlow)
- HashMaps for lookup tables and measurement mappings (TEMP_X_SOS, expectedFlowXmeanRefFlow)
- Statistical utilities from Apache Commons Math (SummaryStatistics, LinearInterpolator)

#### Calibration Types and Error Handling
The system defines comprehensive calibration workflows and error handling:

**Calibration Types (CalibrationTypeEnum)**:
- **FIXED_CONST**: Calibration with fixed constants
- **ESTIMATED_CONST**: Calibration with estimated constants
- **FULL_PROD**: Complete batch production calibration
- **ONLY_VERIFICATION**: Verification-only process
- **INCOMPLETE**: Incomplete calibration process

**Calibration Errors (CalibrationErrorEnum)**:
- Error categories for communication, calculation, verification, and data handling
- Standardized error reporting throughout the calibration process
- Diagnostic information for troubleshooting

The services implement complex mathematical models and statistical methods for fluid dynamics, measurement processing, and process management. They utilize specialized data structures and calculation approaches to handle the complex requirements of meter calibration.

For detailed information about the services module, see [services_description.md](services_description.md).

### si (Service Interface)
The Service Interface (SI) module is the core of the application's data access and business logic layer, facilitating communication between the application and the database.

#### Database Access Objects (DAOs)
The SI module implements a comprehensive DAO layer:
- **Base Components**:
  - DaoInterface: Defines generic CRUD operations
  - DaoParent: Provides transaction handling and session management
- **Entity-specific DAOs**: Over 40 DAO classes implementing database operations for entities like Batch, Meter, Valve, Pump, Sensor, etc.

#### Data Models
The data model layer uses JPA annotations for object-relational mapping:
- **Main Entity Models**:
  - Batch-related: BatchModel, CarcassBatchModel, BateladaModel
  - Meter-related: MeterModel, MeterTypeModel, MeterDataModel
  - Equipment: ValveModel, PumpModel, ScaleModel, RefMeterModel
  - Sensors: TempSensorModel, PressureSensorModel, HumiditySensorModel, LevelSensorModel
  - Flow Control: FlowRateModel, VolumeModel, WaterLineModel
- **Features**: JPA annotations, validation constraints, entity relationships

#### Services
The service layer implements business logic and provides a facade for DAOs:
- **Base Components**:
  - ServiceInterface: Defines the contract for service implementations
- **Entity-specific Services**: Corresponding service class for each entity, implementing business logic and orchestrating DAO operations

For detailed information about the SI module's architecture, components, and database integration, see [si_description.md](si_description.md).

### util
The utility modules provide a diverse set of support functions, tools, and helper classes used throughout the application, organized into two main packages.

#### General Utilities
The main utility package includes:
- **State Management**: Enums defining application states (MachineStates, MainMachineStateEnum, MeterCommStateEnum)
- **Configuration Management**: Utilities for reading properties and settings (PropertiesReaderUtil, SocketConfigReaderUtil)
- **Data Validation/Formatting**: Tools for validating and formatting data (ValidateFields, DateFormaterUtil)
- **File Operations**: Utilities for file operations and logging (MatFileHandlerUtil, ProcessLoggerUtil)
- **Timing and Synchronization**: Utilities for timing operations (AssyncronousTimerUtil, TimeOutCounterUtil)

#### View Utilities
The view.util package provides UI-specific utilities:
- **UI Components**: Custom UI components and helpers (LedViewUtil, WorkIndicatorDialog)
- **Data Visualization**: Tools for displaying data (GraphControl for real-time charts)

For detailed information about the utility modules, including design patterns, usage examples, and integration points, see [util_description.md](util_description.md).

### view
The view module implements the user interface layer of the application using JavaFX, providing a comprehensive set of screens, dialogs, controls, and visualization components that enable user interaction with the system.

#### UI Architecture
The view module follows a standard JavaFX architecture with:
- FXML-defined layouts (.fxml files) for declarative UI definition
- Controller classes (.java files) that handle UI logic and events
- Custom UI components for specialized displays (graphs, gauges, LEDs)
- CSS styling for visual appearance and theming

The architecture is organized around a main dashboard (MainDashboard) that provides navigation to specialized screens for different functional areas of the application.

#### Main Components
- **Main Dashboard**: Central navigation hub with real-time visualization and control panels
- **Dialogs**: Modal windows for user interaction (UserDialog, WorkIndicatorDialog)
- **Custom Controls**: Specialized UI components like GraphControl (real-time graphs), LedViewUtil (status indicators)
- **Domain-Specific Screens**: Specialized interfaces for batch management, meter configuration, process configuration, etc.

#### Package Structure
The view module is organized into numerous domain-specific packages:
- **mainwindow/**: Main application window components
- **dialog/**: Dialog components for user interaction
- **util/**: Utility view components and helpers
- **batch/**, **metertype/**, **flowrate/**, etc.: Domain-specific UI components
- **css/**: Styling resources
- **images/**: Image resources

#### Threading Model
The view module implements a robust threading model to ensure UI responsiveness:
- JavaFX Platform.runLater() for UI updates from non-UI threads
- Background tasks with progress indication
- Thread management to prevent UI freezing during long operations

#### Integration with Other Modules
The view module integrates with other system components:
- Uses controllers for business logic
- Displays and modifies data through the service interface
- Shows connection status and meter readings from the meter communication subsystem
- Triggers commands via the Bench Control Interface API

For detailed information about the view module's architecture, components, and integration, see [view_description.md](view_description.md).

## Data Model

### Entity Relationships
The application implements a complex network of entity relationships representing the domain model:
- **Batch-Meter**: One-to-many relationship between production batches and meters
- **Meter-MeterType**: Many-to-one relationship defining meter specifications
- **Meter-MeterData**: One-to-many relationship for measurement data collection
- **Meter-Calibration**: One-to-many relationship for calibration records
- **Batch-Client**: Many-to-one relationship for customer assignment
- **Equipment Relationships**: Complex relationships between valves, pumps, sensors, and flow control components

This relationship network enables tracking of meters through the entire production, calibration, and verification lifecycle.

### Key Entities
The key entities in the system include:
- **Batch**: Production batches of meters
- **Meter/MeterType**: Individual meters and their specifications
- **Equipment**: Valves, pumps, scales, reference meters
- **Sensors**: Temperature, humidity, pressure, level sensors
- **Flow Control**: Flow rates, water lines, volumes
- **Calibration**: Calibration constants, verification errors
- **Configuration**: Process configurations, communication settings

For detailed information about the entity model and relationships, see [model_description.md](model_description.md).

## Database Schema
*This section will contain information about the database schema.*

## UI Components and Flow
The application provides a comprehensive set of UI components and flows for interacting with the system:

### Main Dashboard
The MainDashboard serves as the primary interface, featuring:
- Menu system for navigation to different functional areas
- Real-time data visualization through graphs and gauges
- System control panels
- Status indicators for monitoring system health

### Specialized Screens
- **Batch Management**: Creating, configuring, and monitoring batches
- **Meter Configuration**: Managing meter types and connections
- **Process Configuration**: Configuring process parameters
- **Flow Rate Management**: Configuring and monitoring flow rates
- **Reporting**: Generating and viewing system reports

### User Interaction Patterns
- Form-based data entry with validation
- Real-time monitoring through dynamic visualizations
- Modal dialogs for confirmation and notification
- Menu-based navigation to different system functions

For detailed information about the UI components and flow, see [view_description.md](view_description.md).

## Business Processes

### Meter Calibration
The system implements a comprehensive meter calibration process with multiple calibration types:
1. **Batch creation and configuration**: Setting up meter batches for testing
2. **Zero flow measurement and calculation**: Establishing baseline measurements
   - Collecting zero flow data
   - Calculating statistical parameters (mean, standard deviation)
   - Filtering outliers and establishing confidence intervals
3. **Multi-point calibration measurements**: Data collection at different flow rates
   - Temperature and flow rate measurements
   - Time-of-flight data collection for ultrasonic meters
   - Conversion of measurements to velocity and flow
4. **Calibration constant computation**: Calculating meter-specific constants
   - K-factors for flow conversion
   - Path length estimation for ultrasonic meters
   - Reynolds number calculations for flow characterization
5. **Verification testing**: Validating calibration results
   - Error calculation at multiple flow points
   - Statistical analysis of verification data
   - Uncertainty determination
6. **Calibration data storage**: Persisting results to database
7. **Certificate generation**: Creating calibration documentation

The system supports several calibration approaches defined in CalibrationTypeEnum:
- **Fixed Constants**: Using predetermined calibration constants
- **Estimated Constants**: Calculating new constants based on measurements
- **Full Production**: Complete calibration workflow for production batches
- **Verification Only**: Testing without recalibration
- **Incomplete**: Partial calibration process

### Verification Testing
The verification testing process includes:
1. Error measurement at multiple flow points
2. Statistical analysis of measurement results
3. Comparison against acceptance criteria
4. Documentation of verification results

The system includes comprehensive error handling through CalibrationErrorEnum, which defines error categories for all aspects of the calibration process, including:
- Communication errors (COMM)
- Calculation errors (CALC, CALC_ZERO, CALC_VERIF)
- Data transfer errors (DOWN, DWZERO)
- Verification errors (VERIF)
- Configuration errors (RFCONF)
- Timing errors (SYSDATE, REPDATE)
- Persistence errors (SAVE_ERR)

### Configuration Management
The system provides configuration management for:
1. Meter type specifications
2. Process parameters
3. Communication settings
4. System configuration

For detailed information about the business processes, see [services_description.md](services_description.md).

## Build and Deployment
*This section will contain information about how to build and deploy the application.*

## Development Setup
*This section will contain information about setting up a development environment.*

## Common Tasks
*This section will contain information about common development tasks.*

---

*This document is under active development and will be updated as more parts of the codebase are analyzed.* 