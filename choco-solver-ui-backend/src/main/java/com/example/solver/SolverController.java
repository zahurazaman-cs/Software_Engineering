package com.example.solver;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")   // allow frontend from any origin (fine for development)
public class SolverController {

   private final SolverService solverService;
private final BoolSolverService boolSolverService;
private final RealSolverService realSolverService;
private final SetSolverService setSolverService;
private final GraphSolverService graphSolverService;

public SolverController(SolverService solverService,
                        BoolSolverService boolSolverService,
                        RealSolverService realSolverService,
                        SetSolverService setSolverService,
                        GraphSolverService graphSolverService) {
    this.solverService = solverService;
    this.boolSolverService = boolSolverService;
    this.realSolverService = realSolverService;
    this.setSolverService = setSolverService;
    this.graphSolverService = graphSolverService;
}


    @PostMapping("/solve")
    public SolveResponse solve(@RequestBody SolveRequest request) {
        return solverService.solve(request);
    }
    @PostMapping("/solve-bool")
public BoolSolveResponse solveBool(@RequestBody BoolSolveRequest request) {
    return boolSolverService.solve(request);
}
@PostMapping("/solve-real")
public RealSolveResponse solveReal(@RequestBody RealSolveRequest request) {
    return realSolverService.solve(request);
}
@PostMapping("/solve-set")
public SetSolveResponse solveSet(@RequestBody SetSolveRequest request) {
    return setSolverService.solve(request);
}
@PostMapping("/solve-graph")
public GraphSolveResponse solveGraph(@RequestBody GraphSolveRequest request) {
    return graphSolverService.solve(request);
}


}
