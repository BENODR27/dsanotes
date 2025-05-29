# How to Use `vcs.py`

## Steps to Run the Python Code

### 1. Save the Code
Save the code provided in the immersive artifact to a file named `vcs.py`. Ensure the file is saved in a directory where you have write permissions.

### 2. Open a Terminal
Open a terminal or command prompt on your system.

### 3. Navigate to the Directory
Use the `cd` command to navigate to the directory where you saved the `vcs.py` file. For example:
```bash
cd my_vcs
```

### 4. Run the Script
Execute the Python script using the following command:
```bash
python vcs.py <command> [<args>]
```
- Replace `<command>` with the desired VCS command (e.g., `init`, `add`, `commit`, `log`).
- Replace `[<args>]` with any arguments required for the specific command (e.g., the file path for `add`, or the commit message for `commit`).

---

## Example Usage

### Initialize a Repository
```bash
python vcs.py init
```

### Add a File to the Staging Area
```bash
python vcs.py add my_file.txt
```

### Commit the Staged Files
```bash
python vcs.py commit "Initial commit"
```

### Display the Commit History
```bash
python vcs.py log
```

### Get Help
```bash
python vcs.py help
```

---

## Important Notes

### 1. Python Installation
Ensure that Python is installed on your system and added to your system's `PATH` environment variable. You can verify this by running:
```bash
python --version
```

### 2. Permissions
Make sure you have the necessary permissions to create directories and files in the location where you are running the script.

### 3. Error Handling
The script includes basic error handling. However, errors may occur if commands are used incorrectly or if there are issues with file operations. Read the error messages in the terminal to diagnose any problems.

