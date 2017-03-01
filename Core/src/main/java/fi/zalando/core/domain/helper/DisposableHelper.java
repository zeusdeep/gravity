package fi.zalando.core.domain.helper;

import android.support.annotation.NonNull;
import fi.zalando.core.utils.Preconditions;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

/**
 * Definition of methods to handle disposes of {@link Disposable}s when {@link
 * android.app.Activity}s of {@link android.app.Fragment}s are stopped
 *
 * Created by jduran on 19/11/15.
 */
public class DisposableHelper {

  private CompositeDisposable disposables;

  /**
   * Constructor
   */
  @Inject
  public DisposableHelper() {

    disposables = new CompositeDisposable();
  }

  /**
   * Adds a {@link Disposable}s to the {@link DisposableHelper}
   *
   * @param disposablesToAdd {@link Disposable} to add
   */
  public void addDisposable(@NonNull Disposable... disposablesToAdd) {

    // Check preconditions
    Preconditions.checkNotNull(disposablesToAdd);
    for (Disposable disposable : disposablesToAdd) {
      disposables.add(disposable);
    }
  }

  /**
   * @return {@link Boolean} indicating if the helper contains has any disposables attached to it
   */
  public boolean hasDisposables() {

    return disposables.size() > 0;
  }


  /**
   * Clears all the disposables hosted in the helper
   */
  public void clear() {

    disposables.clear();
  }

}
