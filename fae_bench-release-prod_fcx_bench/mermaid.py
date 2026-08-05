import os
import re
import subprocess
import tempfile
from pathlib import Path
import argparse
import shutil
import textwrap

def find_mermaid_blocks(markdown_text):
    """Extract all mermaid code blocks from markdown text."""
    pattern = r"```mermaid\n(.*?)\n```"
    return re.findall(pattern, markdown_text, re.DOTALL)+

def sanitize_filename(title, diagram_index):
    """Create a valid filename from diagram title or index."""
    if title:
        # Remove invalid characters and limit length
        sanitized = re.sub(r'[^\w\s-]', '', title).strip()
        sanitized = re.sub(r'[-\s]+', '_', sanitized)
        return f"{diagram_index:02d}_{sanitized[:50]}"
    else:
        return f"diagram_{diagram_index:02d}"

def extract_title(mermaid_code, diagram_index):
    """Try to extract a title from the diagram code."""
    # For graph/flowchart
    graph_title_match = re.search(r"graph\s+[A-Z]+\s+(.+?)(?:\n|$)", mermaid_code)
    if graph_title_match:
        return sanitize_filename(graph_title_match.group(1), diagram_index)
    
    # For class diagram
    class_title_match = re.search(r"classDiagram\s+(.+?)(?:\n|$)", mermaid_code)
    if class_title_match:
        return sanitize_filename(class_title_match.group(1), diagram_index)
    
    # For sequence diagram
    seq_title_match = re.search(r"sequenceDiagram\s+(.+?)(?:\n|$)", mermaid_code)
    if seq_title_match:
        return sanitize_filename(seq_title_match.group(1), diagram_index)
    
    # If no title found, use content description
    content_words = ' '.join(mermaid_code.split()[:5])
    if content_words:
        return sanitize_filename(content_words, diagram_index)
    
    # Default filename
    return sanitize_filename("", diagram_index)

def render_mermaid_to_pdf(mermaid_diagrams, output_dir):
    """Render each Mermaid diagram to a PDF file."""
    # Create output directory if it doesn't exist
    os.makedirs(output_dir, exist_ok=True)
    
    # Check if mmdc is installed
    try:
        # Check if mmdc is available in the system
        subprocess.run(["mmdc", "--version"], 
                      stdout=subprocess.PIPE, 
                      stderr=subprocess.PIPE, 
                      check=True)
    except (subprocess.SubprocessError, FileNotFoundError):
        print("Error: mermaid-cli (mmdc) not found. Please install it with:")
        print("npm install -g @mermaid-js/mermaid-cli")
        return
    
    print(f"Found {len(mermaid_diagrams)} Mermaid diagrams")
    
    for i, diagram in enumerate(mermaid_diagrams, 1):
        # Create a filename based on content
        filename = extract_title(diagram, i)
        
        # Create temporary file for the mermaid content
        with tempfile.NamedTemporaryFile(mode='w', suffix='.mmd', delete=False) as temp_file:
            temp_path = temp_file.name
            temp_file.write(diagram)
        
        try:
            # Output paths
            svg_path = os.path.join(output_dir, f"{filename}.svg")
            pdf_path = os.path.join(output_dir, f"{filename}.pdf")
            
            # Render to SVG first
            print(f"Rendering diagram {i}/{len(mermaid_diagrams)}: {filename}")
            subprocess.run(["mmdc", "-i", temp_path, "-o", svg_path, "-b", "transparent"], 
                          stdout=subprocess.PIPE, 
                          stderr=subprocess.PIPE, 
                          check=True)
            
            # Convert SVG to PDF
            if shutil.which("inkscape") is not None:
                # Use Inkscape if available for better PDF conversion
                subprocess.run(["inkscape", "--export-filename", pdf_path, svg_path],
                              stdout=subprocess.PIPE, 
                              stderr=subprocess.PIPE,
                              check=True)
            else:
                # Try cairosvg as an alternative
                try:
                    import cairosvg
                    cairosvg.svg2pdf(url=svg_path, write_to=pdf_path)
                except ImportError:
                    print("Warning: Neither Inkscape nor cairosvg is available. Installing cairosvg:")
                    print("pip install cairosvg")
                    
                    # Try using the built-in tools in Chrome/Chromium if available
                    try:
                        for browser in ["chromium", "chromium-browser", "chrome", "google-chrome"]:
                            if shutil.which(browser):
                                subprocess.run([browser, "--headless", "--disable-gpu", 
                                              f"--print-to-pdf={pdf_path}", f"file://{os.path.abspath(svg_path)}"],
                                              stdout=subprocess.PIPE, 
                                              stderr=subprocess.PIPE,
                                              check=True)
                                break
                        else:
                            print(f"Warning: Could not convert {svg_path} to PDF.")
                            print("Install Inkscape or cairosvg for PDF conversion.")
                    except subprocess.SubprocessError:
                        print(f"Error converting {svg_path} to PDF")
            
            print(f"  → Saved to {pdf_path}")
            
        except subprocess.SubprocessError as e:
            print(f"Error rendering diagram {i}: {e}")
        finally:
            # Clean up temporary file
            os.unlink(temp_path)

def main():
    parser = argparse.ArgumentParser(description='Convert Mermaid diagrams to PDF files')
    parser.add_argument('input_file', help='Markdown file containing Mermaid diagrams')
    parser.add_argument('--output-dir', '-o', default='mermaid_pdfs', 
                        help='Directory to save PDF files (default: ./mermaid_pdfs)')
    
    args = parser.parse_args()
    
    if not os.path.isfile(args.input_file):
        print(f"Error: Input file '{args.input_file}' not found")
        returni
    
    with open(args.input_file, 'r', encoding='utf-8') as file:
        markdown_content = file.read()
    
    mermaid_diagrams = find_mermaid_blocks(markdown_content)
    
    if not mermaid_diagrams:
        print("No Mermaid diagrams found in the input file")
        return
    
    render_mermaid_to_pdf(mermaid_diagrams, args.output_dir)
    print(f"Completed! PDF files saved to {args.output_dir}")


if __name__ == "__main__":
    main()