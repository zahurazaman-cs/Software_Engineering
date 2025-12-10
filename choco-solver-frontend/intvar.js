console.log("intvar.js loaded");  // DEBUG: see this in the browser console

const constraintTypeSelect = document.getElementById("constraintType");
const basicSection = document.getElementById("basicSection");
const sumSection = document.getElementById("sumSection");
const arithSection = document.getElementById("arithSection");
const domainsSection = document.getElementById("domainsSection");

const solveButton = document.getElementById("solveButton");
const outputVariables = document.getElementById("outputVariables");
const outputExpression = document.getElementById("outputExpression");

// Show / hide sections depending on constraint type
function updateVisibleSections() {
    const type = constraintTypeSelect.value;

    console.log("Constraint type changed to:", type); // DEBUG

    if (type === "BASIC" || type === "SUM") {
        domainsSection.style.display = "block";
    } else {
        domainsSection.style.display = "none";
    }

    basicSection.style.display = type === "BASIC" ? "block" : "none";
    sumSection.style.display = type === "SUM" ? "block" : "none";
    arithSection.style.display = type === "ARITH" ? "block" : "none";

    outputVariables.textContent = 'Click "Solve" to see values.';
    outputExpression.textContent = "Expression result will appear here.";
}

constraintTypeSelect.addEventListener("change", updateVisibleSections);
updateVisibleSections();

// Handle Solve button click
solveButton.addEventListener("click", async () => {
    const type = constraintTypeSelect.value;
    const payload = { constraintType: type };

    console.log("Solve clicked, type:", type); // DEBUG

    // BASIC and SUM: ranges for x and y
    if (type === "BASIC" || type === "SUM") {
        payload.xMin = parseInt(document.getElementById("xMin").value, 10);
        payload.xMax = parseInt(document.getElementById("xMax").value, 10);
        payload.yMin = parseInt(document.getElementById("yMin").value, 10);
        payload.yMax = parseInt(document.getElementById("yMax").value, 10);
    }

    if (type === "BASIC") {
        const basicOp = document.querySelector('input[name="basicOp"]:checked').value;
        payload.comparisonOperator = basicOp;
    }

    if (type === "SUM") {
        const sumComp = document.getElementById("sumComparison").value;
        const zVal = parseInt(document.getElementById("zValue").value, 10);
        payload.comparisonOperator = sumComp;
        payload.z = zVal;
    }

    if (type === "ARITH") {
        const xVal = parseInt(document.getElementById("xValue").value, 10);
        const yVal = parseInt(document.getElementById("yValue").value, 10);
        const arithOp = document.querySelector('input[name="arithOp"]:checked').value;
        payload.xValue = xVal;
        payload.yValue = yVal;
        payload.expressionOperator = arithOp;
    }

    console.log("Payload being sent:", payload); // DEBUG

    outputVariables.textContent = "Solving...";
    outputExpression.textContent = "";

    try {
        const response = await fetch("http://localhost:8080/api/solve", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        // If HTTP status is not 2xx, show error text
        if (!response.ok) {
            const text = await response.text();
            console.error("HTTP error", response.status, text);
            outputVariables.textContent = `HTTP error ${response.status}: ${text}`;
            outputExpression.textContent = "";
            return;
        }

        const data = await response.json();
        console.log("Response from backend:", data); // DEBUG

        if (!data.success) {
            outputVariables.textContent = "Error: " + data.message;
            outputExpression.textContent = "";
            return;
        }

        // Show variable values
        const variableLines = [];
        if (data.x !== null && data.x !== undefined) variableLines.push("x = " + data.x);
        if (data.y !== null && data.y !== undefined) variableLines.push("y = " + data.y);
        if (data.z !== null && data.z !== undefined) variableLines.push("z = " + data.z);
        if (data.sumResult !== null && data.sumResult !== undefined) {
            variableLines.push("x + y = " + data.sumResult);
        }
        outputVariables.textContent = variableLines.join("\n") || "No variable output.";

        // Show expression result (for ARITH)
        if (data.expressionResult !== null && data.expressionResult !== undefined) {
            outputExpression.textContent = "Expression result = " + data.expressionResult;
        } else {
            outputExpression.textContent = "No expression result.";
        }
    } catch (err) {
        console.error("Fetch failed:", err);
        outputVariables.textContent = "Request failed: " + err;
        outputExpression.textContent = "";
    }
});
