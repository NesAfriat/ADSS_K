package BusinessLayer.Responses;

import BusinessLayer.InnerLogicException;
import BusinessLayer.Shifts.Shift;
import BusinessLayer.Workers.Job;
import BusinessLayer.Workers.Worker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftResponse {
    private boolean approved;
    private Map<Job, JobArrangementResponse> currentWorkers;

    public ShiftResponse(Shift shift){
        this.approved = shift.isApproved();
        currentWorkers = new HashMap<>();
        List<Job> jobs = shift.getJobs();
        for (Job job: jobs) {
            try{
                List<Worker> workers = shift.getCurrentWorkers(job);
                int required = shift.getAmountRequired(job);
                int amountAssigned = shift.getCurrentWorkersAmount(job);
                currentWorkers.put(job, new JobArrangementResponse(workers,required,amountAssigned));
            }catch (InnerLogicException ignored){}
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        currentWorkers.forEach((job, jobArrangement) -> {
            stringBuilder.append(job).append(":\n");
            for (WorkerResponse workerResponse: jobArrangement.workers){
                stringBuilder.append("Name: ").append(workerResponse.getName()).append(" || ID: ").append(workerResponse.getId()).append("\n");
            }
        });
        return stringBuilder.toString();
    }

    private static class JobArrangementResponse{
        int required;
        List<WorkerResponse> workers;
        int amountAssigned;

        JobArrangementResponse(List<Worker> workers, int required, int amountAssigned) {
            this.required = required;
            this.amountAssigned = amountAssigned;
            this.workers = new LinkedList<>();
            for (Worker worker: workers) {
                WorkerResponse workerResponse = new WorkerResponse(worker);
                this.workers.add(workerResponse);
            }
        }
    }
}
