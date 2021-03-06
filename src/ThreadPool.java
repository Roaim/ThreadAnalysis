import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
    private List<Thread> mList;

    private ThreadPool() {
        mList = new ArrayList<>();
    }

    public static ThreadPool newPool() {
        return new ThreadPool();
    }

    public ThreadPool addThread(Thread thread) {
        mList.add(thread);
        return this;
    }

    public void execute() {
        mList.forEach(Thread::start);
        mList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}