import java.util.ArrayList;
import java.util.List;

public class SingleVsMultiThread {
    public static void main(String[] args) {
        long timeTakenMulti = startMultiThread();
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println(multiThreadContentCount + " Contents in MultiThread Time taken: " + timeTakenMulti + " sec.");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        try {
            System.out.println("Starting single thread operation in 5 sec.");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long timeTakenSingle = startSingleThread();
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println(singleThreadContentCount + " contents in Single Thread time taken: " + timeTakenSingle + " sec.");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
    }

    static int singleThreadContentCount;

    private static long startSingleThread() {
        System.out.println("Single thread started");
        long initTime = System.currentTimeMillis();
        List<String> listPage = getPageResponse();
		for(String page: listPage) {
			System.out.println("Page: " + page + "; Started");
            List<String> listSelector = getSelectorResponse(page);
            System.out.println("success selector response of " + page);
			for(String selector:listSelector) {
				System.out.println("Selector: " + selector + "; started");
                List<String> listContent = getContentResponse(selector);
                System.out.println("success content response of " + selector);
                for(String content:listContent) {
					System.out.println("Started: " + content);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Ended: " + content);
                    increaseSingleThreadContentCount();
				}
			}

		}
        System.out.println("success page response");
        return (System.currentTimeMillis() - initTime) / 1000;
    }

    private synchronized static void increaseSingleThreadContentCount() {
        singleThreadContentCount++;
    }

    static int multiThreadContentCount;

    private static long startMultiThread() {
        System.out.println("Multi Thread started");
        long initTime = System.currentTimeMillis();
        List<String> listPage = getPageResponse();
        System.out.println("success page response");
        ThreadPool threadPool = ThreadPool.newPool();
		for(String p:listPage) {
			Thread t = getPage(p);
            threadPool.addThread(t);
		}
        threadPool.execute();
        return (System.currentTimeMillis() - initTime) / 1000;
    }

    private static Thread getPage(final String page) {
        Thread thread = new Thread(new Runnable(){

				@Override
				public void run()
				{
					System.out.println("Page: " + page + "; Started");
					List<String> listSelector = getSelectorResponse(page);
					System.out.println("success selector response of " + page);
					ThreadPool threadPool = ThreadPool.newPool();
					for(String selector:listSelector){
						Thread t = getSelector(selector);
						threadPool.addThread(t);
					}
					threadPool.execute();
					System.out.println("Page: " + page + " Ended");
				}
			});
        return thread;
    }

    private static Thread getSelector(final String selector) {
        Thread thread = new Thread(new Runnable(){

				@Override
				public void run()
				{
					System.out.println("Selector: " + selector + "; started");
					List<String> listContent = getContentResponse(selector);
					System.out.println("success content response of " + selector);
					ThreadPool threadPool = ThreadPool.newPool();
					for(String content:listContent){
						Thread t = getContent(content);
						threadPool.addThread(t);
					}
					threadPool.execute();
					System.out.println("Selector: " + selector + "; ended");
				}
			});
        return thread;
    }

    private static Thread getContent(final String content) {
        Thread thread = new Thread(new Runnable(){

				@Override
				public void run()
				{
					System.out.println("Started: " + content);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Ended: " + content);
					increaseMultiThreadContentCount();
				}
			});
        return thread;
    }

    private synchronized static void increaseMultiThreadContentCount() {
        multiThreadContentCount++;
    }

    private static List<String> getContentResponse(String selector) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("Content-" + (i + 1) + " :: " + selector);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static List<String> getSelectorResponse(String page) {
        List<String> listSelector = new ArrayList<>();
        listSelector.add("Banner :: " + page);
        listSelector.add("Most Popular :: " + page);
        listSelector.add("Featured :: " + page);
        listSelector.add("Favorite :: " + page);
        listSelector.add("Recommended :: " + page);
        try {
            Thread.sleep(listSelector.size() * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listSelector;
    }

    private static List<String> getPageResponse() {
        List<String> listPage = new ArrayList<>();
        listPage.add("Home");
        listPage.add("Sports");
        listPage.add("Drama");
        listPage.add("Movies");
        listPage.add("Music");
        try {
            Thread.sleep(listPage.size() * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listPage;
    }
}
