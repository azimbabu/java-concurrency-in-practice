package chapter9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** ListenerExamples */
public class ListenerExamples {
  private static ExecutorService exec = Executors.newCachedThreadPool();

  private final JButton colorButton = new JButton("Change Color");
  private final Random random = new Random();
  private final JButton computeButon = new JButton("Big computation");
  private final JButton button = new JButton("Do");
  private final JLabel label = new JLabel("idle");
  private final JButton startButton = new JButton("Start");
  private final JButton cancelButton = new JButton("Cancel");
  private Future<?> runningTask = null; // thread-confined

  private void backgroundRandom() {
    colorButton.addActionListener(e -> colorButton.setBackground(new Color(random.nextInt())));
  }

  private void longRunningTask() {
    computeButon.addActionListener(
        e ->
            exec.execute(
                () -> {
                  /* Do big computation */
                }));
  }

  private void longRunningTaskWithFeedback() {
    button.addActionListener(
        e -> {
          button.setEnabled(false);
          label.setText("busy");
          exec.execute(
              () -> {
                try {
                  /* Do big computation */
                } finally {
                  GuiExecutor.instance()
                      .execute(
                          () -> {
                            button.setEnabled(true);
                            label.setText("idle");
                          });
                }
              });
        });
  }

  private void taskWithCancellation() {
    startButton.addActionListener(
        e -> {
          if (runningTask != null) {
            runningTask =
                exec.submit(
                    () ->
                        new Runnable() {
                          @Override
                          public void run() {
                            while (moreWork()) {
                              if (Thread.currentThread().isInterrupted()) {
                                cleanUpPartialWork();
                                break;
                              }
                              doSomeWork();
                            }
                          }

                          private void doSomeWork() {}

                          private void cleanUpPartialWork() {}

                          private boolean moreWork() {
                            return false;
                          }
                        });
          }
        });

    cancelButton.addActionListener(
        e -> {
          if (runningTask != null) {
            runningTask.cancel(true);
          }
        });
  }

  private void runInBackground(final Runnable task) {
    startButton.addActionListener(
        e -> {
          class CancelListener implements ActionListener {
            BackgroundTask<?> task;

            @Override
            public void actionPerformed(ActionEvent e) {
              if (task != null) {
                task.cancel(true);
              }
            }
          }

          final CancelListener listener = new CancelListener();
          listener.task =
              new BackgroundTask<Void>() {
                @Override
                protected Void compute() {
                  while (moreWork() && !isCancelled()) {
                    doSomeWork();
                  }
                  return null;
                }

                private void doSomeWork() {}

                private boolean moreWork() {
                  return false;
                }

                @Override
                protected void onCompletion(Void value, Throwable thrown, boolean cancelled) {
                  cancelButton.removeActionListener(listener);
                  label.setText("done");
                }
              };

          cancelButton.addActionListener(listener);
          exec.execute(task);
        });
  }
}
