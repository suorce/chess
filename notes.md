# My Project Notes

## 9/8/2026
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


## Classes and Objects
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

## Java Records
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
    - can add custom methods to a record, useful to provide logic related to the data
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

## Interfaces and Abstract Classes
- polymorphism - the ablility of an object to take on many (poly) forms (morph) to fit into different contexts
    - in java, inheritance and interfaces are the primary way to achieve this
    - `extends` keyword to inherit functionality from another class
    - `implements` keyword to adhere to an interface definition
- Interfaces
    - allow you to define *what* a class does without specifying *how* it does it