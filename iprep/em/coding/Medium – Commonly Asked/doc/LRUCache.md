Here's a full solution to **6. Implement LRU (Least Recently Used) Cache** using **Java 8**, explained step by step like you're learning it for the first time.

---

## ✅ Problem Statement

Design and implement a data structure for **Least Recently Used (LRU) cache**. It should support the following operations:

- `get(key)` – Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
- `put(key, value)` – Set or insert the value if the key is not already present. When the cache reached its **capacity**, it should invalidate the **least recently used** item.

---

### 📘 Example:

```java
LRUCache cache = new LRUCache(2); // capacity = 2
cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // returns 1
cache.put(3, 3);    // evicts key 2
cache.get(2);       // returns -1 (not found)
cache.put(4, 4);    // evicts key 1
cache.get(1);       // returns -1 (not found)
cache.get(3);       // returns 3
cache.get(4);       // returns 4
```

---

## ✅ Java 8 Code using `LinkedHashMap`

```java
import java.util.*;

class LRUCache extends LinkedHashMap<Integer, Integer> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // accessOrder = true
        this.capacity = capacity;
    }

    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        super.put(key, value);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // 1
        cache.put(3, 3);
        System.out.println(cache.get(2)); // -1
        cache.put(4, 4);
        System.out.println(cache.get(1)); // -1
        System.out.println(cache.get(3)); // 3
        System.out.println(cache.get(4)); // 4
    }
}
```

---

## 🧸 Step-by-Step Explanation (Like a Child)

1. 🎒 We need a bag that can **only hold a fixed number of items** (capacity).
2. 🧠 Every time we **access** or **add** an item, it becomes the **most recently used**.
3. 🗑️ If the bag is **full**, we throw away the item that was **used the longest time ago** (least recently used).
4. 🔁 To manage this, we use `LinkedHashMap` because:

   - It remembers the **order in which items were used**
   - It lets us **automatically remove the oldest item** when full.

---

## ⚙️ How It Works:

- `super(capacity, 0.75f, true)`
  → tells `LinkedHashMap` to use **access-order**, not insertion-order.
- `removeEldestEntry`
  → returns `true` when we exceed capacity, which removes the oldest used entry.

---

## ⏱ Time & Space Complexity

| Operation     | Time |
| ------------- | ---- |
| get(key)      | O(1) |
| put(key, val) | O(1) |

Space Complexity: O(capacity)

---

## ✅ Want Advanced Version?

Let me know if you’d like:

- ✅ A version using **Doubly Linked List + HashMap**
- 🚀 Fully Custom Implementation (no `LinkedHashMap`)
- 🔁 Thread-safe version for multi-threading

Would you like the **custom implementation next** (most common in interviews)?
