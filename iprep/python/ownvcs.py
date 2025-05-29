import os
import hashlib
import shutil
import time

class VersionControlSystem:
    """
    A simplified version control system, similar to Git.

    This implementation includes the following commands:
    - init:  Initializes a new repository.
    - add:   Adds a file to the staging area.
    - commit: Commits the staged files to the repository.
    - log:    Displays the commit history.
    - status: Displays the status of the working directory.
    - diff:   Displays the differences between the working directory and the staging area, or between commits.
    - checkout: Checks out a specific commit or file.
    - branch: Lists, creates, or deletes branches.
    - merge: Merges a branch into the current branch.

    The repository structure is as follows:
    .vcs/
        objects/     # Stores the content of files (blobs) and commit information
        refs/
            heads/   # Stores references to the heads of branches
        index        # Stores the staging area information
        config       # Stores configuration information (e.g., branch names)
    """
    def __init__(self, repo_path='.'):
        """
        Initializes the VersionControlSystem object.

        Args:
            repo_path (str, optional): The path to the repository. Defaults to the current directory.
        """
        self.repo_path = repo_path
        self.vcs_dir = os.path.join(repo_path, '.vcs')
        self.objects_dir = os.path.join(self.vcs_dir, 'objects')
        self.refs_heads_dir = os.path.join(self.vcs_dir, 'refs', 'heads')
        self.index_file = os.path.join(self.vcs_dir, 'index')
        self.config_file = os.path.join(self.vcs_dir, 'config')

    def _create_dir(self, path):
        """
        Creates a directory if it does not exist.

        Args:
            path (str): The path to the directory.
        """
        if not os.path.exists(path):
            os.makedirs(path)

    def _write_file(self, path, content, mode='w'):
        """
        Writes content to a file.

        Args:
            path (str): The path to the file.
            content (str or bytes): The content to write.
            mode (str, optional): The file writing mode ('w' or 'wb'). Defaults to 'w'.
        """
        if 'b' in mode:
            if not isinstance(content, bytes):
                content = content.encode('utf-8')
        with open(path, mode) as f:
            f.write(content)

    def _read_file(self, path, mode='r'):
        """
        Reads content from a file.

        Args:
            path (str): The path to the file.
            mode (str, optional): The file reading mode ('r' or 'rb'). Defaults to 'r'.

        Returns:
            str or bytes: The content of the file, or None if the file does not exist.
        """
        if not os.path.exists(path):
            return None
        with open(path, mode) as f:
            return f.read()
        

    def _hash_object(self, content):
        """
        Hashes the content using SHA-1.

        Args:
            content (str or bytes): The content to hash.

        Returns:
            str: The SHA-1 hash of the content.
        """
        if not isinstance(content, bytes):
            content = content.encode('utf-8')
        return hashlib.sha1(content).hexdigest()

    def _get_object_path(self, oid):
        """
        Gets the path to an object given its object ID.

        Args:
            oid (str): The object ID.

        Returns:
            str: The path to the object.
        """
        return os.path.join(self.objects_dir, oid)

    def _create_object(self, content):
        """
        Creates a new object in the object store.

        Args:
            content (str or bytes): The content of the object.

        Returns:
            str: The object ID (SHA-1 hash of the content).
        """
        oid = self._hash_object(content)
        object_path = self._get_object_path(oid)
        if not os.path.exists(object_path):
            self._write_file(object_path, content, 'wb')  # Use 'wb' for consistency
        return oid

    def _get_head_path(self, branch='main'):
        """
        Gets the path to the head of a branch.

        Args:
            branch (str, optional): The name of the branch. Defaults to 'main'.

        Returns:
            str: The path to the branch head.
        """
        return os.path.join(self.refs_heads_dir, branch)

    def _read_head(self, branch='main'):
        """
        Reads the commit ID from the head of a branch.

        Args:
            branch (str, optional): The name of the branch. Defaults to 'main'.

        Returns:
            str: The commit ID, or None if the branch does not exist.
        """
        head_path = self._get_head_path(branch)
        if not os.path.exists(head_path):
            return None
        return self._read_file(head_path).strip()

    def _write_head(self, commit_id, branch='main'):
        """
        Writes the commit ID to the head of a branch.

        Args:
            commit_id (str): The commit ID.
            branch (str, optional): The name of the branch. Defaults to 'main'.
        """
        head_path = self._get_head_path(branch)
        self._write_file(head_path, commit_id)

    def _read_index(self):
        """
        Reads the contents of the index file.

        Returns:
            dict: A dictionary representing the index, where keys are file paths
                  and values are the corresponding object IDs.  Returns an empty
                  dict if the index file does not exist.
        """
        index = {}
        if not os.path.exists(self.index_file):
            return index
        for line in self._read_file(self.index_file).splitlines():
            if line.strip():  # prevent empty lines from causing errors.
                try:
                    filepath, oid = line.split()
                    index[filepath] = oid
                except ValueError:
                    print(f"Skipping malformed line in index: {line}")
        return index

    def _write_index(self, index):
        """
        Writes the index to the index file.

        Args:
            index (dict): A dictionary representing the index.
        """
        contents = [f"{filepath} {oid}" for filepath, oid in index.items()]
        self._write_file(self.index_file, '\n'.join(contents))

    def init(self):
        """
        Initializes a new repository.
        """
        self._create_dir(self.vcs_dir)
        self._create_dir(self.objects_dir)
        self._create_dir(self.refs_heads_dir)
        self._write_file(self.index_file, '')
        self._write_file(self.config_file, 'main')  # Default branch is 'main'
        self._write_head(commit_id='', branch='main') # Initialize main branch with empty commit
        print('Initialized a new repository.')

    def add(self, filepath):
        """
        Adds a file to the staging area.

        Args:
            filepath (str): The path to the file.
        """
        if not os.path.exists(filepath):
            print(f"File not found: {filepath}")
            return

        with open(filepath, 'rb') as f: # Open in binary mode
            content = f.read()
        oid = self._create_object(content)
        index = self._read_index()
        index[filepath] = oid
        self._write_index(index)
        print(f"Added {filepath} to the staging area.")

    def commit(self, message):
        """
        Commits the staged files to the repository.

        Args:
            message (str): The commit message.
        """
        if not os.path.exists(self.vcs_dir):
            print("Not a vcs repository (or any of the parent directories): .vcs")
            return

        index = self._read_index()
        if not index:
            print("No files staged to commit.")
            return

        # Create a commit object
        commit_content = f"tree:\n"  # Placeholder, will be extended.
        tree_content = ""
        for filepath, oid in index.items():
            tree_content += f"{oid} {filepath}\n"
        tree_id = self._create_object(tree_content)
        commit_content = f"tree {tree_id}\n"
        parent_commit_id = self._read_head() # Get the current HEAD
        if parent_commit_id:
            commit_content += f"parent {parent_commit_id}\n"
        commit_content += f"author: User <user@example.com>\n"  #  Add author
        commit_content += f"committer: User <user@example.com>\n" # Add committer
        commit_content += f"date: {int(time.time())}\n"
        commit_content += f"\n{message}\n"
        commit_id = self._create_object(commit_content)

        # Update the branch head to point to the new commit.
        branch = self._read_config()  # Get current branch
        self._write_head(commit_id, branch)
        # Clear the index after commit
        self._write_index({})

        print(f"Committed with commit ID: {commit_id}")

    def log(self):
        """
        Displays the commit history.
        """
        if not os.path.exists(self.vcs_dir):
            print("Not a vcs repository (or any of the parent directories): .vcs")
            return

        head_commit_id = self._read_head()
        if not head_commit_id:
            print("No commits yet.")
            return

        commit_id = head_commit_id
        while commit_id:
            commit_path = self._get_object_path(commit_id)
            if not os.path.exists(commit_path):
                print(f"Warning: Commit object not found: {commit_id}")
                return
            commit_content = self._read_file(commit_path)
            print(f"Commit: {commit_id}")
            for line in commit_content.splitlines():
                if line.startswith("author:"):
                    print(line)
                if line.startswith("date:"):
                    print(line)
                if not line.startswith("tree") and not line.startswith("parent") and not line.startswith("author") and not line.startswith("committer") and not line.startswith("date"):
                    print(line)

            parent_line = [line for line in commit_content.splitlines() if line.startswith("parent ")]
            commit_id = parent_line[0].split(" ")[1] if parent_line else None
            print("-" * 40)

    def status(self):
        """Displays the status of the working directory, staging area, and repository."""
        if not os.path.exists(self.vcs_dir):
            print("Not a vcs repository (or any of the parent directories): .vcs")
            return

        index = self._read_index()
        working_dir_files = set(os.listdir('.')) - {'.vcs'}  # Ignore the .vcs directory.

        staged_files = set(index.keys())
        modified_files = set()
        untracked_files = working_dir_files - staged_files

        for filepath in staged_files:
            if not os.path.exists(filepath):
                modified_files.add(filepath) # File was deleted
                continue
            with open(filepath, 'rb') as f:
                content = f.read()
            oid = self._hash_object(content)
            if oid != index[filepath]:
                modified_files.add(filepath)

        for filepath in working_dir_files:
            if filepath in staged_files:
                with open(filepath, 'rb') as f:
                    content = f.read()
                oid = self._hash_object(content)
                if oid != index[filepath]:
                    modified_files.add(filepath)

        print("Staged changes:")
        if not staged_files - modified_files:
            print("  (none)")
        else:
            for filepath in staged_files - modified_files:
                print(f"  {filepath}")

        print("\nModified but not staged:")
        if not modified_files:
            print("  (none)")
        else:
            for filepath in modified_files:
                print(f"  {filepath}")

        print("\nUntracked files:")
        if not untracked_files:
            print("  (none)")
        else:
            for filepath in untracked_files:
                print(f"  {filepath}")
    
    def diff(self, *args):
        """
        Displays the differences between the working directory and the staging area,
        or between commits.  This is a simplified version of diff.

        Args:
            *args:  Can be either:
                - (filepath):  Diff between working directory and staging area for filepath.
                - (commit_id1, commit_id2): Diff between two commits.
                - (filepath, commit_id): show the diff of a file in a commit
        """
        if not os.path.exists(self.vcs_dir):
            print("Not a vcs repository (or any of the parent directories): .vcs")
            return

        if len(args) == 1:
            filepath = args[0]
            index = self._read_index()
            if filepath not in index:
                print(f"File {filepath} is not staged.")
                return

            staged_oid = index[filepath]
            staged_content = self._read_file(self._get_object_path(staged_oid), 'rb')
            if not os.path.exists(filepath):
                working_content = b'' # empty byte
            else:
                working_content = self._read_file(filepath, 'rb')

            staged_lines = staged_content.splitlines()
            working_lines = working_content.splitlines()

            print(f"Diff for {filepath} (working directory vs. staging area):")
            for line in self._unified_diff(staged_lines, working_lines, "staged", "working"):
                print(line)

        elif len(args) == 2:
            if len(args[0]) == 40 and len(args[1]) == 40: # means it is commit ids
                commit_id1, commit_id2 = args
                commit1_content = self._read_file(self._get_object_path(commit_id1))
                commit2_content = self._read_file(self._get_object_path(commit_id2))
                if commit1_content is None or commit2_content is None:
                    print(f"commit does not exist")
                    return
                tree_id1 = [line.split()[1] for line in commit1_content.splitlines() if line.startswith("tree")][0]
                tree_id2 = [line.split()[1] for line in commit2_content.splitlines() if line.startswith("tree")][0]

                tree_content1 = self._read_file(self._get_object_path(tree_id1))
                tree_content2 = self._read_file(self._get_object_path(tree_id2))
                if tree_content1 is None or tree_content2 is None:
                    return

                files_in_commit1 = {line.split()[1]: line.split()[0] for line in tree_content1.splitlines()}
                files_in_commit2 = {line.split()[1]: line.split()[0] for line in tree_content2.splitlines()}

                all_files = sorted(set(files_in_commit1.keys()) | set(files_in_commit2.keys()))

                print(f"Diff between commits {commit_id1} and {commit_id2}:")
                for filepath in all_files:
                    oid1 = files_in_commit1.get(filepath, None)
                    oid2 = files_in_commit2.get(filepath, None)
                    content1 = self._read_file(self._get_object_path(oid1), 'rb') if oid1 else b''
                    content2 = self._read_file(self._get_object_path(oid2), 'rb') if oid2 else b''
                    lines1 = content1.splitlines()
                    lines2 = content2.splitlines()
                    if content1 != content2:
                        print(f"--- a/{filepath}")
                        print(f"+++ b/{filepath}")
                        for line in self._unified_diff(lines1, lines2, commit_id1[:7], commit_id2[:7]):
                            print(line)
            elif len(args[0]) != 40 and len(args[1]) == 40: # file path and commit id
                filepath, commit_id = args
                commit_content = self._read_file(self._get_object_path(commit_id))
                if commit_content is None:
                    print(f"commit does not exist")
                    return
                tree_id = [line.split()[1] for line in commit_content.splitlines() if line.startswith("tree")][0]
                tree_content = self._read_file(self._get_object_path(tree_id))
                if tree_content is None:
                    return
                files_in_commit = {line.split()[1]: line.split()[0] for line in tree_content.splitlines()}
                if filepath not in files_in_commit:
                    print(f"File {filepath} not found in commit {commit_id}")
                    return
                oid = files_in_commit[filepath]
                commit_content = self._read_file(self._get_object_path(oid), 'rb')
                if not os.path.exists(filepath):
                    working_content = b''
                else:
                    working_content = self._read_file(filepath, 'rb')
                commit_lines = commit_content.splitlines()
                working_lines = working_content.splitlines()
                print(f"Diff for {filepath} (commit {commit_id[:7]} vs. working directory):")
                for line in self._unified_diff(commit_lines, working_lines, f"commit {commit_id[:7]}", "working"):
                    print(line)
            else:
                print("Invalid arguments for diff.  See usage in help.")

        else:
            print("Invalid arguments for diff.  See usage in help.")

    def _unified_diff(self, lines1, lines2, name1, name2):
        """
        Generates a simplified unified diff.

        Args:
            lines1 (list): List of lines for the first version.
            lines2 (list): List of lines for the second version.
            name1 (str): Name for the first version
            name2 (str): Name for the second version

        Returns:
            list: List of diff lines.
        """
        diff_lines = []
        i = 0
        j = 0
        while i < len(lines1) or j < len(lines2):
            if i < len(lines1) and j < len(lines2) and lines1[i] == lines2[j]:
                i += 1
                j += 1
                continue
            
            # Find the longest common prefix and suffix
            start = 0
            while start < len(lines1) and start < len(lines2) and start + i < len(lines1) and start + j < len(lines2) and lines1[i+start] == lines2[j+start]:
                start += 1
            end1 = len(lines1) - 1
            end2 = len(lines2) - 1
            while end1 >= i and end2 >= j and end1 >= 0 and end2 >=0 and lines1[end1] == lines2[end2]:
                end1 -= 1
                end2 -= 1
            
            # Print the diff
            if i < len(lines1) and j < len(lines2):
                diff_lines.append(f"@@ -{i+1},{start + (i>0)} +{j+1},{start + (j>0)} @@")
            elif i < len(lines1):
                diff_lines.append(f"@@ -{i+1},{start + (i>0)} +{j+1},0 @@")
            elif j < len(lines2):
                diff_lines.append(f"@@ -{i+1},0 +{j+1},{start + (j>0)} @@")

            while i < len(lines1) and (i < j + start or i <= end1):
                diff_lines.append(f"-{lines1[i].decode('utf-8')}")
                i += 1
            while j < len(lines2) and (j < i + start or j <= end2):
                diff_lines.append(f"+{lines2[j].decode('utf-8')}")
                j += 1
        return diff_lines
    
    def checkout(self, target):
        """
        Checks out a specific commit or file.

        Args:
            target (str): The commit ID or file path to check out.
        """
        if not os.path.exists(self.vcs_dir):
            print("Not a vcs repository (or any of the parent directories): .vcs")
            return

        if len(target) == 40:  # Assume it's a commit ID
            self._checkout_commit(target)
        else:
            self._checkout_file(target)

    def _checkout_commit(self, commit_id):
        """
        Checks out all files in a specific commit.

        Args:
            commit_id (str): The commit ID.
        """
        commit_path = self._get_object_path(commit_id)
        if not os.path.exists(commit_path):
            print(f"Commit not found: {commit_id}")
            return

        commit_content = self._read_file(commit_path)
        tree_id = [line.split()[1] for line in commit_content.splitlines() if line.startswith("tree")][0]
        tree_content = self._read_file(self._get_object_path(tree_id))
        if tree_content is None:
            print(f"Tree object not found for commit: {commit_id}")
            return

        files_in_commit = {line.split()[1]: line.split()[0] for line in tree_content.splitlines()}

        # Clear the working directory (except for .vcs)
        for filename in os.listdir('.'):
            if filename != '.vcs':
                if os.path.isfile(filename):
                  os.remove(filename)
                elif os.path.isdir(filename):
                    shutil.rmtree(filename) # remove entire directory

        # Restore files from the commit
        for filepath, oid in files_in_commit.items():
            object_path = self._get_object_path(oid)
            if not os.path.exists(object_path):
                print(f"Object not found: {oid} for file {filepath}")
                continue
            content = self._read_file(object_path, 'rb')
            self._write_file(filepath, content, 'wb')

        # Update the index to match the commit
        self._write_index(files_in_commit)
        # update the HEAD
        branch = self._read_config()
        self._write_head(commit_id, branch)

        print(f"Checked out commit: {commit_id}")

    def _checkout_file(self, filepath):
        """
        Checks out a specific file from the staging area or the latest commit.

        Args:
            filepath (str): The path to the file.
        """
        index = self._read_index()
        if filepath in index:
            oid = index[filepath]
            object_path = self._get_object_path(oid)
            if not os.path.exists(object_path):
                print(f"Object not found: {oid} for file {filepath}")
                return
            content = self._read_file(object_path, 'rb')
            self._write_file(filepath, content, 'wb')
            print(f"Checked out {filepath} from staging area.")
        else:
            # Get the latest commit
            head_commit_id = self._read_head()
            if not head_commit_id:
                print(f"File {filepath} not found in any commit.")
                return
            commit_path = self._get_object_path(head_commit_id)
            commit_content =self._read_file(commit_path)
            tree_id = [line.split()[1] for line in commit_content.splitlines() if line.startswith("tree")][0]
            tree_content = self._read_file(self._get_object_path(tree_id))
            if tree_content is None:
                print(f"Tree object not found for commit: {head_commit_id}")
                return

            files_in_commit = {line.split()[1]: line.split()[0] for line in tree_content.splitlines()}
            if filepath in files_in_commit:
                oid = files_in_commit[filepath]
                object_path = self._get_object_path(oid)
                if not os.path.exists(object_path):
                    print(f"Object not found: {oid} for file {filepath} in commit {head_commit_id}")
                    return
                content = self._read_file(object_path, 'rb')
                self._write_file(filepath, content, 'wb')
                print(f"Checked out {filepath} from latest commit.")
            else:
                print(f"File {filepath} not found in any commit.")

    def branch(self, *args):
        """
        Lists, creates, or deletes branches.

        Args:
            *args:
                - (): Lists branches.
                - (branch_name): Creates a new branch.
                - (-d, branch_name): Deletes a branch.
        """
        if not os.path.exists(self.vcs_dir):
            print("Not a vcs repository (or any of the parent directories): .vcs")
            return

        if not args:
            self._list_branches()
        elif len(args) == 1:
            branch_name = args[0]
            self._create_branch(branch_name)
        elif len(args) == 2 and args[0] == '-d':
            branch_name = args[1]
            self._delete_branch(branch_name)
        else:
            print("Invalid arguments for branch.  See usage in help.")

    def _list_branches(self):
        """Lists all branches."""
        print("Branches:")
        current_branch = self._read_config()
        for filename in os.listdir(self.refs_heads_dir):
            if filename == current_branch:
                print(f"* {filename}")  # Indicate the current branch
            else:
                print(f"  {filename}")

    def _create_branch(self, branch_name):
        """
        Creates a new branch.

        Args:
            branch_name (str): The name of the new branch.
        """
        if os.path.exists(os.path.join(self.refs_heads_dir, branch_name)):
            print(f"Branch already exists: {branch_name}")
            return

        head_commit_id = self._read_head()  # Get the current HEAD commit
        self._write_head(head_commit_id, branch_name)  # Create the new branch pointing to the current commit.
        print(f"Created branch {branch_name}")

    def _delete_branch(self, branch_name):
        """
        Deletes a branch.

        Args:
            branch_name (str): The name of the branch to delete.
        """
        branch_path = os.path.join(self.refs_heads_dir, branch_name)
        if not os.path.exists(branch_path):
            print(f"Branch not found: {branch_name}")
            return

        current_branch = self._read_config()
        if branch_name == current_branch:
            print(f"Cannot delete the current branch ({branch_name}).")
            return

        os.remove(branch_path)
        print(f"Deleted branch {branch_name}")

    def _read_config(self):
        """Reads the current branch from the config file."""
        return self._read_file(self.config_file).strip()

    def _write_config(self, branch_name):
        """Writes the current branch to the config file."""
        self._write_file(self.config_file, branch_name)
    
    def merge(self, branch_name):
        """
        Merges the specified branch into the current branch.  This is a simplified merge.

        Args:
            branch_name (str): The name of the branch to merge.
        """
        if not os.path.exists(self.vcs_dir):
            print("Not a vcs repository (or any of the parent directories): .vcs")
            return

        branch_to_merge_path = os.path.join(self.refs_heads_dir, branch_name)
        if not os.path.exists(branch_to_merge_path):
            print(f"Branch not found: {branch_name}")
            return

        current_branch = self._read_config()
        if branch_name == current_branch:
            print(f"Cannot merge a branch into itself ({branch_name}).")
            return

        current_branch_head = self._read_head(current_branch)
        branch_to_merge_head = self._read_head(branch_name)

        if current_branch_head == branch_to_merge_head:
            print(f"Branch {branch_name} is already merged.")
            return
        
        # Get commit contents
        current_commit_content = self._read_file(self._get_object_path(current_branch_head))
        merge_commit_content = self._read_file(self._get_object_path(branch_to_merge_head))

        # Get tree ids
        current_tree_id = [line.split()[1] for line in current_commit_content.splitlines() if line.startswith("tree")][0]
        merge_tree_id = [line.split()[1] for line in merge_commit_content.splitlines() if line.startswith("tree")][0]
        
        # Get tree contents (file paths and object ids)
        current_tree_content = self._read_file(self._get_object_path(current_tree_id))
        merge_tree_content = self._read_file(self._get_object_path(merge_tree_id))

        #store file path and object ids
        current_files = {line.split()[1]: line.split()[0] for line in current_tree_content.splitlines()}
        merge_files = {line.split()[1]: line.split()[0] for line in merge_tree_content.splitlines()}

        merged_files = {}
        
        # Go through all unique files.
        all_files = set(current_files.keys()) | set(merge_files.keys())
        
        for filepath in all_files:
            if filepath in current_files and filepath in merge_files:
                # File exists in both branches, check for conflicts
                if current_files[filepath] == merge_files[filepath]:
                    # No conflict, take either version
                    merged_files[filepath] = current_files[filepath]
                else:
                    # CONFLICT:  For simplicity, we just take the version from the branch being merged in.
                    print(f"Conflict in file {filepath}.  Using version from {branch_name}.")
                    merged_files[filepath] = merge_files[filepath]  #  take merge branch
            elif filepath in current_files:
                # File only exists in current branch
                merged_files[filepath] = current_files[filepath]
            else:
                # File only exists in branch to merge
                merged_files[filepath] = merge_files[filepath]

        # Create new tree object
        new_tree_content = "\n".join(f"{oid} {filepath}" for filepath, oid in merged_files.items())
        new_tree_id = self._create_object(new_tree_content)

        # Create new commit object for the merge
        merge_message = f"Merge branch {branch_name} into {current_branch}"
        new_commit_content = f"tree {new_tree_id}\n"
        new_commit_content += f"parent {current_branch_head}\n"
        new_commit_content += f"parent {branch_to_merge_head}\n" # add second parent
        new_commit_content += f"author: User <user@example.com>\n"
        new_commit_content += f"committer: User <user@example.com>\n"
        new_commit_content += f"date: {int(time.time())}\n"
        new_commit_content += f"\n{merge_message}\n"
        new_commit_id = self._create_object(new_commit_content)

        # Update current branch to point to the new merge commit
        self._write_head(new_commit_id, current_branch)
        self._write_index(merged_files) # update index

        # update working directory
        for filepath, oid in merged_files.items():
            object_path = self._get_object_path(oid)
            content = self._read_file(object_path, 'rb')
            self._write_file(filepath, content, 'wb')

        print(f"Merged branch {branch_name} into {current_branch}.  Commit ID: {new_commit_id}")

    def help(self):
        """Displays help information for the available commands."""
        print("Usage: vcs <command> [<args>]")
        print("Available commands:")
        print("  init        Initialize a new repository")
        print("  add       Add a file to the staging area")
        print("  commit    Commit the staged files")
        print("  log         Display commit history")
        print("  status      Display repository status")
        print("  diff        Display differences between working directory, staging area, and commits")
        print("  checkout    Checkout a commit or file")
        print("  branch      List, create, or delete branches")
        print("  merge       Merge a branch into the current branch")
        print("  help        Display this help message")
        print("\nSee 'vcs help <command>' for more information on a specific command.")

if __name__ == "__main__":
    import sys

    if len(sys.argv) < 2:
        print("Usage: vcs <command> [<args>]")
        print("Type 'vcs help' for a list of commands.")
        sys.exit(1)

    command = sys.argv[1]
    args = sys.argv[2:]
    vcs = VersionControlSystem()

    if command == 'init':
        vcs.init()
    elif command == 'add':
        if not args:
            print("Usage: vcs add <filepath>")
            sys.exit(1)
        for filepath in args: # Allow multiple files to be added
            vcs.add(filepath)
    elif command == 'commit':
        if not args:
            print("Usage: vcs commit <message>")
            sys.exit(1)
        message = " ".join(args)
        vcs.commit(message)
    elif command == 'log':
        vcs.log()
    elif command == 'status':
        vcs.status()
    elif command == 'diff':
        vcs.diff(*args)
    elif command == 'checkout':
        if not args:
            print("Usage: vcs checkout <commit_id> or vcs checkout <filepath>")
            sys.exit(1)
        vcs.checkout(args[0])
    elif command == 'branch':
        vcs.branch(*args)
    elif command == 'merge':
        if not args:
            print("Usage: vcs merge <branch_name>")
            sys.exit(1)
        vcs.merge(args[0])
    elif command == 'help':
        vcs.help()
    else:
        print(f"Unknown command: {command}")
        print("Type 'vcs help' for a list of commands.")
        sys.exit(1)
