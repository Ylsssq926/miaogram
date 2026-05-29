/*
 * MiaoGram custom code.
 * Dialog for editing the keyword-mute list.
 *
 * Shown when the user taps the "Muted keywords" row in MiaoGram settings.
 * One keyword per line; saved back through KeywordFilter (which normalizes
 * and de-duplicates). Keeps dialog-building out of the settings page so the
 * preferences code stays focused on rows.
 */
package com.miaogram.miao.feature;

import android.content.Context;
import android.text.InputType;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public final class KeywordFilterDialog {

    private KeywordFilterDialog() {
        // utility class
    }

    /**
     * Shows a multi-line text-input dialog to edit the keyword list.
     *
     * @param context   UI context
     * @param onChanged  callback invoked after the list is saved (to refresh UI)
     */
    public static void show(Context context, Runnable onChanged) {
        final EditTextBoldCursor editText = new EditTextBoldCursor(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setSingleLine(false);
        editText.setMaxLines(8);
        editText.setHintText(LocaleController.getString(R.string.MiaoKeywordsHint));
        editText.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        editText.setPadding(0, AndroidUtilities.dp(6), 0, AndroidUtilities.dp(6));
        String existing = KeywordFilter.getRawText();
        if (existing != null && existing.length() > 0) {
            editText.setText(existing);
            editText.setSelection(existing.length());
        }

        final android.widget.FrameLayout container = new android.widget.FrameLayout(context);
        container.addView(editText, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT,
                android.view.Gravity.CENTER_VERTICAL, 24, 4, 24, 4));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(R.string.MiaoKeywords));
        builder.setMessage(LocaleController.getString(R.string.MiaoKeywordsInfo));
        builder.setView(container);
        builder.setPositiveButton(LocaleController.getString(R.string.Save), (dialog, which) -> {
            KeywordFilter.setRawText(editText.getText() != null ? editText.getText().toString() : "");
            if (onChanged != null) {
                onChanged.run();
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.show();

        editText.requestFocus();
        AndroidUtilities.showKeyboard(editText);
    }
}
