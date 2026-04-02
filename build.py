import os
import shutil
import subprocess
import sys

def main():
    print("=" * 50)
    print(" Minecraft Admin Mod - Build Script")
    print("=" * 50)
    print()
    
    # Get current directory
    project_dir = r"c:\Users\lenor\OneDrive\Documenten\Code\minecraft-mod"
    old_command_dir = os.path.join(project_dir, "src", "main", "java", "net", "russianphonks", "adminmod", "command")
    
    # Change to project directory
    os.chdir(project_dir)
    print(f"Working directory: {project_dir}")
    print()
    
    # Step 1: Delete old command folder
    print("Step 1: Removing old command folder...")
    if os.path.exists(old_command_dir):
        try:
            shutil.rmtree(old_command_dir)
            print("✓ Old command folder deleted successfully!")
        except Exception as e:
            print(f"✗ Error deleting folder: {e}")
            print("  Please delete manually:")
            print(f"  {old_command_dir}")
            input("Press Enter to continue anyway...")
    else:
        print("✓ Old command folder already deleted.")
    print()
    
    # Step 2: Run Gradle build
    print("Step 2: Building with Gradle...")
    print("-" * 50)
    print()
    
    gradlew = "gradlew.bat" if sys.platform == "win32" else "./gradlew"
    
    try:
        result = subprocess.run(
            [gradlew, "clean", "build", "--stacktrace"],
            cwd=project_dir,
            capture_output=False,
            text=True
        )
        
        print()
        print("-" * 50)
        
        if result.returncode == 0:
            print()
            print("=" * 50)
            print(" ✓ BUILD SUCCESSFUL!")
            print("=" * 50)
            print()
            jar_file = os.path.join(project_dir, "build", "libs", "adminmod-1.0.0.jar")
            print(f"JAR file location:")
            print(f"{jar_file}")
            print()
            
            if os.path.exists(jar_file):
                size_mb = os.path.getsize(jar_file) / (1024 * 1024)
                print(f"File size: {size_mb:.2f} MB")
        else:
            print()
            print("=" * 50)
            print(" ✗ BUILD FAILED!")
            print("=" * 50)
            print()
            print("Please check the error messages above.")
            
    except Exception as e:
        print()
        print("=" * 50)
        print(f" ✗ Error running Gradle: {e}")
        print("=" * 50)
    
    print()
    input("Press Enter to exit...")

if __name__ == "__main__":
    main()
