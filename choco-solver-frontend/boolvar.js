const kindSelect = document.getElementById("boolConstraintKind");
const numBoolsInput = document.getElementById("numBools");
const kRow = document.getElementById("kRow");
const kInput = document.getElementById("kValue");
const solveButton = document.getElementById("boolSolveButton");
const output = document.getElementById("boolOutput");

// Show/hide k row for counting constraints
function updateKVisibility() {
    const kind = kindSelect.value;
    if (kind === "COUNT_EQ" || kind === "COUNT_LE" || kind === "COUNT_GE") {
        kRow.style.display = "block";
    } else {
        kRow.style.display = "none";
    }
}

kindSelect.addEventListener("change", updateKVisibility);
updateKVisibility();

solveButton.addEventListener("click", async () => {
    const kind = kindSelect.value;
    const numBools = parseInt(numBoolsInput.value, 10);
    const payload = {
        constraintKind: kind,
        numBools: numBools,
    };

    if (kind === "COUNT_EQ" || kind === "COUNT_LE" || kind === "COUNT_GE") {
        payload.k = parseInt(kInput.value, 10);
    }

    output.textContent = "Solving BoolVar constraint...";

    try {
        const response = await fetch("http://localhost:8080/api/solve-bool", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const text = await response.text();
            output.textContent = `HTTP error ${response.status}: ${text}`;
            return;
        }

        const data = await response.json();

        if (!data.success) {
            output.textContent = "Error: " + data.message;
            return;
        }

        const lines = [];
        lines.push(data.message);
        if (Array.isArray(data.boolValues)) {
            data.boolValues.forEach((val, idx) => {
                lines.push(`b${idx} = ${val ? "true" : "false"}`);
            });
        }
        if (data.trueCount !== null && data.trueCount !== undefined) {
            lines.push(`Total true booleans = ${data.trueCount}`);
        }

        output.textContent = lines.join("\n");
    } catch (err) {
        console.error(err);
        output.textContent = "Request failed: " + err;
    }
});
