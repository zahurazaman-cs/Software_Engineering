const graphKindSelect = document.getElementById("graphConstraintKind");
const numNodesInput = document.getElementById("numNodes");
const nodeIndexInput = document.getElementById("nodeIndex");
const degreeKInput = document.getElementById("degreeK");
const graphSolveButton = document.getElementById("graphSolveButton");
const graphOutput = document.getElementById("graphOutput");

graphSolveButton.addEventListener("click", async () => {
    const kind = graphKindSelect.value;
    const numNodes = parseInt(numNodesInput.value, 10);
    const nodeIndex = parseInt(nodeIndexInput.value, 10);
    const degreeK = parseInt(degreeKInput.value, 10);

    const payload = {
        constraintKind: kind,
        numNodes: numNodes,
        nodeIndex: nodeIndex,
        degreeK: degreeK,
    };

    graphOutput.textContent = "Solving graph constraint...";

    try {
        const response = await fetch("http://localhost:8080/api/solve-graph", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const text = await response.text();
            graphOutput.textContent = `HTTP error ${response.status}: ${text}`;
            return;
        }

        const data = await response.json();

        if (!data.success) {
            graphOutput.textContent = "Error: " + data.message;
            return;
        }

        const lines = [];
        lines.push(data.message);

        if (Array.isArray(data.degrees)) {
            lines.push("Degrees by node:");
            data.degrees.forEach((deg, idx) => {
                lines.push(`  node ${idx}: degree = ${deg}`);
            });
        }

        if (Array.isArray(data.adjacency)) {
            lines.push("");
            lines.push("Edges (undirected):");
            for (let i = 0; i < data.adjacency.length; i++) {
                for (let j = i + 1; j < data.adjacency.length; j++) {
                    if (data.adjacency[i][j]) {
                        lines.push(`  ${i} -- ${j}`);
                    }
                }
            }
        }

        graphOutput.textContent = lines.join("\n");
    } catch (err) {
        console.error(err);
        graphOutput.textContent = "Request failed: " + err;
    }
});
