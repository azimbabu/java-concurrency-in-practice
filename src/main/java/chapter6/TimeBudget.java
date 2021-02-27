package chapter6;

import java.util.*;
import java.util.concurrent.*;

/**
 * QuoteTask
 *
 * <p>Requesting travel quotes under a time budget
 */
public class TimeBudget {
  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public List<TravelQuote> getRankedTravelQuotes(
      TravelInfo travelInfo,
      Set<TravelCompany> companies,
      Comparator<TravelQuote> ranking,
      long time,
      TimeUnit timeUnit)
      throws InterruptedException {
    List<QuoteTask> tasks = new ArrayList<>();
    for (TravelCompany company : companies) {
      tasks.add(new QuoteTask(company, travelInfo));
    }

    List<Future<TravelQuote>> futures = executor.invokeAll(tasks, time, timeUnit);
    List<TravelQuote> quotes = new ArrayList<>(tasks.size());
    Iterator<QuoteTask> taskIterator = tasks.iterator();
    for (Future<TravelQuote> future : futures) {
      QuoteTask task = taskIterator.next();

      try {
        quotes.add(future.get());
      } catch (ExecutionException e) {
        quotes.add(task.getFailureQuote(e.getCause()));
      } catch (CancellationException e) {
        quotes.add(task.getTimeoutQuote(e));
      }
    }

    Collections.sort(quotes, ranking);
    return quotes;
  }

  interface TravelCompany {
    TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception;
  }

  interface TravelQuote {}

  interface TravelInfo {}

  private static class QuoteTask implements Callable<TravelQuote> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
      this.company = company;
      this.travelInfo = travelInfo;
    }

    TravelQuote getFailureQuote(Throwable throwable) {
      return null;
    }

    TravelQuote getTimeoutQuote(CancellationException e) {
      return null;
    }

    @Override
    public TravelQuote call() throws Exception {
      return company.solicitQuote(travelInfo);
    }
  }
}
