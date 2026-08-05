import os
import codecs
import chardet

def convert_to_utf8(file_path):
    # Detect the encoding of the file
    with open(file_path, 'rb') as f:
        raw_data = f.read()
        detection = chardet.detect(raw_data)
        encoding = detection['encoding']
        
    if encoding is None:
        print(f"Could not detect encoding for {file_path}, skipping...")
        return
    
    # Skip if already UTF-8
    if encoding.lower() == 'utf-8' or encoding.lower() == 'utf8':
        print(f"{file_path} is already UTF-8, skipping...")
        return
    
    try:
        # Read the file with detected encoding
        with codecs.open(file_path, 'r', encoding=encoding) as f:
            content = f.read()
        
        # Write back with UTF-8 encoding
        with codecs.open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Successfully converted {file_path} from {encoding} to UTF-8")
    except Exception as e:
        print(f"Error converting {file_path}: {str(e)}")

def process_directory(directory_path):
    count = 0
    for root, dirs, files in os.walk(directory_path):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                convert_to_utf8(file_path)
                count += 1
    return count

if __name__ == "__main__":
    import sys
    
    if len(sys.argv) > 1:
        directory = sys.argv[1]
    else:
        directory = input("Enter directory path to process: ")
    
    if not os.path.isdir(directory):
        print(f"Error: {directory} is not a valid directory")
        sys.exit(1)
    
    print(f"Converting .java files to UTF-8 in {directory} and its subdirectories...")
    count = process_directory(directory)
    print(f"Completed! Processed {count} .java files.")