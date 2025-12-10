const kindSelectReal = document.getElementById("realConstraintKind");

const xMinInput = document.getElementById("realXMin");
const xMaxInput = document.getElementById("realXMax");
const yMinInput = document.getElementById("realYMin");
const yMaxInput = document.getElementById("realYMax");

const constantRow = document.getElementById("constRow");
const constantInput = document.getElementById("realConstant");

const linearRow = document.getElementById("linearRow");
const coef1Input = document.getElementById("coef1");
const coef2Input = document.getElementById("coef2");

const distanceRow = document.getElementById("distanceRow");
const distanceInput = document.getElementById("distanceBound");

const solveRealButton = document.getElementById("realSolveButton");
const realOutput = document.getElementById("realOutput");

// Show/hide sections depending on constraint kind
function updateRealVisibility() {
    const kind = kindSelectReal.value;

    const needsConstant =
        kind === "X_LE_CONST" ||
        kind === "X_GE_CONST" ||
        kind === "X_EQ_CONST" ||
        kind === "LIN_EQ" ||
        kind === "LIN_LE" ||
        kind === "LIN_GE";

    const needsLinear =
        kind === "LIN_EQ" || kind === "LIN_LE" || kind === "LIN_GE";

    const needsDistance = kind === "ABS_DIFF_LE";

    constantRow.style.display = needsConstant ? "block" : "none";
    linearRow.style.display = needsLinear ? "block" : "none";
    distanceRow.style.display = needsDistance ? "block" : "none";
}

kindSelectReal.addEventListener("change", updateRealVisibility);
updateRealVisibility();

solveRealButton.addEventListener("click", async () => {
    const kind = kindSelectReal.value;

    const payload = {
        constraintKind: kind,
        xMin: parseFloat(xMinInput.value),
        xMax: parseFloat(xMaxInput.value),
        yMin: parseFloat(yMinInput.value),
        yMax: parseFloat(yMaxInput.value),
    };

    if (
        kind === "X_LE_CONST" ||
        kind === "X_GE_CONST" ||
        kind === "X_EQ_CONST" ||
        kind === "LIN_EQ" ||
        kind === "LIN_LE" ||
        kind === "LIN_GE"
    ) {
        payload.constant = parseFloat(constantInput.value);
    }

    if (kind === "LIN_EQ" || kind === "LIN_LE" || kind === "LIN_GE") {
        payload.coef1 = parseInt(coef1Input.value, 10);
        payload.coef2 = parseInt(coef2Input.value, 10);
    }

    if (kind === "ABS_DIFF_LE") {
        payload.distanceBound = parseFloat(distanceInput.value);
    }

    realOutput.textContent = "Solving RealVar constraint...";

    try {
        const response = await fetch("http://localhost:8080/api/solve-real", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const text = await response.text();
            realOutput.textContent = `HTTP error ${response.status}: ${text}`;
            return;
        }

        const data = await response.json();

        if (!data.success) {
            realOutput.textContent = "Error: " + data.message;
            return;
        }

        const lines = [];
        lines.push(data.message);
        if (data.x !== null && data.x !== undefined) {
            lines.push("x = " + data.x);
        }
        if (data.y !== null && data.y !== undefined) {
            lines.push("y = " + data.y);
        }

        realOutput.textContent = lines.join("\n");
    } catch (err) {
        console.error(err);
        realOutput.textContent = "Request failed: " + err;
    }
});
