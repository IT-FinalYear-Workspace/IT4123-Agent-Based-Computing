# IT4123 - Agent-Based Computing

## Course Overview

This repository contains the practical work and simulation projects for the **Agent-Based Computing (IT4123)** course, part of the **BSc (Hons) in Information Technology, Semester 4.1** at the **University of Vavuniya**.

The course focuses on understanding and implementing **multi-agent systems (MAS)**, their architectures, behaviors, and interactions using frameworks like **JADE (Java Agent DEvelopment Framework)** and later **Python-based simulations**.

---

## Prerequisites

### For JADE (Java Agent Development Framework)

- **Java JDK** – Version 8 or higher (recommended: Java 17+)
- **JADE Library** – `jade.jar` and optional `jadeTools.jar`
- **Maven** _(optional)_ – for modern builds or forks
- **IDE** – IntelliJ IDEA / Eclipse / VS Code with Java extension

### For Python-based Simulations

- **Python 3.8+**
- **Anaconda** (optional, for environment management)
- **Common Libraries**:
  ```bash
  pip install numpy pandas matplotlib seaborn mesa
  ```

* Alternatively, you can use **Google Colab** for executing Python simulations.

---

## 1. Overview of JADE

- **JADE** (Java Agent DEvelopment Framework) is an open-source middleware for developing **multi-agent systems** compliant with **FIPA (Foundation for Intelligent Physical Agents)** specifications.
- It simplifies **peer-to-peer agent communication** via asynchronous **ACL (Agent Communication Language)** messages.
- It provides:

  - A **Directory Facilitator (DF)** — a “yellow pages” service for agent discovery
  - **Distributed platform support** across networks
  - **Agent mobility and NAT traversal**
  - **GUI tools** for debugging and monitoring

Agents are autonomous, reactive entities that extend `jade.core.Agent` and operate within **JADE containers**.

**Key Benefits:**

- FIPA-ACL compliance for interoperability
- Task execution via **behaviors**
- Built-in **graphical tools** for debugging and monitoring

---

## 2. Basic JADE Setup and Commands

### 2.1 Setting Up JADE

- Download the latest stable release:
  **JADE 4.6.0 (July 2022)** → [Official site](http://jade.tilab.com)
  **Community fork (4.6.1+)** → [dpsframework/jade-platform](https://github.com/dpsframework/jade-platform)
- Add `jade.jar` (and optional `jadeTools.jar`) to your project's **classpath**.

**System Requirements:**

- Java 8+ (Java 17 recommended)
- Optional: Maven for building modern forks

---

### 2.2 Directory Structure

```
MyJadeProject/
│
├── lib/
│   ├── jade.jar
│   └── jadeTools.jar
│
├── src/
│   └── MyAgent.java
│
└── classes/
    └── (compiled .class files)
```

---

### 2.3 Compiling Agent Classes

Compile using `javac` with JADE in the classpath:

```bash
# On Windows
javac -cp "lib/jade.jar;lib/jadeTools.jar" -d classes src/MyAgent.java

# On Linux/Mac
javac -cp "lib/jade.jar:lib/jadeTools.jar" -d classes src/MyAgent.java
```

For multiple source files:

```bash
javac -cp "lib/jade.jar" -d classes src/*.java
```

With Maven:

```bash
mvn compile
```

---

### 2.4 Running JADE Platform

#### Start the Main Container (with GUI):

```bash
java -cp "lib/jade.jar;classes" jade.Boot -gui
```

- `-gui`: Launches RMA (Remote Management Agent) for platform control.

#### Start a Peripheral Container:

```bash
java -cp "lib/jade.jar;classes" jade.Boot -container -host <main-host> -port 1099
```

- Connects to main container (supports distributed systems).

---

### 2.5 Running an Agent

Launch agents at boot:

```bash
java -cp "lib/jade.jar;classes" jade.Boot -gui "agent1:package.MyAgent(arg1,arg2)"
```

**Explanation:**

- `agent1` → Agent name
- `package.MyAgent` → Fully qualified class name
- `(arg1,arg2)` → Optional arguments passed to `getArguments()`
- Multiple agents → `-agents "agent1:MyAgent;agent2:OtherAgent"`

After startup, agents can also be launched from **RMA GUI**:

> RMA > Tools > New Agent

---

## 3. Creating a JADE Agent

Example:

```java
import jade.core.Agent;

public class MyAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("Hello! I am " + getLocalName());
    }

    @Override
    protected void takeDown() {
        System.out.println("Terminating agent: " + getLocalName());
    }
}
```

### Agent Lifecycle:

```
INITIATED → ACTIVE → (SUSPENDED / WAITING) → DELETED
```

---

## 4. Transition to Python-Based Agent Simulations

In later practicals, we’ll use **Python** for implementing agent-based models using frameworks such as:

- **Mesa** (Python library for agent-based modeling)
- **SimPy** for process-based discrete-event simulation
- **NetworkX** for interaction modeling between agents

---

## References

- [JADE Official Documentation](http://jade.tilab.com/doc/)
- [JADE GitHub Forks (dpsframework)](https://github.com/dpsframework/jade-platform)
- [FIPA Specifications](https://www.fipa.org/)
- [Mesa Documentation](https://mesa.readthedocs.io/)

---

## Author

**Udara Kavishka**  
BSc (Hons) in Information Technology  
University of Vavuniya, Sri Lanka  
Semester 4.1 – 2025

```

```
