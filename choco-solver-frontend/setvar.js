const setKindSelect = document.getElementById("setConstraintKind");
const universeInput = document.getElementById("universeSize");
const cardRow = document.getElementById("cardRow");
const cardInput = document.getElementById("cardK");
const elemRow = document.getElementById("elemRow");
const elemInput = document.getElementById("elementX");
const setSolveButton = document.getElementById("setSolveButton");
const setOutput = document.getElementById("setOutput");

// Show/hide inputs depending on constraint
function updateSetVisibility() {
    const kind = setKindSelect.value;

    const needsK =
        kind === "CARD_EQ" || kind === "CARD_LE" || kind === "CARD_GE";

    const needsElem = kind === "MEMBER" || kind === "NOT_MEMBER";

    cardRow.style.display = needsK ? "block" : "none";
    elemRow.style.display = needsElem ? "block" : "none";
}

setKindSelect.addEventListener("change", updateSetVisibility);
updateSetVisibility();

setSolveButton.addEventListener("click", async () => {
    const kind = setKindSelect.value;
    const universeSize = parseInt(universeInput.value, 10);

    const payload = {
        constraintKind: kind,
        universeSize: universeSize,
    };

    if (kind === "CARD_EQ" || kind === "CARD_LE" || kind === "CARD_GE") {
        payload.k = parseInt(cardInput.value, 10);
    }

    if (kind === "MEMBER" || kind === "NOT_MEMBER") {
        payload.element = parseInt(elemInput.value, 10);
    }

    setOutput.textContent = "Solving SetVar constraint...";

    try {
        const response = await fetch("http://localhost:8080/api/solve-set", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const text = await response.text();
            setOutput.textContent = `HTTP error ${response.status}: ${text}`;
            return;
        }

        const data = await response.json();

        if (!data.success) {
            setOutput.textContent = "Error: " + data.message;
            return;
        }

        const lines = [];
        lines.push(data.message);

        if (Array.isArray(data.s1Elements)) {
            lines.push("S or S₁ = {" + data.s1Elements.join(", ") + "}");
        }

        if (Array.isArray(data.s2Elements) && data.s2Elements.length > 0) {
            lines.push("S₂ = {" + data.s2Elements.join(", ") + "}");
        }

        setOutput.textContent = lines.join("\n");
    } catch (err) {
        console.error(err);
        setOutput.textContent = "Request failed: " + err;
    }
});
