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

### 1.2 Constructors in depth

A **constructor** is a special block that runs exactly once, when `new` creates the object. Its job is to put the object in a valid starting state.

**Kinds at a glance:** default (compiler-written, A), no-arg (you write it, B), parameterised (C), overloaded and chained with `this(...)` (D), copy constructor (E), private (I). Copy constructors can be shallow or deep, see 1.9.

**Rules that make it a constructor:**
- Same name as the class, exactly.
- **No return type**, not even `void`.
- Called only through `new` (or through `this(...)` / `super(...)` from another constructor).
- Not inherited by subclasses. Can be `public`, `private`, `protected` or package-private.

#### A. Default constructor (written by the compiler)

If a class has **no constructor at all**, Java adds an invisible, empty, no-argument one. Fields keep their default values.

```java
class Box {
    int w;
    String label;
}                        // no constructor written: the compiler adds Box() { }

Box b = new Box();
System.out.println("Box defaults: w = " + b.w + ", label = " + b.label);
// Box defaults: w = 0, label = null
```

**The trap:** write **any** constructor yourself and the free default one is gone.

```java
class Student {
    String name;
    Student(String name) { this.name = name; }
}

Student s = new Student();   // compile error:
// error: constructor Student in class Student cannot be applied to given types;
//   required: String
```

Fix: also write `Student() { }` yourself if you still want `new Student()`.

#### B. No-arg constructor (written by you)

Looks like the default one, but you wrote it, so it can set values or log.

```java
Point() {
    System.out.println("Point() no-arg");
}
```

#### C. Parameterised constructor

Takes the starting values as arguments. Use `this.x` for the field when the parameter has the same name.

```java
Point(int x, int y) {
    this.x = x;
    this.y = y;
}
```

#### D. Overloading and chaining with `this(...)`

A class can have several constructors with **different parameter lists** (overloading). One constructor can call another with `this(...)` to avoid repeating code. `this(...)` must be the **first statement**.

```java
Point(int both) {
    this(both, both);                                  // reuse Point(int, int)
    System.out.println("Point(int) after this(...)");
}

Point p3 = new Point(7);
// Point(int, int)
// Point(int) after this(...)
// p3 is (7, 7)
```

The chained constructor runs first, then the rest of the caller's body.

#### E. Copy constructor

Builds a new object with the same values as an existing one. The two are separate objects afterwards.

```java
Point(Point other) {
    this(other.x, other.y);
}

Point p2 = new Point(3, 4);
Point p4 = new Point(p2);
p4.x = 99;
// p2 is still (3, 4), p4 is (99, 4)
```

Compare with `Point p4 = p2;`, which copies only the reference (both names, one object; see 1.4).

#### F. Order of initialisation

When `new` runs, things happen in this order:
1. All fields get default values (`0`, `null`, `false`).
2. Field initialisers (`int a = 10;`) and instance blocks (`{ ... }`) run, top to bottom.
3. The constructor body runs.

```java
class Order {
    int a = 10;
    int b;
    { System.out.println("instance block: a = " + a + ", b = " + b); b = 20; }
    Order() { System.out.println("constructor: a = " + a + ", b = " + b); b = 30; }
}

new Order();
// instance block: a = 10, b = 0
// constructor: a = 10, b = 20
// (b ends as 30)
```

#### G. Constructors and inheritance: `super()`

Every constructor's first line is either `this(...)` or `super(...)`. If you write neither, Java inserts `super()` (the parent's no-arg constructor), so the **parent is built first**.

```java
class Animal { Animal() { System.out.println("Animal()"); } }
class Dog extends Animal { Dog() { System.out.println("Dog()"); } }

new Dog();
// Animal()
// Dog()
```

If the parent has **no** no-arg constructor, the child must call `super(args)` explicitly, or it does not compile.

#### H. `final` fields must be set by every constructor

```java
class Account {
    final String id;          // must be assigned exactly once
    double balance;
    Account(String id) { this.id = id; }
    Account(String id, double balance) { this(id); this.balance = balance; }
}
// new Account("A1", 500) -> id A1, balance 500.0
```

#### I. Private constructor

Stops anyone outside the class from calling `new`. Used for utility classes (only static methods) and the Singleton pattern.

```java
class MathUtil {
    private MathUtil() { }
    static int square(int x) { return x * x; }
}
// MathUtil.square(5) -> 25; new MathUtil() outside the class does not compile
```

#### J. Constructor traps

1. **A return type turns it into a method.** Asked in class (Oct 23): "Can we add a return type to a constructor?" Teacher's answer: **no**. A constructor has no return type by definition. The twist: if you write one anyway, Java gives **no error**; it silently treats it as an ordinary method that happens to share the class name, and `new` never calls it.
   ```java
   class Point {
       void Point() {     // compiles, but this is a METHOD
           System.out.println("I am a method, not a constructor");
       }
   }

   Point p1 = new Point();   // runs the compiler's default constructor; prints nothing
   p1.Point();               // only an explicit call runs it:
   // I am a method, not a constructor
   ```
   Why it is dangerous: no compile error, just setup code that never runs on `new`.
   Interview line: *"Constructors have no return type. If you add one, even void, Java treats it as a normal method with the class's name, so it won't run on new. It compiles, which makes the bug easy to miss."*
2. **Forgetting `this.`** when the parameter shadows the field: `age = age;` assigns the parameter to itself, and the field stays 0.
   ```java
   class Shadow { int age; Shadow(int age) { age = age; } }
   new Shadow(25).age   // 0, not 25
   ```
3. Writing a parameterised constructor and still calling `new X()` (A).
4. `this(...)` or `super(...)` anywhere except the first line: compile error. You also cannot use both in one constructor.
5. Calling an overridable method from a constructor: the subclass version runs before the subclass fields are set (advanced; just avoid it).

#### Constructor vs method

| | Constructor | Method |
|---|---|---|
| Name | Same as the class | Any name |
| Return type | None (not even `void`) | Required (`void` or a type) |
| Called by | `new`, `this(...)`, `super(...)` | Explicitly, any number of times |
| Runs | Once per object, at creation | Whenever called |
| Inherited | No | Yes (unless private) |
| Compiler adds one | Yes, if you write none | Never |

### 1.3 Key details

| Concept | What to remember |
|---|---|
| Default constructor | If you write **no** constructor, Java gives you an empty `Student()`. Write any constructor and that free one disappears. |
| `this` | The current object. Needed when a parameter has the same name as a field (`this.age = age`). `this(...)` calls another constructor. |
| Default field values | `int` 0, `boolean` false, `double` 0.0, any object (including `String`) `null`. Local variables get **no** default: using one uninitialised is a compile error. |
| `static` field | Shared by all objects (`Student.count`). Access it through the class name. |
| `static` method | Belongs to the class, has no `this`, so it cannot touch instance fields directly. `main` is static. Full detail in 1.8. |
| Access modifiers | `private` (this class only), default/package, `protected` (package + subclasses), `public` (everyone). Good practice: private fields + public methods (encapsulation). |
| `toString()` | Override it to print objects nicely; otherwise you see `Student@1b6d3586`. |

### 1.4 References: the most important idea (it powers linked lists)

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

### 1.5 `==` vs `.equals()`

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

### 1.6 Java is always pass-by-value

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

### 1.7 `null` and NullPointerException

A reference that points at nothing is `null`. Calling a method or reading a field through it (`s.age` when `s == null`) throws **NullPointerException**. Always ask "can this be null?" before using a dot.

### 1.8 Static vs instance: variables and methods

**Instance** members belong to each object. **Static** members belong to the class itself, one copy shared by every object, and exist even before any object is created.

```java
class Employee {
    static int count = 0;                      // static (class) variable: one copy
    static final String COMPANY = "Launch";    // static final: a constant
    static String policy;

    static {                                   // static block: runs ONCE, when the class is first used
        policy = "Hybrid";
        System.out.println("static block ran");
    }

    String name;                               // instance variable: one copy per object
    int id;

    Employee(String name) {
        this.name = name;
        count++;                               // every object bumps the SAME counter
        id = count;
    }

    static int getCount() {                    // static method: no this, no instance fields
        return count;
    }

    String describe() {                        // instance method: can use instance AND static
        return name + " #" + id + " of " + count + " at " + COMPANY + " (" + policy + ")";
    }
}
```

```java
System.out.println("before any Employee");
System.out.println("count = " + Employee.count);   // usable with zero objects
Employee e1 = new Employee("Sai");
Employee e2 = new Employee("Ravi");
System.out.println(e1.describe());
System.out.println(e2.describe());
System.out.println("Employee.getCount() = " + Employee.getCount());
```
```
before any Employee
static block ran              <- class loads on first use, block runs once
count = 0
Sai #1 of 2 at Launch (Hybrid)
Ravi #2 of 2 at Launch (Hybrid)
Employee.getCount() = 2
```

`name` and `id` differ per object; `count` is shared, so both report "of 2".

```
            Employee class (one copy)
            count = 2, COMPANY, policy
               ^                ^
               |                |
e1 --> [ name: Sai, id: 1 ]   e2 --> [ name: Ravi, id: 2 ]
```

**What can access what:**

| From | Instance variable / method | Static variable / method |
|---|---|---|
| Instance method | Yes (through `this`) | Yes |
| Static method | **No** (no `this`; which object would it mean?) | Yes |

```java
public class StaticErr {
    int x = 5;
    static void show() { System.out.println(x); }
}
// error: non-static variable x cannot be referenced from a static context
```

That is why `main` (static) has to create an object first, `new Solution().solve(...)`, or call only static helpers, which is why our DSA helpers are all `static`.

**Static through an object reference works, but it is misleading:**

```java
e1.count = 50;
System.out.println("after e1.count = 50, e2.count = " + e2.count);
// after e1.count = 50, e2.count = 50
```

There is only one `count`, so changing it "through e1" changes it for everyone. Always write `Employee.count`.

| | Instance | Static |
|---|---|---|
| Belongs to | Each object | The class |
| Copies | One per object | Exactly one |
| Created | With `new` | When the class loads |
| Access | `obj.name`, `obj.describe()` | `Employee.count`, `Employee.getCount()` |
| `this` available | Yes | No |
| Typical use | Per-object state and behaviour | Counters, constants (`static final`), utility methods (`Math.max`), `main` |

Static traps:
1. Using an instance field inside a static method (compile error above).
2. Expecting a static field to be per-object (the `e1.count` surprise).
3. Static state survives between test cases: a static counter or list must be reset, or test 2 sees test 1's leftovers. In DSA, prefer local variables.

### 1.9 Creating a copy: reference copy, shallow copy, deep copy

The question is: when an object holds **other objects** (an `Address`, an array), what does a copy share?

```java
class Address {
    String city;
    Address(String city) { this.city = city; }
}

class Person {
    String name;
    int age;              // primitive
    Address address;      // reference to another object
    int[] scores;         // arrays are objects too

    Person(String name, int age, Address address, int[] scores) { ... }

    Person shallowCopy() {   // new Person, but the SAME Address and array
        return new Person(name, age, address, scores);
    }

    Person deepCopy() {      // new Person AND new copies of everything inside
        return new Person(name, age, new Address(address.city), scores.clone());
    }
}
```

The experiment: make three copies, then change the original in three places.

```java
Person original = new Person("Sai", 30, new Address("Hyderabad"), new int[] { 90, 80 });

Person ref     = original;               // 1. reference copy
Person shallow = original.shallowCopy(); // 2. shallow copy
Person deep    = original.deepCopy();    // 3. deep copy

original.age = 31;                   // a primitive field
original.address.city = "Bengaluru"; // inside the nested object
original.scores[0] = 10;             // inside the array
```
```
original: Sai, 31, Bengaluru, [10, 80]
ref:      Sai, 31, Bengaluru, [10, 80]   <- same object: sees every change
shallow:  Sai, 30, Bengaluru, [10, 80]   <- own age, but SHARES address and scores
deep:     Sai, 30, Hyderabad, [90, 80]   <- fully independent
shallow.address == original.address: true
deep.address == original.address: false
```

```
Reference copy:  original --\
                             >--> [Person] --> [Address] , [scores]
                 ref -------/

Shallow copy:    original --> [Person] --\
                                          >--> [Address] , [scores]   (shared)
                 shallow  --> [Person] --/

Deep copy:       original --> [Person] --> [Address] , [scores]
                 deep     --> [Person] --> [Address'] , [scores']   (separate)
```

| Kind | How | New outer object? | Nested objects | Change to `original.address.city` shows in the copy? |
|---|---|---|---|---|
| Reference copy | `b = a` | No | Same | Yes |
| Shallow copy | Copy every field as is (copy constructor that assigns fields, `Object.clone()` by default) | Yes | **Shared** | **Yes** |
| Deep copy | Also copy every nested object, recursively | Yes | New copies | No |

Key details:
- Primitives (`int age`) and immutable objects (`String name`) are safe in a shallow copy: you cannot change a String in place, so sharing it is harmless.
- **Mutable** nested objects (`Address`, arrays, lists) are where shallow copies bite.
- `array.clone()` is shallow: fine for `int[]`, but for a 2D array it copies only the outer array of row references.

```java
int[][] grid = { { 1, 2 }, { 3, 4 } };
int[][] g2 = grid.clone();           // shallow: rows are shared
g2[0][0] = 99;
// grid[0][0] after changing clone: 99

int[][] g3 = new int[grid.length][];
for (int i = 0; i < grid.length; i++) g3[i] = grid[i].clone();   // deep: copy each row
g3[1][1] = 77;
// grid[1][1] after changing deep copy: 4
```

- In DSA this matters when you **save a snapshot** (for example, adding the current path to the answer list in recursion/backtracking): add `new ArrayList<>(path)`, not `path` itself, or every saved answer changes when `path` changes later.

When to use which: shallow is cheap and fine when nested parts are immutable or meant to be shared; deep copy when the copy must stay independent (snapshots, undo, passing data you do not want modified).

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
6. A method that changes the head must **return** it, and the caller must write `head = method(head, ...)` (pass-by-value, 1.6).
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

- *"Static members belong to the class, one shared copy; instance members belong to each object. A static method has no this, so it cannot use instance fields without an object."*
- *"A shallow copy creates a new outer object but shares the nested mutable objects; a deep copy copies those too, so changes to one never show in the other."*

- *"A class is a blueprint; an object is an instance created with new. The variable holds a reference to the object, not the object itself."*
- *"Java is pass-by-value; for objects the value is the reference, so I can change the object through it but reassigning it inside a method does not affect the caller. That is why my linked list methods return the head."*
- *"A linked list gives O(1) insert and delete at the head, but O(n) access by index, the opposite trade-off from an array."*
