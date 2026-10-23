# Classes, Objects and Linked List Introduction

Scaler class, Fri Oct 23, 2026. Language: Java. Every snippet below was compiled and run; the outputs shown are real.

Note: linked lists are **not** on the Contest 2 (Tue Oct 27) or mock (Wed Oct 28) syllabus. Understand them now, practise after the mock.

---

## Part 1. Classes and objects

### 1.1 Class = blueprint, object = the real thing

A **class** describes what data a thing has (fields) and what it can do (methods). An **object** is one concrete instance made from that class with `new`. One class, many objects.

```java
class Student {
    String name;              // field (instance variable): every object has its own copy
    int age;
    static int count = 0;     // static: ONE copy shared by the whole class

    Student(String name, int age) {   // constructor: runs once, when the object is created
        this.name = name;             // this.name = the field, name = the parameter
        this.age = age;
        count++;
    }

    Student(String name) {    // overloaded constructor
        this(name, 18);       // calls the other constructor (must be the first line)
    }

    void birthday() {         // method: behaviour that works on this object's data
        age++;
    }

    public String toString() { // what println shows for this object
        return name + "(" + age + ")";
    }
}
```

```java
Student a = new Student("Sai", 30);
```

What happens on that line:
1. `new` reserves memory for a Student object on the **heap**.
2. Fields get default values (`null`, `0`), then the constructor runs and sets them.
3. `new` returns the **address** (reference) of the object.
4. `a` (on the **stack**) stores that reference, not the object itself.

### 1.2 Key details

| Concept | What to remember |
|---|---|
| Default constructor | If you write **no** constructor, Java gives you an empty `Student()`. Write any constructor and that free one disappears. |
| `this` | The current object. Needed when a parameter has the same name as a field (`this.age = age`). `this(...)` calls another constructor. |
| Default field values | `int` 0, `boolean` false, `double` 0.0, any object (including `String`) `null`. Local variables get **no** default: using one uninitialised is a compile error. |
| `static` field | Shared by all objects (`Student.count`). Access it through the class name. |
| `static` method | Belongs to the class, has no `this`, so it cannot touch instance fields directly. `main` is static. |
| Access modifiers | `private` (this class only), default/package, `protected` (package + subclasses), `public` (everyone). Good practice: private fields + public methods (encapsulation). |
| `toString()` | Override it to print objects nicely; otherwise you see `Student@1b6d3586`. |

### 1.3 References: the most important idea (it powers linked lists)

A variable of a class type holds a **reference** (an arrow to the object), not the object.

```java
Student a = new Student("Sai", 30);
Student b = a;          // copies the ARROW, not the object
b.birthday();
System.out.println(a + " " + b + " same object: " + (a == b));
// Sai(31) Sai(31) same object: true
```

```
a ---\
      >---> [ name: "Sai", age: 31 ]
b ---/
```

Both arrows point at one object, so a change through `b` is visible through `a`.

### 1.4 `==` vs `.equals()`

- `==` on objects compares **references**: "is it the same object?"
- `.equals()` compares **content**, if the class defines it (`String` does).

```java
Student c = new Student("Sai", 31);
System.out.println("a == c: " + (a == c));     // a == c: false  (same data, different objects)

String s1 = "hi", s2 = new String("hi");
System.out.println("s1 == s2: " + (s1 == s2) + ", equals: " + s1.equals(s2));
// s1 == s2: false, equals: true
```

Rule: compare strings (and any objects by value) with `.equals()`, never `==`.

### 1.5 Java is always pass-by-value

Java copies the value into the parameter. For an object, the value **is the reference**. So:

```java
static void changeInt(int x) { x = 100; }                       // changes the copy only
static void changeAge(Student s) { s.age = 100; }               // follows the arrow: changes the real object
static void replace(Student s) { s = new Student("Other", 1); } // repoints the copy only

int n = 5;
changeInt(n);
changeAge(a);
replace(a);
System.out.println("n = " + n + ", a = " + a);
// n = 5, a = Sai(100)
```

- Change **through** the reference (`s.age = ...`): the caller sees it.
- Reassign the reference itself (`s = new ...`): the caller does not.

This is exactly why linked list methods **return the new head**: reassigning `head` inside a method does not change the caller's `head`.

### 1.6 `null` and NullPointerException

A reference that points at nothing is `null`. Calling a method or reading a field through it (`s.age` when `s == null`) throws **NullPointerException**. Always ask "can this be null?" before using a dot.

---

## Part 2. Linked list introduction

### 2.1 Why a linked list

An array stores elements side by side in one block, so index access is O(1), but inserting at the front means shifting everything, O(n), and the size is fixed.

A linked list stores each element in its own **node**; every node holds a value and a reference to the next node. Nodes can be anywhere in memory.

```java
class Node {
    int val;
    Node next;          // reference to the next node; null at the end

    Node(int val) {
        this.val = val; // next starts as null (default value)
    }
}
```

A class that contains a field of its own type is called **self-referential**. That is all a linked list is.

### 2.2 Building a list by hand

```java
Node head = new Node(10);
head.next = new Node(20);
head.next.next = new Node(30);
```

```
head
 |
 v
[10 | *]--->[20 | *]--->[30 | null]
```

- `head` is the only way in. Lose it and the whole list is gone.
- The last node's `next` is `null`. That is how you know you reached the end.

### 2.3 Traversal: the core loop

```java
static void print(Node head) {
    StringBuilder sb = new StringBuilder();
    for (Node cur = head; cur != null; cur = cur.next) {
        sb.append(cur.val).append(" -> ");
    }
    System.out.println(sb.append("null"));
}
// 10 -> 20 -> 30 -> null
```

- Walk with a **separate pointer** (`cur`), never with `head` itself.
- The condition is `cur != null` (visit every node). Use `cur.next != null` only when you need to **stop at the last node** (as in insert at tail).

Size and search are the same loop:

```java
static int size(Node head) {
    int n = 0;
    for (Node cur = head; cur != null; cur = cur.next) n++;
    return n;
}

static boolean contains(Node head, int x) {
    for (Node cur = head; cur != null; cur = cur.next) {
        if (cur.val == x) return true;
    }
    return false;
}
// size 3, contains 20: true, contains 99: false
```

### 2.4 Insert at head: O(1)

```java
static Node insertAtHead(Node head, int x) {
    Node node = new Node(x);
    node.next = head;   // 1. new node points at the old head
    return node;        // 2. new node IS the new head
}

head = insertAtHead(head, 5);
// 5 -> 10 -> 20 -> 30 -> null
```

Works for an empty list too (`head == null`): the new node's `next` is just `null`.

### 2.5 Insert at tail: O(n)

```java
static Node insertAtTail(Node head, int x) {
    Node node = new Node(x);
    if (head == null) return node;     // empty list: the new node is the head
    Node cur = head;
    while (cur.next != null) cur = cur.next;   // stop AT the last node
    cur.next = node;
    return head;
}

head = insertAtTail(head, 40);
// 5 -> 10 -> 20 -> 30 -> 40 -> null
```

(Keeping a `tail` reference makes this O(1).)

### 2.6 Insert at a position: O(pos)

Walk to the node **before** the position, then link the new node in. Order matters: connect the new node first, then repoint `prev`.

```java
// 0-based position, 0 <= pos <= size
static Node insertAt(Node head, int pos, int x) {
    if (pos == 0) return insertAtHead(head, x);
    Node prev = head;
    for (int i = 0; i < pos - 1; i++) prev = prev.next;
    Node node = new Node(x);
    node.next = prev.next;   // 1. new node points at what comes after prev
    prev.next = node;        // 2. prev points at the new node
    return head;
}

head = insertAt(head, 2, 15);
// 5 -> 10 -> 15 -> 20 -> 30 -> 40 -> null
```

Swap steps 1 and 2 and you lose the rest of the list: `prev.next` already points at the new node, so `node.next = prev.next` makes it point at itself.

### 2.7 Delete by value: O(n)

To delete a node you change the `next` of the node **before** it, so stop one early.

```java
static Node deleteValue(Node head, int x) {
    if (head == null) return null;             // empty list
    if (head.val == x) return head.next;       // deleting the head: the second node becomes head
    Node prev = head;
    while (prev.next != null && prev.next.val != x) prev = prev.next;
    if (prev.next != null) prev.next = prev.next.next;   // skip over the node
    return head;                               // value not found: list unchanged
}

head = deleteValue(head, 5);    // 10 -> 15 -> 20 -> 30 -> 40 -> null  (head deleted)
head = deleteValue(head, 40);   // 10 -> 15 -> 20 -> 30 -> null        (tail deleted)
head = deleteValue(head, 99);   // 10 -> 15 -> 20 -> 30 -> null        (not found)
```

Java's garbage collector frees the skipped node once nothing points to it.

### 2.8 Empty list

```java
Node empty = null;
print(empty);                    // null
empty = insertAtTail(empty, 1);  // 1 -> null
size(null);                      // 0
```

Every method must handle `head == null` first.

### 2.9 Array vs linked list

| Operation | Array | Singly linked list |
|---|---|---|
| Access by index | O(1) | O(n) (walk from head) |
| Search a value | O(n) | O(n) |
| Insert / delete at front | O(n) (shift) | **O(1)** |
| Insert at end | O(1) amortised (ArrayList) | O(n), O(1) with a tail pointer |
| Insert / delete in the middle | O(n) (shift) | O(1) once you are at the node, O(n) to get there |
| Memory | Contiguous, compact | Extra `next` reference per node, scattered |
| Size | Fixed (array) | Grows freely |

### 2.10 Variants (names to know)

- **Singly linked:** `next` only (this file).
- **Doubly linked:** `prev` and `next`; can walk backwards, delete a node in O(1) when you hold it. Java's `java.util.LinkedList` is doubly linked.
- **Circular:** the last node points back to the head instead of `null`.

---

## Part 3. Traps

1. Moving `head` while traversing: use `Node cur = head`.
2. Forgetting the empty list (`head == null`) or the single-node list.
3. `cur != null` vs `cur.next != null`: visit every node vs stop at the last one. Mixing them up skips the last node or throws NullPointerException.
4. Wrong order of the two pointer assignments when inserting (2.6).
5. Deleting: you need the node **before** the target, and the head is a special case.
6. A method that changes the head must **return** it, and the caller must write `head = method(head, ...)` (pass-by-value, 1.5).
7. `==` vs `.equals()` when comparing node values that are objects (`Integer`, `String`).
8. Draw boxes and arrows for every pointer change. Most linked list bugs are visible on paper.

## Part 4. Practice after the mock (Oct 29 onwards)

| # | Problem | Idea |
|---|---|---|
| 1 | Print, size, search | Traversal loop |
| 2 | Insert at head / tail / position | 2.4-2.6 |
| 3 | Delete by value / at position | Stop at the previous node |
| 4 | Reverse a linked list | Three pointers: prev, cur, next |
| 5 | Middle of the list | Slow and fast pointers |
| 6 | Detect a cycle | Floyd: slow and fast meet |
| 7 | Merge two sorted lists | Dummy head node |

## Interview lines

- *"A class is a blueprint; an object is an instance created with new. The variable holds a reference to the object, not the object itself."*
- *"Java is pass-by-value; for objects the value is the reference, so I can change the object through it but reassigning it inside a method does not affect the caller. That is why my linked list methods return the head."*
- *"A linked list gives O(1) insert and delete at the head, but O(n) access by index, the opposite trade-off from an array."*
