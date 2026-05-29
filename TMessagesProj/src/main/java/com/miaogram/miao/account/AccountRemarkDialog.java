/*
 * MiaoGram custom code.
 * Dialog for setting a per-account local remark.
 *
 * Shown on long-press of an account row in the account switcher.
 * Keeps the dialog-building code out of upstream MainTabsActivity (whose hook
 * stays a single line), per the minimal-hook principle.
 */
package com.miaogram.miao.account;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.UserObject;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public final class AccountRemarkDialog {

    private AccountRemarkDialog() {
    }

    /**
     * Shows a text-input dialog to set/clear the remark for the given account.
     *
     * @param context   UI context
     * @param accountId  account index
     * @param onChanged  callback invoked after the remark is saved (to refresh UI)
     */
    public static void show(Context context, int accountId, Runnable onChanged) {
        final TLRPC.User user = UserConfig.getInstance(accountId).getCurrentUser();
        final String currentName = user != null ? UserObject.getUserName(user) : "";

        final EditTextBoldCursor editText = new EditTextBoldCursor(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setHintText(currentName);
        editText.setSingleLine(true);
        String existing = AccountRemark.getRemark(accountId);
        if (existing != null) {
            editText.setText(existing);
            editText.setSelection(existing.length());
        }

        final FrameLayout container = new FrameLayout(context);
        container.addView(editText, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT,
                android.view.Gravity.CENTER_VERTICAL, 24, 0, 24, 0));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(R.string.MiaoAccountRemarkTitle));
        builder.setView(container);
        builder.setPositiveButton(LocaleController.getString(R.string.Save), (dialog, which) -> {
            AccountRemark.setRemark(accountId, editText.getText() != null ? editText.getText().toString() : null);
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
