package fi.zalando.core.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Utility class to help with UI related tasks
 *
 * Created by vraisanen on 16/02/16.
 */
public class UIUtils {

    /**
     * Private constructor to avoid class instances
     */
    private UIUtils() {
    }

    /**
     * Runs the given {@link Runnable} in the UI thread once the layout is complete. It registers a
     * {@link ViewTreeObserver.OnGlobalLayoutListener} to the given {@link View}, runs the Runnable,
     * and finally unregisters the said listener. This method needs to be called before the layout
     * is finished, e.g. in onCreate.
     *
     * @param view     View or Layout whose view hierarchy onGlobalLayout to monitor.
     * @param runnable Runnable to be run once the layout is finished.
     */
    public static void runOnGlobalLayout(@NonNull View view, @NonNull Runnable runnable) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onGlobalLayout() {
                        //Run the runnable:
                        runnable.run();
                        //Unregister the listener so that the runnable is only run once:
                        if (PlatformUtils.isNewerOrEqualSDKVersion(Build.VERSION_CODES
                                .JELLY_BEAN)) {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                }
        );
    }

    /**
     * Updates the status of the password visibility of the given {@link EditText} to match the
     * given {@link CheckBox}.
     *
     * @param passwordEditText     {@link EditText} whose transformation method to update
     * @param showPasswordCheckBox {@link CheckBox} whose status determines the method.
     */
    public static void updatePasswordTransformation(EditText passwordEditText, CheckBox
            showPasswordCheckBox) {
        //Save the selection start and end, as these will reset when changing the transformation:
        final int selectionStart = passwordEditText.getSelectionStart();
        final int selectionEnd = passwordEditText.getSelectionEnd();
        //Update transformation method:
        if (showPasswordCheckBox.isChecked()) {
            passwordEditText.setTransformationMethod(null);
        } else {
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        }
        //Restore original selection:
        passwordEditText.setSelection(selectionStart, selectionEnd);
    }

    /**
     * Shows styled {@link Snackbar} with the given text.
     *
     * @param parent parent {@link View}
     * @param resId  text to show
     * @param duration      {@link Snackbar.Duration} of the snackbar
     * @return Snackbar that is shown
     */
    public static Snackbar showSnack(@NonNull View parent,
                                     @StringRes int resId,
                                     @Snackbar.Duration int duration) {

        final Snackbar snackbar = Snackbar.make(parent, resId, duration);
        styleSnackbar(parent.getContext(), snackbar);
        snackbar.show();
        return snackbar;
    }

    /**
     * Shows styled {@link Snackbar} with the given text and style it with given parameters
     *
     * @param parent          parent {@link View}
     * @param backgroundColor {@link ColorRes} for the Snackbar background
     * @param resId           text to show
     * @param duration        {@link Snackbar.Duration} of the snackbar
     * @return Snackbar that is shown
     */
    public static Snackbar showSnack(@NonNull View parent,
                                     @StringRes int resId,
                                     @ColorRes int backgroundColor,
                                     @Snackbar.Duration int duration) {

        final Snackbar snackbar = Snackbar.make(parent, resId, duration);
        // We first style it, and then change the needed params
        styleSnackbar(parent.getContext(), snackbar);
        snackbar.getView()
                .setBackgroundColor(ContextCompat.getColor(parent.getContext(), backgroundColor));
        snackbar.show();
        return snackbar;
    }

    /**
     * Shows styled {@link Snackbar} with the given text and button
     *
     * @param parent        parent {@link View}
     * @param resId         text resource id
     * @param buttonTextId  button text resource id
     * @param onClickAction {@link Runnable} to execute on button press
     * @param duration      {@link Snackbar.Duration} of the snackbar
     * @return Snackbar that is shown
     */
    public static Snackbar showSnack(@NonNull View parent,
                                     @StringRes int resId,
                                     @StringRes int buttonTextId,
                                     @NonNull Runnable onClickAction,
                                     @Snackbar.Duration int duration) {

        final Snackbar snackbar = Snackbar.make(parent, resId, duration);
        snackbar.setAction(buttonTextId, (v) -> onClickAction.run());
        styleSnackbar(parent.getContext(), snackbar);
        snackbar.show();
        return snackbar;
    }

    /**
     * Shows styled {@link Snackbar} with the given text and button, styling it with the given
     * parameters
     *
     * @param parent          parent {@link View}
     * @param resId           text resource id
     * @param buttonTextId    button text resource id
     * @param backgroundColor {@link ColorRes} for the Snackbar background
     * @param actionColor     {@link ColorRes} for the color of the action button text
     * @param onClickAction   {@link Runnable} to execute on button press
     * @param duration      {@link Snackbar.Duration} of the snackbar
     * @return Snackbar that is shown
     */
    public static Snackbar showSnack(@NonNull View parent,
                                     @StringRes int resId,
                                     @StringRes int buttonTextId,
                                     @ColorRes int backgroundColor,
                                     @ColorRes int actionColor,
                                     @NonNull Runnable onClickAction,
                                     @Snackbar.Duration int duration) {

        final Snackbar snackbar = Snackbar.make(parent, resId, duration);
            snackbar.setAction(buttonTextId, (v) -> onClickAction.run());
        // no need to style it first... everything styleable will override
        snackbar.setActionTextColor(ContextCompat.getColor(parent.getContext(), actionColor));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(parent.getContext(),
                backgroundColor));
        snackbar.show();
        return snackbar;
    }

    /**
     * Styles the given Snackbar based on the styles defined in application styles with a name
     * "SnackbarStyle".
     *
     * @param context  {@link Context}
     * @param snackbar {@link Snackbar}
     */
    @SuppressWarnings("ResourceType") //Solves lint bug
    private static void styleSnackbar(Context context, Snackbar snackbar) {
        View snackView = snackbar.getView();

        //Get the styles defined in the XML:
        int[] attrs = {android.R.attr.textColor, android.R.attr.background};
        int textColor = Color.BLACK;
        int backgroundColor = Color.WHITE;

        int resID = context.getResources().getIdentifier("SnackbarStyle", "style",
                context.getPackageName());

        //Try to fetch the style attributes
        try {
            TypedArray ta = context.getTheme().obtainStyledAttributes(resID, attrs);
            try {
                textColor = ta.getColor(0, Color.BLACK);
                backgroundColor = ta.getColor(1, Color.WHITE);
            } finally {
                ta.recycle();
            }
        } catch (Resources.NotFoundException e) { //Attributes could not be found
            //ignore
        }

        //Apply the styles:
        snackView.setBackgroundColor(backgroundColor);
        snackbar.setActionTextColor(textColor);
    }

    /**
     * Get the focus to the first error of the registration
     */
    public static void focusOnFirstError(TextView... textViews) {

        for (TextView textView : textViews) {
            if (!TextUtils.isEmpty(textView.getError())) {
                textView.requestFocus();
                return;
            }
        }
    }
}
