import serial
import time

"""
Serial interface for FCX 3.0 optical communication protocol

Message structure:
- Byte 1: Command code (e.g., 0x05 for read firmware)
- Byte 2: Communication version (always 0x01)
- Bytes 3-4: Message size (little-endian)
- Bytes 5-6: CRC16 calculated for the message
"""

# CRC16 lookup table for polynomial calculation
CRC16_TABLE = [
 0x0000, 0x3d65, 0x7aca, 0x47af, 0xf594, 0xc8f1, 0x8f5e, 0xb23b, 0xd64d,
    0xeb28, 0xac87, 0x91e2, 0x23d9, 0x1ebc, 0x5913, 0x6476, 0x91ff, 0xac9a,
    0xeb35, 0xd650, 0x646b, 0x590e, 0x1ea1, 0x23c4, 0x47b2, 0x7ad7, 0x3d78,
    0x001d, 0xb226, 0x8f43, 0xc8ec, 0xf589, 0x1e9b, 0x23fe, 0x6451, 0x5934,
    0xeb0f, 0xd66a, 0x91c5, 0xaca0, 0xc8d6, 0xf5b3, 0xb21c, 0x8f79, 0x3d42,
    0x0027, 0x4788, 0x7aed, 0x8f64, 0xb201, 0xf5ae, 0xc8cb, 0x7af0, 0x4795,
    0x003a, 0x3d5f, 0x5929, 0x644c, 0x23e3, 0x1e86, 0xacbd, 0x91d8, 0xd677,
    0xeb12, 0x3d36, 0x0053, 0x47fc, 0x7a99, 0xc8a2, 0xf5c7, 0xb268, 0x8f0d,
    0xeb7b, 0xd61e, 0x91b1, 0xacd4, 0x1eef, 0x238a, 0x6425, 0x5940, 0xacc9,
    0x91ac, 0xd603, 0xeb66, 0x595d, 0x6438, 0x2397, 0x1ef2, 0x7a84, 0x47e1,
    0x004e, 0x3d2b, 0x8f10, 0xb275, 0xf5da, 0xc8bf, 0x23ad, 0x1ec8, 0x5967,
    0x6402, 0xd639, 0xeb5c, 0xacf3, 0x9196, 0xf5e0, 0xc885, 0x8f2a, 0xb24f,
    0x0074, 0x3d11, 0x7abe, 0x47db, 0xb252, 0x8f37, 0xc898, 0xf5fd, 0x47c6,
    0x7aa3, 0x3d0c, 0x0069, 0x641f, 0x597a, 0x1ed5, 0x23b0, 0x918b, 0xacee,
    0xeb41, 0xd624, 0x7a6c, 0x4709, 0x00a6, 0x3dc3, 0x8ff8, 0xb29d, 0xf532,
    0xc857, 0xac21, 0x9144, 0xd6eb, 0xeb8e, 0x59b5, 0x64d0, 0x237f, 0x1e1a,
    0xeb93, 0xd6f6, 0x9159, 0xac3c, 0x1e07, 0x2362, 0x64cd, 0x59a8, 0x3dde,
    0x00bb, 0x4714, 0x7a71, 0xc84a, 0xf52f, 0xb280, 0x8fe5, 0x64f7, 0x5992,
    0x1e3d, 0x2358, 0x9163, 0xac06, 0xeba9, 0xd6cc, 0xb2ba, 0x8fdf, 0xc870,
    0xf515, 0x472e, 0x7a4b, 0x3de4, 0x0081, 0xf508, 0xc86d, 0x8fc2, 0xb2a7,
    0x009c, 0x3df9, 0x7a56, 0x4733, 0x2345, 0x1e20, 0x598f, 0x64ea, 0xd6d1,
    0xebb4, 0xac1b, 0x917e, 0x475a, 0x7a3f, 0x3d90, 0x00f5, 0xb2ce, 0x8fab,
    0xc804, 0xf561, 0x9117, 0xac72, 0xebdd, 0xd6b8, 0x6483, 0x59e6, 0x1e49,
    0x232c, 0xd6a5, 0xebc0, 0xac6f, 0x910a, 0x2331, 0x1e54, 0x59fb, 0x649e,
    0x00e8, 0x3d8d, 0x7a22, 0x4747, 0xf57c, 0xc819, 0x8fb6, 0xb2d3, 0x59c1,
    0x64a4, 0x230b, 0x1e6e, 0xac55, 0x9130, 0xd69f, 0xebfa, 0x8f8c, 0xb2e9,
    0xf546, 0xc823, 0x7a18, 0x477d, 0x00d2, 0x3db7, 0xc83e, 0xf55b, 0xb2f4,
    0x8f91, 0x3daa, 0x00cf, 0x4760, 0x7a05, 0x1e73, 0x2316, 0x64b9, 0x59dc,
    0xebe7, 0xd682, 0x912d, 0xac48
]

class FCX_Serial(serial.Serial):
    """
    FCX Serial communication class extending standard serial interface
    Implements the protocol for optical communication with FCX 3.0 devices
    """
    def __init__(self, port, baudrate, bytesize, parity, stopbits, timeout):
        """
        Initialize serial connection with FCX device
        
        Args:
            port: Serial port name (e.g., 'COM3')
            baudrate: Baud rate (e.g., 9600)
            bytesize: Number of data bits
            parity: Parity checking type
            stopbits: Number of stop bits
            timeout: Read timeout in seconds
        """
        super().__init__(port, baudrate, bytesize, parity, stopbits, timeout)
        
        # Define constant sizes for different message types
        self.ACK_DATA_SIZE = 6
        self.READ_FIRMWARE_DATA_SIZE = 8
        self.READ_SERIALN_DATA_SIZE = 12

        # Command constants
        self.OPTCOMM_READ_FIRMWARE = [0x05, 0x01, 0x00]
        self.OPTCOMM_SYNC_REQ = [0xA5, 0x01, 0x00]
        
        # Protocol command codes dictionary
        self.PROTOCOL = {
            "cmd_sync": [0xA6],           # Synchronization command
            "cmd_sync_comm": [0xA7],      # Communication synchronization
            "cmd_read_sn": [0x05],        # Read serial number
            "cmd_write_sn": [0x03],       # Write serial number
            "cmd_post_mode": [0x12],      # Post mode command
            "cmd_nack": [0xF2],           # Negative acknowledgment
            "cmd_memory_checksum": [0x30], # Memory checksum
            "cmd_invalid": [0xFF]         # Invalid command
        }
        
        # Maximum packet length
        self.MAX_PACK_LEN = 40

    def calculate_crc(self, data, initial_crc=0):
        """
        Calculate CRC16 for the given data using lookup table
        
        Args:
            data: List or bytes of data to calculate CRC for
            initial_crc: Initial CRC value (default: 0)
            
        Returns:
            List of two bytes [high, low] representing the CRC
        """
        # Convert list to bytes if needed
        if isinstance(data, list):
            data = bytes(data)
        elif not isinstance(data, bytes):
            raise TypeError("Data must be a list of integers or bytes")
        
        # Calculate CRC
        crc = initial_crc
        for byte in data:
            crc = CRC16_TABLE[byte ^ ((crc >> 8) & 0xFF)] ^ ((crc << 8) & 0xFFFF)
        
        # Final calculation
        temp = ~crc & 0xFFFF
        result_high = (temp & 0xFF00) >> 8
        result_low = (temp & 0x00FF) << 8
        
        # Return as a list of two bytes [high, low]
        return [result_low >> 8, result_high]
    
    def fcx_open_connection(self):
        """Open serial connection if not already open"""
        if not self.is_open():
            self.open()

    def fcx_send_command(self, data):
        """
        Send raw command data to device
        
        Args:
            data: List or bytes to send
        """
        if isinstance(data, list):
            data = bytes(data)
        self.write(data)

    def assemble_pack(self, cmd):
        """
        Assembles a command packet with CRC
        
        Args:
            cmd: Command code (from PROTOCOL dictionary)
            
        Returns:
            bytes: Assembled packet with header and CRC
        """
        # Create a 4-byte header array
        array = [0 for _ in range(4)]
        
        # Set header fields
        array[0] = cmd[0] if isinstance(cmd, list) else cmd  # Command code
        array[1] = 0x01  # Protocol version (always 0x01)
        array[2] = 0x06  # Message size low byte (6 bytes: 4-byte header + 2-byte CRC)
        array[3] = 0x00  # Message size high byte
        
        # Calculate CRC
        high_byte, low_byte = self.calculate_crc(array)
        print(high_byte, low_byte)
        
        # Add CRC to packet
        array.append(low_byte)
        array.append(high_byte)

        return bytes(array)
    
    def write_cmd(self, cmd, payload):
        """
        Writes a command with payload to the device
        
        Args:
            cmd: Command code (from PROTOCOL dictionary)
            payload: Byte array of payload data
        
        Returns:
            bytes/bool: Response data if successful, False otherwise
        """
        result = False
        
        # Convert list to bytes if needed
        if isinstance(payload, list):
            payload = bytes(payload)
        
        # Calculate packet length
        length_header = 4
        length_crc = 2
        length_payload = len(payload)
        length_pack = length_header + length_payload + length_crc
        print(length_pack)
        
        # Create packet array with header
        array = [0 for _ in range(4)]
        
        # Set header fields
        array[0] = cmd[0] if isinstance(cmd, list) else cmd  # Command code
        array[1] = 0x01  # Protocol version (always 0x01)
        array[2] = 0x06  # Message size low byte (default 6 bytes for basic commands)
        array[3] = 0x00  # Message size high byte
        
        # Calculate CRC
        high_byte, low_byte = self.calculate_crc(array)
        print(high_byte, low_byte)
        
        # Add CRC to packet
        array.append(low_byte)
        array.append(high_byte)

        print(array)

        # Setup for transmission
        to_send = True
        remaining_bytes = length_pack
        
        # Determine initial chunk size to send
        length_to_send = length_pack
        if length_pack >= self.MAX_PACK_LEN:
            length_to_send = 4  # Start with header if packet is large
        
        # Check for sync command from device
        pack = self.read(self.ACK_DATA_SIZE)
        if not pack or len(pack) == 0:
            return False
        
        # Check if the device is ready to receive
        sync_cmd_value = self.PROTOCOL["cmd_sync"][0]
        sync_cmd_comm_value = self.PROTOCOL["cmd_sync_comm"][0]
        if pack[0] == sync_cmd_value or pack[0] == sync_cmd_comm_value:
            print(f"Device ready to receive (SYNC_COMM detected)")
            
            # Send the command in chunks until completed
            while remaining_bytes > 0:
                if to_send:
                    # Get chunk to send
                    array_to_send = array[length_pack - remaining_bytes:length_pack - remaining_bytes + length_to_send]
                    
                    # Send the chunk
                    self.write(array_to_send)
                    print(f"Sent {length_to_send} bytes, {remaining_bytes} remaining")
                
                # Wait for response
                data_read = self.read(self.ACK_DATA_SIZE)
                
                if data_read and len(data_read) > 0:
                    # Process response based on command code
                    if data_read[0] == self.PROTOCOL["cmd_sync"][0]:
                        # Device sent sync command
                        to_send = False
                        print("Received SYNC command, pausing send")
                        return data_read 
                    elif data_read[0] == self.PROTOCOL["cmd_sync_comm"][0]:
                        to_send = False 
                        print("Received SYNC_COMM, pausing send")
                        return data_read
                    else:
                        # Unknown response
                        to_send = False
                        print(f"Received unknown response: {data_read[0]:02X}")
                else:
                    # No response or timeout
                    result = False
                    print("No response from device")
                    break
        else:
            print(f"Device not ready: expected {sync_cmd_value:02X}, got {pack[0]:02X}")
        
        return result
    
    def print_array(self, msg, array):
        """
        Prints byte array in hex format for debugging
        
        Args:
            msg: Message to prepend
            array: Byte array to print
        """
        hex_values = [f"{b:02x}" for b in array]
        hex_string = ",".join(hex_values)
        print(f"{msg} {hex_string}")
    
    def parse_payload(self, pack):
        """
        Extract payload from response packet
        
        Args:
            pack: Full packet including header
            
        Returns:
            bytes: Extracted payload data
        """
        if pack is None or len(pack) < 6:
            return None
            
        # Calculate payload length from header
        length_payload = ((pack[3] & 0xFF) << 8 | (pack[2] & 0xFF)) - 6  # 6 = header + CRC
        array = pack[4:4+length_payload]
        return array
    
    def read_cmd(self, cmd):
        """
        Sends a read command and receives response with data
        
        Args:
            cmd: Command code (from PROTOCOL dictionary)
            
        Returns:
            bytes: Complete response packet, or None if failed
        """
        # Check for sync command
        pack = self.read(self.ACK_DATA_SIZE)
        if pack and len(pack) > 0:
            sync_cmd_value = self.PROTOCOL["cmd_sync_comm"][0]
            if pack[0] == sync_cmd_value:
                print(f"Device ready to receive (SYNC_COMM detected)")
                
                # Assemble and send command packet
                send = self.assemble_pack(cmd)
                self.write(send)
                self.print_array("Sent command:", send)
                
                # Try to read response multiple times
                for i in range(5):  # Try up to 5 times
                    pack = self.read(40)
                    if pack and len(pack) > 0:
                       return pack
                    else:
                        print("Failed to receive additional data")
            else:
                print(f"Device not ready: expected {sync_cmd_value:02x}, got {pack[0]:02x}")
        else:
            print("No initial response from device")
        
        return None

    def optint_read_firmware(self):
        """
        Send command to read firmware version
        
        This method calculates CRC for the read firmware command,
        appends it to the command, and sends the command
        """
        high, low = self.calculate_crc(self.OPTCOMM_READ_FIRMWARE, len(self.OPTCOMM_READ_FIRMWARE))
        print(high, low)
        self.OPTCOMM_READ_FIRMWARE.append(high)
        self.OPTCOMM_READ_FIRMWARE.append(low)
        self.fcx_send_command(self.OPTCOMM_SYNC_REQ)

    def optint_sync(self):
        """
        Send synchronization request to device
        
        This method calculates CRC for the sync request command,
        appends it to the command, and sends it to the device
        """
        high, low = self.calculate_crc(self.OPTCOMM_SYNC_REQ, len(self.OPTCOMM_SYNC_REQ))
        self.OPTCOMM_SYNC_REQ.append(high)
        self.OPTCOMM_SYNC_REQ.append(low)
        self.fcx_send_command(self.OPTCOMM_READ_FIRMWARE)

if __name__ == "__main__":
    # Initialize serial connection with FCX device
    serial_interface = FCX_Serial(
                port='COM3', 
                baudrate=9600,  
                bytesize=serial.EIGHTBITS,
                parity=serial.PARITY_NONE,
                stopbits=serial.STOPBITS_ONE,
                timeout=2
                )
    
    # Try to read firmware version with multiple attempts
    attempt = 0
    while(True):
        if attempt < 10:
            try:
                # Send sync request command
                result = serial_interface.write_cmd(0xA8, [])
                if result:
                    print("Sync request command sent successfully")
                else:
                    print("Failed to send sync request command")

                # Read firmware version
                firmware_version = serial_interface.read_cmd(0x05)
                
                # Find the start of the firmware version data
                index = firmware_version.find(b'\x05')

                # Extract firmware version if found
                if index != -1:
                    extracted = firmware_version[index + 4 : index + 11]
                    
                    # Convert to human-readable format
                    decimal_list = list(extracted)
                    firmware_version = ""
                    for element in decimal_list:
                        if element == 0:
                            firmware_version += "."
                        else:
                            firmware_version += str(element)
                    
                    print(f"Sucesso: Versão de firmware: {firmware_version}")
                    break
                else:
                    print("Falha ao ler firmware, tentando novamente\n")
                    time.sleep(0.5)
                    attempt += 1

            except Exception as error:
                # Continue to next attempt on error
                continue
                    
        else:
            print("Falha ao ler versão firmware, remova a conexão e tente novamente")
            break