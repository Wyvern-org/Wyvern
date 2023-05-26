package wyvern.util.jobs;

public abstract class AsyncJob extends Thread
{
    private final IJob job;
    private final IPostJob postJob;

    public AsyncJob(IJob job, IPostJob postJob)
    {
        this.job = job;
        this.postJob = postJob;
    }

    public void run()
    {
        Object result = job.run();
        postJob.completed(result);
    }
}
