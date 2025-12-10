Name: Zahura Zaman

Project Name: Web-Based Frontend Extension for Choco Solver: Open Source Software for Constraint Programming

A Web-Based Constraint Modeling and Solving Environment Using Choco-Solver and Spring Boot
Overview
This project provides a complete web-based interface for interacting with the Choco constraint solver. It consists of:
A static HTML/CSS/JavaScript frontend that allows users to construct constraint problems in a browser.
A Spring Boot backend exposing REST APIs that receive user-defined constraints, translate them into Choco-solver models, execute the solver, and return JSON responses.
The system supports the full range of variable types implemented in Choco-solver, including IntVar, BoolVar, RealVar, SetVar, and GraphVar.
The aim of this project is to offer a structured and user-friendly interface for constraint specification while delegating problem solving to Choco’s underlying engine.

Documentation, Support, and Issues
The project demonstrates how a modern frontend communicates with a Java + Spring backend to construct and solve constraint programming models.
Issues and enhancement requests can be submitted through the GitHub issue tracker of this repository.

Architecture
Project Structure
/choco-solver-frontend
    index.html
    intvar.html
    boolvar.html
    realvar.html
    setvar.html
    graphvar.html
    style.css
    *.js (frontend request handlers)

/choco-solver-backend-ui
    src/main/java/...
    Controller classes (IntVarController, BoolVarController, etc.)
    Model translation and solver classes
    pom.xml

The frontend consists of static HTML pages styled through CSS and controlled through lightweight JavaScript that sends JSON to the backend via HTTP POST calls.
The backend is a Spring Boot application that uses Choco-solver internally.

Backend Dependencies
The backend of this project is implemented using Spring Boot 3.3.0, Java 17, and Choco-solver 4.10.14. Maven is used for dependency management and project configuration.
Requirements
JDK 17+
Maven 3+
Spring Boot 3.3.x (Web starter)
Choco-solver 4.10.14
Lombok (optional, used for reducing boilerplate)
Jackson (included automatically with Spring Boot)
Maven Dependencies (excerpt from pom.xml)
The following dependencies are used to run the backend:
<!-- Spring Boot Web Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Choco Solver (Constraint Programming Engine) -->
<dependency>
    <groupId>org.choco-solver</groupId>
    <artifactId>choco-solver</artifactId>
    <version>4.10.14</version>
</dependency>

<!-- Lombok (Optional: simplifies POJOs with annotations) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <optional>true</optional>
</dependency>

<!-- Testing utilities -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

Notes
Jackson JSON processor is automatically included as part of
spring-boot-starter-web, so no additional Jackson dependency is required.
This enables automatic JSON serialization/deserialization for all REST API controllers.
The project uses Spring Boot’s dependency management via:
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring.boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

This ensures consistent dependency versions across the backend.

Connecting to Choco-Solver
Each backend controller follows a general pattern:
Receive structured JSON describing variable domains, constraint type, and parameters.
Create a Choco Model.
Instantiate the appropriate variable type (IntVar, BoolVar, RealVar, SetVar, GraphVar).
Post the corresponding constraint onto the model.
Call solver.solve().
Return extracted solution(s) in JSON format.
General Flow Example
@PostMapping("/api/intvar/solve")
public ResponseEntity<?> solveIntVar(@RequestBody IntVarRequest request) {

    Model model = new Model("IntVar Problem");

    IntVar x = model.intVar("x", request.getXMin(), request.getXMax());
    IntVar y = model.intVar("y", request.getYMin(), request.getYMax());
    IntVar z = model.intVar("z", 0, 1000); // if needed

    Constraint c;

    switch(request.getConstraintType()) {
        case "BASIC":
            c = createBasicComparison(model, x, y, request);
            break;
        case "SUM":
            c = createSumConstraint(model, x, y, request.getZValue(), request);
            break;
        case "ARITH":
            c = createArithmeticConstraint(model, x, y, request.getOperation());
            break;
        default:
            throw new IllegalArgumentException("Unknown constraint");
    }

    c.post();

    Solver solver = model.getSolver();
    solver.solve();

    Map<String, Object> result = new HashMap<>();
    result.put("x", x.getValue());
    result.put("y", y.getValue());
    result.put("z", z.getValue());

    return ResponseEntity.ok(result);
}

The other controllers follow the same structure but use variable types appropriate to their domain.

API Structure and JSON Formats
The frontend communicates with the backend exclusively using JSON via HTTP POST calls.
Each variable type has a dedicated API endpoint.
1. IntVar API
POST /api/intvar/solve
Example Request JSON
{
  "constraintType": "SUM",
  "operation": "EQ",
  "xMin": 0,
  "xMax": 10,
  "yMin": 0,
  "yMax": 10,
  "zValue": 12
}

Example Response
{
  "x": 4,
  "y": 8,
  "z": 12
}


2. BoolVar API
POST /api/boolvar/solve
Request JSON
{
  "constraintType": "AND",
  "numBools": 3,
  "kValue": 1
}

Example Constraint Construction
BoolVar[] vars = model.boolVarArray(n);
model.and(vars).post();

Response JSON
{
  "solution": [1, 1, 1]
}


3. RealVar API
POST /api/realvar/solve
Request JSON
{
  "constraintType": "LIN_EQ",
  "xMin": 0,
  "xMax": 10,
  "yMin": 0,
  "yMax": 10,
  "coef1": 1,
  "coef2": 2,
  "constant": 8
}

Example Constraint
RealVar x = model.realVar("x", xmin, xmax, 0.01);
RealVar y = model.realVar("y", ymin, ymax, 0.01);

model.realLinearCombination(
    new RealVar[]{x, y},
    new double[]{coef1, coef2}
).eq(constant).post();


4. SetVar API
POST /api/setvar/solve
Request JSON
{
  "constraintType": "CARD_EQ",
  "universeSize": 5,
  "k": 2
}

Example Constraint
SetVar s = model.setVar("S", new int[]{}, universeElements);
model.cardinality(s, k).post();


5. GraphVar API
POST /api/graphvar/solve
Request JSON
{
  "constraintType": "DEGREE_GE",
  "numNodes": 5,
  "nodeIndex": 2,
  "degreeK": 3
}

Example Constraint
GraphVar g = model.graphVar("G", model.nodeSet(0, n-1));
model.degree(g, nodeIndex, ">=", k).post();


Frontend–Backend Communication
Each HTML page includes a corresponding JavaScript file:
intvar.js
boolvar.js
realvar.js
setvar.js
graphvar.js
The JavaScript constructs JSON objects from the user inputs and sends them to the backend using fetch:
Example (intvar.js)
fetch("http://localhost:8080/api/intvar/solve", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
})
.then(response => response.json())
.then(data => {
    document.getElementById("outputVariables").textContent = JSON.stringify(data, null, 2);
});

Each frontend page corresponds exactly to one API controller.
The backend responds synchronously with the computed solution.

Running the Project
Running the Backend
cd choco-solver-backend-ui
mvn spring-boot:run

The backend will be accessible at:
http://localhost:8080

Running the Frontend
Open the file:
choco-solver-frontend/index.html

in any modern browser.
No additional server is required; the frontend communicates directly with the backend.

Summary of Supported Constraint Types
Variable Type
Supported Constraints
IntVar
Basic comparisons, arithmetic, sums
BoolVar
Logical operations, equivalence, counting
RealVar
Linear, relational, distance constraints
SetVar
Cardinality, membership, subset, equality
GraphVar
Node degree constraints


Contributing
Contributions are welcome, including:
Additional constraint types
UI improvements
Backend extensions
Documentation updates
Submit pull requests or open issues in the repository.

License
This project follows the licensing terms of the included components (Choco-solver: BSD 4-Clause License). 

Acknowledgement
This project provides a custom frontend interface developed by Zahura Zaman for interacting with the Choco-solver engine. The underlying constraint solver, its algorithms, and the core libraries are developed and maintained by the Choco-solver development team. All copyrights and licenses for the Choco-solver library belong to the original authors.
The official Choco-solver repository can be found at: https://github.com/chocoteam/choco-solver
