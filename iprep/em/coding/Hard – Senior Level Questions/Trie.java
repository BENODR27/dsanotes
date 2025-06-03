//     root
//      |
//      a
//      |
//      p
//      |
//      p
//     / \
//    l   (end of "app")
//    |
//    e (end of "apple")

class TrieNode {
    TrieNode[] children = new TrieNode[26]; // For 'a' to 'z'
    boolean isEndOfWord;

    public TrieNode() {
        isEndOfWord = false;
    }
}

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode(); // root is empty
    }

    // Insert word into the Trie
    public void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            int index = ch - 'a'; // get position from 0-25
            if (node.children[index] == null) {
                node.children[index] = new TrieNode(); // create if not exist
            }
            node = node.children[index]; // go down the path
        }
        node.isEndOfWord = true; // mark end of word
    }

    // Search for full word
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEndOfWord;
    }

    // Check if any word starts with the given prefix
    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    // Helper to search prefix
    private TrieNode searchPrefix(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            int index = ch - 'a';
            if (node.children[index] == null)
                return null;
            node = node.children[index];
        }
        return node;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple")); // true
        System.out.println(trie.search("app")); // false
        System.out.println(trie.startsWith("app")); // true
        trie.insert("app");
        System.out.println(trie.search("app")); // true
    }
}
