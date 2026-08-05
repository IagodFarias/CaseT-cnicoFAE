# Services Documentation

## Overview
The services module in the service-interface-java-21 codebase implements specialized business logic outside the core Service Interface (SI) module. These services focus on complex, domain-specific operations and calculations related to meter calibration, testing, and verification processes.

## Service Architecture

### Design Approach
The services follow a domain-driven design approach:
- Separation of concerns between data access and business logic
- Focused implementation of complex domain algorithms
- Encapsulation of specialized knowledge and business rules
- Integration with controllers and data access objects

### Architectural Patterns
- **Domain Service Pattern**: Implementation of complex domain operations
- **Calculation Service Pattern**: Specialized mathematical and statistical operations
- **Process Orchestration Pattern**: Coordination of multi-step business processes

## Key Services

### CalibrationService
Located in the `services` package, this service implements the core calibration algorithms for meter calibration:

#### Responsibilities
- Calculating zero flow parameters for meter calibration
- Computing time-of-flight to velocity and flow rate conversions
- Determining calibration constants based on bench test data
- Calculating verification errors and uncertainty
- Statistical analysis of calibration data
- Temperature compensation for speed of sound in water

#### Key Methods
- **calculateZeroFlow()**: Processes zero flow measurements to establish baseline
- **estimatePathLength()**: Calculates acoustic path lengths using time-of-flight data
- **calculateVerifError()**: Calculates verification errors for test points
- **calculateConstants()**: Computes calibration constants for meters
- **ttToVel()**: Converts time-of-flight measurements to velocity
- **ttToFlow()**: Converts time-of-flight measurements to flow rate
- **flowToReynolds()**: Calculates Reynolds number for flow characteristics
- **sosWater()**: Calculates speed of sound in water based on temperature
- **viscWater()**: Calculates water viscosity based on temperature

#### Data Structures
- **benchDataZeroFlow**: ArrayList of zero flow measurement data
- **benchData**: ArrayList of calibration measurement data
- **benchVerifData**: ArrayList of verification measurement data
- **calibConstants**: ArrayList of calculated calibration constants
- **errorConstants**: ArrayList of verification error results
- **TEMP_X_SOS**: Static HashMap mapping temperature to speed of sound
- **expectedFlowXmeanRefTemp**: HashMap relating expected flow to measured temperatures
- **expectedFlowXmeanRefFlow**: HashMap relating expected flow to measured flow rates
- **meanMeterFlowRateHash**: HashMap storing mean meter flow rates
- **expectedFlowXmeanVel**: HashMap storing mean velocities for expected flows

#### Key Algorithms
- **Zero Flow Calculation**: Determines the zero-flow baseline for meter calibration
- **Path Length Estimation**: Calculates acoustic path lengths based on time-of-flight measurements
- **Calibration Constants**: Computes K-factors and other calibration constants
- **Error Analysis**: Calculates measurement errors and performs statistical analysis
- **Reynolds Number Calculation**: Determines fluid flow characteristics
- **Speed of Sound Calculation**: Computes temperature-dependent speed of sound in water

#### Integration Points
- Interfaces with controllers for data collection (MeterController)
- Utilizes data access services for persistence (CalibConstantsService)
- Provides calculated parameters to the UI for visualization

### Calibration Types and Error Handling

#### Calibration Types (CalibrationTypeEnum)
The system supports multiple calibration approaches:
- **FIXED_CONST**: Calibration with fixed constants
- **ESTIMATED_CONST**: Calibration with estimated constants
- **FULL_PROD**: Complete batch production calibration
- **ONLY_VERIFICATION**: Verification-only process without calibration
- **INCOMPLETE**: Incomplete calibration process

Each calibration type defines a specific workflow and set of operations to be performed during the meter calibration process.

#### Calibration Errors (CalibrationErrorEnum)
The system defines comprehensive error codes for diagnostics:
- **COMM**: Communication errors
- **TRIM**: Trimming-related errors
- **DESV**: Deviation errors
- **CALC**: Calculation errors
- **DOWN**: Download errors
- **DWZERO**: Zero flow download errors
- **CALC_VERIF**: Verification calculation errors
- **VERIF**: Verification errors
- **RFCONF**: Reference configuration errors
- **SYSDATE**: System date errors
- **REPDATE**: Reporting date errors
- **SAVE_ERR**: Data saving errors
- **CALC_ZERO**: Zero flow calculation errors

These error codes are used throughout the application to provide detailed diagnostics and error reporting during calibration processes.

## Support Services

### Statistical Analysis
The services implement a range of statistical methods:
- Descriptive statistics (mean, standard deviation, etc.)
- Outlier detection and handling
- Confidence interval calculations
- Uncertainty analysis

### Mathematical Models
Complex mathematical models are implemented for:
- Fluid dynamics calculations
- Temperature compensation
- Flow rate and velocity conversions
- Physical property calculations (viscosity, density, etc.)

### Process Management
Services implement process flow management for:
- Multi-step calibration procedures
- Test sequence orchestration
- Verification test execution

## Implementation Patterns

### Data Structure Usage
- Utilizes hash maps for lookup tables and interpolation (e.g., TEMP_X_SOS for temperature to speed of sound mapping)
- Employs array lists for time series data (benchData, benchDataZeroFlow)
- Uses statistical utility classes (SummaryStatistics from Apache Commons Math)
- Implements custom data structures for specialized calculations

### Calculation Approaches
- Linear interpolation for property lookup (LinearInterpolator)
- Polynomial approximations for physical properties (e.g., viscWater method)
- Statistical methods for uncertainty analysis (calcVelStdDeviation, calcConfIntervalZeroFLow)
- Iterative algorithms for solving complex equations

### Integration Techniques
- Controller injection for system integration
- Service-to-service collaboration for complex operations
- Data access object integration for persistence

## Technical Implementation

### Key Libraries and Frameworks
- **Apache Commons Math**: For statistical calculations and interpolation
  - SummaryStatistics for statistical analysis
  - LinearInterpolator for interpolation operations
- **Custom Numerical Methods**: For specialized fluid calculations
- **JPA/Hibernate**: For data persistence (via SI module)

### Threading Model
- Single-threaded calculation approach
- Support for bulk processing of calibration data
- Integration with UI thread through controller callbacks

### Error Handling
- Input validation for calculation parameters
- Domain-specific error detection (e.g., detecting implausible calibration results)
- Exception handling for mathematical operations
- Comprehensive error classification through CalibrationErrorEnum

## Example Workflows

### Meter Calibration Process
1. Collect zero flow measurements
2. Calculate zero flow parameters using calculateZeroFlow() and calculateStdDeviationZeroFlow()
3. Collect flow measurements at reference points
4. Calculate mean values and statistical parameters with meanRefFlowRate() and meanBenchTemperatureFlows()
5. Compute calibration constants with calculateConstants()
6. Validate calibration results
7. Persist calibration data

### Verification Testing
1. Collect verification measurements
2. Calculate measurement errors with calculateVerifError()
3. Perform statistical analysis
4. Compare against acceptance criteria
5. Generate verification report

## Best Practices Observed

- Clear separation of calculation steps
- Comprehensive documentation of algorithms
- Consistent error handling approach
- Reuse of common calculation methods
- Stateless service design where possible

## Extension Points

- Addition of new calibration algorithms
- Implementation of alternative statistical methods
- Support for new meter types and measurement principles
- Integration with additional external systems
- Enhanced reporting and analysis capabilities 