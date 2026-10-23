# My Project Notes

## Object Oriented Progamming

### Java Fundamentals
- 4 Pillars of OOP
    1. Encapsulation - hide details that don't matter
        - bundle data and methods
        - restrict internal access
    2. abstraction - provide details that matter
        - hide complex implementation
        - show only essentials
    3. inheritance
        - subclasses extend & inherit from a parent class
        - ex.
            - animal class (base)
                - properties - color, size
                - behaviors - move(), eat()
            - dog - speak() -> bark
            - cat - speak() -> meow
    4. polymorphism - multiple forms
        - uniform treatment of different types shared interface
        - ex. animal.makeSound()
            - dog -> woof
            - cat -> meow
            - bird -> tweet
- Java
    - static : callable even if instance of class doesn't exist
    - Everything is a child of the base `Object` object
        - toString() : string
        - equals() : boolean
        - hashCode() : int
        - clone() : Object
        - wait()
        - notify()
    - var = auto
    - Packages = importing
    - Strings are built in
    
### Phase 0


### Classes and Objects
- Classes contain `fields` and `methods`
    - fields = state (data) within class ~ string name
    - methods = behavior (operations) the class performs ~ sayName()
- Object
    - instantiation of class initialized with specific values
    - classes often include a `constructor` = block of code used to initialize fields when instantiated
    ```java
    public class Person {
        // Field
        private String name;

        // Constructor
        public Person(String name) {
            this.name = name;
        }

        // Method
        public void sayName() {
            System.out.println(name);
        }
    }
    ```
    - To create `Person` object from `Person` class, use `new` operator and pass required arguments to constructor
    ```java
    public class HelloPerson {
        public static void main(String[] args) {
            var person = new Person("James Gosling");
            person.sayName();
        }
    }
    ```
    - `this` keyword - only needed when naming conflict occurs, common in constructors
    - constructors - receives parameters and executes code necessary to initialize a new object
        ```java
        public class ConstructorExample {
            public String value;

            /** Default constructor */
            public ConstructorExample() {
                value = "default";
            }

            /** Overloaded explicit constructor */
            public ConstructorExample(String value) {
                this.value = value;
            }

            /** Copy constructor */
            public ConstructorExample(ConstructorExample copy) {
                this(copy.value); // Calls the explicit constructor above
            }

            public static void main(String[] args) {
                System.out.println(new ConstructorExample().value);
                System.out.println(new ConstructorExample("A").value);
                System.out.println(new ConstructorExample(new ConstructorExample("B")).value);
            }
        }
        ```
        - if no constructors provided, java will provide an empty one
    - getters (accessors) and setters (mutators)
        - may seem like more work, but allows change for how data is stored or validated later on
        ```java
        public class GetSetExample {
            private int[] scores = new int[10];

            public int[] getScores() {
                // Return a copy to protect the internal array
                int[] copy = new int[scores.length];
                System.arraycopy(scores, 0, copy, 0, scores.length);
                return copy;
            }

            public void setScores(int[] scores) {
                // Validate data before updating
                for (int score : scores) {
                    if (score > 100) {
                        return; // Or throw an exception
                    }
                }
                this.scores = scores;
            }
        }
        ```
    - Enumerations
        - fixed set of named constants, making code more readable and prevents invalid values from being used
        ```java
        public enum Peak {
            NEBO, PROVO, SANTAQUIN, TIMPANOGOS, CASCADE, SPANISH, LONE
        }
        ```
        - useful for validating parameters nd restricting variables to a closed set of options
        - how to parse a string into enum constant
            ```java
            public static void main(String[] args) {
                try {
                    var e = Enum.valueOf(Peak.class, args[0].toUpperCase());

                    if (e == Peak.LONE) {
                        System.out.println("You chose Lone Peak");
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Unknown peak provided");
                }
            }
            ```

### Java Records
- "data objects" or "data-carrier classes" - classes that exist solely to act as containers for data
    - ex. `Pet` object with `id`, `name`, and `type` fields passed into `PetHealth` object
- immutability (`final` keyword)- state cannot be modified after creation
- Consider the following code:
    - while functional, requires over 50 lines of boilerplate just to represent 3 fields
    ```java
    import java.util.Objects;

    class PetClass {
        private final int id;
        private final String name;
        private final String type;

        PetClass(int id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PetClass petClass = (PetClass) o;
            return id == petClass.id && 
                Objects.equals(name, petClass.name) && 
                Objects.equals(type, petClass.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, type);
        }

        @Override
        public String toString() {
            return "PetClass[" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ']';
        }
    }
    ```
    - java introduced `record` keyword to make this syntax much more concise
    ```java
    record PetRecord(int id, String name, String type) {}
    ```
    - When using Java records, compiler automatically provides following
        1. Immutability: All fields are private and final.
        2. Canonical Constructor: A constructor that initializes all fields is created for you.
        3. Accessor Methods: Instead of getName(), records use the field name as the method name (e.g., name()).
        4. equals(): A method that compares two records based on their field values.
        5. hashCode(): A method that calculates a hash based on all fields.
        6. toString(): A string representation showing all field names and their values.
    - can add custom methods to a record inside `{}`, useful to provide logic related to the data
        ```java
        public record PetRecord(int id, String name, String type) {
            /**
            * Returns a new PetRecord with the updated name.
            */
            public PetRecord rename(String newName) {
                return new PetRecord(id, newName, type);
            }
        }
        ```

### Interfaces and Abstract Classes
- polymorphism - the ablility of an object to take on many (poly) forms (morph) to fit into different contexts
    - in java, inheritance and interfaces are the primary way to achieve this
    - `extends` keyword to inherit functionality from another class
    - `implements` keyword to adhere to an interface definition
- Interfaces
    - **mainly contain abstract methods, a class can implement multiple interfaces**
    - allow you to define *what* a class does without specifying *how* it does it
        - allows creation of different implementations for same interface
    - `@Override` annotation not required, but best practice to clarify method is implementing an interface requirement
- Extending Classes
    - **mainly for new methods**
    - inherit from class using `extends`
    - can access superclass implementation of functions by using `super` keyword
        - ex. `super.toString()`
    - 
- Abstract Classes
    - **abstract and normal methods, classes can only extend one**
    - can provide some implemention while leaving other methods to be defined by subclasses
    - `public static **abstract** class ClassName **implements** SuperClass`
- `instanceof` - bool check to determine object's type at runtime
    - pattern matching for instanceof allows you to test the type and cast it to a variable in a single statement
    -   ```java
        if (item instanceof String stringItem) {
        ```
- `final`
    - if you want to prevent subclass from overriding a method, mark method as `final`
    - can also use on objects to make them immutable, doesn't prevent modification through object methods

### Java Exceptions
- allow you to deviate from normal execution flow of program when an "exceptional" event occurs
    - when thrown, searches call stack for a matching handler (`catch` block)
    - allows you to pass control from point of failure to higher-level frame equipped to handle the error
- uses `try`, `catch`, `throw` syntax
    - exception might occur in `try`
    - followed by one or more `catch` blocks, each specifying exception type
    - `throw` raises new exception
        - unchecked exceptions (Error, NullPointer, IndexOutOfBounds) do not need to be declared or caught
- `finally`
    - follows `try` `catch` block and ALWAYS executes
    - useful for cleaning resources (closing db connections)
    - if exception thrown and not caught, `finally` executes BEFORE it continues up stack
        - also when `try` contains return statement
- custom exception types - subclass `Exception` for checked or `RuntimeException` for unchecked
    - ```java
        public class AlreadyTakenException extends Exception {
            public AlreadyTakenException(String message) {
                super(message);
            }
        }
    ```
- `try-with-resources`
    - instead of using `finally` to close resources, this can be used with any class that implements `AutoCloseable` or `Closeable` interface
    - RESOURCE DECLARED IN PARENTHESES'
- where to use `catch` and `throws`
    - good rule of thumb: **Handle exception at the level where you can take meaningful action to resume normal execution**
    - ex:
        1. db throws `UserNotFoundException`
        2. service layer cant fulfill request without user, lets it pass
        3. UI shows "Invalid username", appropriate place to "resume" program by waiting for new input
- Exceptions should be Exceptional
    - `Principle of Least Astonishment` to avoid use in routine control flow
    - using exceptions to exit loops, return standard values violates `Separation of Concerns`
- Fail-Fast principle
    - program should report failure as soon as an unexpected state is detected
    - in Java, often validating method arguments at beginning of method, throwing if they are invalid
- Separation of Concerns
    - Catch specific exceptions: Avoid catch (Exception e) unless you are at the very top level of the application.
    - Preserve the cause: When re-throwing an exception, always pass the original exception as a cause to maintain the stack trace.
    - Use Custom Exceptions: Create domain-specific exceptions (e.g., InsufficientFundsException) to make the code more readable and easier to handle at higher layers
- Document exceptions: Use the @throws Javadoc tag to inform users of your API about the checked and unchecked exceptions they might encounter.

### Java Collections
- Java Collections Framework is a unified architecture for representing and manipulating collections. It provides a set of interfaces, implementations, and algorithms that allow developers to manage groups of objects efficiently
- Core interfaces
    - `Collection` - root of hierarchy
    - `List` - ordered collection
        - ArrayList - dynamic array
        - LinkedList - doubly-linked
    - `Set` - cannot contain dupes
        - Hashset - speed
        - LinkedHashSet - insertion order
        - TreeSet - natural sorting
    - `Queue` - FIFO
        - PriorityQueue - heap-based ordering
        - ArrayDeque - resizable array implementation
    - `Deque` - double ended queue
        - supports insertion/removal at both ends
    - `Map` - maps keys to values
        - no dupe keys, each key can map to at MOST one value
        - does not inherit from Collection
        - HashMap, LinkedMashMap, TreeMap like set
- Polymorphic Algorithms
    - JCF provides set of static methods through java.util.Collections to perform operations on collection objects ; polymorphic because the same method can be used on different implementations of the same interface
    - sorting - reorder `List` based on natural ordering or a `Comparator`
    - Searching - Finds specific elements in a `List` (`binarySearch`)
    - Shuffling - randomly permute elements in a `List`
    - Data Manipulation - methods to reverse, fill, copy, swap elements within a collection
    - Extreme Values - find min or max element in a collection
- Infrastructure and Interoperability
    - Iterators - `Iterator` and `ListIterator` interfaces provide a way to traverse collection sequentially
    - Standardization - common set of interfaces = reduce effort to learn new APIs, code reuse encouraged
    - Performance - provided implementations are highly tuned
    - Thread-Safety - includes wrappers for creating synchronized collections and specialized concurrent collections (`java.util.concurrent`) for multi-threaded env's
- to use objects with collections that require sorting or utility methods like sort(), class must implement `Comparable` interface. Allows you to define "natural ordering" of class objects
    - `compareTo` - returns -(less than), 0 (equal to), +(greater than)

### Object-Oriented Design
- focuses on describing objects in the application domain as literal programming constructs
    - classes as nouns (cat), methods as verbs (eat)
- Object Relationships
    - in OOD, categorized into three primary types
        - Generalization (is-a) - Inheritance. A specialized class (subclass) extends a more general class (superclass).
        - Composition (has-a) - Ownership or containment. One object contains another as a member field.
            - further distinguished by object lifecycle
            - Composition (*--)- Strong ownership. "Part" cannot exist without the "whole" (Room in House)
            - Aggregation (o--)- Weak ownership. "Part" can exist independently of "whole" (Professor in Department)
        - Dependency (uses-a) - Transient interaction. An object depends on another temporarily, often as a method parameter.
- Core class Design Principles
    - should ideally represent a single concept or entity within application domain (principle of single responsibility)
    - Concepts
        - Composition - focus on cohesion
            - A car is composed of an engine, wheels, and a frame.
            - lots of "has-a" relationships to delegate tasks to internal components
            - more flexible than inheritance because components can be replaced/updated
        - Decomposition - focus on decoupling
            - A restaurant kitchen divided into specialized stations (grill, salad, pastry).
            - breaking a complex system or problem into smaller, more manageable parts
                - involves identifying the distinct entities and behaviors within a domain and assigning them to specific classes
            - allows developers to focus on individual pieces of logic in isolation
        - Encapsulation - focus on data privacy
             - A medicine capsule hiding the chemicals inside.
            - hiding details that do not need to be shared makes it easier to evolve the model as requirements change
        - Abstraction - focus on interface
            - A TV remote; you press "Power" without knowing the circuitry.
            - identifying the essential characteristics of an object while hiding the non-essential implementation details
    - Beyond Objects: Alternative Design Paradigms
        - Procedural Programming - treats a program as a sequence of instructions or function calls
            - Focus: Logic and linear flow.
            - State Management: State is often global or passed explicitly through function arguments.
            - Best for: System-level programming, simple scripts, and high-performance drivers where the overhead of objects and dynamic dispatch is unwanted.
        - Functional Programming - treats computation as the evaluation of mathematical functions and avoids changing-state and mutable data
            - Immutability: Once a data structure is created, it cannot be changed.
            - First-Class Functions: Functions can be passed as arguments, returned from other functions, and assigned to variables.
            - Declarative Nature: You describe what you want to achieve rather than how to update the state of the machine
        - Event-Driven Programming - centers the program's flow around events
            - Focus: Events and asynchronous reactions
            - State Management: State is often managed within handlers or through a central event bus
            - Best for: Graphical User Interfaces (GUIs), web servers, and distributed systems where components need to remain decoupled
        - Data-Oriented Design - focuses on how data is laid out in memory to maximize CPU cache efficiency
            - Instead of an array of Soldier objects, you might have an array of "Positions," an array of "Health Values," and an array of "Velocities."
            - frequently used in high-performance fields like game development
### Design Principles
- create systems that:
    - work and satisfy requirements
    - as easy as possible to understand, debug, maintain
    - hold up well under change
- Design is Inherently Iterative
    - design as much as you can before starting, but expect to make changes or additions throughout the process
- Abstraction
    - primary tool for coping with COMPLEXITY
    - other classes do not correspond to real world concepts but are needed for internal implementation
    - keep public interfaces as simple/minimal as possible
    - only include aspects of objects that are relevant to the software you are creating
- Good Naming
    - sometimes use a THESAURUS !
    - class and variable names should describe what they represent, usually nouns
    - methods should clearly convey function or purpose, usually verbs
- Single-Responsibility Principle
    - each class/method should have a single responsibility
    - classes - one,  well-defined concept
    - methods - one, well-defined task
        - methods that perform multiple should delegate tasks to sub-methods that perform a single task
    - cohesive classes and methods are easy to name
- Decomposition
    - another fundamental technique for managing COMPLEXITY
    - large problems divided into smaller sub-problems/pieces
    - subdivision continues until leaf-level problems are simple enough to solve directly
    - solutions to sub-problems recombined into solutions for larger problems
    - STRONGLY RELATED TO ABSTRACTION
        - solutions to sub-problems are usually encapsulated in own class
    - Levels of decomposition
        - System
        - Subsystem
        - Packages - in java, folder that contains related classes
        - Classes
        - Methods
- Good Algorithm and Data Structure Selection
    - "What are access patterns of the program?"
    - "How can I make it fast an efficient?"
    - "What are Big-O's of algorithms I'm using?
    - "What structures are my algorithms accessing?"
- Low Coupling
    - the less classes know about each other, the better
    - coupling - classes calling each other, reaching into each other, accessing the same files
    - low coupling reduces ripple effects when classes are changed
    - Encapsulate - hide information
        - classes should hide internal variables and methods that are not essential for users to know about (public, private, protected)
            - protected - children in inheritance tree can see
        - don't let internal details "leak"
            - naming a student list `ClassRoll` is better than `StudentLinkedList`
    - Separation of interface
        - maintain separation between class interface and implementation
- Avoid Code Duplication
    - DRY principle: Don't repeat yourself
    - Disadvantages
        - N copies to maintain
        - bugs duplicated N times
        - makes program longer
    - Solutions
        - Factor common code into separate method/class
        - shared code might be placed in common superclass

### SOLID
- Single Responsibility: A class should have one, and only one, reason to change
- Open-Closed: Software entities should be open for extension but closed for modification.
    - common way to achieve this is by using interfaces or abstract classes to control behavior
    - instead of modifying a class's methods to handle new scenarios, you pass in an implementation of an interface that provides the new behavior
- Liskov Substitution: Subtypes must be substitutable for their base types.
    - if a program is using a base class or an interface, it should be able to use any of its subclasses without knowing it and without the program failing
    - violations cause unexpected behavior and force developers to understand the internal implementation of every subclass before they can safely use them
- Interface Segregation: Clients should not be forced to depend on methods they do not use.
    - When defining an interface, you should only include methods that form a cohesive whole
    - If you need to alter a "fat" interface, you must examine every consumer, even those that don't use the methods you are changing
    - create multiple, specific interfaces
- Dependency Inversion: Depend on abstractions, not on concretions.
    - high-level modules (business logic) should not depend on low-level modules (implementation details like databases or specific hardware). Both should depend on abstractions (interfaces), which decouples code
        - this might feel contrary to the concept of decomposition. here's why it isn't
            - decomposition - separating high-level logic into digestible pieces of low-level logic
            - dependency inversion - making higher level logic independent of low-level **implementations**
    - to apply, "invert" the dependency by passing an interface as a parameter
    - "Can you explain Dependency Inversion to me? How do I invert a dependency?"
        - "Dependency Inversion is the principle that high-level parts of your system should be independent of the implementations of the low-level parts. Notice how I said "implementations of". This does NOT contradict with the principle of decomposition (big parts break down into small parts). To invert a dependency, you can put an interface between the high-level system and low-level implementation of your code, which allows for more dynamic handling of the low-level task you're trying to complete.  This makes code more sustainable when you want to make changes to low-level implementations in your system."

### Java Inner Classes
- Intro
    - If you need to implement a class in different ways for different classes, that class should be defined inside the class that needs it
        - iterators are different for different data structures, so a data structure that needs a particular iterator is a good place to put it
        - that internal iterator class object will implement Iterator
- Static Inner-Classes
    - can be defined inside some other class, but doesn't have access to anything related to the class it's defined in
        - can't access outer-class variables directly, so data has to be passed in
    - equivalent to creating in its own class file, but convenient placement (only relevant inside outer-class)
        - follows principle of declaring things as close as possible to where they are used
    - ```java
        public class StaticOuterExample {
            public static void main(String[] args) {
                System.out.println(new StaticOuterExample());
            }

            private StaticInnerExample inner = new StaticInnerExample();

            private static class StaticInnerExample {
                public String toString() {
                    var inner = this.getClass().getName();
                    return String.format("Inner: %s", inner);
                }
            }

            public String toString() {
                return inner.toString();
            }
        }
        ```
    - output: `Inner: StaticOuterExample$StaticInnerExample`
- Inner-Classes
    - NOT STATIC, can access instance variables of outer-class
    - ```java
        public class OuterExample {
            public static void main(String[] args) {
                System.out.println(new OuterExample());
            }

            private InnerExample inner = new InnerExample();

            private class InnerExample {
                public String toString() {
                    var inner = this.getClass().getName();
                    // Note the use of the outer class's this pointer.
                    var outer = OuterExample.this.getClass().getName();
                    return String.format("Inner: %s has access to Outer: %s", inner, outer);
                }
            }

            public String toString() {
                return inner.toString();
            }
        }
        ```
    - output: `Inner: OuterExample$InnerExample has access to Outer: OuterExample`
- Local Inner-Classes
    - not only can you declare a class inside of a class that needs it, you can declare it inside of a METHOD that needs it
    - useful if there are hundreds of lines of code and several inner classes, puts us closer to where it is needed
    - RESTRICTION: can only access method's local variables if they are either final or effectively final
        - parameters go away when method exited
        - if a local inner-class accesses a parameter and then outlasts the method (when parameter is destroyed)
        - works because when instance of class is created, parameter (or variable) is COPIED and stuffed in object
    - ```java
        public class LocalOuterExample {
            public static void main(String[] args) {
                System.out.println(new LocalOuterExample());
            }

            public String toString() {
                var outerLocalVar = "outerLocalVar";

                class InnerExample {
                    public String toString() {
                        var inner = this.getClass().getName();
                        // Note the use of the outer class's this pointer and scope variables.
                        var outer = LocalOuterExample.this.getClass().getName();
                        return String.format("Inner: %s has access to Outer: %s, and variables: %s", inner, outer, outerLocalVar);
                    }
                }

                InnerExample inner = new InnerExample();

                return inner.toString();
            }
        }
        ```
    - output: `Inner: OuterExample$1InnerExample has access to Outer: OuterExample, and variables: outerLocalVar`
- Anonymous Inner-Classes
    - can not only declare class inside a METHOD, but inside an EXPRESSION
    - defined by referencing an interface, followed by the implementation
    - useful in cases where you have commonly used interfaces that only have a single method
    - invented for event handlers
    - ```java
        public class AnonymousExample {
            public interface Speaker {
                String sayHello();
            }

            public static void main(String[] args) {
                var spanish = new Speaker() {
                    public String sayHello() {
                        return "Hola";
                    }
                };
                var german = new Speaker() {
                    public String sayHello() {
                        return "Hallo";
                    }
                };

                System.out.printf("Spanish: %s\nGerman: %s", spanish.sayHello(), german.sayHello());
            }
        }
        ```
### Lambda Expressions
- Overview
    - Overview
        - Lambda expression = a block of code with the specification of any parameters that must be passed into it, that can be stored in variables, passed as parameters, and executed later
    - Benefits
        - with lamdas, it's easy to store code in variables, pass code as parameters, and write code to be used in event handlers, threads, sort algorithms, etc.
        - added functional programming paradigm to java
    - Example: normal vs lambda
        - ```java
            class StringLengthComparator implements Comparator<String> {
                public int compare(String first, String second) {
                    return Integer.compare(first.length(), second.length()); }
            }

            Arrays.sort(stringArray, new StringLengthComparator () ) ;
            ```
        - ```java
            Arrays.sort(stringArray, (first, second) -> Integer.compare(first.length (), second.length()));
            ```
- How Lambdas Work in Java
    - Example explained
        - JVM infers data type for lamda expression, return type (or void) for expression, and parameter types (unless explicit), from the method being called
        - constructs in-memory instance of a class expected by called method with a single method whose body is constructed from lambda expression (part after ->)
        - part before -> names parameters in method to be created and acts as declaration
        - constructed instance is passed as parameter in method call
        - Example above
            - JVM infers method being called is Arrays.sort(T[] a, Comparator<? super T>c) with T being String (assuming datatype of StringArray param is String [])
            - JVM constructs instance of Comparator<String> that looks like:
                - ```java
                    new Comparator<String> {
                        public int compare(String first, String second) {
                            return Integer.compare(first.length(), second.length()); }
                    }
                    ```
            - JVM invokes sort method with Comparator instance passed as second parameter
    - Functional Interfaces
        - what data types can be used for lambda expressions (for variables holding them or parameters receiving them)?
            - Functional Interface
        - What is a Functional Interface?
            - any interface with exactly one abstract method
            - examples:
                - comparable
                - runnable
                - some event handlers
- Lambda Syntax
    - `(Parameter List) -> Body`
    - `Parameter List`
        - comma separated list of formal parameters
        - data types are optional and can usually be inferred
        - parentheses are optional for single param
        - empty parentheses required for empty param list
    - `->`
        - arrow token required between param list and body
    - `Body`
        - A single expression or statement block
        - return is inferred for a single expression
        - statement blocks enclosed in curly braces
        - return must be explicitly stated (unless void) for statement blocks
- Function/Lambda Variables
    - usually using lambdas as parameter to other method call, but can also be used as variables
    - ```java
        public class LambdaVariable {
            Predicate<String> strLenPredicate = ș -> s.length() > 10;
        }
        ```
    - creates predicate function (function with test method that returns bool)
        - in this case, returns true if string length is >10
    - data type of lambda inferred by context (type of variable to which it's being assigned, in this case Predicate<String>)
    - can be passed as parameter to any method that takes Predicate<String> parameter
    - can be invoked by calling it's test(String) method
    - Predicate is one of 43 interfaces in java.util.function package
    - not super common, usually lambdas declared as method is called
- Creating APIs with Lambdas
    - ```java
        public class StringSelectorExample {
            public static List<String> select (Collection<String> strings, Predicate<String> selector) {
                List<String> selectedStrings = new ArrayList<>();

                for (String aString : strings) {
                    if (selector.test(aString)) {
                        selectedStrings.add(aString);
                    }
                }

                return selectedStrings;
            }
        }
        ```
        - `select(strings, x -> x.startsWith("I "))`
            - I doubted if I should ever come back.
            - I shall be telling this with a sigh
            - I took the one less traveled by,

        - `select(strings, x -> x.startsWith("And"))`
            - And sorry I could not travel both
            - And be one traveler, long I stood
            - And looked down one as far as I could
            - And having perhaps the better claim,
            - And both that morning equally lay
            - And that has made all the difference.

        - `select(strings, x -> x.contains("road"))`
            - Two roads diverged in a yellow wood,
            - Two roads diverged in a wood, and I-
- Use of Lambdas in Existing Java Classes
    - use of labmdas has been integreated thoughout the Java API, especially in collections API
    - ```java
        public class RemoveIfExample {
            public static void main(String[] args)
                List<Integer> intList = new ArrayList<>();
                intList.addAll(Arrays.osList(23, 5, 10, 71, 100, 1203, 4, 7, 748));

            intList.removeIf(x -> x >= 100);

            for(Integer value : intList) {
                System.out.print(value + " ");
            }
        }
        ```
- Method References
    - even shorter syntax that can be used for certain lambda expressions
    - can be used on lambda expressions calling just one method, and calling that method on the parameter passed in
    - ```java
        List<Integer> intList = Arrays.asList(23, 5, 10, 71);
        intList.forEach(x -> System.out.println (x));
        ```
    - ````java
        List<Integer> intList = Arrays.asList (23, 5, 10, 71);
        intList.forEach (System.out::println);
        ```
    - double colon indicates method reference, instead of method call
    - parameter list not needed or allowed for method reference and no `->` operator
    - can be used for static method, instance method, constructor invocations
        - static method: `ClassName::methodName`
        - instance method: `objectRefernce::methodName` (equivalent to `x -> objectReference.methodName(x)`)
        - instance method (with instance defined as first param in expression): `ClassName::methodName` (equivalent to `(x,y) -> x.methodName(y)`)
        - constructor invocation: `ClassName::new`

### Java Input and Output (IO)
- Overview
    - Ways to Read/Write Files
        - Streams
            - Read or write a file (or other source or destination of bytes) sequentially
        - Scanner Class
            - Tokenize stream input (read one token at a time)
        - Files Class
            - Read, copy, etc. whole files
        - RandomAccessFile Class
            - Use a file pointer to read from / write to any location in a file
    - The File Class
        - Used to represent, create, or delete a file, but not to read one
        - check existence: `file.exists()`
        - create file: `file.createNewFile()`
        - delete file: `file.delete()`
        - used to create the other classes
- Streams
    - most common way to work with files
    - Overview
        - writing to / reading from files (or other sources) FROM BEGINNING TO END
        - two choices: Binary-Formatted or Text-Formatted Data
            - Binary Formatted: 00 00 04 D2 (4 bytes)
            - Text Formatted: 1234 (4 chars)
        - `InputStream` and `OutputStream`
            - reading/writing bytes
            - reading/writing binary-formatted data
        - `Reader` and `Writer`
            - reading/writing characters
            - reading/writing text-formatted data
        - MUST USE SPECIFIC SUBCLASSES
    - `InputStream` and `OutputStream`
        - InputStream interface
            - FilelnputStream
            - PipedInputStream - allow you to read data from a thread
            - URLConnection.getInputStream() - connect and read data from server
            - HttpExchange.getRequestBody() - server can read data from a client
            - ResultSet.getBinaryStream(int columnIndex)
            - Many more examples in the Java API
        - Filter Input Streams
            - you may want to enable features when consuming InputStream data like decompress, decrypt, count, buffer
            - each stream in pipeline can manipulate data in the way it needs to, then pass it to the next
        - OutputStreams
            - same interface as InputStream, just reverse
        - Filter Output Streams
            - may want to compress, encrypt, etc data as it goes into stream
        - Reading/Writing Binary-Formatted Data
            - DataInputStream gives methods for datatypes
- `Readers` and `Writers`
    - Example
        - ```java
            private void copy(String from, String to) throws IOException {
            File fromFile = new File(from);
            File toFile = new File(to);

            try(FileReader fr = new FileReader(fromFile);
                BufferedReader br = new BufferedReader(fr);

                FileWriter fw = new FileWriter(toFile);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw)) {

                String line;
                while((line = br.readLine()) != null) {
                    pw.println(line);
                }
                }
            }
            ```
    - Convert from Streams to Readers or Writers
        - Convert from an input stream to a reader with InputStreamReader:
            - `new InputStreamReader(new FileInputStream("myfile.txt"));`
        - Convert from an output stream to a writer with OutputStreamWriter:
            - `new OutputStreamWriter(new FileOutputStream("myfile.txt"));`
- Scanner
    - can read "tokens" one at a time from a source of characters
        - tokens are characters that MATTER, delimited by whitespace and \n
- Other Ways to Read and Write Files
    - Files: Read Entire File into a List
        - ```java
            public List<String> readFile(File file) throws IOException {
                Path path = Paths.get(file.getPath () );
                List<String> fileContents = Files.readAllLines (path) ;
                return fileContents;
            }
            ```
    - Random Access Files
        - allows "random access" to a file's contents for both reading and writing
        - Random access
            - The "file pointer" represents the current location in the file (similar to an array index)
            - Use the seek(long) or skipBytes(int) methods to move the "file pointer" to any location in the file
            - Read or write bytes at the current file pointer location using various overloaded read and write methods
- System.in and System.out
    - java.lang.System class has public fields named `in` and `out`
    - `System.in.read()` - the program will attempt to read a byte from the console
    - `System.out.println()` - displays the provided text in the console window
- Best Practices and Anti-Patterns
    - When working with Java IO design patterns, adhering to established practices ensures your code remains performant and leak-free.
    - Good Practices:
        - Use Try-with-Resources: Always use the try-with-resources statement to ensure that streams are closed automatically, preventing memory and file handle leaks.
        - Always Buffer: Reading or writing directly to a disk or network is expensive. Always wrap your base streams in a BufferedInputStream or BufferedReader unless you have a specific reason not to.
        - Specify Charsets: When using adapters like InputStreamReader, explicitly specify the Charset (e.g., StandardCharsets.UTF_8) to avoid platform-dependent behavior.
    - Bad Practices (Anti-patterns):
        - Manual Closing in Finally: Before Java 7, closing in a finally block was standard, but it is error-prone (e.g., if close() itself throws an exception). Avoid this in modern Java and use try-with-resources instead.
        - Ignoring the Decorator Chain: Don't forget that closing the outermost decorator (the "top" of the wrapper) will automatically close all underlying streams in the chain. You do not need to close each one individually.
        - Byte-by-Byte Processing: Processing large files using InputStream.read() without a buffer leads to excessive system calls and poor performance.

### Generic Types
- Overview
    - Overview
        - Generics allow you to specify one or more placeholder (generic) types when creating a class or interface whose actual type is determined when an instance is created
        - can be used as types for instance variables, method parameters, and return values
        - make classes more reusable than they otherwise would be
        - needed to understand Lambda's
    - Example
        - ```java
            public class Pair<T, U> {
                private T value1;
                private U value2;

                public Pair(T value1, U value2) {
                    this.value1 = value1;
                    this.value2 = value2;
                }

                public T getValue1() {
                    return value1;
                }
                public void setValue1(T value1) {
                    this.value1 = value1;
                }

                public U getValue2() {
                    return value2;
                }
                public void setValue2(U value2) {
                    this.value2 = value2;
                }
            }
            ```
        - instantiate with: `Pair<String, Integer> pair = new Pair<>("Hello", 123);`
- Using Generic Classes
    - Specify types in extends clause:
        - ```java
            public class StringPair extends Pair<String, String> {
                public StringPair(String valuel, String value2) {
                    super (value1, value2);
                }
            }
            ```
        - instantiate without declaring generic types:
            - `StringPair p = new StringPair("Hello","BYU");`
    - pass generic types from a generic cubclass
        - ```java
            public class KeyValuePair<V> extends Pair<String, V> {
                public KeyValuePair(String valuel, V value2) {
                    super (value1, value2);
                }
            }
            ```
        - instantiate by declaring a type for V:
            - KeyValuePair<Date> pair4 = new KeyValuePair<>("Hello", new Date());
- Using Generic Interfaces
    - takes type T as param, applies something, returns type R
        - ```java
            public interface Function<T, R> {
                R apply (T param) ;
            }
            ```
        - ```java
            public class Capitalizer implements Function<String, String> {
                @Override
                public String apply (String param) {
                    return param == null ? null :
                        param.toUpperCase ();
                }
            }

            public class StringManipulator {
                public static void main(String[] args) {
                    StringManipulator manipulator = new StringManipulator();
                    System.out.println(manipulator.manipulateString( str: "my string", new Capitalizer()));
                }
                public String manipulateString(String str, Function<String, String> manipulationFunction) {
                        return manipulationFunction.apply(str);
                }
            }
            ```
- Generic Type Wildcards
    - wildcards with special syntax can be used to expand acceptable types; denoted with `?`
        - `List<? super T> param` - data types of objects are T OR any of it's parent classes
        - `List<? extends T> param` - data types of objects are T OR any of it's subclasses
    - Example:
        - ```java
            public class GenericClassExample<T> {
                public void method1 (List <? super T> param) {};
                public T method2() { return null; }
            }
            ```
    - In this example, method1 accepts a List of whatever T is, or of any of T' s super classes.
    - Replacing super with extends results in the method accepting a list of T' s or any of T' s subclasses.

---
## Server

### JSON and Serialization
- JSON Introduction
    - primitive data types: supports numbers (ints and float), strings, booleans, null
    - aggregate data types: arrays (delimited with []), object (delimited with {})
    - NO TRAILING COMMAS IN JSON
        - ex. object (CATALOG) containing array of objects (CD's) containing primitives (TITLE)
        - ```json
            {
                "CATALOG": [
                    {
                        "CD": {
                            "TITLE": "Song Name",
                            "ARTIST": "Artist Name",
                            "YEAR": 1988
                        },
                        "CD": {
                            "TITLE": "Song Name 2",
                            "ARTIST": "Artist Name 2",
                            "YEAR": 1999
                        }
                    }
                ] 
            }
            ```
- Parsing JSON
    - most languages provide JSON parsers, so no need to write one
    - Three Major Types of Parsers
        1. Stream Parsers
            - tokenizers that return one token at a time from the JSON data file
        2. DOM Parsers
            - usually built on stream parsers
            - convert JSON text to an in-memory tree data structure (tree called DOM)
            - after running parser to create DOM, traverse DOM to extract data
        3. Serializers/Deserializers
            - use library to convert from JSON to Java Objects (and vice versa)
            - GSON and Jackson are popular ones
- JSON Stream Parser
    - laborious
    - useful if you only need a few pieces of info from a huge JSON file
- JSON DOM Parser
    - JSON is basically a tree of nested objects and nodes
    - DOM Parser parses ENTIRE file in ONE method
    - returns data structure shaped like the tree
- JSON Object Serialization
    - process
        - convert objects to array of bytes (or chars in JSON's case) to store and send
        - deserialize (reassemble) when returned
    - say you have an object you want to save
        - create a serializer object (gson)
        - call .toJson() method on serializer, passing in object (ex. catalog) as parameter
        - write jsonString to file
            - ```java
                private void generate(Catalog catalog, File file) throws IOException {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonString = gson. toson(catalog);

                    try(FileWriter fileWriter = new FileWriter(file);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                            bufferedWriter.write(jsonString);
                        }
                }
                ```
    - say you want to convert a json string to java object
        - parse (using file and buffered reader) the string
        - create deserializer (gson, same as serializer)
        - call .fromJson() method on deserializer, passing in file (buffered reader) and name of Java class that should be created from the data
            - ```java
                private List<CD> parse(File file) throws IOException {
                    try(FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader)) {

                        Gson gson = new Gson();
                        Catalog catalog = gson.fromJson(bufferedReader, Catalog.class);
                        return catalog.getCds();
                    }
                }
                ```
- GSON Type Adapters
    - Type Adapters are used to customize the way data is de/serialized
    - examples
        - write attribute in smth other than default
        - deserialize json string to obj whose class does not have a no-argument constructor
        - deserialize json string into one of several subclasses
        - deserialize json string into one of several implementing classes of an interface
        - deserialize json string into an object whose instance variables are interfaces or abstract objects
        - you have complex parsing or serialization logic
- Complex Type Adapter
    - Garage storing Automobile array; with Car and Truck subclasses (truck has 4wheeldrive bool)
    - GSON doesn't know how to handle array storing different subclasses of array class
    - Serialization
        - register a type adapter
            - ```java
                // Register a type adapter for the automobile interface
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Automobile.class, new AutomobileTypeAdapter());
                Gson gson = builder.create();
                ```
        - write a type adapter class, serialize cars and trucks in different way
            - ```java
                private static class AutomobileTypeAdapter extends TypeAdapter<Automobile> {
                    @Override
                    public void write(JsonWriter jsonWriter, Automobile automobile) throws IOException {
                    Gson gson = new Gson();

                    switch(automobile.getType()) {
                        case Car -> gson.getAdapter(Car.class).write(jsonWriter, (Car) automobile);
                        case Truck -> gson.getAdapter(Truck.class).write(jsonWriter, (Truck) automobile);
                    }
                }
                ```
    - Deserialization
        - perform token level parse (stream) to make out object type
        - read properties of object
        - after "type" is parsed, can perform switch on Car/Truck
            - ```java
                @0verride
                public Automobile read(JsonReader jsonReader) throws IOException {
                    String make = null;
                    String model = null;
                    int year = 0;
                    Automobile.AutomobileType type = null;
                    boolean fourWheelDrive = false;

                    jsonReader.begin0bject();

                    while (jsonReader.hasNext()) {
                        String name = jsonReader.nextName();
                        switch (name) {
                            case "make" -> make = jsonReader.nextString();
                            case "model" -> model = jsonReader.nextString();
                            case "year" -> year = jsonReader.nextInt();
                            case "type" -> type = Automobile.AutomobileType.value0f(jsonReader.nextString());
                            case "fourWheelDrive" -> fourWheelDrive = jsonReader. nextBoolean();
                        }
                    }

                    jsonReader.endObject();

                    if(type == null) {
                        return null;
                    } else {
                        return switch (type) {
                            case Car -> new Car(make, model, year);
                            case Truck -> new Truck(make, model, year, fourWheelDrive);
                        }
                    }
                }
                ```

### Hypertext Transfer Protocol (HTTP)
- HTTP GET Requests
    - example: GETTING list of games
    - Uniform Resource Locator (URL)
        - Protocol: `http`/`https`
        - Domain Name: `www.google.com`
        - Port Number: `:443`
        - Path: `images/branding/googlelogo`
        - Query String: not used in this class
    - GET Request
        - First Line: method, path, version
            - `GET images/branding/googlelogo HTTP/1.1`
            - Method (i.e., request type): `GET`
            - URL Path: `images/branding/googlelogo` (DNS already done)
            - HTTP Version: `HTTP/1.1`
        - Headers: attach metadata or properties to request
            - `Accept: image/png,image/gif,image/jpg`
            - Header name: `Accept:`
            - Header value: `image/png,image/gif,image/jpg`
    - GET Response
        - First Line: version, code, phrase
            - `HTTP/1.1 200 OK`
        - Headers: metadata (INCLUDING AUTH TOKEN)
            - `Content-Type: image/png`
            - `Content-Length: 5969`
        - Empty Line: SEPARATE HEADERS FROM BODY
            - `\n`
        - Response Body: Content
            - `PNG image bytes ...`
- HTTP POST Requests
    - example: POST a login request 
    - URL: establish connection with server
    - POST Request
        - First Line: method, path (program to process sent data), version
            - `POST /search HTTP/1.1`
        - Headers: metadata
            - `Content-Type: application/x-www-form-urlencoded`
        - Empty Line:
            - `\n`
        - Request Body: data we are sending (ex search terms)
            - `keywords=byu&cs&240`
    - Post Response
        - First Line: version, code, phrase
            - `HTTP/1.1 200 OK`
        - Headers: metadata
            - `Content-Type: text/html`
            - `Content-LengthL 10986`
        - Empty Line:
            - `\n`
        - Response Body:
            - `html containing search results ...`
- HTTP Methods: often used to implement REST API's (CRUD)
    - GET: retrieve data
    - POST: submit/send data
    - PUT: replace specified data
    - DELETE: delete data
    - others rarely used: HEAD, OPTIONS, TRACE, PATCH

### cURL
- cURL: Client for URLs
    -command line client to send/receive http requests/responses
        - GET request by default
            - `curl https://api.chucknorris.io/jokes/random?category=dev`
        - to POST
            - `curl -X POST {URL}`
    - Parameters
        - | Parameter       | Purpose                                 | Example                               |
            | --------------- | --------------------------------------- | ------------------------------------- |
            | `-X`            | HTTP method (GET is default)            | `-X POST`                             |
            | `-V`            | Verbose. Display connection and headers | —                                     |
            | `-d`            | Body data                               | `-d '{"name":"joe"}'`                 |
            | `-H`            | Provide an HTTP header                  | `-H "Content-Type: application/json"` |
            | `-o`            | Output response to a file               | `-o response.json`                    |
            | `--data-binary` | Body data from file                     | `--data-binary @req.json`             |
            | `-D`            | Dump headers to the given file          | `-D -`                                |
- cURL for chess: great way to debug!
    - getting auth
        - `curl localhost:8080/game`
            - `{"success":false,"message":"Error: Invalid auth token"}`
        - `curl -X POST localhost:8080/session -d '{ "username":"bob", "password":"bob"}'`
            - `{"authToken": "1c8ceb9f-6433-42b4-8e8f-1fb562567d72","username" : "bob", "success" : true}`
        - `curl -X GET localhost: 8080/game -H "Authorization: 1c8ceb9f-6433-42b4-8e8f-1fb562567d72"`
            - `{"games": [{"gameID": 2, "gameName": "fhe"}, {"gameID": 1, "gameName" : "test"}], "success" :true}`

### Web API
- Javalin Overview
    - Simple example
        - simple server using lambda - useful for short handler
            - ```java
                public class SimpleHelloBYUServer {
                    public static void main(String[] args) {
                        Javalin.create()
                            .get("/hello", ctx -> ctx.result("Hello BYU!"))
                            .start(8080);
                    }
                }
                ```
        - alternative using separate method (and method reference)
            - ```java
                public class SimpleHelloBYUServer {
                    public static void main(String[] args) {
                        Javalin.create()
                            .get("/hello", SimpleHelloBYUServer::handleHello)
                            .start(8080);
                    }

                    private static void handleHello(Context ctx) {
                        ctx.result("Hello BYU!")
                    }
                }
                ```
        - alternative using separate class for handler (usually placed in separate file, useful for if handler is complex)
            - ```java
                public class SimpleHelloBYUServer {
                    public static void main(String[] args) {
                        Javalin.create()
                            .get("/hello", new HelloHandler())
                            .start(8080);
                    }

                    private static class HelloHandler implements Handler {
                        @0verride
                        public void handle(@NotNull Context ctx) throws Exception {
                            ctx.result("Hello BYU!");
                        }
                    }
                }
                ```
    - More complex example - allows port assignment as CLI param
        - ```java
            public class HelloBYUServer {
                public static void main(String[] args) {
                    int requestedPort = Integer.parseInt(args[0]);

                    var server = new HelloBYUServer();
                    int port = server.run(requestedPort);
                    System.out.println("Running on port " + port);
                }

                public int run(int requestedPort) {
                    Javalin javalinServer = Javalin.create();

                    createHandlers(javalinServer);

                    javalinServer.start(requestedPort);
                    return javalinServer.port();
                }

                private void createHandlers(Javalin javalinServer) {
                    javalinServer.get("/hello", new HelloBYUHandler());
                    // other routes here
                }
            }
            ```
        - ```java
            import io.javalin.http.Context;
            import io.javalin.http.Handler;

            public class HelloBYUHandler implements Handler {
                public void handle(Context context) {
                    context.json("Hello BYU!");
                }
            }
            ```
    - more detailed handler: can use context methods to configure the exact response to client
        - ```java
            public class HelloBYUDetailedHandler implements Handler {
                public void handle(Context context) {
                    context.status(200);
                    context.contentType("application/json");
                    context.header("CS240", "Awesome!");
                    context.result("{message: Hello BYU!}");
                }
            }
            ```
- Javalin Handlers
    - endpoint handlers
        - handlers are matched in order they are defined. the first handler that matches the request is invoked
        - ```java
            get("/", (ctx) -> {
                // show something
            });

            post("/", (ctx) -> {
                // show something
            });

            put("/", (ctx) -> {
                // show something
            });

            delete("/", (ctx) -> {
                // show something
            });
            ```
    - handlers that run for all requests (cross-cutting concern) - CAN RUN MULTIPLE BEFORE AND AFTER HANDLERS
        - `before` handler runs before every request; prevents duplication
            - only param is the handler you want to run every time a request is received
            - doesn't need URL path because it runs all the time
            - throwing exception prevents further processing by other handlers
            - ```java
                before(context -> {
                    boolean authenticated;

                    // .. check if authenticated

                    if (!authenticated) {
                        throw new UnauthorizedResponse ();
                    }
                });
                ```
        - named parameters
            - can pass names in URL path, HTTP headers, request body
            - ex. URL
                - `<...>` allows slashes, `{...}  does not
                - ```java
                    public class SimpleNameServer {
                        private ArrayList<String> names = new ArrayList<>();

                        private void run() {
                            Javalin.create(config -> config.staticFiles.add("web"))
                                .post("/name/{name}", this::addName)
                                .get("/name", this::listNames)
                                .delete("/name/{name}", this::deleteName)
                                .start(8080)
                        }

                        private void addName(Context context) {
                            names.add(context.pathParam("name"));
                            listNames(context);
                        }

                        private void listNames(Context context) {
                            String jsonNames = new Gson().toJson(Map.of("name", names));
                            context.json(jsonNames);
                        }

                        private void deleteName(Conext context) {
                            names.remove(context.pathParam("name"));
                            listNames(context);
                        }
                    }
                    ```
        - wildcard params
            - ```java
                // matches "GET /hello/foo/bar"
                get("/hello/*", context -> {
                    // url part matching * not available
                    // use <...> syntax to access
                });
                ```
    - Useful Request and Response Methods
        - Request
            - body() - retrieve request body
            - headerMap() - retrieves all headers (as Map<String,String>)
            - header("...") - retrieves the specified header
            - path() - retrieves request URL path
        - Response
            - result("Hello") - set the response body to "Hello"
            - json("{message: hello}") - sets the repsonse body and content type
            - header("...", "...") - sets a header
            - contentType("...") - sets the content type
            - status(404) - sets the status code to 404 (not found)
- Error Handling
    - simple example
        - `exception()` and `error()` allow for global error handling
        - Handling Exceptions
            - `exception(e, ctx)` method on Javalin object will handle any exception for the whole sever
                - e - exception thrown
                - ctx - ctx object
            - build http response to send back to client
            - ```java
                app.exception(Exception.class, (e, ctx) -> {
                    // handle general exceptions here
                    // will not trigger if more specific exception-mapper found
                });
                ```
        - Handling status codes:
            - handle http error status codes from server
                - 404 - ex. error type
                - ctx - ctx object
            - ```java
                app.error(404, ctx -> {
                    ctx.result("Generic 404 message");
                });
                ```
- Serving Static Files
    - `main/java/StaticFileServer`
        - ```java
            public class StaticFileServer {
                public static void main(String[] args) {
                    int requestedPort = Integer.parseInt(args[0]);

                    var server = new StaticFileServer();
                    int port = server.run(requestedPort);
                    System.out.println("Running on port " + port);
                }

                public int run(int requestedPort) {
                    Javalin javalinServer = Javalin.create(
                        config -> config.staticFiles.add("web")
                    );

                    createhandlers(javalinServer);

                    javalinServer.start(requestedPort);
            ```
    - `main/resources/web/index.html`
        -  EVERYTHING in `/web` gets served, `index.html` is default
- Javalin Installation
    - `import io.javalin.Javalin`
    - LOWK TALK TO REECE ABOUT THIS
- Client Web API Access
    - Java's HttpClient class can be used to make and recieve HTTP requests to/from the server
    - java.net.http package
    - steps
        - create httpClient
        - capture CLI params and pass to doGet(host, portString, path) method
        - inside doGet()
            - create urlString
            - construct http request with HttpRequest.newBuilder()
    - GET Request
        - ```java
            public class GetExample {
                private static final int TIMEOUT_MILLIS = 5000;

                private final HttpClient httpClient = HttpClient.newHttpClient();

                public static void main(String[] args) throws IOException, InterruptedException {
                    if(args.length == 3) {
                        String host = args[0];
                        String portString = args[1];
                        String path = args[2];

                        var client = new GetExample();

                        try {
                            client.doGet(host, Integer.parseInt(portString), path);
                        } catch (URISyntaxException | NumberFormatException e) {
                            // Print usage if port is not an int or path is invalid
                            printUsage();
                        }
                    } else {
                        printUsage();
                    }
                }

                private static void printUsage() {
                    System.err.println("USAGE: java GetExample <host> <port> <path>");
                }

                public void doGet(String host, int port, String urlPath) throws (URISyntaxException, IOException, InterruptedB...) {
                    String urlString = String.format("http://%s:%d%s", host, port, urlPath);

                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(urlString))
                        .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                        .header("authorization", "abc123")
                        .GET()  // ANY REQUEST WE WANT TO MAKE GOES HERE
                        .build();   // creates HTTP request object and returns it

                    HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()); // first param request, second param response body expected

                    if (httpResonse.statusCode() == 200) {
                        HttpHeaders headers = httpResponse.headers();
                        Optional<String> LengthHeader = headers.firstValue("Content-Length");

                        System.out.printf("Received %s bytes%n", lengthHeader.orElse("unknown"));
                        System.out.println(httpResponse.body());
                    } else {
                        System.out.println("Error: received status code " + httpResponse.statusCode());
                        System.out.println(httpResponse.body());
                    }
                }
            }
            ```
    - POST Request
        - ```java
            public class PostExample {
                private static final int TIMEOUT_MILLIS = 5000;

                private final HttpClient httpClient = HttpClient.newHttpClient();

                public static void main(String[] args) throws IOException, InterruptedException {
                    if(args.length == 3) {
                        String host = args[0];
                        String portString = args[1];
                        String path = args[2];
                        String message = args[3]    // text passed into request body

                        var client = new PostExample();

                        try {
                            client.doPost(host, Integer.parseInt(portString), path, message);
                        } catch (URISyntaxException | NumberFormatException e) {
                            // Print usage if port is not an int or path is invalid
                            printUsage();
                        }
                    } else {
                        printUsage();
                    }
                }

                private static void printUsage() {
                    System.err.println("USAGE: java PostExample <host> <port> <path>");
                }

                public void doPost(String host, int port, String urlPath, String message) throws (URISyntaxException, IOException, InterruptedB...) {
                    String urlString = String.format("http://%s:%d%s", host, port, urlPath);

                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(urlString))
                        .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                        .header("authorization", "abc123")
                        .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8)) // specify data going into request body, message string and character set
                        .build();   // creates HTTP request object and returns it

                    HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()); // first param request, second param response body expected

                    if (httpResonse.statusCode() == 200) {
                        HttpHeaders headers = httpResponse.headers();
                        Optional<String> LengthHeader = headers.firstValue("Content-Length");

                        System.out.printf("Received %s bytes%n", lengthHeader.orElse("unknown"));
                        System.out.println(httpResponse.body());
                    } else {
                        System.out.println("Error: received status code " + httpResponse.statusCode());
                        System.out.println(httpResponse.body());
                    }
                }
            }
            ```

### Phase 3: Architectural Patterns
- In this architecture, the flow of data typically moves linearly through three primary components:
    - The Server (Web Layer): Handled by Javalin. Its job is to parse HTTP requests (JSON, path parameters), validate basic input format, and return the appropriate HTTP status codes.
    - The Service (Business Logic Layer): This is the "brain" of the application. It contains the rules of chess, move validation logic, and coordinates game state changes.
    - The Data Access Object (DAO/Persistence Layer): This layer communicates with the database or memory store. It abstracts the details of SQL or file I/O away from the business logic.
- To keep this architecture clean, we rely on several key software design principles:
    - Separation of Concerns (SoC): The Server shouldn't know how to check for "Checkmate," and the DAO shouldn't know anything about HTTP status codes.
    - Single Responsibility Principle (SRP): Each class should have one, and only one, reason to change. If you change your database from MySQL to MongoDB, only the DAO layer should be affected.
    - Dependency Injection (DI): Instead of a Server creating its own Service instance (e.g., new ChessService()), the Service should be "injected" via a constructor. This makes unit testing significantly easier by allowing the use of "Mocks."
- Prevent "Fat Server"
    - ```java
        // GOOD: Server only handles the "Web" side of things
        public class ChessServer {
            private final ChessService service;

            public ChessServer(ChessService service) {
                this.service = service;
            }

            public void handleMove(Context ctx) {
                Move move = ctx.bodyAsClass(Move.class);
                try {
                    // Server asks the service to perform the action
                    Game updatedGame = service.makeMove(move);
                    ctx.json(updatedGame);
                } catch (InvalidMoveException e) {
                    ctx.status(400).result(e.getMessage());
                }
            }
        }
        ```
- Common Pitfalls
    - Leaky Abstractions: Passing a java.sql.ResultSet or a Javalin Context into the Service layer.	Use Plain Old Java Objects (POJOs) or DTOs to pass data between layers.
    - Circular Dependencies: The Service layer calling the Server, or two Services calling each other directly.	Ensure a strict top-down flow: Server -> Service -> DAO.
    - Hardcoding Dependencies: Using new to instantiate dependencies inside a constructor.	Use Constructor Injection to pass dependencies from the main application entry point.

