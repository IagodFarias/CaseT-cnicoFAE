# Model Documentation

## Overview
The model components in the service-interface-java-21 codebase represent the data structures that form the foundation of the application. These models fall into two main categories: database entities using JPA annotations for persistence, and non-persistent data models used for transient data handling.

## Data Model Architecture

### Database Entity Models
Located primarily in the `si.dbcomm.model` package, these classes represent persistent database entities:
- Utilize JPA annotations for object-relational mapping
- Follow a consistent naming pattern (`EntityNameModel.java`)
- Include generated metadata classes (`EntityNameModel_.java`) for JPA's Criteria API
- Implement serialization interfaces for data transfer

### Core Model Design Principles
- Rich domain model approach with behavior encapsulated within model classes
- Extensive use of entity relationships (one-to-many, many-to-one, many-to-many)
- Consistent naming conventions for database columns
- Clear separation between persistent and transient attributes

## Key Entity Models

### Batch-Related Models
- **BatchModel**: Represents a production batch of meters
  - Contains batch metadata (ID, description, manufacturer code)
  - Tracks production status (finished, numMeters, serialSequence)
  - Maintains relationships with meters, client, meter type, and firmware
  - Includes processing configuration references

- **BateladaModel**: Represents a processing batch within a production batch
  - Contains processing parameters and configuration
  - Tracks processing status and execution details
  - Maintains relationships with meters being processed

- **CarcassBatchModel**: Represents physical meter housings associated with batches
  - Tracks physical characteristics and inventory details

### Meter-Related Models
- **MeterModel**: Central entity representing an individual meter device
  - Comprehensive attribute set (80+ fields and relationships)
  - Tracks meter state through entire lifecycle (configuration, calibration, testing)
  - Maintains complex relationships with many other entities
  - Records calibration parameters, verification results, and processing history

- **MeterTypeModel**: Defines the specifications for a type of meter
  - Contains physical dimensions and characteristics
  - Defines measurement parameters and operational ranges
  - Establishes default values for meter configuration

- **MeterDataModel**: Stores measurement data collected from meters
  - Records raw measurement values
  - Contains timestamps and measurement context
  - Linked to specific meters and test operations

### Equipment Models
- **ValveModel**: Represents control valves in the bench system
  - Tracks valve type, position, and operational status
  - Records configuration parameters and control settings

- **PumpModel**: Represents pumps used in the bench system
  - Contains pump specifications and operational parameters
  - Tracks control settings and status

- **ScaleModel**: Represents weighing scales used for volumetric verification
  - Contains scale specifications and calibration data
  - Records measurement parameters and correction factors

- **RefMeterModel**: Represents reference meters used for calibration
  - Contains detailed calibration data and traceability information
  - Records calibration history and verification parameters

### Sensor Models
- **TempSensorModel**: Temperature sensor specifications and readings
- **PressureSensorModel**: Pressure sensor specifications and readings
- **HumiditySensorModel**: Humidity sensor specifications and readings
- **LevelSensorModel**: Level sensor specifications and readings

### Flow Control Models
- **FlowRateModel**: Defines flow rate parameters for testing and calibration
  - Contains nominal flow rate values
  - Defines test points and measurement parameters
  - Includes test sequence information

- **WaterLineModel**: Represents water flow paths in the bench system
  - Contains configuration for flow routing
  - Defines operational parameters for flow control

- **VolumeModel**: Represents volumetric measurements
  - Records volume readings and calculations
  - Contains reference values and uncertainty data

### Calibration and Verification Models
- **CalibConstantsModel**: Stores calibration constants for meters
  - Records calibration parameters calculated during calibration
  - Links to specific meters and calibration points

- **VerificationErrorModel**: Stores verification test results
  - Records measurement errors at different test points
  - Contains statistical analysis of verification results

- **VolumetricErrorModel**: Stores volumetric verification results
  - Records volumetric measurement errors
  - Contains reference values and uncertainty calculations

### Configuration Models
- **ProcessConfigModel**: Defines process configurations for batch processing
  - Contains comprehensive process parameters (60+ fields)
  - Defines process flow and control parameters
  - Configures test sequences and acceptance criteria

- **RadioWmBusConfigModel**: Configuration for wireless M-Bus communication
  - Contains communication parameters and protocol settings
  - Defines meter addressing and data formats

- **PidConfigModel**: Configuration for PID controllers
  - Contains tuning parameters for process control
  - Defines control loops and response characteristics

## Non-Persistent Models
Located in the `model` package, these classes represent transient data structures:
- **EstimatedDistanceModel**: Contains calculations for estimated distances between measurement points
  - Used for acoustic path length estimations
  - Stores calculation parameters and intermediate results

## Metadata Generation
The JPA implementation uses static metamodel generation, creating companion classes (`EntityNameModel_.java`) that:
- Define static field references for use in type-safe queries
- Support the Criteria API for complex database queries
- Enable compile-time checking of query expressions

## Relationships and Associations
The models implement complex relationships:
- **One-to-Many**: BatchModel to MeterModel, MeterModel to MeterDataModel
- **Many-to-One**: MeterModel to MeterTypeModel, BatchModel to ClientModel
- **One-to-One**: MeterModel to RadioWmBusConfigModel
- **Many-to-Many**: Implemented through join tables and bidirectional associations

## Data Validation
Models implement validation through:
- JPA constraints (`@Column` attributes like nullable, unique)
- Custom validation logic in service layer
- Database constraints enforced by the schema

## Best Practices Observed
- Consistent naming patterns for database columns
- Clear separation of concerns between entity and behavior
- Proper encapsulation with getters and setters
- Thoughtful use of fetch types (LAZY vs EAGER)
- Appropriate cascading of operations

## Extension Points
The model layer can be extended through:
- Addition of new entity types for new functionality
- Extension of existing entities with new attributes
- Implementation of new relationship types
- Enhancement of validation rules and constraints 