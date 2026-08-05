# Service Interface (SI) Module

## Overview
The Service Interface (SI) module serves as the core of the application, providing a comprehensive data access and business logic layer between the application and the database. It implements a layered architecture with clear separation of concerns between data models, data access objects (DAOs), and service components.

## Architecture

### 1. Database Communication Layer

The SI module implements a layered architecture for database access:

#### DataBasePersistence
- **Purpose**: Manages database connections and Hibernate session factory
- **Responsibilities**:
  - Initializing database connections using Hibernate
  - Maintaining connection pools
  - Providing session instances for database operations
  - Configuring entity mappings

#### Data Access Objects (DAOs)
- **Base Components**:
  - **DaoInterface**: Defines the contract for all DAO implementations with generic CRUD operations
  - **DaoParent**: Provides base implementation for transaction handling and session management

- **Entity-specific DAOs**:
  - Implement CRUD operations for each domain entity
  - Handle transaction management for database operations
  - Implement specific query methods for entity retrieval

### 2. Service Layer

The service layer implements business logic and provides a facade for the DAO layer:

#### ServiceInterface
- **Purpose**: Defines the contract for service implementations
- **Responsibilities**:
  - Defining CRUD operations to be exposed to the application
  - Providing a consistent interface for all services

#### Entity-specific Services
- **Implementation**: Each entity has a dedicated service class
- **Responsibilities**:
  - Implementing business logic specific to the entity
  - Orchestrating multiple DAO operations when needed
  - Handling cross-cutting concerns like validation
  - Providing a clean API to the rest of the application

### 3. Data Models

The data models represent the domain entities and their relationships:

#### Main Entity Models
- **Batch-related**: BatchModel, CarcassBatchModel, BateladaModel
- **Meter-related**: MeterModel, MeterTypeModel, MeterDataModel
- **Equipment**: ValveModel, PumpModel, ScaleModel, RefMeterModel
- **Sensors**: TempSensorModel, PressureSensorModel, HumiditySensorModel, LevelSensorModel
- **Flow Control**: FlowRateModel, VolumeModel, WaterLineModel
- **Configuration**: ProcessConfigModel, FirmwareModel, RadioWmBusConfigModel
- **Calibration**: CalibConstantsModel, CalculatedFixedConstModel

#### Model Features
- Implemented using JPA annotations for ORM mapping
- Include validation constraints
- Define relationships between entities
- Generated metadata classes (with `_` suffix) for JPA criteria queries

## Key Components

### 1. Core Entity Management

#### Batch Processing
- Complete batch lifecycle management from creation to completion
- Tracking of batch metadata and statistics
- Association with meters, clients, and test processes

#### Meter Management
- Meter registration, type configuration, and firmware management
- Meter data collection and processing
- Integration with socket communication for meter reading

#### Equipment Control
- Management of physical components (valves, pumps, sensors)
- Configuration of operational parameters
- State tracking and monitoring

### 2. Calibration and Measurement

#### Flow Rate Management
- Recording and analyzing flow rate data
- Statistical analysis of flow measurements
- Calibration against reference meters

#### Error Calculation
- Volumetric error calculation and recording
- Verification error tracking and analysis
- Tolerance checking and quality assurance

### 3. Process Configuration

#### Test Process Configuration
- Definition of test parameters and sequences
- Configuration of acceptance criteria
- Process automation settings

#### System Configuration
- Equipment configuration and calibration
- Communication settings management
- User preferences and system parameters

## Database Integration

### PostgreSQL Connection
- Configured through Hibernate for object-relational mapping
- Connection pooling for efficient database access
- Transaction management for data integrity

### Schema Design
- Normalized table structure following relational database principles
- Foreign key relationships for data integrity
- Indexing strategy for query performance
- Entity versioning for optimistic locking

## Exception Handling

### Custom Exceptions
- **CrudDatabaseException**: For database operation failures
- Transaction rollback mechanisms
- Detailed error logging and recovery strategies

## Thread Safety

- Thread-local session management for Hibernate
- Connection pooling for concurrent access
- Transaction isolation for data consistency 